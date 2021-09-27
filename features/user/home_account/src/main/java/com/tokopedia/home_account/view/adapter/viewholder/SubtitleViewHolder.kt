package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemSubtitleBinding
import com.tokopedia.home_account.view.adapter.uimodel.SubtitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SubtitleViewHolder(
    view: View
) : BaseViewHolder(view) {

    private val binding: FundsAndInvestmentItemSubtitleBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(item: SubtitleUiModel?) {
        setText(item?.subtitle)
    }

    private fun setText(text: String? = context.getString(R.string.funds_and_investment_try_another)) {
        binding?.subtitle?.text = text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_subtitle
    }
}