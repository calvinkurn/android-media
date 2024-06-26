package com.tokopedia.feedcomponent.view.viewmodel.carousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.play.widget.ui.PlayWidgetState

/**
 * Created by jegul on 08/10/20
 */
data class CarouselPlayCardModel(
        val playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true),
        val isAutoRefresh: Boolean = false
) : Visitable<DynamicFeedTypeFactory> {

    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}
