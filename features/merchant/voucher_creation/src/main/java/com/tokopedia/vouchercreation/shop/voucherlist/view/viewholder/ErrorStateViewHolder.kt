package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherListErrorStateBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.ErrorStateUiModel

/**
 * Created By @ilhamsuaib on 26/04/20
 */

class ErrorStateViewHolder(itemView: View?,
                           private val onTryAgain: () -> Unit,
                           private val onImpression: (String) -> Unit) : AbstractViewHolder<ErrorStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_error_state
    }

    private var binding: ItemMvcVoucherListErrorStateBinding? by viewBinding()

    override fun bind(element: ErrorStateUiModel) {
        onImpression(ErrorStateUiModel.DATA_KEY)
        binding?.geMvcList?.setActionClickListener {
            onTryAgain()
        }
    }
}