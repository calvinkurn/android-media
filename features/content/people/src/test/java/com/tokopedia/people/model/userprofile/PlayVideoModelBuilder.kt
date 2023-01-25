package com.tokopedia.people.model.userprofile

import com.tokopedia.people.model.*

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class PlayVideoModelBuilder {

    fun buildModel(
        size: Int = 10,
        isEmpty: Boolean = false,
    ): UserPostModel {
        return UserPostModel(
            playGetContentSlot = PlayGetContentSlot(
                data = if (isEmpty) emptyList<PlayPostContent>().toMutableList() else mutableListOf(
                    PlayPostContent(
                        hash = "",
                        type = "",
                        title = "Title",
                        id = "",
                        items = MutableList(size) {
                            PlayPostContentItem(
                                appLink = "appLink $it",
                                coverUrl = "https://tokopedia.com/cover/$it",
                                description = "Description $it",
                                isLive = false,
                                id = it.toString(),
                                title = "Title $it",
                                startTime = "",
                                airTime = "",
                                webLink = "webLink $it",
                                stats = PlayPostContentItemStats(
                                    view = StatsView(
                                        value = "123",
                                        formatted = "123",
                                    ),
                                ),
                                configurations = PlayPostConfigurations(
                                    hasPromo = false,
                                    reminder = PostReminder(
                                        isSet = false,
                                    ),
                                    promoLabels = emptyList(),
                                ),
                                partner = Partner(
                                    id = "1",
                                    name = "Siapa Yah",
                                ),
                            )
                        },
                    ),
                ),
                playGetContentSlot = PlayGetContentSlotMeta(
                    isAutoplay = true,
                    maxAutoplayInCell = 30,
                    nextCursor = "123",
                ),
            ),
        )
    }
}
