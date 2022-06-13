package com.tokopedia.developer_options.applink.domain.usecase

import com.tokopedia.developer_options.applink.domain.mapper.AppLinkMapper
import com.tokopedia.developer_options.applink.domain.model.AppLinkListModelItem
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import com.tokopedia.developer_options.applink.utils.DeepLinkFileUtils
import javax.inject.Inject

class GetAppLinkListUseCase @Inject constructor(
    private val deepLinkFileUtils: DeepLinkFileUtils,
    private val appLinkMapper: AppLinkMapper
) {

    fun execute(json: String): List<AppLinkUiModel> {
        return appLinkMapper.mapToDeepLinkUiModel(
            deepLinkFileUtils.getJsonArrayResources(json)
        )
    }
}