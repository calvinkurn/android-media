import xml.etree.ElementTree as ET

# def main():
#     filename = "./customerapp/src/main/AndroidManifest.xml"
#     ET.register_namespace('android', 'http://schemas.android.com/apk/res/android')
#     ET.register_namespace('tools', 'http://schemas.android.com/tools')
#     tree = ET.parse(filename)
#     root = tree.getroot()
#     application = root.find('application')
#
#     md_channel = ET.SubElement(application, 'meta-data')
#     md_channel.set("android:name", "CHANNEL")
#     md_channel.set("android:value", "store")
#
#     md_af = ET.SubElement(application, 'meta-data')
#     md_af.set("android:name", "AF_PRE_INSTALL_NAME")
#     md_af.set("android:value", "store")
#
#     print ET.tostring(root)
#     tree.write(filename)
#
#     f = open(filename,'r')
#     temp = f.read()
#     f.close()
#
#     f = open(filename, 'w')
#     f.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
#     f.write("\n")
#     f.write(temp)
#     f.close()
import sys, getopt

def main(argv):
    android_manifest = ''
    name = ''
    channel = ''
    try:
        opts, args = getopt.getopt(argv,"ham:n:c",["android-manifest=","name=","channel="])
    except getopt.GetoptError:
        print 'preinstall.py -am <android manifest path> -n <name> -c <channel>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'preinstall.py -am <android manifest path> -n <name> -c <channel>'
            sys.exit()
        elif opt in ("-am", "--android-manifest"):
            android_manifest = arg
        elif opt in ("-n", "--name"):
            name = arg
        elif opt in ("-c", "--channel"):
            channel = arg

    print android_manifest + " " + name + " " + channel


#     "oppo_int", "oppo_preinstall","oppo_store_site"



if __name__ == '__main__':
    main(sys.argv[1:])