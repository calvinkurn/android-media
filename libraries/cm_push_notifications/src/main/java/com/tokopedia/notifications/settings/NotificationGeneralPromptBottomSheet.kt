package com.tokopedia.notifications.settings

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.NotificationReminderPromptGtmTracker
import com.tokopedia.notifications.common.NotificationSettingsGtmEvents
import com.tokopedia.notifications.databinding.CmLayoutNotificationsGeneralPromptBinding
import com.tokopedia.notifications.utils.NotificationSettingsUtils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class NotificationGeneralPromptBottomSheet(
    private val isReminderPrompt: Boolean,
    private val pageName: String
    ) : BottomSheetUnify() {

    private var binding: CmLayoutNotificationsGeneralPromptBinding? = null

    private val userSession: UserSessionInterface by lazy(LazyThreadSafetyMode.NONE) {
        UserSession(context)
    }

    private val activityName
        get() = activity?.javaClass?.simpleName ?: ""

    private var lastTimeClicked: Long = 0
    private val defaultInterval = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View.inflate(requireContext(), R.layout.cm_layout_notifications_general_prompt, null)

        binding = CmLayoutNotificationsGeneralPromptBinding.bind(view)
        binding?.let {
            setChild(it.root)
        }

        initView(activity)

        sendEventImpression()

        setCloseClickListener(::onCloseClick)
    }

    private fun initView(activity: FragmentActivity?) {
        val config = getResourcesConfig()

        binding?.cmGeneralPromptImage?.setImageResource(config.first)
        binding?.cmGeneralPromptTitle?.text = getString(config.second)
        binding?.cmGeneralPromptDescription?.text = getString(config.third)
        binding?.cmGeneralPromptTurnOnNotification?.setOnClickListener {
            onTurnOnNotificationClick(activity)
        }
    }

    private fun getResourcesConfig(): Triple<Int, Int, Int> =
        if (GlobalConfig.isSellerApp()) {
            if (isReminderPrompt) {
                getResourcesConfigForReminderPrompt()
            } else {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_sellerapp,
                    R.string.cm_notifications_general_prompt_sellerapp_title,
                    R.string.cm_notifications_general_prompt_sellerapp_description
                )
            }

        } else {
            if (isReminderPrompt) {
                getResourcesConfigForReminderPrompt()
            } else {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                    R.string.cm_notifications_general_prompt_mainapp_title,
                    R.string.cm_notifications_general_prompt_mainapp_description
                )
            }
        }

    private fun getResourcesConfigForReminderPrompt(): Triple<Int, Int, Int> =
        when(pageName) {
            KEJAR_DISKON -> {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                    R.string.cm_notifications_general_prompt_kejar_diskon_title,
                    R.string.cm_notifications_general_prompt_kejar_diskon_description
                )
            }
            TAP_TAP_KOTAK -> {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                    R.string.cm_notifications_general_prompt_tap_tap_kotak_title,
                    R.string.cm_notifications_general_prompt_tap_tap_kotak_description
                )
            }
            LIVE_SHOPPING -> {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                    R.string.cm_notifications_general_prompt_live_shopping_title,
                    R.string.cm_notifications_general_prompt_live_shopping_description
                )
            }
            STOCK_REMINDER -> {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_sellerapp,
                    R.string.cm_notifications_general_prompt_product_list_title,
                    R.string.cm_notifications_general_prompt_product_list_description
                )
            }
            else -> {
                Triple(
                    R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                    R.string.cm_notifications_general_prompt_mainapp_title,
                    R.string.cm_notifications_general_prompt_mainapp_description
                )
            }
        }

    private fun sendEventImpression() {
        context?.let {
            if (isReminderPrompt) {
                NotificationReminderPromptGtmTracker(
                    userSession,
                    it.applicationContext,
                    pageName
                ).sendReminderPromptImpressionEvent(it, activityName)
            } else {
                NotificationSettingsGtmEvents(
                    userSession,
                    it.applicationContext
                ).sendGeneralPromptImpressionEvent(it, activityName)
            }
        }
    }

    private fun onCloseClick(ignored: View) {
        sendEventClose()

        dismiss()
    }

    private fun sendEventClose() {
        context?.let {
            if (isReminderPrompt) {
                NotificationReminderPromptGtmTracker(
                    userSession,
                    it.applicationContext,
                    pageName
                ).sendReminderPromptClickCloseEvent(it, activityName)
            } else {
                NotificationSettingsGtmEvents(
                    userSession,
                    it.applicationContext
                ).sendGeneralPromptClickCloseEvent(it, activityName)
            }

        }
    }

    private fun onTurnOnNotificationClick(activity: FragmentActivity?) {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastTimeClicked <= defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        sendEventClickCta()
        if (isReminderPrompt && activity != null) {
            OpenAppNotificationSettingPage().goToAppNotificationSettingsPage(activity)
            dismiss()
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

            context?.let {
                NotificationSettingsUtils(it.applicationContext).sendNotificationPromptEvent()
            }

            requestPermissions(
                arrayOf(POST_NOTIFICATIONS),
                POST_NOTIFICATIONS_REQUEST_CODE
            )
        }
    }

    private fun sendEventClickCta() {
        context?.let {
            if (isReminderPrompt) {
                NotificationReminderPromptGtmTracker(
                    userSession,
                    it.applicationContext,
                    pageName
                ).sendReminderPromptClickCtaEvent(it, activityName)
            } else {
                NotificationSettingsGtmEvents(
                    userSession,
                    it.applicationContext
                ).sendGeneralPromptClickCtaEvent(it, activityName)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (isPostNotificationPermissions(requestCode, permissions)) {
            sendEventPostNotificationPermissionResult(grantResults)
        }
        dismiss()
    }

    private fun isPostNotificationPermissions(
        requestCode: Int,
        permissions: Array<out String>
    ) = requestCode == POST_NOTIFICATIONS_REQUEST_CODE &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        permissions.contains(POST_NOTIFICATIONS)

    private fun sendEventPostNotificationPermissionResult(grantResults: IntArray) {
        val context = context ?: return

        if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            NotificationSettingsGtmEvents(userSession, context).sendActionAllowEvent(context)
        } else {
            NotificationSettingsGtmEvents(userSession, context).sendActionNotAllowEvent(context)
        }
    }

    override fun onDestroyView() {
        view?.let {
            if (it is ViewGroup) {
                it.removeAllViews()
            }
        }

        binding = null

        super.onDestroyView()
    }

    companion object {
        const val TAG = "CM_GENERAL_PROMPT_TAG"
        private const val POST_NOTIFICATIONS_REQUEST_CODE = 12341
        const val KEJAR_DISKON = "kejarDiskon"
        const val TAP_TAP_KOTAK = "tapTapKotak"
        const val LIVE_SHOPPING = "liveShopping"
        const val STOCK_REMINDER = "stockReminder"

    }
}
