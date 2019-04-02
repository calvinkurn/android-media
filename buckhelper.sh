#!/usr/bin/env bash

initOkBuck( ) {
    if [ -z "$1" ]; then
        echo "Please specify Android SDK Location!"
        exit 1
    fi
    if [ ! -f buckw ]; then
        if [ ! -f gradlew ]; then
            echo "please install gradle!"
        fi
        ./gradlew okbuck
    fi

    if [ -f .buckconfig ]; then
        rm .buckconfig
    fi

    if [ -f .buckjavaargs ]; then
        rm .buckjavaargs
    fi

    touch .buckconfig
    echo "[cache]" >> .buckconfig
    echo "mode = http" >> .buckconfig
    echo "http_url = http://jenkins.tkpd:6457" >> .buckconfig
    echo "http_mode = readwrite" >> .buckconfig
    echo "[android]" >> .buckconfig
    echo "sdk_path = $1" >> .buckconfig

    touch .buckjavaargs
    echo "-Xmx4g" >> .buckjavaargs
}

cleanCache( ) {
    rm -rf .okbuck/
    rm -rf buck-out/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/
}

syncProject( ) {
    ./buckw project --ide intellij
}

buckBuild( ) {
    if [ -z "$1" ]; then
        echo "Please select customerapp / sellerapp !"
        exit 1
    fi
    if [ $1 = "customerapp" ]; then
        ./buckw build //customerapp:bin_liveDevDebug
    elif [ $1 = "sellerapp" ]; then
        ./buckw build //sellerapp:bin_liveDevDebug
    fi
}

buckInstall( ) {
    if [ -z "$1" ]; then
        echo "Please select customerapp / sellerapp !"
        exit 1
    fi
    if [ $1 = "customerapp" ]; then
        ./buckw install -r //customerapp:bin_liveDevDebug
    elif [ $1 = "sellerapp" ]; then
        ./buckw install -r //sellerapp:bin_liveDevDebug
    fi
}

showHelp( ) {
    echo "List Command:"
    echo "init <android_sdk_path> -> init buck config on project"
    echo "clean -> remove cache and buck binary"
    echo "sync -> sync IDE with buck config (equal to gradle sync)"
    echo "build <project> -> build customerapp / sellerapp"
    echo "install <project> -> install costumerapp / sellerapp on connected device"
}

if [ -z "$1" ]; then
    showHelp
    exit 1
fi
if [ $1 = "init" ]; then
    initOkBuck $2
elif [ $1 = "clean" ]; then
    cleanCache
elif [ $1 = "sync" ]; then
    syncProject
elif [ $1 = "build" ]; then
    buckBuild $2
elif [ $1 = "install" ]; then
    buckInstall $2
else
    showHelp
fi