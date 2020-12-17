package com.tokopedia.tkpd.feed_component.mock

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Action
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.CtaLink
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title

object MockTitleModel {
    fun get() = Title(text = "Promoted", textBadge = "https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/terompet.png",
            isIsClicked = true, action = MockAction.get(), ctaLink = MockCtaLink.get()
    )
}

object MockCtaLink {
    fun get() = CtaLink(text = "", appLink = "", webLink = "")
}

object MockAction {
    fun get() = Action(event = "", action = "", appLink = "", webLink = "")
}