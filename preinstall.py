import xml.etree.ElementTree as ET

import sys, getopt

def main(argv):
    android_manifest = ''
    name = ''
    channel = ''

    try:
        opts, args = getopt.getopt(argv,"h",["android-manifest=","name=","channel="])
    except getopt.GetoptError:
        print 'preinstall.py --android-manifest <android manifest path> --name <name> --channel <channel>'
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print 'preinstall.py -am <android manifest path> -n <name> -c <channel>'
            sys.exit()
        elif opt in ("--android-manifest"):
            android_manifest = arg
        elif opt in ("--name"):
            name = arg
        elif opt in ("--channel"):
            channel = arg

    if(android_manifest is None or name is None or channel is None):
        print 'preinstall.py -am <android manifest path> -n <name> -c <channel>'
        sys.exit(1)
    else:
        appendManifest(android_manifest, name, channel)

def appendManifest(android_manifest, name, channel):
    ET.register_namespace('android', 'http://schemas.android.com/apk/res/android')
    ET.register_namespace('tools', 'http://schemas.android.com/tools')
    tree = ET.parse(android_manifest)
    root = tree.getroot()
    application = root.find('application')

    md_channel = ET.SubElement(application, 'meta-data')
    md_channel.set("android:name", "CHANNEL")
    md_channel.set("android:value", channel)

    md_af = ET.SubElement(application, 'meta-data')
    md_af.set("android:name", "AF_PRE_INSTALL_NAME")
    md_af.set("android:value", name) 

    print ET.tostring(root)
    tree.write(android_manifest)

    f = open(android_manifest,'r')
    temp = f.read()
    f.close()

    f = open(android_manifest, 'w')
    f.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
    f.write("\n")
    f.write(temp)
    f.close()
    #"oppo_int", "oppo_preinstall","oppo_store_site"



if __name__ == '__main__':
    main(sys.argv[1:])
