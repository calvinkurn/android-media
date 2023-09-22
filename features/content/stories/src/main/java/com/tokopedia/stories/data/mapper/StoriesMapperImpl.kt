package com.tokopedia.stories.data.mapper

import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel.ContentStoriesDetails
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel.ContentStoriesGroups
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.Meta
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.IMAGE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.VIDEO
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.stories.R as storiesR

class StoriesMapperImpl @Inject constructor(private val userSession: UserSessionInterface) :
    StoriesMapper {

    override fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel,
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
                    isSelected = groupSelectedPos == indexGroupHeader,
                )
            },
            groupItems = groupsItem.mapIndexed { indexGroupItem, group ->
                StoriesGroupItem(
                    groupId = group.value,
                    groupName = group.name,
                    detail = if (groupSelectedPos == indexGroupItem) {
                        mapStoriesDetailRequest(
                            selectedGroupId = group.value,
                            dataDetail = dataDetail,
                        )
                    } else StoriesDetail()
                )
            }
        )
    }

    override fun mapStoriesDetailRequest(
        selectedGroupId: String,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesDetail {
        val detailData = dataDetail.data
        if (detailData == ContentStoriesDetails()) return StoriesDetail()

        val storiesSelectedPos = detailData.meta.selectedStoriesIndex
        val storiesItem = detailData.stories
        return StoriesDetail(
            selectedGroupId = selectedGroupId,
            selectedDetailPosition = storiesSelectedPos,
            selectedDetailPositionCached = storiesSelectedPos,
            detailItems = storiesItem.map { stories ->
                StoriesDetailItem(
                    id = stories.id,
                    event = StoriesDetailItemUiEvent.PAUSE,
                    content = StoriesItemContent(
                        type = if (stories.media.type == IMAGE.value) IMAGE else VIDEO,
                        data = stories.media.link,
                        duration = 7 * 1000,
                    ),
                    resetValue = -1,
                    isSameContent = false,
                    author = buildAuthor(stories.author),
                    menus = buildMenu(stories.interaction, stories.author),
                    share = StoriesDetailItem.Sharing(
                        isShareable = stories.interaction.shareable,
                        metadata = LinkProperties(
                            ogTitle = stories.meta.shareTitle,
                            ogImageUrl = stories.meta.shareImage,
                            ogDescription = stories.meta.shareDescription
                        )
                    ),
                    productCount = stories.totalProductsFmt.ifEmpty { "0" },
                    meta = Meta(
                        activityTracker = stories.meta.activityTracker,
                        templateTracker = stories.meta.templateTracker,
                    ),
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
            when {
                isOwner(author) && template.deletable -> add(
                    ContentMenuItem(
                        iconUnify = IconUnify.DELETE,
                        name = storiesR.string.stories_delete_story_title,
                        type = ContentMenuIdentifier.Delete,
                    )
                )
            }
        }

    private fun buildAuthor(author: ContentStoriesDetails.Stories.Author): StoryAuthor {
        val type = AuthorType.convertValue(author.type)

        return if (type == AuthorType.User) {
            StoryAuthor.Buyer(
                userName = author.name,
                userId = author.id,
                avatarUrl = author.thumbnailURL,
            )
        } else {
            StoryAuthor.Shop(
                shopName = author.name,
                shopId = author.id,
                avatarUrl = author.thumbnailURL,
                badgeUrl = author.badgeURL
            )
        }
    }
}
