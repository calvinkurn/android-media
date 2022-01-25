package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
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

class PostMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) : BaseWidgetMapper(lastUpdatedSharedPref),
    BaseResponseMapper<GetPostDataResponse, List<PostListDataUiModel>> {

    companion object {
        private const val MAX_ITEM_PER_PAGE = 3
    }

    private var dataKeys: List<DataKeyModel> = emptyList()

    override fun mapRemoteDataToUiData(
        response: GetPostDataResponse,
        isFromCache: Boolean
    ): List<PostListDataUiModel> {
        return response.getPostWidgetData?.data.orEmpty().mapIndexed { i, post ->
            val maxDisplay = dataKeys.getOrNull(i)?.maxDisplay ?: MAX_ITEM_PER_PAGE
            PostListDataUiModel(
                dataKey = post.dataKey.orEmpty(),
                postPagers = getPostPagers(post.list.orEmpty(), post.emphasizeType, maxDisplay),
                cta = PostCtaDataUiModel(
                    text = post.cta?.text.orEmpty(),
                    appLink = post.cta?.appLink.orEmpty()
                ),
                error = post.error.orEmpty(),
                isFromCache = isFromCache,
                showWidget = post.showWidget.orFalse(),
                emphasizeType = post.emphasizeType ?: PostListDataUiModel.IMAGE_EMPHASIZED,
                lastUpdated = getLastUpdatedMillis(post.dataKey.orEmpty(), isFromCache)
            )
        }
    }

    fun setDataKeys(dataKeys: List<DataKeyModel>) {
        this.dataKeys = dataKeys
    }

    private fun getPostPagers(
        postItems: List<PostItemDataModel>,
        emphasizeType: Int?,
        maxDisplay: Int
    ): List<PostListPagerUiModel> {
        val maxItemPerPage = if (maxDisplay == 0) {
            MAX_ITEM_PER_PAGE
        } else {
            maxDisplay
        }
        return postItems.chunked(maxItemPerPage).map {
            val postListItem = getPostItems(it, emphasizeType)
            PostListPagerUiModel(postListItem)
        }
    }

    private fun getPostItems(
        postList: List<PostItemDataModel>,
        emphasizeType: Int?
    ): List<PostItemUiModel> {
        return postList.sortedByDescending { it.isPinned }
            .mapIndexed { index, postItem ->
                when (emphasizeType) {
                    PostListDataUiModel.TEXT_EMPHASIZED -> {
                        PostItemUiModel.PostTextEmphasizedUiModel(
                            title = postItem.title.orEmpty(),
                            appLink = postItem.appLink.orEmpty(),
                            url = postItem.url.orEmpty(),
                            featuredMediaUrl = postItem.featuredMediaURL.orEmpty(),
                            subtitle = postItem.subtitle.orEmpty(),
                            textEmphasizeType = PostListDataUiModel.TEXT_EMPHASIZED,
                            stateText = postItem.stateText.orEmpty(),
                            stateMediaUrl = postItem.stateMediaUrl.orEmpty(),
                            shouldShowUnderLine = index != postList.size.minus(1),
                            isPinned = postItem.isPinned
                        )
                    }
                    else -> {
                        PostItemUiModel.PostImageEmphasizedUiModel(
                            title = postItem.title.orEmpty(),
                            appLink = postItem.appLink.orEmpty(),
                            url = postItem.url.orEmpty(),
                            featuredMediaUrl = postItem.featuredMediaURL.orEmpty(),
                            subtitle = postItem.subtitle.orEmpty(),
                            textEmphasizeType = PostListDataUiModel.IMAGE_EMPHASIZED,
                            isPinned = postItem.isPinned
                        )
                    }
                }
            }
    }
}