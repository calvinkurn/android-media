package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherListEmptyStateBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.EmptyStateUiModel

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class EmptyStateViewHolder(
    itemView: View?,
    private val onImpressionListener: (dataKey: String) -> Unit
) : AbstractViewHolder<EmptyStateUiModel>(itemView) {

    companion object {
        const val EMPTY_IMAGE_URL = TokopediaImageUrl.EMPTY_IMAGE_URL

        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_empty_state
    }

    private var binding: ItemMvcVoucherListEmptyStateBinding? by viewBinding()

    override fun bind(element: EmptyStateUiModel) {
        binding?.apply {
            imgMvcEmptyState.loadImage(EMPTY_IMAGE_URL)
            root.addOnImpressionListener(element.impressHolder) {
                onImpressionListener(EmptyStateUiModel.DATA_KEY)
            }
            val title = if (element.isActiveVoucher) {
                root.context.getString(R.string.mvc_no_active_voucher)
            } else {
                root.context.getString(R.string.mvc_no_voucher_history_yet)
            }
            imgMvcEmptyState.loadImage(VoucherUrl.NO_VOUCHER_RESULT_URL)
            tvMvcEmptyStateTitle.text = title
            tvMvcEmptyStateViewHistory.isVisible = element.isActiveVoucher

            if (!element.isEligible) btnMvcEmptyStateAction?.hide()
            btnMvcEmptyStateAction.setOnClickListener {
                element.onCreateVoucherClicked()
            }
            tvMvcEmptyStateViewHistory.setOnClickListener {
                element.onSeeHistoryClicked()
            }
        }
    }
}
