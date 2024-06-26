package com.tokopedia.utils.appsignature

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest

object AppSignatureUtil {

    const val TOKO_APP_SIGNATURE = "7456F3BE6944332817E43ECE6A053F9744466C95F536C4503F25330237DD81BE"
    const val MIN_VERSION_SELLER_APP: Long = 206219100
    fun getVersionCodeOtherApp(context: Context, packageName: String): Long {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
        return versionCode
    }

    fun getAppSignature(context: Context, packageName: String): String {
        val packageManager = context.packageManager
        val appSignature: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfoSeller = packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNING_CERTIFICATES
            )
            getSignatureString(packageInfoSeller.signingInfo.signingCertificateHistory)
        } else {
            val packageInfoSeller = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures
            getSignatureString(packageInfoSeller)
        }
        return appSignature
    }

    fun isSignatureMatch(sig1: String, sig2: String): Boolean {
        return (sig1.isNotEmpty() && sig2.isNotEmpty()) && sig1 == sig2
    }

    private fun getSignatureString(sig: Array<Signature>): String {
        sig.map {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(it.toByteArray())
            return bytesToHex(digest.digest())
        }
        return ""
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
