package com.tokopedia.feedcomponent.view.viewmodel.highlight

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by yoasfs on 2019-08-06
 */
data class HighlightCardModel (
    var postId: String = "",
    var positionInFeed: Int = 0,
    val thumbnail: String = "",
    val applink: String = "",
    val type: String = "",
    val header: Header = Header(),
    val footer: Footer = Footer(),
    val template: Template = Template(),
    val tracking: MutableList<TrackingModel> = ArrayList(),
    val videoDuration: String = "",
    val impressHolder: ImpressHolder = ImpressHolder()
)
