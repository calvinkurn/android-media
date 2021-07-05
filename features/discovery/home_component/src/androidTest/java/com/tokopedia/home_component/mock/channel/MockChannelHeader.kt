package com.tokopedia.home_component.mock.channel

import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel

object MockChannelHeader {
    fun get(): ChannelHeader {
        return ChannelHeader(
                "1",
                "Title Mock Channel Header",
                "Subtitle Mock Channel Header",
                "2020-07-14T13:59:00+07:00",
                1594701227,
                "tokopedia://home",
                "https://www.tokopedia.com/",
                "red",
                "https://ecs7-p.tokopedia.net/img/attachment/2020/2/19/31902163/31902163_54855172-a370-46cc-93a2-154e6919143f.jpg",
                ""
        )
    }
}