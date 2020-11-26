package com.tokopedia.home.benchmark

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel

object DynamicChannelGenerator {
    fun createLego6(): DynamicHomeChannel.Channels {
        return DynamicHomeChannel.Channels(
                name = "Test Header",
                header = DynamicHomeChannel.Header(name = "Test Header", applink = "www.tokopedia.com"),
                grids = arrayOf(
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_6789d6a3-2599-40a7-9740-b32720a1f60c.png",
                                applink = "www.tokopedia.com"
                        ),
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_5a07ff96-8ee1-4615-955c-f8290b070c0a.png",
                                applink = "www.tokopedia.com"
                        ),
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_333f5898-15e2-453c-afaa-2aadffbfba69.png",
                                applink = "www.tokopedia.com"
                        ),
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        ),
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        ),
                        DynamicHomeChannel.Grid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        )
                )
        )
    }


    fun createLego6Component(): ChannelModel {
        return ChannelModel(
                id = "1",
                groupId = "10",
                type = "Test Header",
                channelHeader = ChannelHeader(name = "Test Header", applink = "www.tokopedia.com"),
                channelGrids = listOf(
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_6789d6a3-2599-40a7-9740-b32720a1f60c.png",
                                applink = "www.tokopedia.com"
                        ),
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_5a07ff96-8ee1-4615-955c-f8290b070c0a.png",
                                applink = "www.tokopedia.com"
                        ),
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_333f5898-15e2-453c-afaa-2aadffbfba69.png",
                                applink = "www.tokopedia.com"
                        ),
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        ),
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        ),
                        ChannelGrid(
                                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square//",
                                applink = "www.tokopedia.com"
                        )
                )
        )
    }
}