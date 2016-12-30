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
        echo "artifactory_username=apps" >> $GRADLE_PROPERTIES
	echo "artifactory_password=tokopedia789" >> $GRADLE_PROPERTIES
    fi
}
