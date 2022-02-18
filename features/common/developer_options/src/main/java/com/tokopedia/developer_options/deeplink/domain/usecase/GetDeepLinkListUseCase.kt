package com.tokopedia.developer_options.deeplink.domain.usecase

import com.tokopedia.developer_options.deeplink.domain.mapper.DeepLinkMapper
import com.tokopedia.developer_options.deeplink.presentation.uimodel.DeepLinkUiModel
import com.tokopedia.developer_options.deeplink.utils.DeepLinkFileUtils
import javax.inject.Inject

class GetDeepLinkListUseCase @Inject constructor(
    private val deepLinkFileUtils: DeepLinkFileUtils,
    private val deepLinkMapper: DeepLinkMapper
) {

    fun execute(): List<DeepLinkUiModel> {
        return deepLinkMapper.mapToDeepLinkUiModel(
            deepLinkFileUtils.getJsonArrayResources(DEEPLINK_RESOURCE)
        )
    }

    companion object {
        const val DEEPLINK_RESOURCE = "raw/deeplink.json"
    }
}