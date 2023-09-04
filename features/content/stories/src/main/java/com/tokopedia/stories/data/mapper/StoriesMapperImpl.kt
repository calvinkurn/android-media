package com.tokopedia.stories.data.mapper

import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItemUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class StoriesMapperImpl @Inject constructor(private val userSession: UserSessionInterface) :
    StoriesMapper {

    override fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel
    ): StoriesGroupUiModel {
        return StoriesGroupUiModel(
            selectedGroupId = dataGroup.data.groups[dataGroup.data.meta.selectedGroupIndex].value,
            selectedGroupPosition = dataGroup.data.meta.selectedGroupIndex,
            groupHeader = dataGroup.data.groups.mapIndexed { indexGroupHeader, group ->
                StoriesGroupHeader(
                    groupId = group.value,
                    image = group.image,
                    title = group.name,
                    isSelected = dataGroup.data.meta.selectedGroupIndex == indexGroupHeader,
                )
            },
            groupItems = dataGroup.data.groups.mapIndexed { indexGroupItem, group ->
                StoriesGroupItemUiModel(
                    groupId = group.value,
                    detail = if (dataGroup.data.meta.selectedGroupIndex == indexGroupItem) {
                        StoriesDetailUiModel(
                            selectedGroupId = group.value,
                            selectedDetailPosition = dataDetail.data.meta.selectedStoriesIndex,
                            selectedDetailPositionCached = dataDetail.data.meta.selectedStoriesIndex,
                            detailItems = dataDetail.data.stories.map { stories ->
                                StoriesDetailItemUiModel(
                                    id = stories.id,
                                    event = StoriesDetailItemUiEvent.PAUSE,
                                    imageContent = stories.media.link,
                                    author = StoryAuthor.Shop(
                                        shopName = stories.author.name,
                                        shopId = stories.author.id,
                                        avatarUrl = stories.author.thumbnailURL,
                                        badgeUrl = stories.author.badgeURL
                                    ),
                                    menus = buildMenu(stories.interaction, stories.author),
                                )
                            }
                        )
                    } else StoriesDetailUiModel()
                )
            }
        )
    }

    override fun mapStoriesDetailRequest(dataDetail: StoriesDetailsResponseModel): StoriesDetailUiModel {
        return StoriesDetailUiModel(
            selectedGroupId = "",
            selectedDetailPosition = dataDetail.data.meta.selectedStoriesIndex,
            selectedDetailPositionCached = dataDetail.data.meta.selectedStoriesIndex,
            detailItems = dataDetail.data.stories.map { stories ->
                StoriesDetailItemUiModel(
                    id = stories.id,
                    event = StoriesDetailItemUiEvent.PAUSE,
                    imageContent = stories.media.link,
                    resetValue = -1,
                    isSameContent = false,
                    author = buildAuthor(stories.author),
                    menus = buildMenu(stories.interaction, stories.author),
                )
            }
        )
    }

    private fun isOwner(author: StoriesDetailsResponseModel.ContentStoriesDetails.Stories.Author): Boolean =
        author.id == userSession.shopId

    private fun buildMenu(
        template: StoriesDetailsResponseModel.ContentStoriesDetails.Stories.Interaction,
        author: StoriesDetailsResponseModel.ContentStoriesDetails.Stories.Author
    ) =
        buildList {
            when {
                !isOwner(author) && template.reportable -> add(
                    ContentMenuItem(
                        iconUnify = IconUnify.WARNING,
                        name = R.string.content_common_menu_report,
                        type = ContentMenuIdentifier.Report
                    )
                )

                isOwner(author) && template.deletable -> add(
                    ContentMenuItem(
                        iconUnify = IconUnify.DELETE,
                        name = com.tokopedia.stories.R.string.stories_delete_story_title,
                        type = ContentMenuIdentifier.Delete,
                    )
                )
            }
        }

    private fun buildAuthor(author: StoriesDetailsResponseModel.ContentStoriesDetails.Stories.Author): StoryAuthor {
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
