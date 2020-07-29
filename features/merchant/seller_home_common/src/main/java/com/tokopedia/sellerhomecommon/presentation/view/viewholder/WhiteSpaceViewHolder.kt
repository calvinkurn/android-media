package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.WhiteSpaceUiModel

/**
 * Created By @ilhamsuaib on 29/07/20
 */

class WhiteSpaceViewHolder(itemView: View?) : AbstractViewHolder<WhiteSpaceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_white_space
    }

    override fun bind(element: WhiteSpaceUiModel?) {}
}