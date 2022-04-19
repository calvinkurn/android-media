package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemTitleBinding
import com.tokopedia.home_account.view.adapter.uimodel.TitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleViewHolder(
    view: View
) : BaseViewHolder(view) {

    private val binding: FundsAndInvestmentItemTitleBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(item: TitleUiModel?) {
        setText(item?.title)
    }

    private fun setText(text: String? = context.getString(R.string.funds_and_investment_balance_and_points)) {
        binding?.title?.text = text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_title
    }
}