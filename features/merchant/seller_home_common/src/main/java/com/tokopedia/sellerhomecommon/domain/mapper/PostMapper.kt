package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.PostDataModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class PostMapper @Inject constructor() {

    fun mapRemoteDataModelToUiDataModel(widgetDataList: List<PostDataModel>): List<PostListDataUiModel> {
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
                    error = it.error.orEmpty()
            )
        }
    }
}