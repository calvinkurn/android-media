import groovy.time.TimeCategory
import org.gradle.api.DefaultTask

class ReadGradlefileTasks extends DefaultTask{
    def subprojects
    ArrayList<VersionModel> listVersion = new ArrayList()
    ArrayList<TreeModel> listTree = new ArrayList()
    HashMap<String, Integer> listModule = new HashMap<>()
    HashMap<String, Integer> listNum = new HashMap<>()
    Graph graph
    Map<String, Integer> versionConfig = new HashMap<String, Integer>()
    def versionMap = new HashMap<String, String>()

    String lastRelease
    
    def readGradleFile(){
        def reader = new File('tools/version/version.gradle')
        def writer = new File('tools/version/version_temp.gradle')

        reader.eachLine{ line ->
            def mark = true
            listVersion.each {
                if (line.trim().startsWith("ext.${it.project}VersionName = ")) {
                    String text = line.replace("ext.${it.project}VersionName = ","").replace("\"","")
                    Float temp = Float.valueOf(text)
                    temp=temp+Float.valueOf(it.version)
                    writer.append("ext.${it.project}VersionName = \"$temp\"\n")
                    mark = false
                }
            }
            if(mark) {
                writer.append("$line\n")
            }
        }
        reader.delete()
        writer.renameTo("tools/version/version.gradle")
        if(!listVersion.empty){
            File file = new File("tools/version/release_date.txt")
            def newdate = new Date().parse("yyyy-MM-dd HH:mm:ss", lastRelease)
            use( TimeCategory ){
                newdate += 30.minutes
            }
            file << "\n"
            file.append(newdate.format("yyyy-MM-dd HH:mm:ss"))
        }
    }
    def topList(){
        int num=0
        subprojects.each{
            listModule.put("${it.name}",num)
            listNum.put(num,"${it.name}")
            num++
        }
        graph = new Graph(listModule.size())
        listTree.each{ tree ->
            graph.addEdge(listModule.get("${tree.first}"),listModule.get("${tree.second}"))
        }
        graph.topologicalSort()

    }
    void copyFile(File source, File dest){
        def src = source
        def dst = dest
        dst << src.text
    }

    void readVersionConfig(){
        def log = new File("tools/version/config_version.txt")

        log.eachLine { line ->
            if(!line.isEmpty()&&!line.startsWith("//")){
                def splits = line.split("=")
                versionConfig.put(splits[0], splits[1].toInteger())
            }
        }
    }

    static String calculateVersion(String currentVersion, Map<String, Integer> config, boolean incrementOrNot){
        def tail = config.get("Tail").toInteger()
        def mid = config.get("Mid").toInteger()
        def head = config.get("Head").toInteger()

        def unit = [mid*tail, tail, 1]

        if(incrementOrNot){
            def valueResult = 0
            def result = [0,0,0]
            def splits = currentVersion.split("\\.")

            // total first
            splits.eachWithIndex { String entry, int i ->
                valueResult += entry.toInteger()*unit[i]
            }

            // increment by one
            valueResult++

            for(int i=0;i<unit.size();i++){
                result[i] = (valueResult/unit[i]).toInteger()
                valueResult %= unit[i]
            }
            return result.join(".")
        }else
            return currentVersion
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
	        def compile_log = new File("${module}/${module}_log.txt")
            backup.delete()
            copyFile(reader, backup)
            reader.eachLine{ line ->
                def tanda = true
                listVersion.each {
                    if ((it.projectName).equals(module) && line.trim().startsWith("versionName = "))  {
                        String text = line.replace("versionName = ","").replace("\"","")
                        def version = calculateVersion(text, versionConfig, it.incrementCount>0)
                        writer.append("    versionName = \"$version\"\n")
                        versionMap.put(it.artifactId, version)
                        tanda = false
                    }else if(line.trim().startsWith("implementation") && line.contains(":")){
                        def text = line.split(":")
                        if((it.artifactId).equals(text[1])){
                            def version = versionMap.get(text[1])
                            writer.append("${text[0]}:${text[1]}:$version\"\n")
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
            compile_log.append(saveStatus)
            if(saveStatus.contains("BUILD SUCCESSFUL")){
                def version = versionMap.get(module)
                log.append("${module} - ${version} - SUCCESSFUL\n")
                return false
            }else{
                def version = versionMap.get(module)
                log.append("${module} - ${version} - FAILED\n")
                returnBackup()
                return true
            }
        }
        if(!listVersion.empty){
            File file = new File("tools/version/release_date.txt")
            def newdate = new Date().parse("yyyy-MM-dd HH:mm:ss", lastRelease)
            use( TimeCategory ){
                newdate += 30.minutes
            }
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