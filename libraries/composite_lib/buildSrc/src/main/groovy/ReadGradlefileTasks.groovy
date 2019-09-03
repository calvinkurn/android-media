import org.gradle.api.DefaultTask

class ReadGradlefileTasks extends DefaultTask{
    def subprojects
    ArrayList<VersionModel> listVersion = new ArrayList()
    ArrayList<TreeModel> listTree = new ArrayList()
    HashMap<String, Integer> listModule = new HashMap<>()
    HashMap<String, Integer> listNum = new HashMap<>()
    Graph graph

    String lastRelease
    def time = 7
    
    def readGradleFile(){
        def reader = new File('tools/version/version.gradle')
        def writer = new File('tools/version/version_temp.gradle')

        reader.eachLine{ line ->
            println line
            def tanda = true
            listVersion.each {
                if (line.trim().startsWith("ext.${it.project}VersionName = ")) {
                    String text = line.replace("ext.${it.project}VersionName = ","").replace("\"","")
                    Float temp = Float.valueOf(text)
                    temp=temp+Float.valueOf(it.version)
                    writer.append("ext.${it.project}VersionName = \"$temp\"\n")
                    tanda = false
                }
            }
            if(tanda) {
                writer.append("$line\n")
            }
        }
        reader.delete()
        writer.renameTo("tools/version/version.gradle")
        //gradleModuleUpdate()
        if(!listVersion.empty){
            File file = new File("tools/version/release_date.txt")
            println lastRelease
            def newdate = new Date().parse("yyyy-MM-dd HH:mm:ss", lastRelease)
            newdate +=time
            file << "\n"
            file.append(newdate.format("yyyy-MM-dd HH:mm:ss"))
        }
    }
    def topList(){
        int num=0
        subprojects.each{
            listModule.put("${it.name}",num)
            listNum.put(num,"${it.name}")
           // println " ${it.name} $num"
            num++
        }
        graph = new Graph(listModule.size())
        listTree.each{ tree ->
            graph.addEdge(listModule.get("${tree.first}"),listModule.get("${tree.second}"))
            println listModule.get("${tree.first}")
            println listModule.get("${tree.second}")
            println "${tree.first} - ${tree.second}"
        }
        graph.topologicalSort()

    }
    void copyFile(File source, File dest){
        def src = source
        def dst = dest
        dst << src.text
    }

    void moduleVersionUpdate(){
        def listst = graph.getListTop()
        def log = new File("tools/version/log.txt")
        log.delete()
        listst.find {
            String module = listNum.get(it)
            def reader = new File("${module}/build.gradle")
            def backup = new File("${module}/.build_backup")
            def writer = new File("${module}/build_temp.gradle")
            backup.delete()
            copyFile(reader, backup)
            reader.eachLine{ line ->
                //println line
                def tanda = true
                listVersion.each {
                    if ((it.projectName).equals(module) && line.trim().startsWith("versionName = "))  {
                        String text = line.replace("versionName = ","").replace("\"","")
                        Float temp = Float.valueOf(text)
                        temp=temp+Float.valueOf(it.incrementCount)
                        writer.append("    versionName = \"$temp\"\n")
                        tanda = false
                    }else if(line.trim().startsWith("implementation") && line.contains(":")){
                        def text = line.split(":")
                        if((it.artifactId).equals(text[1])){
                            Float temp = Float.valueOf(text[2].replace("\"",""))
                            temp=temp+Float.valueOf(it.version)
                            writer.append("${text[0]}:${text[1]}:$temp\"\n")
                            tanda = false
                        }
                    }
                }
                if(tanda) {
                    writer.append("$line\n")
                }
            }
            reader.delete()
            writer.renameTo("${module}/build.gradle")
            def saveStatus =  compileModule(module)
            println saveStatus
            if(saveStatus.contains("BUILD SUCCESSFUL")){
                log.append("${module} - SUCCESSFUL\n")
                return false
            }else{
                log.append("${module} - FAILED\n")
                returnBackup()
                return true
            }
        }
        if(!listVersion.empty){
            File file = new File("tools/version/release_date.txt")
            println lastRelease
            def newdate = new Date().parse("yyyy-MM-dd HH:mm:ss", lastRelease)
            newdate +=time
            file << "\n"
            file.append(newdate.format("yyyy-MM-dd HH:mm:ss"))
        }
    }
    def returnBackup(){
        def listst = graph.getListTop()
        logger.info('Return backup build.gradle proses.')
        listst.each {
            String module = listNum.get(it)
            def reader = new File("${module}/build.gradle")
            def backup = new File("${module}/.build_backup")
            if(backup.exists()){
                reader.delete()
                backup.renameTo("${module}/build.gradle")
            }else {
                backup.delete()
            }
        }
    }
    def compileModule(String module){
        def stdout = new ByteArrayOutputStream()
        stdout = "./gradlew assemble artifactoryPublish  -p $module --parallel -x lint --stacktrace".execute().text
        return stdout.toString().trim().replace("'", "").replace(","," ")
    }
}