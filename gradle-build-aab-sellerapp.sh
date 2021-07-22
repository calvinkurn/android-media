#!/usr/bin/env bash
SELLERAPP=sellerapp/build/outputs/bundle/liveDevDebug
APKS=$SELLERAPP/sellerapp.apks
APK=$SELLERAPP/universal.apk
AAB=$SELLERAPP/sellerapp-live-dev-debug.aab
ZIP=$SELLERAPP/sellerapp.zip
JAR=~/Library/Bundletool/bundletool-all-1.5.0.jar
# shellcheck disable=SC2046
kill $(ps aux | grep "java" | grep -v 'grep' | awk '{print $2}')
rm $AAB $APKS $ZIP $APK
./tools/aab/install.sh :sellerapp:bundleLiveDevDebug -install-mode=universal-apk -launch=com.tokopedia.tkpd/com.tokopedia.tkpd.ConsumerSplashScreen && gradle --stop && pKill -9 java && pKill -D java
java -jar $JAR build-apks --bundle=$AAB --output=$APKS --mode=universal
mv $APKS $ZIP
unzip $ZIP -d $SELLERAPP
adb install -r $APK