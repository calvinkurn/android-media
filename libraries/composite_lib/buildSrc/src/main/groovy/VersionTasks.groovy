import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

// compare git last release with each module release
class VersionTasks extends DefaultTask {
    def latestReleaseDate = ""
    // contoh :  "com.tokopedia.url" -> "graphql"
    ArrayList<TreeModel> listTree = new ArrayList()

    // untuk project yang berubah & punya properties artifact dkk, maka disimpan disini
    ArrayList<VersionModel> listVersion = new ArrayList()

    def rootProjectTask // simpan object root projectName

    // should be change to gitLog
    def getVersionName(String module){
        def stdout = new ByteArrayOutputStream()
        stdout = "git log -1 --pretty=\'%ad\' --date=format:\'%Y-%m-%d,%H:%M:%S\' $module".execute().text
        return stdout.toString().trim().replace("'", "").replace(","," ")
    }

    @TaskAction
    void versionProcessTask() {
//        def dot = new File(rootProjectTask.buildDir, 'reports/dependency-graph/project.dot')
//        dot.parentFile.mkdirs()
//        dot.delete()
//
//        dot << 'digraph {\n'
//        dot << "  graph [label=\"${rootProjectTask.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n"
//        dot << '  node [style=filled, fillcolor="#bbbbbb"];\n'
//        dot << '  rankdir=TB;\n'

        // get all projectName
        def rootProjects = []
        def queue = [rootProjectTask]
        while (!queue.isEmpty()) {
            def project = queue.remove(0)
            rootProjects.add(project) // simpan semua projectName dan subproject
            queue.addAll(project.childProjects.values())
        }

        def projects = new LinkedHashSet<Project>() // untuk simpan semua dependency dan projectName

        // ini di-sort
        // key projectName-dependency dan isi string nya
        def dependencies = new LinkedHashMap<Tuple2<Project, Project>, List<String>>()
//        def multiplatformProjects = []
//        def jsProjects = []
//        def androidProjects = []
//        def javaProjects = []


        // simpan di `dependencies`
        // semua yang dikerjakan disini `mutable`
        // BFS ~ semua projectName
        queue = [rootProjectTask]
        while (!queue.isEmpty()) {
            def project = queue.remove(0)

            queue.addAll(project.childProjects.values())

//            if (project.plugins.hasPlugin('org.jetbrains.kotlin.multiplatform')) {
//                multiplatformProjects.add(project)
//            }
//            if (project.plugins.hasPlugin('kotlin2js')) {
//                jsProjects.add(project)
//            }
//            if (project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('com.android.application')) {
//                androidProjects.add(project)
//            }
//            if (project.plugins.hasPlugin('java-library') || project.plugins.hasPlugin('java')) {
//                javaProjects.add(project)
//            }

            project.configurations.all { config ->
                config.dependencies.each { dependency ->
                    projects.add(project)
                    projects.add(dependency)

                    def graphKey = new Tuple2<Project, Project>(project, dependency)
                    def traits = dependencies.computeIfAbsent(graphKey) { new ArrayList<String>() }

                    // sekarang hanya untuk `implmentation` saja
//                    if (config.name.toLowerCase().endsWith('implementation')) {
//                        traits.add('style=dotted')
//                    }
                }
            }
        }

        projects = projects.sort { it.name }

        // semua projectName di list (tapi karena kecampur sama dependency)
        // untuk module-module yang berubah, mulai versi dari 1
        // dari semua projectName
//        dot << '\n  # Projects\n\n'
        for (project in projects) {
//            def traits = []

//            if (rootProjects.contains(project)) {
//                traits.add('shape=box')
//            }

//            if (multiplatformProjects.contains(project)) {
//                traits.add('fillcolor="#ffd2b3"')
//            } else if (jsProjects.contains(project)) {
//                traits.add('fillcolor="#ffffba"')
//            } else if (androidProjects.contains(project)) {
//                traits.add('fillcolor="#baffc9"')
//            } else if (javaProjects.contains(project)) {
//                traits.add('fillcolor="#ffb3ba"')
//            } else {
//                traits.add('fillcolor="#eeeeee"')
//            }

            // git log in here, kalau tidak kosong
            if(!getVersionName(project.name).isEmpty()){
                def DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

                def lastReleaseDate = new Date().parse(DATE_FORMAT,latestReleaseDate)
                def gitReleaseDate = new Date().parse(DATE_FORMAT,getVersionName(project.name))

                // kalau date terakhir <= date log terakhir
                if(lastReleaseDate <= gitReleaseDate){
                    if(project.properties.artifactId!=null){
                        // karena diri sendiri berubah, maka versi mulai dari 1
                        def CHANGES = 1
                        listVersion.add(new VersionModel(
                                project.name,
                                CHANGES,
                                project.properties.groupId,
                                project.properties.artifactId,
                                project.properties.artifactName))
                    }
                }
            }

//            dot << "  \"${project.name}\" [${traits.join(", ")}];\n"

        }

        listVersion = listVersion.toUnique { a, b -> a.projectName <=> b.projectName }

        //  print rank to dot
//        dot << '\n  {rank = same;'
//        for (project in projects) {
//            if (rootProjects.contains(project)) {
//                dot << " \"${project.name}\";"
//            }
//        }
//        dot << '}\n'

//        dot << '\n  # Dependencies\n\n'
        dependencies.forEach { key, traits ->
            listVersion.each{
                // how does this comparision exist
               if(key.second.name.equals(it.projectName)){

                   listTree.add(new TreeModel(key.second.name,key.first.name))

//                   dot << "  \"${key.second.group}\" -> \"${key.first.name}\""
//                   // fungsi join menambahkan koma untuk lebih dari 1 & di tengah-tengah sebelum akhir
//                   if (!traits.isEmpty()) {
//                       dot << " [${traits.join(", ")}]"
//                   }
//                   dot << '\n'
                }
            }
        }

//        dot << '}\n'

        // create `project.dot`
//        def p = 'dot -Tpng -O project.dot'.execute([], dot.parentFile)
//        p.waitFor()
//        if (p.exitValue() != 0) {
//            throw new RuntimeException(p.errorStream.text)
//        }

        // fix version update if parent is update
        // linear search `nama project` di dependencies & increment count
        // graphql
        listTree.each{ tree ->
            // com.tokopedia.url
            listVersion.each{
                // url adalah depenendency `graphql` maka tambah increment 1.
                if(it.projectName.equals(tree.second)) {
                    it.incrementCount++
                }
            }
        }
//        println("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}