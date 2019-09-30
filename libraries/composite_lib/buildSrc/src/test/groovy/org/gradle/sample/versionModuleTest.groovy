//
//import org.gradle.testkit.runner.GradleRunner
//import spock.lang.Unroll
//
//import static org.gradle.testkit.runner.TaskOutcome.*
//import org.junit.Rule
//import org.junit.rules.TemporaryFolder
//import spock.lang.Specification
//
//class versionModuleTest extends Specification {
//    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
//    File buildFileModuleA
//    File buildFileModuleB
//    File buildFileModuleC
//    File buildFileModuleD
//    File settingsFile
//    File local
//    File log
//    File buildFile
//    File releaseDateFile
//
//    def  treeModel
//    def  versionModel
//    Graph graph
//
//    def setup() {
//        settingsFile = testProjectDir.newFile('settings.gradle')
//        buildFile = testProjectDir.newFile('build.gradle')
//        releaseDateFile = testProjectDir.newFile('release_date.txt')
//        local = testProjectDir.newFile('local.properties')
//        testProjectDir.newFolder("modulea")
//        testProjectDir.newFolder("moduleb")
//        testProjectDir.newFolder("modulec")
//        testProjectDir.newFolder("moduled")
//        buildFileModuleA = testProjectDir.newFile('modulea/build.gradle')
//        buildFileModuleB = testProjectDir.newFile('moduleb/build.gradle')
//        buildFileModuleC = testProjectDir.newFile('modulec/build.gradle')
//        buildFileModuleD = testProjectDir.newFile('moduled/build.gradle')
//        log = testProjectDir.newFile('log.txt')
//    }
//    @Unroll
//    def "test tree model"() {
//        when:
//        treeModel = new TreeModel("test1","test2")
//
//        then:
//        assert treeModel.first=="test1"
//        assert treeModel.second=="test2"
//    }
//
//    @Unroll
//    def "test incrementCount model"() {
//        when:
//        versionModel = new VersionModel("projectName",3, "com.tokopedia.graphql", "graphql","graphql")
//
//        then:
//        assert versionModel.projectName=="projectName"
//        assert versionModel.incrementCount==3
//        assert versionModel.artifactId=="graphql"
//        assert versionModel.artifactName=="graphql"
//        assert versionModel.groupId=="com.tokopedia.graphql"
//    }
//
//    @Unroll
//    def "test graph toposort"() {
//        when:
//        graph = new Graph(6)
//        graph.addEdge(5, 2);
//        graph.addEdge(5, 0);
//        graph.addEdge(4, 0);
//        graph.addEdge(4, 1);
//        graph.addEdge(2, 3);
//        graph.addEdge(3, 1);
//        graph.topologicalSort()
//
//        then:
//        def answer = [5, 4, 2, 3, 1, 0]
//        int i = 0
//        graph.listTop.each {
//            assert it == answer[i]
//            i++
//        }
//    }
//
//
//    @Unroll
//    def "can execute scan release date task"() {
//        given:
//        settingsFile << "rootProject.name = 'test'"
//        releaseDateFile << """
//        2019-06-09 00:00:00\n
//        2019-07-26 00:00:00
//        """
//
//        buildFile << """
//        class ScanReleaseDateTasks extends DefaultTask{
//            def listReleaseDate = []
//            String fileDir
//
//            @TaskAction
//            def scanReleaseDateProcess(){
//                new File(fileDir).eachLine { line ->
//                    listReleaseDate.add(line)
//                }
//            }
//
//
//        }
//
//        task scanReleaseDate(type: ScanReleaseDateTasks){
//            fileDir = '${releaseDateFile.path}'
//            doLast{
//                println "scanInput Proses"
//            }
//        }
//        """
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('scanReleaseDate')
//                .build()
//
//        then:
//        result.output.contains('scanInput Proses')
//        result.task(":scanReleaseDate").outcome == SUCCESS
//    }
//
//    @Unroll
//    def "can execute scan subproject task"() {
//        given:
//        settingsFile << "rootProject.name = 'test'"
//
//        buildFile << """
//        class ScanSubprojectTasks extends DefaultTask{
//            def listModule = []
//
//            @TaskAction
//            void scanSubProject(){
//                projectName.subprojects.each { p ->
//                    listModule.add(p.path.substring(1))
//                }
//            }
//
//        }
//        task scanSubproject(type: ScanSubprojectTasks){}
//        """
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('scanSubproject')
//                .build()
//
//        then:
//        result.task(":scanSubproject").outcome == SUCCESS
//    }
//
//
//
//    @Unroll
//    def "can execute incrementCount task"() {
//        given:
//        local << "sdk.dir=/Users/nakama/Library/Android/sdk"
//
//        settingsFile << """
//        rootProject.name = 'test'
//        include ":modulea"
//        include ":moduleb"
//        include ":modulec"
//        include ":moduled"
//        """
//        releaseDateFile << """
//        2019-06-09 00:00:00\n
//        2019-07-26 00:00:00"""
//
//        buildFileModuleA << """
//        apply plugin: 'com.android.library'
//
//        projectName.ext {
//            artifactId = 'modulea'
//            groupId = 'com.tokopedia.modulea'
//            versionName = "1"
//            artifactName = "modulea"
//        }
//        android {
//            compileSdkVersion 29
//            buildToolsVersion "29.0.0"
//            defaultConfig {
//                minSdkVersion 21
//                targetSdkVersion 29
//                versionCode 1
//                versionName "1.0"
//            }
//        }
//
//        dependencies {
//            implementation "com.tokopedia.moduleb:moduleb:1"
//            implementation "com.tokopedia.moduleb:moduled:1"
//        }
//        """
//        buildFileModuleB << """
//                apply plugin: 'com.android.library'
//
//        projectName.ext {
//            artifactId = 'moduleb'
//            groupId = 'com.tokopedia.moduleb'
//            versionName = "1"
//            artifactName = "moduleb"
//        }
//        android {
//            compileSdkVersion 29
//            buildToolsVersion "29.0.0"
//            defaultConfig {
//                minSdkVersion 21
//                targetSdkVersion 29
//                versionCode 1
//                versionName "1.0"
//            }
//
//        }
//        dependencies {
//            implementation "com.tokopedia.modulec:modulec:1"
//        }
//        """
//        buildFileModuleC << """
//        apply plugin: 'com.android.library'
//
//        projectName.ext {
//            artifactId = 'modulec'
//            groupId = 'com.tokopedia.modulec'
//            versionName = "1"
//            artifactName = "modulec"
//        }
//        android {
//            compileSdkVersion 29
//            buildToolsVersion "29.0.0"
//            defaultConfig {
//                minSdkVersion 21
//                targetSdkVersion 29
//                versionCode 1
//                versionName "1.0"
//            }
//
//        }
//        """
//        buildFileModuleD << """
//        apply plugin: 'com.android.library'
//
//        projectName.ext {
//            artifactId = 'moduled'
//            groupId = 'com.tokopedia.moduled'
//            versionName = "1"
//            artifactName = "moduled"
//        }
//        android {
//            compileSdkVersion 29
//            buildToolsVersion "29.0.0"
//            defaultConfig {
//                minSdkVersion 21
//                targetSdkVersion 29
//                versionCode 1
//                versionName "1.0"
//            }
//
//        }
//        """
//        buildFile << """
//                buildscript {
//            repositories {
//                google()
//                jcenter()
//                mavenCentral()
//
//
//            }
//            dependencies {
//                classpath 'com.android.tools.build:gradle:3.4.1'
//            }
//            repositories {
//                google()
//            }
//        }
//        plugins { id 'base' }
//
//        repositories {
//            jcenter()
//        }
//
//        class ScanReleaseDateTasks extends DefaultTask{
//            def listReleaseDate = []
//            String fileDir
//
//            @TaskAction
//            def scanReleaseDateProcess(){
//                new File(fileDir).eachLine { line ->
//                    listReleaseDate.add(line)
//                }
//            }
//        }
//        class ScanSubprojectTasks extends DefaultTask{
//            def listModule = []
//
//            @TaskAction
//            void scanSubProject(){
//                projectName.subprojects.each { p ->
//                    listModule.add(p.path.substring(1))
//                }
//            }
//
//        }
//        class TreeModel {
//            def first
//            def second
//
//            TreeModel(def first, def second){
//                this.first = first
//                this.second = second
//            }
//
//        }
//        class VersionModel {
//            def projectName
//            float incrementCount
//            def groupId
//            def artifactId
//            def artifactName
//            VersionModel(def projectName, int incrementCount, def groupId, def artifactId, def artifactName){
//                this.projectName = projectName
//                this.incrementCount = incrementCount
//                this.groupId = groupId
//                this.artifactId = artifactId
//                this.artifactName = artifactName
//            }
//        }
//        class Graph
//        {
//            private int V;   // No. of vertices
//            private LinkedList<Integer> adj; // Adjacency List
//            private ArrayList<Integer> listTop = new ArrayList<>();
//
//            //Constructor
//            Graph(int v)
//            {
//                V = v;
//                adj = new LinkedList[v];
//                for (int i=0; i<v; ++i)
//                    adj[i] = new LinkedList();
//            }
//
//            public List<Integer> getListTop() {
//                return listTop;
//            }
//
//            // Function to add an edge into the graph
//            void addEdge(int v,int w) { adj[v].add(w); }
//
//            // A recursive function used by topologicalSort
//            void topologicalSortUtil(int v, List<Boolean> visited,
//                                     Stack stack)
//            {
//                // Mark the current node as visited.
//                visited[v] = true;
//                Integer i;
//
//                // Recur for all the vertices adjacent to this
//                // vertex
//                Iterator<Integer> it = adj[v].iterator();
//                while (it.hasNext())
//                {
//                    i = it.next();
//                    if (!visited[i])
//                        topologicalSortUtil(i, visited, stack);
//                }
//
//                // Push current vertex to stack which stores result
//                stack.push(new Integer(v));
//            }
//
//            // The function to do Topological Sort. It uses
//            // recursive topologicalSortUtil()
//            void topologicalSort()
//            {
//                Stack stack = new Stack();
//
//                // Mark all the vertices as not visited
//                List<Boolean> visited = new boolean[V];
//                for (int i = 0; i < V; i++){
//                    visited[i] = false;
//                }
//
//                for (int i = 0; i < V; i++){
//                    if (visited[i] == false){
//                        topologicalSortUtil(i, visited, stack);
//                    }
//                }
//
//                // Print contents of stack
//                while (stack.empty()==false){
//                    Integer element = (Integer) stack.pop();
//                    listTop.add(element);
//                }
//            }
//        }
//
//        class VersionTasks extends DefaultTask {
//            def listReleaseDate = []
//            ArrayList<TreeModel> listTree = new ArrayList()
//            ArrayList<VersionModel> listVersion = new ArrayList()
//            def codeVersion = 1
//            def rootProjectTask
//            def getVersionName(String module){
//                return "2019-06-09 00:00:00"
//            }
//            @TaskAction
//            void versionProcessTask(){
//                def df = "yyyy-MM-dd HH:mm:ss"
//
//                def rootProjects = []
//                def queue = [rootProjectTask]
//                while (!queue.isEmpty()) {
//                    def projectName = queue.remove(0)
//                    rootProjects.add(projectName)
//                    queue.addAll(projectName.childProjects.values())
//                }
//
//                def projects = new LinkedHashSet<Project>()
//                def dependencies = new LinkedHashMap<Tuple2<Project, Project>, List<String>>()
//                def multiplatformProjects = []
//                def jsProjects = []
//                def androidProjects = []
//                def javaProjects = []
//
//                queue = [rootProjectTask]
//                while (!queue.isEmpty()) {
//                    def projectName = queue.remove(0)
//                    queue.addAll(projectName.childProjects.values())
//
//                    if (projectName.plugins.hasPlugin('org.jetbrains.kotlin.multiplatform')) {
//                        multiplatformProjects.add(projectName)
//                    }
//                    if (projectName.plugins.hasPlugin('kotlin2js')) {
//                        jsProjects.add(projectName)
//                    }
//                    if (projectName.plugins.hasPlugin('com.android.library') || projectName.plugins.hasPlugin('com.android.application')) {
//                        androidProjects.add(projectName)
//                    }
//                    if (projectName.plugins.hasPlugin('java-library') || projectName.plugins.hasPlugin('java')) {
//                        javaProjects.add(projectName)
//                    }
//
//                    projectName.configurations.all { config ->
//                        config.dependencies.each { dependency ->
//                            projects.add(projectName)
//                            projects.add(dependency)
//                            def graphKey = new Tuple2<Project, Project>(projectName, dependency)
//                            def traits = dependencies.computeIfAbsent(graphKey) { new ArrayList<String>() }
//
//                            if (config.name.toLowerCase().endsWith('implementation')) {
//                                traits.add('style=dotted')
//                            }
//                        }
//                    }
//                }
//
//                projects = projects.sort { it.name }
//
//                for (projectName in projects) {
//                    def traits = []
//
//                    if (rootProjects.contains(projectName)) {
//                        traits.add('shape=box')
//                    }
//
//                    if (multiplatformProjects.contains(projectName)) {
//                        traits.add('fillcolor="#ffd2b3"')
//                    } else if (jsProjects.contains(projectName)) {
//                        traits.add('fillcolor="#ffffba"')
//                    } else if (androidProjects.contains(projectName)) {
//                        traits.add('fillcolor="#baffc9"')
//                    } else if (javaProjects.contains(projectName)) {
//                        traits.add('fillcolor="#ffb3ba"')
//                    } else {
//                        traits.add('fillcolor="#eeeeee"')
//                    }
//                    if(!getVersionName(projectName.name).isEmpty()){
//                        if(new Date().parse(df,listReleaseDate[listReleaseDate.size()-1]) <= new Date().parse(df,getVersionName(projectName.name)) && !rootProjects.contains(projectName.name)){
//                            if(!(projectName.properties.artifactId).equals(null)){
//                                listVersion.add(new VersionModel(projectName.name,1,projectName.properties.groupId,projectName.properties.artifactId,projectName.properties.artifactName))
//                            }
//                        }else if(new Date().parse(df,listReleaseDate[listReleaseDate.size()-1]) < new Date().parse(df,getVersionName(projectName.name)) && !rootProjects.contains(projectName.name)){
//                            if(!(projectName.properties.artifactId).equals(null)) {
//                                listVersion.add(new VersionModel(projectName.name, 0, projectName.properties.groupId, projectName.properties.artifactId, projectName.properties.artifactName))
//                            }
//                        }
//                    }
//
//                }
//                def unique = listVersion.toUnique { a, b -> a.projectName <=> b.projectName }
//                listVersion=unique
//
//
//                dependencies.forEach { key, traits ->
//
//                    listVersion.each{
//                        if(key.second.name.equals(it.projectName)){
//                            listTree.add(new TreeModel(key.second.name,key.first.name))
//                        }
//                    }
//                }
//                listTree.each{ tree ->
//                    listVersion.each{
//                        if(it.projectName.equals(tree.second)) {
//                            it.incrementCount+=codeVersion
//                        }
//                    }
//                }
//            }
//        }
//        class ReadGradlefileTasks extends DefaultTask{
//            def subprojects
//            ArrayList<VersionModel> listVersion = new ArrayList()
//            ArrayList<TreeModel> listTree = new ArrayList()
//            HashMap<String, Integer> listModule = new HashMap<>()
//            HashMap<String, Integer> listNum = new HashMap<>()
//            Graph graph
//
//            String lastRelease
//            def time = 7
//
//            def topList(){
//                int num=0
//                subprojects.each{
//                    listModule.put(it.name,num)
//                    listNum.put(num,it.name)
//                    num++
//                }
//                graph = new Graph(listModule.size())
//                listTree.each{ tree ->
//                    graph.addEdge(listModule.get(tree.first),listModule.get(tree.second))
//                    println listModule.get(tree.first)
//                    println listModule.get(tree.second)
//                }
//                graph.topologicalSort()
//
//            }
//            void copyFile(File source, File dest){
//                def src = source
//                def dst = dest
//                dst << src.text
//            }
//
//            void moduleVersionUpdate(){
//                def listst = graph.getListTop()
//                def log = new File("$log.path")
//                log.delete()
//                listst.find {
//                    String module = listNum.get(it)
//                    def reader = new File("$testProjectDir.root/"+module+"/build.gradle")
//                    def backup = new File("$testProjectDir.root/"+module+"/.build_backup")
//                    def writer = new File("$testProjectDir.root/"+module+"/build_temp.gradle")
//                    backup.delete()
//                    copyFile(reader, backup)
//                    reader.eachLine{ line ->
//                        def tanda = true
//                        listVersion.each {
//                            if ((it.projectName).equals(module) && line.trim().startsWith("versionName = "))  {
//                                String text = line.replace("versionName = ","").replace("\\\\","")
//                                        Float temp = Float.valueOf(text)
//                                        temp=temp+Float.valueOf(it.incrementCount)
//                                        writer.append("    versionName = \\\\'"+temp+"\\\\'\\\\n")
//                                        tanda = false
//                            }else if(line.trim().startsWith("implementation") && line.contains(":")){
//                                def text = line.split(":")
//                                if((it.artifactId).equals(text[1])){
//                                    Float temp = Float.valueOf(text[2].replace("\\\\'",""))
//                                            temp=temp+Float.valueOf(it.incrementCount)
//                                            writer.append(text[0]+":"+text[1]+":"+temp+"\\\\'\\\\n")
//                                                    tanda = false
//                                }
//                            }
//                        }
//                        if(tanda) {
//                            writer.append(line+"\\\\n")
//                        }
//                    }
//                    reader.delete()
//                    writer.renameTo("$testProjectDir.root/"+module+"/build.gradle")
//                    String saveStatus =  compileModule(module)
//                    println saveStatus
//                    if(saveStatus.contains("BUILD SUCCESSFUL")){
//                        log.append(module+" - SUCCESSFUL\\\\n")
//                        return false
//                    }else{
//                        log.append(module+" - FAILED\\\\n")
//                        returnBackup()
//                        return true
//                    }
//                }
//                if(!listVersion.empty){
//                    File file = new File("release_date.txt")
//                    println lastRelease
//                    def newdate = new Date().parse("yyyy-MM-dd HH:mm:ss", lastRelease)
//                    newdate +=time
//                    file << "\\\\n"
//                    file.append(newdate.format("yyyy-MM-dd HH:mm:ss"))
//                }
//            }
//            def returnBackup(){
//                def listst = graph.getListTop()
//                logger.info('Return backup build.gradle proses.')
//                listst.each {
//                    String module = listNum.get(it)
//                    def reader = new File("$testProjectDir.root/"+module+"/build.gradle")
//                    def backup = new File("$testProjectDir.root/"+module+"/.build_backup")
//                    if(backup.exists()){
//                        reader.delete()
//                        backup.renameTo("$testProjectDir.root/"+module+"/build.gradle")
//                    }else {
//                        backup.delete()
//                    }
//                }
//            }
//            def compileModule(String module){
//                return "BUILD SUCCESSFUL"
//            }
//        }
//        task scanSubproject(type: ScanSubprojectTasks){
//        }
//        task scanReleaseDate(type: ScanReleaseDateTasks,dependsOn: scanSubproject){
//            fileDir = '${releaseDateFile.path}'
//            doLast{
//                println "scanInput Proses"
//            }
//        }
//        task readGradlefile(type: ReadGradlefileTasks){
//            subprojects = projectName.subprojects
//            doLast{
//                lastRelease = scanReleaseDate.listReleaseDate[scanReleaseDate.listReleaseDate.size()-1]
//                listVersion = versionDependency.listVersion
//                listTree = versionDependency.listTree
//                topList()
//                moduleVersionUpdate()
//            }
//        }
//
//        task versionDependency(type: VersionTasks, dependsOn: scanReleaseDate) {
//            listReleaseDate = scanReleaseDate.listReleaseDate
//            finalizedBy readGradlefile
//            rootProjectTask = rootProject
//            doLast {
//            }
//        }
//
//        """
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('versionDependency')
//                .build()
//
//        then:
//        result.task(":scanSubproject").outcome == SUCCESS
//        result.task(":scanReleaseDate").outcome == SUCCESS
//        result.task(":versionDependency").outcome == SUCCESS
//        result.task(":readGradlefile").outcome == SUCCESS
//
//    }
//
//    @Unroll
//    def "can execute read gradle task"() {
//        given:
//        settingsFile << "rootProject.name = 'test'"
//
//        buildFile << """
//        class ScanSubprojectTasks extends DefaultTask{
//            def listModule = []
//
//            @TaskAction
//            void scanSubProject(){
//                projectName.subprojects.each { p ->
//                    listModule.add(p.path.substring(1))
//                }
//            }
//
//        }
//        task scanSubproject(type: ScanSubprojectTasks){}
//        """
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('scanSubproject')
//                .build()
//
//        then:
//        result.task(":scanSubproject").outcome == SUCCESS
//    }
//}
//
