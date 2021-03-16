package com.tokopedia.vouchercreation.create.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import kotlinx.android.synthetic.main.mvc_next_step_button.view.*

class NextButtonViewHolder(itemView: View) : AbstractViewHolder<NextButtonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_next_step_button
    }

    override fun bind(element: NextButtonUiModel) {
        itemView.nextButton?.run {
            isEnabled = element.isEnabled
            isLoading = false
            setOnClickListener {
                isLoading = true
                element.onNext()
            }
        }
    }

}