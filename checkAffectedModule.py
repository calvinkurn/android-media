import sys, getopt, os, json

def main(argv):
    head = None
    token = None
    config = None
    try:
        opts, args = getopt.getopt(argv,"h",["token=","config=", "head="])
    except getopt.GetoptError:
        print 'checkAffectedModule.py --token <git auth token> --config <configuration file\'s path> --head <head branch>'
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print 'checkAffectedModule.py --token <git auth token> --config <configuration file\'s path> --head <head branch>'
            sys.exit()
        elif opt in ("--head"):
            head = arg
        elif opt in ("--token"):
            token  = arg
        elif opt in ("--config"):
            config = arg

    if token is None or config is None or head is None :
        print 'Input value has been detected as null'
        print 'checkAffectedModule.py --token <git auth token> --config <configuration file\'s path> --head <head branch>'
        sys.exit(1)
    else:
        detectAffectedModule(token, config, head)

def detectAffectedModule(token, config, head):
    configuration = getConfig(config)
    if configuration is None :
        exit(1)
    os.system("git fetch " + configuration["remote_name"])
    os.system("git diff --name-only " + head + ".." + configuration["master"] + " > file_changes.log")
    f = open("./file_changes.log", "r")
    for path in configuration["paths"]:
        os.system("git diff --name-only " + head + ".." + configuration["master"] + " | grep " + path)
        for x in f:
            if x.startswith(path):
                print("module affected")
                sys.exit(0)

    print("no module affected")
    sys.exit(1)
        
def getConfig(config):
    data = None
    with open(config) as f:
        data = json.load(f)
    return data


if __name__ == '__main__':
    main(sys.argv[1:])
