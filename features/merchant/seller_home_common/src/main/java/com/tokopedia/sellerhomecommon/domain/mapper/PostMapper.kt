package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils.orFalse
import com.tokopedia.sellerhomecommon.domain.model.PostDataModel
import com.tokopedia.sellerhomecommon.presentation.model.PostCtaDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class PostMapper @Inject constructor() {

    fun mapRemoteDataModelToUiDataModel(widgetDataList: List<PostDataModel>, isFromCache: Boolean): List<PostListDataUiModel> {
        return widgetDataList.map {
            PostListDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    items = it.list?.map { postItem ->
                        PostUiModel(
                                postItem.title.orEmpty(),
                                postItem.appLink.orEmpty(),
                                postItem.url.orEmpty(),
                                postItem.featuredMediaURL.orEmpty(),
                                postItem.subtitle.orEmpty()
                        )
                    }.orEmpty(),
                    cta = PostCtaDataUiModel(
                            text = it.cta?.text.orEmpty(),
                            appLink = it.cta?.appLink.orEmpty()
                    ),
                    error = it.error.orEmpty(),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }
}