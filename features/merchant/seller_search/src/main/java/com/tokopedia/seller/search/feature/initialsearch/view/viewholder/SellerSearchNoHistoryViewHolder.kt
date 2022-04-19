package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.IC_NO_HISTORY_URL
import com.tokopedia.seller.search.databinding.InitialSearchNoHistoryBinding
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SellerSearchNoHistoryViewHolder(view: View): AbstractViewHolder<SellerSearchNoHistoryUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_no_history
    }

    private val binding: InitialSearchNoHistoryBinding? by viewBinding()

    override fun bind(element: SellerSearchNoHistoryUiModel) {
        binding?.run {
            ivNoHistory.setImageUrl(IC_NO_HISTORY_URL)
        }
    }
}