package com.tokopedia.developer_options.applink.domain.mapper

import com.tokopedia.developer_options.applink.domain.model.AppLinkListModelItem
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import javax.inject.Inject

class AppLinkMapper @Inject constructor() {

    fun mapToDeepLinkUiModel(appLinkList: List<AppLinkListModelItem>): List<AppLinkUiModel> {
        val appLinkMap = hashMapOf<String, AppLinkUiModel>()

        appLinkList.forEach {
            if (appLinkMap[it.deepLink] == null) {
                val appLinkVariable = if (it.deepLinkVariable.isNullOrEmpty()) "-" else it.deepLinkVariable
                appLinkMap[it.deepLink] = AppLinkUiModel(it.deepLink, appLinkVariable, it.destinationActivity)
            }
        }
        return appLinkMap.entries.map { it.value }
    }
}