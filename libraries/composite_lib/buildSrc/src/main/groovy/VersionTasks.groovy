import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class VersionTasks extends DefaultTask {
    def listReleaseDate = []
    ArrayList<TreeModel> listTree = new ArrayList()
    ArrayList<VersionModel> listVersion = new ArrayList()
    def codeVersion = 1
    def rootProjectTask
    def getVersionName(String module){
        def stdout = new ByteArrayOutputStream()
        stdout = "git log -1 --pretty=\'%ad\' --date=format:\'%Y-%m-%d,%H:%M:%S\' $module".execute().text
        return stdout.toString().trim().replace("'", "").replace(","," ")
    }
    @TaskAction
    void versionProcessTask(){
        def df = "yyyy-MM-dd HH:mm:ss"
        def dot = new File(rootProjectTask.buildDir, 'reports/dependency-graph/project.dot')
        dot.parentFile.mkdirs()
        dot.delete()

        dot << 'digraph {\n'
        dot << "  graph [label=\"${rootProjectTask.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n"
        dot << '  node [style=filled, fillcolor="#bbbbbb"];\n'
        dot << '  rankdir=TB;\n'

        def rootProjects = []
        def queue = [rootProjectTask]
        while (!queue.isEmpty()) {
            def project = queue.remove(0)
            rootProjects.add(project)
            queue.addAll(project.childProjects.values())
        }

        def projects = new LinkedHashSet<Project>()
        def dependencies = new LinkedHashMap<Tuple2<Project, Project>, List<String>>()
        def multiplatformProjects = []
        def jsProjects = []
        def androidProjects = []
        def javaProjects = []

        queue = [rootProjectTask]
        while (!queue.isEmpty()) {
            def project = queue.remove(0)
            queue.addAll(project.childProjects.values())

            if (project.plugins.hasPlugin('org.jetbrains.kotlin.multiplatform')) {
                multiplatformProjects.add(project)
            }
            if (project.plugins.hasPlugin('kotlin2js')) {
                jsProjects.add(project)
            }
            if (project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('com.android.application')) {
                androidProjects.add(project)
            }
            if (project.plugins.hasPlugin('java-library') || project.plugins.hasPlugin('java')) {
                javaProjects.add(project)
            }

            project.configurations.all { config ->
                config.dependencies.each { dependency ->
                    projects.add(project)
                    projects.add(dependency)
                    //rootProjects.remove(dependency)
                    //println " ${project.path} -> ${dependency.group} "

                    def graphKey = new Tuple2<Project, Project>(project, dependency)
                    def traits = dependencies.computeIfAbsent(graphKey) { new ArrayList<String>() }

                    if (config.name.toLowerCase().endsWith('implementation')) {
                        traits.add('style=dotted')
                    }
                }
            }
        }

        projects = projects.sort { it.name }

        dot << '\n  # Projects\n\n'
        for (project in projects) {
            def traits = []

            if (rootProjects.contains(project)) {
                traits.add('shape=box')
            }

            if (multiplatformProjects.contains(project)) {
                traits.add('fillcolor="#ffd2b3"')
            } else if (jsProjects.contains(project)) {
                traits.add('fillcolor="#ffffba"')
            } else if (androidProjects.contains(project)) {
                traits.add('fillcolor="#baffc9"')
            } else if (javaProjects.contains(project)) {
                traits.add('fillcolor="#ffb3ba"')
            } else {
                traits.add('fillcolor="#eeeeee"')
            }
            if(!getVersionName(project.name).isEmpty()){
                //println project
                //println new Date().parse(df,getVersionName(project.name))
                if(new Date().parse(df,listReleaseDate[listReleaseDate.size()-1]) <= new Date().parse(df,getVersionName(project.name)) && !rootProjects.contains(project.name)){
                    if(!(project.properties.artifactId).equals(null)){
                        listVersion.add(new VersionModel(project.name,1,project.properties.groupId,project.properties.artifactId,project.properties.artifactName))
                    }
                    //println "ini ${project}"
                    println "${project.artifacts} - ${project.version} - ${project.properties.artifactId}"
                }else if(new Date().parse(df,listReleaseDate[listReleaseDate.size()-1]) < new Date().parse(df,getVersionName(project.name)) && !rootProjects.contains(project.name)){
                    if(!(project.properties.artifactId).equals(null)) {
                        listVersion.add(new VersionModel(project.name, 0, project.properties.groupId, project.properties.artifactId, project.properties.artifactName))
                    }
                }
            }

            dot << "  \"${project.name}\" [${traits.join(", ")}];\n"

        }
        def unique = listVersion.toUnique { a, b -> a.project <=> b.project }
        listVersion=unique

        dot << '\n  {rank = same;'
        for (project in projects) {
            if (rootProjects.contains(project)) {
                dot << " \"${project.name}\";"
            }
        }
        dot << '}\n'

        dot << '\n  # Dependencies\n\n'
        dependencies.forEach { key, traits ->
            listVersion.each{
               if(key.second.name.equals(it.project)){
                   listTree.add(new TreeModel(key.second.name,key.first.name))
                   dot << "  \"${key.second.group}\" -> \"${key.first.name}\""
                   if (!traits.isEmpty()) {
                       dot << " [${traits.join(", ")}]"
                   }
                   dot << '\n'
                }
            }
        }

        dot << '}\n'

        def p = 'dot -Tpng -O project.dot'.execute([], dot.parentFile)
        p.waitFor()
        if (p.exitValue() != 0) {
            throw new RuntimeException(p.errorStream.text)
        }
        listTree.each{ tree ->
            listVersion.each{
                if(it.project.equals(tree.second)) {
                    it.version+=codeVersion
                }
            }
        }

        listVersion.each{
            println "${it.project}  - version : ${it.version} "
        }
        println("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}