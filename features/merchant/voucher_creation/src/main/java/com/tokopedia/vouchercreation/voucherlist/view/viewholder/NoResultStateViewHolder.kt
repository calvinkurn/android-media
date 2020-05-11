package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.NoResultStateUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list_no_result_state.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 26/04/20
 */

class NoResultStateViewHolder(itemView: View?) : AbstractViewHolder<NoResultStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_no_result_state
    }

    override fun bind(element: NoResultStateUiModel) {
        with(itemView) {
            try {
                imgMvcNoResultState.loadImageDrawable(R.drawable.il_mvc_no_result)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}