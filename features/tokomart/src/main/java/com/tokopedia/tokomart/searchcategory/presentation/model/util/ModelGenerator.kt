package com.tokopedia.tokomart.searchcategory.presentation.model.util

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel

object ModelGenerator {
    fun createChannelModel(): ChannelModel {
        val testApplink1 = "tokopedia://digital/form?category_id=6&menu_id=4&template=voucher"
        val testApplink2 = "www.tokopedia.com"
        return ChannelModel(
                id = "1",
                groupId = "10",
                type = "Test Header",
                channelHeader = ChannelHeader(name = "", applink = "www.tokopedia.com"),
                channelGrids = listOf(
                        ChannelGrid(
                                id = "1",
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_6789d6a3-2599-40a7-9740-b32720a1f60c.png",
                                applink = testApplink2
                        ),
                        ChannelGrid(
                                id = "2",
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_5a07ff96-8ee1-4615-955c-f8290b070c0a.png",
                                applink = testApplink2
                        ),
                        ChannelGrid(
                                id = "3",
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_333f5898-15e2-453c-afaa-2aadffbfba69.png",
                                applink = testApplink2
                        )
                )
        )
    }
}