package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.TopadsCreateKeywordListItemBinding
import com.tokopedia.topads.view.adapter.product.viewmodel.KeyWordItemViewModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding

class KeyWordItemViewHolder(val view: View) : ProductViewHolder<KeyWordItemViewModel>(view) {

    private val binding: TopadsCreateKeywordListItemBinding? by viewBinding()

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_keyword_list_item
    }

    override fun bind(item: KeyWordItemViewModel) {
        binding?.title?.text = item.keywordDataItem.keyword
        binding?.bid?.text = String.format(view.context.getString(R.string.topads_ads_price_format_1),
            CurrencyFormatHelper.convertToRupiah(item.keywordDataItem.bidSuggest))
    }
}
