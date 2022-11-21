package com.tokopedia.notifications.settings

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifications.R
import com.tokopedia.notifications.databinding.CmLayoutNotificationsGeneralPromptBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class NotificationGeneralPromptBottomSheet: BottomSheetUnify() {

    private var binding: CmLayoutNotificationsGeneralPromptBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View.inflate(requireContext(), R.layout.cm_layout_notifications_general_prompt, null)

        binding = CmLayoutNotificationsGeneralPromptBinding.bind(view)
        binding?.let {
            setChild(it.root)
        }

        initView()
    }

    private fun initView() {
        val config = getResourcesConfig()

        binding?.cmGeneralPromptImage?.setImageResource(config.first)
        binding?.cmGeneralPromptTitle?.text = getString(config.second)
        binding?.cmGeneralPromptDescription?.text = getString(config.third)
        binding?.cmGeneralPromptTurnOnNotification?.setOnClickListener(::onTurnOnNotificationClick)
    }

    private fun getResourcesConfig(): Triple<Int, Int, Int> =
        if (GlobalConfig.isSellerApp())
            Triple(
                R.drawable.cm_notifications_general_prompt_bottomsheet_sellerapp,
                R.string.cm_notifications_general_prompt_sellerapp_title,
                R.string.cm_notifications_general_prompt_sellerapp_description,
            )
        else
            Triple(
                R.drawable.cm_notifications_general_prompt_bottomsheet_mainapp,
                R.string.cm_notifications_general_prompt_mainapp_title,
                R.string.cm_notifications_general_prompt_mainapp_description,
            )

    private fun onTurnOnNotificationClick(ignored: View) {
        dismiss()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        requestPermissions(
            arrayOf(POST_NOTIFICATIONS),
            POST_NOTIFICATIONS_REQUEST_CODE,
        )
    }

    override fun onDestroyView() {
        view?.let {
            if (it is ViewGroup)
                it.removeAllViews()
        }

        binding = null

        super.onDestroyView()
    }

    companion object {
        const val TAG = "CM_GENERAL_PROMPT_TAG"
        private const val POST_NOTIFICATIONS_REQUEST_CODE = 12341
    }
}
