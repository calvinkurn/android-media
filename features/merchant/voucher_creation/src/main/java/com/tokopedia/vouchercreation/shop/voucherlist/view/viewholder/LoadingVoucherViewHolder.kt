package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.item_mvc_shimmer_searchbar.view.*

class LoadingVoucherViewHolder(itemView: View?): AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_loading_voucher
    }

    override fun bind(element: LoadingModel) {
        with(itemView) {
            mvcSearchBarContainer?.gone()
        }
    }
}