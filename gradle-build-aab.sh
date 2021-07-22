#!/usr/bin/env bash
CUSTOMERAPP=customerapp/build/outputs/bundle/liveDevDebug
APKS=$CUSTOMERAPP/customerapp.apks
APK=$CUSTOMERAPP/universal.apk
AAB=$CUSTOMERAPP/customerapp-live-dev-debug.aab
ZIP=$CUSTOMERAPP/customerapp.zip
JAR=~/Library/Bundletool/bundletool-all-1.5.0.jar
# shellcheck disable=SC2046
kill $(ps aux | grep "java" | grep -v 'grep' | awk '{print $2}')
rm $AAB $APKS $ZIP $APK
./tools/aab/install.sh :customerapp:bundleLiveDevDebug -install-mode=universal-apk -launch=com.tokopedia.tkpd/com.tokopedia.tkpd.ConsumerSplashScreen && gradle --stop && pKill -9 java && pKill -D java
java -jar $JAR build-apks --bundle=$AAB --output=$APKS --mode=universal
mv $APKS $ZIP
unzip $ZIP -d $CUSTOMERAPP
adb install -r $APK