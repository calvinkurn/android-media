package com.tokopedia.vouchergame.list.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchergame.R

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameListShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) {

    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_voucher_game_shimmering
    }
}