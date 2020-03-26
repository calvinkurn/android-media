
# Build 
## Update C++ distribution
1. delete folder `distribution` & `distribution.gz` in root. 
2. run `./s3sync.sh`
3. Build:  clean => Refresh c++ => Rebuild

# Debug
#### Turn on debug mode for native HTTP package
add -D_DEBUG to cppFlags in build.gradle
