package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemTitleBinding
import com.tokopedia.home_account.view.adapter.uiview.TitleUiView
import com.tokopedia.utils.view.binding.viewBinding

class TitleViewHolder(
    view: View
) : AbstractViewHolder<TitleUiView>(view) {

    private val binding: FundsAndInvestmentItemTitleBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: TitleUiView?) {
        setText(element?.title)
    }

    private fun setText(text: String? = context.getString(R.string.funds_and_investment_balance_and_points)) {
        binding?.title?.text = text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_title
    }
}