package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherUrl.NO_VOUCHER_RESULT_URL
import com.tokopedia.vouchercreation.databinding.ItemMvcBottomsheetMenuBinding
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherListNoResultStateBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.NoResultStateUiModel
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 26/04/20
 */

class NoResultStateViewHolder(itemView: View?,
                              private val listener: VoucherViewHolder.Listener) : AbstractViewHolder<NoResultStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_no_result_state
    }

    private var binding: ItemMvcVoucherListNoResultStateBinding? by viewBinding()

    override fun bind(element: NoResultStateUiModel) {
        try {
            binding?.imgMvcNoResultState?.loadImage(NO_VOUCHER_RESULT_URL)
            binding?.root?.addOnImpressionListener(element.impressHolder) {
                listener.onImpressionListener(NoResultStateUiModel.DATA_KEY)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}