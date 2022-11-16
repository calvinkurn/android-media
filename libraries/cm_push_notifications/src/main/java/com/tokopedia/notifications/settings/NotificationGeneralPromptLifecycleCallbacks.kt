package com.tokopedia.notifications.settings

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class NotificationGeneralPromptLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is FragmentActivity || isActivityExcludedFromGeneralPrompt(activity)) return

        val isNotificationPermissionDenied = isNotificationPermissionDenied(activity)
        val repo = NotificationGeneralPromptSharedPreferences(activity.applicationContext)
        val prompt = NotificationGeneralPrompt(
            isNotificationPermissionDenied,
            notificationGeneralPromptView(activity),
            repo,
            FirebaseRemoteConfigImpl(activity.applicationContext)
        )

        prompt.showNotification()

        activity.application.unregisterActivityLifecycleCallbacks(this)
    }

    private fun isActivityExcludedFromGeneralPrompt(activity: Activity) =
        exceptionActivityList.contains(activity::class.java.simpleName)

    private fun isNotificationPermissionDenied(activity: Activity) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PERMISSION_DENIED
        else
            false

    private fun notificationGeneralPromptView(activity: FragmentActivity) =
        object : NotificationGeneralPromptView {
            override fun show() {
                Handler(Looper.getMainLooper()).postDelayed({
                    NotificationGeneralPromptBottomSheet().show(
                        activity.supportFragmentManager,
                        NotificationGeneralPromptBottomSheet.TAG
                    )
                }, 500)
            }
        }

    override fun onActivityStarted(activity: Activity) { }

    override fun onActivityResumed(activity: Activity) { }

    override fun onActivityPaused(activity: Activity) { }

    override fun onActivityStopped(activity: Activity) { }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }

    override fun onActivityDestroyed(activity: Activity) { }

    companion object {
        private val exceptionActivityList = listOf(
            "SplashScreenActivity",
            "SellerOnboardingActivity",
            "SeamlessLoginEmailPhoneActivity",
            "SellerSeamlessLoginActivity",
            "LoginActivity",
            "LandingShopCreationActivity",
            "DeeplinkActivity",
            "PhoneShopCreationActivity",
            "VerificationActivity",
            "DeveloperOptionActivity",
            "RemoteConfigFragmentActivity"
        )
    }
}
