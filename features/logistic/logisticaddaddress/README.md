## Logistic Module

This is a feature module in which contains pages from logistic in user scope.
logistic module consist of the following feature/page:

- Address List (Account)
- Legacy Add New Address
- Legacy Pinpoint Picker
- District Recommendation
- New Add New Address


### Testing
End-to-end test is located within module's androidTest source dirs to separate the concern and reach independent of app module,
However, the test is usually executed from testapp module with the following configuration:

Add the following to testapp's build.gradle file
```
androidTest {
    java.srcDirs += ["${project(rootProject.ext.features.logisticaddaddress).projectDir}/src/androidTest/java"]
}
```
