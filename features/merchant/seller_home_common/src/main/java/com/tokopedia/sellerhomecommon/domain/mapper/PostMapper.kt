package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.domain.model.PostItemDataModel
import com.tokopedia.sellerhomecommon.presentation.model.PostCtaDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class PostMapper @Inject constructor() : BaseResponseMapper<GetPostDataResponse, List<PostListDataUiModel>> {

    companion object {
        private const val MAX_ITEM_PER_PAGE = 3
    }

    override fun mapRemoteDataToUiData(response: GetPostDataResponse, isFromCache: Boolean): List<PostListDataUiModel> {
        return response.getPostWidgetData?.data.orEmpty().map {
            PostListDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    postPagers = getPostPagers(it.list.orEmpty(), it.emphasizeType),
                    cta = PostCtaDataUiModel(
                            text = it.cta?.text.orEmpty(),
                            appLink = it.cta?.appLink.orEmpty()
                    ),
                    error = it.error.orEmpty(),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse(),
                    emphasizeType = it.emphasizeType ?: PostListDataUiModel.IMAGE_EMPHASIZED
            )
        }
    }

    private fun getPostPagers(postItems: List<PostItemDataModel>, emphasizeType: Int?): List<PostListPagerUiModel> {
        return postItems.chunked(MAX_ITEM_PER_PAGE).map {
            val postListItem = getPostItems(it, emphasizeType)
            PostListPagerUiModel(postListItem)
        }
    }

    private fun getPostItems(postList: List<PostItemDataModel>, emphasizeType: Int?): List<PostItemUiModel> {
        return postList.mapIndexed { index, postItem ->
            when (emphasizeType) {
                PostListDataUiModel.TEXT_EMPHASIZED -> {
                    PostItemUiModel.PostTextEmphasizedUiModel(
                            title = postItem.title.orEmpty(),
                            appLink = postItem.appLink.orEmpty(),
                            url = postItem.url.orEmpty(),
                            featuredMediaUrl = postItem.featuredMediaURL.orEmpty(),
                            subtitle = postItem.subtitle.orEmpty(),
                            stateText = postItem.stateText.orEmpty(),
                            stateMediaUrl = postItem.stateMediaUrl.orEmpty(),
                            shouldShowUnderLine = index != postList.size.minus(1)
                    )
                }
                else -> {
                    PostItemUiModel.PostImageEmphasizedUiModel(
                            title = postItem.title.orEmpty(),
                            appLink = postItem.appLink.orEmpty(),
                            url = postItem.url.orEmpty(),
                            featuredMediaUrl = postItem.featuredMediaURL.orEmpty(),
                            subtitle = postItem.subtitle.orEmpty()
                    )
                }
            }
        }
    }
}