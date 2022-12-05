package com.tokopedia.videoTabComponent.model

import com.tokopedia.videoTabComponent.domain.model.data.*

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayVideoModelBuilder {

    fun getContentSlotResponse(
        playGetContentSlot: PlayGetContentSlotResponse = PlayGetContentSlotResponse(),
        isDataFromTabClick: Boolean = false
    ) = ContentSlotResponse(
        playGetContentSlot = playGetContentSlot,
        isDataFromTabClick = isDataFromTabClick
    )

    fun getContentSlotResponseWithTabData(
        playGetContentSlot: PlayGetContentSlotResponse = getContentPlayTabResponse(),
        isDataFromTabClick: Boolean = false
    ) = ContentSlotResponse(
        playGetContentSlot = playGetContentSlot,
        isDataFromTabClick = isDataFromTabClick
    )

    fun getContentSlotResponseWithTabDataTabListEmpty(
        playGetContentSlot: PlayGetContentSlotResponse = getContentPlayTabResponseWithTabListEmpty(),
        isDataFromTabClick: Boolean = false
    ) = ContentSlotResponse(
        playGetContentSlot = playGetContentSlot,
        isDataFromTabClick = isDataFromTabClick
    )

    private fun getContentPlayTabResponse(
        playSlotList: List<PlaySlot> = buildPlaySlotList()
    ) = PlayGetContentSlotResponse(
        data = playSlotList
    )

    private fun getContentPlayTabResponseWithTabListEmpty(
        playSlotList: List<PlaySlot> = buildPlaySlotListWithTabListEmpty()
    ) = PlayGetContentSlotResponse(
        data = playSlotList
    )

    private fun buildPlaySlotList() = listOf(
        PlaySlot(
            id = "83",
            title = "Tab Menu",
            type = "tabMenu",
            items = listOf(
                PlaySlotItems(
                    id = "18",
                    slug_id = "live",
                    label = "Live",
                    icon_url = "",
                    group = "chan-v2_1-live",
                    source_type = "",
                    source_id = "",
                    configurations = Configurations(
                        hasPromo = false,
                        reminder = Configurations.Reminder(isSet = false),
                        promoLabels = emptyList()
                    )
                )
            ),
            hash = "k)F?Pb5*hT7lTW.\u003db\u003cb7nrMad",
            lihat_semua = PlayLihatSemua(
                show = false,
                label = "",
                web_link = ""
            ),
            inplace_pager = PlaySlotParams()
        )
    )

    private fun buildPlaySlotListWithTabListEmpty() = listOf(
        PlaySlot(
            id = "83",
            title = "Tab Menu",
            type = "tabMenu",
            items = emptyList(),
            hash = "k)F?Pb5*hT7lTW.\u003db\u003cb7nrMad",
            lihat_semua = PlayLihatSemua(
                show = false,
                label = "",
                web_link = ""
            ),
            inplace_pager = PlaySlotParams()
        )
    )
}
