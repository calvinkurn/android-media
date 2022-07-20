package com.tokopedia.tokopatch.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings.Secure
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*


/**
 * Author errysuprayogi on 17,June,2020
 */
object Utils {

    @JvmStatic
    fun isDebuggable(context: Context): Boolean =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) !== 0

    @JvmStatic
    fun versionName(context: Context): String =
            context.packageManager.getPackageInfo(context.packageName, 0).versionName

    @JvmStatic
    fun versionCode(context: Context): String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toString()
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toString()
            }

    @JvmStatic
    fun packageName(context: Context): String = context.packageName

    @JvmStatic
    fun getDeviceId(context: Context): String = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)

    private var sID: String? = null
    private const val INSTALLATION = "installationId"
    @Synchronized
    fun installId(context: Context): String {
        if (sID == null) {
            val installation = File(context.filesDir, INSTALLATION)
            try {
                if (!installation.exists()) writeInstallationFile(installation)
                sID = readInstallationFile(installation)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        return sID.toString()
    }

    @Throws(IOException::class)
    private fun readInstallationFile(installation: File): String {
        val f = RandomAccessFile(installation, "r")
        val bytes = ByteArray(f.length().toInt())
        f.readFully(bytes)
        f.close()
        return String(bytes)
    }

    @Throws(IOException::class)
    private fun writeInstallationFile(installation: File) {
        val out = FileOutputStream(installation)
        val id = UUID.randomUUID().toString()
        out.write(id.toByteArray())
        out.close()
    }
}