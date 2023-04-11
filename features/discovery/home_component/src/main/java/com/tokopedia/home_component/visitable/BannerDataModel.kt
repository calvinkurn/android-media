package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by devarafikry on 17/11/20.
 */
data class BannerDataModel(
        val channelModel: ChannelModel? = null,
        val isCache: Boolean = false,
        val dimenMarginTop: Int = com.tokopedia.home_component.R.dimen.home_banner_default_margin_top,
        val dimenMarginBottom: Int = com.tokopedia.home_component.R.dimen.home_banner_default_margin_bottom,
        val cardInteraction: Boolean = false,
        val enableDotsAndInfiniteScroll: Boolean = false,
        val scrollTransitionDuration: Long = 5000L
): ImpressHolder(), HomeComponentVisitable{
    override fun visitableId(): String? {
        return channelModel?.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is BannerDataModel && b.channelModel == channelModel
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
