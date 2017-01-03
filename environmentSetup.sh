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
	echo "Run Gradle wrapper task...."
	gradle wrapper
	echo "FINISH!"
    fi
}
