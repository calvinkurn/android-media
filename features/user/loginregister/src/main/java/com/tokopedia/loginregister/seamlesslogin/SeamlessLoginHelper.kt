package com.tokopedia.loginregister.seamlesslogin

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest

object SeamlessLoginHelper {

    const val SELLER_APP_SIG = "8FB89A43A31199AC78D1F1C2A242388BA1DB9FAE"

    fun getSellerSig(context: Context): String {
        val packageManager = context.packageManager
        val signatureSellerApp: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfoSeller = packageManager.getPackageInfo(
                RemoteService.PACKAGE_SELLERAPP, PackageManager.GET_SIGNING_CERTIFICATES
            )
            getSignatureString(packageInfoSeller.signingInfo.signingCertificateHistory)
        } else {
            val packageInfoSeller = packageManager.getPackageInfo(
                RemoteService.PACKAGE_SELLERAPP,
                PackageManager.GET_SIGNATURES
            ).signatures
            getSignatureString(packageInfoSeller)
        }
        return signatureSellerApp
    }

    fun isSignatureMatch(sig1: String, sig2: String): Boolean {
        return (sig1.isNotEmpty() && sig2.isNotEmpty()) && sig1 == sig2
    }

    private fun getSignatureString(sig: Array<Signature>): String {
        sig.map {
            val digest = MessageDigest.getInstance("SHA")
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
