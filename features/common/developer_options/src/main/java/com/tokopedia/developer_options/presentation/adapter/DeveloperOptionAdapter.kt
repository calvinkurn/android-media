package com.tokopedia.developer_options.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactoryImpl
import com.tokopedia.developer_options.presentation.model.*

class DeveloperOptionAdapter(
    typeFactory: DeveloperOptionTypeFactoryImpl,
    differ: DeveloperOptionDiffer,
    context: Context
) : BaseDeveloperOptionAdapter<Visitable<*>, DeveloperOptionTypeFactoryImpl>(typeFactory, differ) {

    private val defaultItems = listOf(
        PdpDevUiModel(context.getString(R.string.pdp_dev)),
        AccessTokenUiModel(context.getString(R.string.access_token)),
        SystemNonSystemAppsUiModel(context.getString(R.string.system_apps_non_system_apps)),
        ResetOnBoardingUiModel(context.getString(R.string.reset_onboarding)),
        ForceCrashUiModel(context.getString(R.string.force_crash)),
        SendFirebaseCrashExceptionUiModel(context.getString(R.string.send_firebase_exception)),
        OpenScreenRecorderUiModel(context.getString(R.string.open_screen_recorder)),
        NetworkLogOnNotificationUiModel(context.getString(R.string.enable_network_log_on_notification)),
        ViewNetworkLogUiModel(context.getString(R.string.view_network_log)),
        DeviceIdUiModel(context.getString(R.string.device_id)),
        ForceDarkModeUiModel(context.getString(R.string.force_dark_mode)),
        TopAdsLogOnNotificationUiModel(context.getString(R.string.enable_topads_notif)),
        ViewTopAdsLogUiModel(context.getString(R.string.view_topads_log))
    )

    fun searchItem(text: String) {
        val newItems = mutableListOf<OptionItemUiModel>()
        defaultItems.forEach {
            if (it.text.lowercase().contains(text.lowercase())) {
                newItems.add(it)
            }
        }
        submitList(newItems)
    }

    fun setDefaultItem() {
        submitList(defaultItems)
    }
}