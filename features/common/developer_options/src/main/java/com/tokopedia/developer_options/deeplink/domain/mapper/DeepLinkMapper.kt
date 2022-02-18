package com.tokopedia.developer_options.deeplink.domain.mapper

import com.tokopedia.developer_options.deeplink.domain.model.DeepLinkListModelItem
import com.tokopedia.developer_options.deeplink.presentation.uimodel.DeepLinkUiModel
import javax.inject.Inject

class DeepLinkMapper @Inject constructor() {

    fun mapToDeepLinkUiModel(deepLinkList: List<DeepLinkListModelItem>): List<DeepLinkUiModel> {
        val deepLinkMap = hashMapOf<String, DeepLinkUiModel>()

        deepLinkList.forEach {
            if (deepLinkMap[it.depplink] == null) {
                deepLinkMap[it.depplink] = DeepLinkUiModel(it.depplink, it.depplinkVariable, it.depplinkVariable)
            }
        }
        return deepLinkMap.entries.map { it.value }
    }
}