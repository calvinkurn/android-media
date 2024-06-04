package com.tokopedia.stories.data.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel.ContentStoriesDetails
import com.tokopedia.stories.internal.model.StoriesGroupsResponseModel
import com.tokopedia.stories.internal.model.StoriesGroupsResponseModel.ContentStoriesGroups
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.Meta
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Image
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Unknown
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Video
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesType
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.stories.R as storiesR

class StoriesMapperImpl @Inject constructor(private val userSession: UserSessionInterface) :
    StoriesMapper {

    override fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel
    ): StoriesUiModel {
        val groupsData = dataGroup.data
        if (groupsData == ContentStoriesGroups()) return StoriesUiModel()

        val groupSelectedPos = dataGroup.data.meta.selectedGroupIndex
        val groupsItem = groupsData.groups
        return StoriesUiModel(
            selectedGroupId = groupsItem[groupSelectedPos].value,
            selectedGroupPosition = groupSelectedPos,
            groupHeader = groupsItem.mapIndexed { indexGroupHeader, group ->
                StoriesGroupHeader(
                    groupId = group.value,
                    image = group.image,
                    groupName = group.name,
                    isSelected = groupSelectedPos == indexGroupHeader
                )
            },
            groupItems = groupsItem.mapIndexed { indexGroupItem, group ->
                StoriesGroupItem(
                    groupId = group.value,
                    groupName = group.name,
                    detail = if (groupSelectedPos == indexGroupItem) {
                        mapStoriesDetailRequest(
                            selectedGroupId = group.value,
                            dataDetail = dataDetail
                        )
                    } else {
                        StoriesDetail()
                    },
                    author = buildAuthor(group.author),
                    type = StoriesType.get(group.type)
                )
            }
        )
    }

    override fun mapStoriesDetailRequest(
        selectedGroupId: String,
        dataDetail: StoriesDetailsResponseModel
    ): StoriesDetail {
        val detailData = dataDetail.data
        if (detailData == ContentStoriesDetails()) return StoriesDetail.EmptyDetail

        val storiesSelectedPos = detailData.meta.selectedStoriesIndex
        val storiesItem = detailData.stories
        return StoriesDetail(
            selectedGroupId = selectedGroupId,
            selectedDetailPosition = storiesSelectedPos,
            selectedDetailPositionCached = storiesSelectedPos,
            detailItems = storiesItem.map { stories ->
                val author = buildAuthor(stories.author)
                StoriesDetailItem(
                    id = stories.id,
                    event = StoriesDetailItemUiEvent.PAUSE,
                    content = StoriesItemContent(
                        type = when (stories.media.type) {
                            Image.value -> Image
                            Video.value -> Video
                            else -> Unknown
                        },
                        data = stories.media.link,
                        duration = when (stories.media.type) {
                            Image.value -> DEFAULT_DURATION
                            Video.value -> ERROR_DURATION
                            else -> ERROR_DURATION
                        }
                    ),
                    resetValue = -1,
                    isContentLoaded = false,
                    author = author,
                    category = StoriesDetailItem.StoryCategory.getByValue(stories.category),
                    categoryName = stories.categoryName,
                    publishedAt = stories.publishedAt,
                    menus = buildMenu(stories.interaction, stories.author),
                    share = StoriesDetailItem.Sharing(
                        isShareable = stories.interaction.shareable,
                        shareText = MethodChecker.fromHtml(stories.meta.shareTextDescription)
                            .toString(),
                        metadata = LinkProperties(
                            linkerType = LinkerData.STORIES_TYPE,
                            ogTitle = MethodChecker.fromHtml(stories.meta.shareTitle).toString(),
                            ogImageUrl = stories.meta.shareImage,
                            ogDescription = MethodChecker.fromHtml(stories.meta.shareDescription)
                                .toString(),
                            deeplink = stories.appLink,
                            desktopUrl = stories.webLink
                        )
                    ),
                    productCount = stories.totalProductsFmt.ifEmpty { "0" },
                    meta = Meta(
                        activityTracker = stories.meta.activityTracker,
                        templateTracker = stories.meta.templateTracker
                    ),
                    status = StoriesDetailItem.StoryStatus.getByValue(stories.status),
                    performanceLink = buildPerformanceLink(author, stories.id),
                )
            }
        )
    }

    private fun isOwner(author: ContentStoriesDetails.Stories.Author): Boolean =
        author.id == userSession.shopId

    private fun buildMenu(
        template: ContentStoriesDetails.Stories.Interaction,
        author: ContentStoriesDetails.Stories.Author
    ) =
        buildList {
            if (!isOwner(author) && template.reportable) add(
                ContentMenuItem(
                    iconUnify = IconUnify.WARNING,
                    name = storiesR.string.stories_report,
                    type = ContentMenuIdentifier.Report
                )
            )

            if (isOwner(author)) add(
                ContentMenuItem(
                    iconUnify = IconUnify.GRAPH,
                    name = storiesR.string.stories_performance,
                    type = ContentMenuIdentifier.SeePerformance
                )
            )

            if (isOwner(author) && template.deletable) add(
                ContentMenuItem(
                    iconUnify = IconUnify.DELETE,
                    name = storiesR.string.stories_delete_story_title,
                    type = ContentMenuIdentifier.Delete
                )
            )
        }

    private fun buildAuthor(author: ContentStoriesDetails.Stories.Author): StoryAuthor {
        val type = AuthorType.convertValue(author.type)
        val name = MethodChecker.fromHtml(author.name).toString()

        return if (type == AuthorType.User) {
            StoryAuthor.Buyer(
                userName = name,
                userId = author.id,
                avatarUrl = author.thumbnailURL,
                appLink = author.appLink
            )
        } else {
            StoryAuthor.Shop(
                shopName = name,
                shopId = author.id,
                avatarUrl = author.thumbnailURL,
                badgeUrl = author.badgeURL,
                appLink = author.appLink
            )
        }
    }

    private fun buildAuthor(author: StoriesGroupsResponseModel.Author): StoryAuthor {
        val type = AuthorType.convertValue(author.type)
        val name = MethodChecker.fromHtml(author.name).toString()

        return if (type == AuthorType.User) {
            StoryAuthor.Buyer(
                userName = name,
                userId = author.id,
                avatarUrl = author.thumbnailURL,
                appLink = author.appLink,
            )
        } else {
            StoryAuthor.Shop(
                shopName = name,
                shopId = author.id,
                avatarUrl = author.thumbnailURL,
                badgeUrl = author.badgeURL,
                appLink = author.appLink
            )
        }
    }

    /**
     * https://www.tokopedia.com/stories/{authorType}/{shopId/userId}/statistic/{storyId} -> use tokopedia web
     */
    private fun buildPerformanceLink(author: StoryAuthor, storyId: String) =
        "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fstories%2F${author.type.type}%2F${author.id}%2Fstatistic%2F$storyId"

    companion object {
        private const val DEFAULT_DURATION = 7 * 1000
        private const val ERROR_DURATION = 3 * 1000
    }
}
