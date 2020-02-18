package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.PostDataModel
import com.tokopedia.sellerhome.view.model.PostListDataUiModel
import com.tokopedia.sellerhome.view.model.PostUiModel
import javax.inject.Inject

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