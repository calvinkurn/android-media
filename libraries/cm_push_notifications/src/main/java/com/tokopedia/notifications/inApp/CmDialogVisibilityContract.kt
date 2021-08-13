package com.tokopedia.notifications.inApp

import android.app.Activity

interface CmDialogVisibilityContract {
    fun onDialogShown(activity: Activity)
    fun onDialogDismiss(activity: Activity)
    fun isDialogVisible(activity: Activity): Boolean
}