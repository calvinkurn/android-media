package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherActiveLoadingStateViewHolder(itemView: View?) : AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_active_list_loading_state
    }

    override fun bind(element: LoadingModel?) {

    }
}