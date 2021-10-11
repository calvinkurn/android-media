package com.tokopedia.sellerfeedback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment
import com.tokopedia.sellerfeedback.presentation.util.ScreenshotPreferenceManager
import com.tokopedia.sellerfeedback.presentation.util.SuccessToasterHelper
import com.tokopedia.unifycomponents.Toaster
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class SellerFeedbackScreenshot(private val context: Context) : Screenshot(context.contentResolver) {

    companion object {
        private const val PATTERN_DATE_PREFS = "yyyy-MM-dd"
        private const val ACTIVITY_STATUS_PAUSED = 0
        private const val ACTIVITY_STATUS_RESUMED = 1
        private const val TOASTER_MARGIN_BOTTOM = 104
        private const val TOASTER_DELAY = 1000L
    }

    private var lastTimeUpdate = 0L

    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var currentActivity: WeakReference<Activity>? = null
    private var onResumeCounter = ACTIVITY_STATUS_PAUSED

    private val screenshotPreferenceManager by lazy { ScreenshotPreferenceManager(context) }

    override val toasterSellerListener = object : ToasterSellerListener {
        override fun showToaster(uri: Uri?, currentActivity: Activity?) {
            showToasterSellerFeedback(uri, currentActivity)
        }
    }

    override fun onScreenShotTaken(uri: Uri) {
        if (screenshotPreferenceManager.isScreenShootTriggerEnabled()) {
            processScreenshotTaken(uri)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
        super.onActivityResumed(activity)
    }

    private fun processScreenshotTaken(uri: Uri) {
        triggerScreenShotTaken(uri)
    }

    private fun triggerScreenShotTaken(uri: Uri) {
        SellerFeedbackTracking.Impression.eventViewHomepage()
        super.onScreenShotTaken(uri)
        lastTimeUpdate = System.currentTimeMillis()
    }

    private fun getEnableSellerGlobalFeedbackRemoteConfig(activity: Activity?): Boolean {
        return activity?.let {
            if (remoteConfig == null) {
                remoteConfig = FirebaseRemoteConfigImpl(activity)
            }
            remoteConfig?.getBoolean(
                REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK,
                REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
            ) ?: REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
        } ?: REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
    }

    private fun openFeedbackForm(uri: Uri, currentActivity: Activity) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_FEEDBACK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SellerFeedbackFragment.EXTRA_URI_IMAGE, uri)
        currentActivity.startActivity(intent)

        handleFeedbackSuccessToaster(currentActivity)
    }

    private fun handleFeedbackSuccessToaster(currentActivity: Activity) {
        if (!screenshotPreferenceManager.isFeedbackFormSavedSuccess()) return

        (currentActivity as? ComponentActivity)?.lifecycle?.let { lifecycle ->
            lifecycle.addObserver(getLifecycleObserver(lifecycle, onResumeCallback = {
                currentActivity.window.decorView.rootView?.let { view ->
                    val isScreenShootTriggerEnabled =
                        screenshotPreferenceManager.isScreenShootTriggerEnabled()
                    SuccessToasterHelper.showToaster(
                        currentActivity,
                        view,
                        isScreenShootTriggerEnabled
                    )
                }
            }))
        }
    }

    private fun getLifecycleObserver(
        lifecycle: Lifecycle,
        onResumeCallback: () -> Unit
    ): LifecycleObserver {
        return object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                if (onResumeCounter == ACTIVITY_STATUS_RESUMED) {
                    onResumeCallback()
                    onResumeCounter = ACTIVITY_STATUS_PAUSED
                    lifecycle.removeObserver(this)
                } else {
                    onResumeCounter++
                }
            }
        }
    }

    private fun isDifferentDays(dateString: String): Boolean {
        return try {
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_PREFS, DateFormatUtils.DEFAULT_LOCALE)
            val targetDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - targetDate?.time.orZero())
            val days = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return days > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getNowDate(): String {
        return try {
            val sdf = SimpleDateFormat(PATTERN_DATE_PREFS, DateFormatUtils.DEFAULT_LOCALE)
            sdf.format(System.currentTimeMillis())
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun showToasterSellerFeedback(uri: Uri?, currentActivity: Activity?) {
        val view = currentActivity?.window?.decorView?.rootView
        view?.run {
            postDelayed({
                Toaster.toasterCustomBottomHeight = context.dpToPx(TOASTER_MARGIN_BOTTOM).toInt()
                Toaster.build(this,
                    text = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_text),
                    actionText = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_cta_text),
                    type = Toaster.TYPE_NORMAL,
                    duration = Toaster.LENGTH_LONG,
                    clickListener = {
                        uri?.let { uri ->
                            SellerFeedbackTracking.Click.eventClickFeedbackButton()
                            openFeedbackForm(uri, currentActivity)
                        }
                    }
                ).show()
            }, TOASTER_DELAY)
        }
    }
}