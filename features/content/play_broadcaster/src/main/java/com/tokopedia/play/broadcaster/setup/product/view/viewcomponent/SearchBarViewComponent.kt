package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class SearchBarViewComponent(
    private val view: PlaySearchBar,
    eventBus: EventBus<in Any>,
) : ViewComponent(view) {

    init {

    }

    fun setState(text: String, isSelected: Boolean) {
//        view.chipText = text
//        view.chipType = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }

    fun setPlaceholder(placeholder: String) {
        view.setHint(placeholder)
    }
}