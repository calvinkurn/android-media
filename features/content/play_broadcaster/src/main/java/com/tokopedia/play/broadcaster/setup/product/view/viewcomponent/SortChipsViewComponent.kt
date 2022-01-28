package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class SortChipsViewComponent(
    private val view: ChipsUnify
) : ViewComponent(view) {

    init {
        view.setChevronClickListener {
        }
    }

    fun setText(text: String) {
        if (text.isEmpty()) {
            view.chipText = getString(R.string.play_bro_etalase_sort)
            view.chipType = ChipsUnify.TYPE_NORMAL
        } else {
            view.chipText = text
            view.chipType = ChipsUnify.TYPE_SELECTED
        }
    }
}