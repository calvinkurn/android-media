package com.tokopedia.home_component.mock.channel

import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel

object MockChannelBanner {
    fun get(): ChannelBanner {
        return ChannelBanner(
                "1",
                "Title Mock Channel Banner",
                "Description Mock Channel Banner",
                "",
                "https://www.tokopedia.com/",
                "tokopedia://home",
                "",
                "https://ecs7-p.tokopedia.net/img/attachment/2020/5/12/53561064/53561064_7ce01cda-746c-48cc-827c-58633f2b0793.png",
                ""
        )
    }
}