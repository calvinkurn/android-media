package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.LoadingStateUiModel

/**
 * Created By @ilhamsuaib on 27/04/20
 */

class LoadingStateVoucherViewHolder(itemView: View?) : AbstractViewHolder<LoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_loading_state_voucher
    }

    override fun bind(element: LoadingStateUiModel) { }
}