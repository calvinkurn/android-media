package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.model.QuestionOptionMultipleUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class OptionMultipleViewHolder(
    itemView: View
) : AbstractViewHolder<QuestionOptionMultipleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_question_option_multiple
    }

    override fun bind(element: QuestionOptionMultipleUiModel) {

    }
}