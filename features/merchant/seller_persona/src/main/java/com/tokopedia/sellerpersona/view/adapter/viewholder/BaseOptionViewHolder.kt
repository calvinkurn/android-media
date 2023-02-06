package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 06/02/23.
 */

abstract class BaseOptionViewHolder<T : BaseOptionUiModel>(
    itemView: View
) : AbstractViewHolder<T>(itemView) {

    interface Listener {
        fun onOptionItemSelectedListener(option: BaseOptionUiModel)
    }
}