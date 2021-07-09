package com.tokopedia.sellerfeedback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment
import com.tokopedia.sellerfeedback.presentation.util.ScreenshotPreferenceManage
import com.tokopedia.unifycomponents.Toaster
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class SellerFeedbackScreenshot(private val context: Context) : Screenshot(context.contentResolver) {

    companion object {
        private const val THRESHOLD_TIME = 2000L
        private const val PATTERN_DATE_PREFS = "yyyy-MM-dd"
    }

    private var lastTimeCall = 0L
    private var lastTimeUpdate = 0L

    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var currentActivity: WeakReference<Activity>? = null

    private val screenshotPreferenceManage by lazy { ScreenshotPreferenceManage(context) }

    override val toasterSellerListener = object : ToasterSellerListener {
        override fun showToaster(uri: Uri?, currentActivity: Activity?) {
            showToasterSellerFeedback(uri, currentActivity)
        }
    }

    override fun onScreenShotTaken(uri: Uri) {
        val enableSellerFeedbackScreenshot = getEnableSellerGlobalFeedbackRemoteConfig(currentActivity?.get())
        //temporary there is no remote config checker for environment test
        //I think remote config will be checked before check threshold calculation or process screenshot taken
        lastTimeCall = System.currentTimeMillis()
        if (lastTimeCall - lastTimeUpdate > THRESHOLD_TIME) {
            processScreenshotTaken(uri)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
        super.onActivityResumed(activity)
    }

    private fun processScreenshotTaken(uri: Uri) {
        val date = screenshotPreferenceManage.getDateToaster()
        if (date.isNotBlank()) {
            if (isDifferentDays(date)) {
                screenshotPreferenceManage.setDateToaster(getNowDate())
                setScreenShotTaken(uri)
            }
        } else {
            screenshotPreferenceManage.setDateToaster(getNowDate())
            setScreenShotTaken(uri)
        }
    }

    private fun setScreenShotTaken(uri: Uri) {
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

    private fun openFeedbackForm(uri: Uri) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_FEEDBACK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SellerFeedbackFragment.EXTRA_URI_IMAGE, uri)
        context.startActivity(intent)
    }

    private fun isDifferentDays(dateString: String): Boolean {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_PREFS, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
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
            Toaster.build(this, text = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_text),
                    actionText = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_cta_text),
                    type = Toaster.TYPE_NORMAL,
                    duration = Toaster.LENGTH_SHORT,
                    clickListener = {
                        uri?.let { uri ->
                            SellerFeedbackTracking.Click.eventClickFeedbackButton()
                            openFeedbackForm(uri)
                        }
                    }
            ).show()
        }
    }
}