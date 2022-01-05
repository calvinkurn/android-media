# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontshrink
-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-keep class com.modiface.mfemakeupkit.** {*;}
-keep enum com.modiface.mfemakeupkit.** {*;}
-keep interface com.modiface.mfemakeupkit.** {*;}
-keepclassmembers class com.modiface.mfemakeupkit.** {
  private static native <methods>;
  *;
}
-keepclassmembers class * {
  private static native <methods>;
  *;
}
-keepclassmembers class * {
    native <methods>;
    *;
}
-keepclassmembers class * {
    public native <methods>;
    *;
}
-keepclassmembers class * {
    private native <methods>;
    *;
}
-keepclassmembers class * {
    protected native <methods>;
    *;
}
-keepclassmembers class * {
    native <methods>;
    *;
}