package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.domain.model.PostItemDataModel
import com.tokopedia.sellerhomecommon.presentation.model.PostCtaDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class PostMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetPostDataResponse, List<PostListDataUiModel>> {

    companion object {
        const val MAX_ITEM_PER_PAGE = 3
        private const val MILLIS_1000 = 1000
    }

    private var dataKeys: List<DataKeyModel> = emptyList()

    override fun mapRemoteDataToUiData(
        response: GetPostDataResponse,
        isFromCache: Boolean
    ): List<PostListDataUiModel> {
        return response.getPostWidgetData.data.mapIndexed { i, post ->
            val maxDisplay = dataKeys.getOrNull(i)?.maxDisplay ?: MAX_ITEM_PER_PAGE
            PostListDataUiModel(
                dataKey = post.dataKey,
                postPagers = getPostPagers(post.list, post.emphasizeType, maxDisplay),
                cta = PostCtaDataUiModel(
                    text = post.cta.text,
                    appLink = post.cta.appLink
                ),
                error = post.error,
                isFromCache = isFromCache,
                showWidget = post.showWidget,
                emphasizeType = post.emphasizeType ?: PostListDataUiModel.IMAGE_EMPHASIZED,
                lastUpdated = getLastUpdatedMillis(post.dataKey, isFromCache),
                widgetDataSign = post.widgetDataSign
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
        val maxItemPerPage = if (maxDisplay == Int.ZERO) {
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
                            title = postItem.title,
                            appLink = postItem.appLink,
                            url = postItem.url,
                            featuredMediaUrl = postItem.featuredMediaURL,
                            subtitle = postItem.subtitle,
                            textEmphasizeType = PostListDataUiModel.TEXT_EMPHASIZED,
                            stateText = postItem.stateText,
                            stateMediaUrl = postItem.stateMediaUrl,
                            shouldShowUnderLine = index != postList.size.minus(Int.ONE),
                            isPinned = postItem.isPinned,
                            postItemId = postItem.postItemID
                        )
                    }
                    else -> {
                        PostItemUiModel.PostImageEmphasizedUiModel(
                            title = postItem.title,
                            appLink = postItem.appLink,
                            url = postItem.url,
                            featuredMediaUrl = postItem.featuredMediaURL,
                            subtitle = postItem.subtitle,
                            textEmphasizeType = PostListDataUiModel.IMAGE_EMPHASIZED,
                            isPinned = postItem.isPinned,
                            countdownDate = try {
                                if (postItem.countdownDate.isNullOrBlank()) {
                                    null
                                } else {
                                    val timeMillis =
                                        postItem.countdownDate.toLongOrZero().times(MILLIS_1000)
                                    Date(timeMillis)
                                }
                            } catch (e: Exception) {
                                Timber.e(e)
                                null
                            }
                        )
                    }
                }
            }
    }
}
