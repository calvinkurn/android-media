package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemSubtitleBinding
import com.tokopedia.home_account.view.adapter.uiview.SubtitleUiView
import com.tokopedia.utils.view.binding.viewBinding

class SubtitleViewHolder(
    view: View
) : AbstractViewHolder<SubtitleUiView>(view) {

    private val binding: FundsAndInvestmentItemSubtitleBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: SubtitleUiView?) {
        setText(element?.subtitle)
    }

    private fun setText(text: String? = context.getString(R.string.funds_and_investment_try_another)) {
        binding?.subtitle?.text = text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_subtitle
    }
}