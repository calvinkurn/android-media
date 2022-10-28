package com.tokopedia.vouchercreation.shop.create.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcNextStepButtonBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.NextButtonUiModel

class NextButtonViewHolder(itemView: View) : AbstractViewHolder<NextButtonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_next_step_button
    }

    private var binding: MvcNextStepButtonBinding? by viewBinding()

    override fun bind(element: NextButtonUiModel) {
        binding?.nextButton?.run {
            isEnabled = element.isEnabled
            isLoading = false
            setOnClickListener {
                isLoading = true
                element.onNext()
            }
        }
    }

}