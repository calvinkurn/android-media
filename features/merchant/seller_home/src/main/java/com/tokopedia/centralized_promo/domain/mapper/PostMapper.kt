package com.tokopedia.centralized_promo.domain.mapper

import com.tokopedia.centralized_promo.view.model.PostListUiModel
import com.tokopedia.centralized_promo.view.model.PostUiModel
import com.tokopedia.sellerhome.domain.model.PostDataModel
import javax.inject.Inject

class PostMapper @Inject constructor() {
    fun mapRemoteDataModelToUiDataModel(widgetDataList: List<PostDataModel>): List<PostListUiModel> {
        return widgetDataList.map {
            PostListUiModel(
                    posts = it.list?.map { postItem ->
                        PostUiModel(
                                postItem.title.orEmpty(),
                                postItem.appLink.orEmpty(),
                                postItem.url.orEmpty(),
                                postItem.featuredMediaURL.orEmpty(),
                                postItem.subtitle.orEmpty()
                        )
                    }.orEmpty()
            )
        }
    }
}