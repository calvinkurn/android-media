#!/usr/bin/env bash

function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle Properties does not exist"

    	echo "Creating gradle folder"
    	mkdir $HOME/.gradle

        echo "Creating Gradle Properties file..."
        touch $GRADLE_PROPERTIES

        echo "Writing ARTIFACTORY CREDENTIALS to gradle.properties..."
        echo "artifactory_username=$ARTIFACTORY_USERNAME_ENV" >> $GRADLE_PROPERTIES
    	echo "artifactory_password=$ARTIFACTORY_PASSWORD_ENV" >> $GRADLE_PROPERTIES
    	echo "artifactory_url=$ARTIFACTORY_URL_ENV" >> $GRADLE_PROPERTIES
    fi
}

function setupGradleWrapper {
    echo "Delete old gradle wrapper"
    rm gradle/wrapper/gradle-wrapper.properties
    
    echo "Creating new gradle wrapper"
    touch gradle/wrapper/gradle-wrapper.properties

    echo "distributionBase=GRADLE_USER_HOME" >> gradle/wrapper/gradle-wrapper.properties
    echo "distributionPath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties
    echo "zipStoreBase=GRADLE_USER_HOME" >> gradle/wrapper/gradle-wrapper.properties
    echo "zipStorePath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties
    echo "distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip" >> gradle/wrapper/gradle-wrapper.properties
}

function getGradle {
    echo "Downloading gradle"
    curl -s https://get.sdkman.io | bash

    sdk install gradle 2.14.1

    echo "FINISH!!"

}


