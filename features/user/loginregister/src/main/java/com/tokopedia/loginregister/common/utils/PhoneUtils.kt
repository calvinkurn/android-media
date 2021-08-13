package com.tokopedia.loginregister.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.tokopedia.loginregister.common.PartialRegisterInputUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class PhoneUtils {

    companion object {
        private const val REGEX_REMOVE_SYMBOL_PHONE = "[+| |-]"

        fun removeSymbolPhone(phone: String): String = phone.replace(Regex(REGEX_REMOVE_SYMBOL_PHONE), "")

        @SuppressLint("MissingPermission", "HardwareIds", "PrivateResource")
        fun getPhoneNumber(activity: FragmentActivity, permissionCheckerHelper: PermissionCheckerHelper): ArrayList<String> {
            val phoneNumbers = arrayListOf<String>()
            if (permissionCheckerHelper.hasPermission(activity, arrayOf(PermissionCheckerHelper.Companion.PERMISSION_READ_PHONE_STATE))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val subscription = activity.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                    if (subscription.activeSubscriptionInfoList != null && subscription.activeSubscriptionInfoCount > 0) {
                        for (info in subscription.activeSubscriptionInfoList) {
                            if (!info.number.isNullOrEmpty() &&
                                    PartialRegisterInputUtils.getType(info.number) == PartialRegisterInputUtils.PHONE_TYPE &&
                                    PartialRegisterInputUtils.isValidPhone(info.number)) {
                                phoneNumbers.add(removeSymbolPhone(info.number))
                            }
                        }
                    }
                } else {
                    val telephony = activity.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
                    if (!telephony.line1Number.isNullOrEmpty() &&
                            PartialRegisterInputUtils.getType(telephony.line1Number) == PartialRegisterInputUtils.PHONE_TYPE &&
                            PartialRegisterInputUtils.isValidPhone(telephony.line1Number)) {
                        phoneNumbers.add(removeSymbolPhone(telephony.line1Number))
                    }
                }
                return phoneNumbers
            }
            return phoneNumbers
        }
    }
}