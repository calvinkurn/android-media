package com.tokopedia.vouchercreation.create.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import kotlinx.android.synthetic.main.mvc_next_step_button.view.*

class NextButtonViewHolder(itemView: View) : AbstractViewHolder<NextButtonUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.mvc_next_step_button
    }

    override fun bind(element: NextButtonUiModel) {
        itemView.nextButton?.setOnClickListener {
            element.onNext()
        }
    }

}