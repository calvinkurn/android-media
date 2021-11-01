# Local AARs with the flexibility to specify dev modules

In development, we can specify which module to use aars, and which modules to use project

## How it's work

1. Generate local maven repository (Manual way)
```
./gradlew publishAarPublicationToMavenRepository -PuseAARForDevBuild=false

```
OR
```
./gradlew clean publishAarPublicationToMavenRepository -PuseAARForDevBuild=false --rerun-tasks
```
This will generate folder with name `localMavenRepository` containing all aars in the project

2. Specify what module to develop
```
useAARForDevBuild=true
modules=customerapp features:entertainment:event
```
put that lines to local.properties.

More modules can be added by <space> separator.

For customerapp only:
Currently, `features:entertainment:event` must be added because of this issue https://issuetracker.google.com/issues/140856013.

For sellerapp can use below lines:
```
useAARForDevBuild=true
modules=sellerapp
```

3. Reload gradle Project

4. We can now can do development for those module we interested on

To build apk, we can use below command:
```
./gradlew bundleLiveDevDebug -p customerapp
bundletool build-apks --bundle=customerapp/build/outputs/bundle/liveDevDebug/customerapp-live-dev-debug.aab --output=customerapp.apks --mode=universal
cp customerapp.apks customerapp.zip
unzip -o customerapp.zip
```