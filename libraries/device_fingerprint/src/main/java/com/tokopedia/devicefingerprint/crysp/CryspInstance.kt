package com.tokopedia.devicefingerprint.crysp

import android.app.Activity
import android.content.Context
import android.os.Build
import com.crysp.sdk.CryspAPI
import com.crysp.sdk.CryspNetworkManager
import java.security.MessageDigest

class CryspInstance {
    companion object {
        fun init(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check if Android 6.0+ and check if runtime permissions were granted
                // TODO check permission
                CryspAPI.getInstance().initXAC(activity.applicationContext, activity);
            } else { // This is not Marshmallow. No runtime permissions required
                CryspAPI.getInstance().initXAC(activity.applicationContext, activity);
            }
        }

        fun sendToken(context: Context, userId: String,
                      onSuccess: (String)-> Unit,
                      onError:(String)->Unit) {
            CryspNetworkManager.sendAysncRequest(context, md5(userId), "1CA0D05DDC914EE69D5D1F8BF7FAA0AB", "") { cryspResponse ->
                if (cryspResponse.errorCode == 1) { // Server Returned an Error
                    val errorMsg = cryspResponse.getErrorMsg();
                    onError (errorMsg)
                } else { // Received a Valid Response from Server
                    onSuccess(cryspResponse.toString())
                }
            }
        }

        private fun md5(string: String): String {
            val bytes = MessageDigest.getInstance("MD5").digest(string.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

}