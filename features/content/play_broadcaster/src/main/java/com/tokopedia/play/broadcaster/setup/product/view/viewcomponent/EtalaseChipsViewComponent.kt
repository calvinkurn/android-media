package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class EtalaseChipsViewComponent(
    private val view: ChipsUnify
) : ViewComponent(view) {

    init {
        view.setChevronClickListener {
        }
    }

    fun setState(text: String, isSelected: Boolean) {
        view.chipText = text
        view.chipType = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }
}