package com.tokopedia.privacycenter.domain

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class DevicePermissionUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preference: SharedPreferences,
    private val permissionCheckerHelper: PermissionCheckerHelper
) {

    fun isLocationAllowed(): Boolean {
        return permissionCheckerHelper.hasPermission(
            context,
            arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION)
        )
    }

    fun isShakeShakeAllowed(): Boolean {
        return preference.getBoolean(KEY_PREF_SHAKE, false)
    }

    fun setShakeShakePermission(isAllowed: Boolean) {
        preference.edit()
            .putBoolean(KEY_PREF_SHAKE, isAllowed)
            .apply()
    }

    companion object {
        private const val KEY_PREF_SHAKE = "notification_shake_shake"
    }
}
