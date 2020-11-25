package com.tokopedia.centralizedpromo.domain.mapper

import com.tokopedia.centralizedpromo.view.model.PostListUiModel
import com.tokopedia.centralizedpromo.view.model.PostUiModel
import com.tokopedia.centralizedpromo.domain.model.PostDataModel
import javax.inject.Inject

class PostMapper @Inject constructor() {
    fun mapDomainDataModelToUiDataModel(widgetDataList: List<PostDataModel>): List<PostListUiModel> {
        return widgetDataList.map {
            PostListUiModel(
                    items = it.list?.map { postItem ->
                        PostUiModel(
                                postItem.title.orEmpty(),
                                postItem.appLink.orEmpty(),
                                postItem.url.orEmpty(),
                                postItem.featuredMediaURL.orEmpty(),
                                postItem.subtitle.orEmpty()
                        )
                    }.orEmpty(),
                    errorMessage = ""
            )
        }
    }
}