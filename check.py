import sys, getopt, os, json

def main(argv):
    token = None
    config = None
    head = None
    user = None
    gradle = "./gradlew" 
    adb = "adb"
    deviceId = "galaxy.tkpd:7545"

    try:
        opts, args = getopt.getopt(argv,"h",["token=","config=", "head=", "user=", "gradle=", "adb=", "deviceId="])
    except getopt.GetoptError:
        print("System Error")
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            sys.exit()
        elif opt in ("--token"):
            token  = arg
        elif opt in ("--config"):
            config = arg
        elif opt in ("--head"):
            head = arg
        elif opt in ("--user"):
            user = arg
        elif opt in ("--gradle"):
            gradle = arg
        elif opt in ("--adb"):
            adb = arg
        elif opt in ("--deviceId"):
            deviceId = arg

    if token is None or config is None or head is None :
        print 'Input value has been detected as null'
        print 'check.py --token <git auth token> --config <configuration file\'s path> --head <head branch>'
        sys.exit(1)
    else:
        detectAffectedModule(token, config, head, user, gradle, adb, deviceId)

def detectAffectedModule(token, config, head, user, gradle, adb, deviceId):
    configuration = getConfig(config)
    if configuration is None :
        exit(1)
    doCommand("git fetch " + configuration["remote_name"])
    doCommand("git checkout " + configuration["master"])
    doCommand("git pull " + configuration["remote_name"] + " " + configuration["master"])
    doCommand("git checkout " + head)
    doCommand("git pull " + configuration["remote_name"] + " " + head)
    doCommand("git diff --name-only " + head + ".." + configuration["master"] + " > file_changes.log")
    f = open("./file_changes.log", "r")
    modulesAffected = []
    pathAffected = []
    doCommand("rm -rf coverageResults")
    doCommand("mkdir coverageResults")

    for path in configuration["modules"]:
        for x in f:
            if x.startswith(path["path"]) and path["module_name"] not in modulesAffected:
                modulesAffected.append(path["module_name"])
                pathAffected.append([path["path"], path["name"]])

    doCommand(adb + " connect " + deviceId)
    for module in modulesAffected:
        print doCommand(gradle + " " + module + "customjacocoTestCoverageVerification --stacktrace")

    doCommand(gradle + " sonarqube -Dsonar.host.url=http://10.164.8.12:9111")
    
    for path in pathAffected:
        doCommand("zip -r " + path[1] + "Result.zip ./" + path[0] + "build/reports/coverage/debug")
        doCommand("cp ./" + path[1] + "Result.zip ./coverageResults")

def doCommand(command):
    print("> " + command + "\n")
    return os.system(command)

def getConfig(config):
    data = None
    with open(config) as f:
        data = json.load(f)
    return data


if __name__ == '__main__':
    main(sys.argv[1:])
