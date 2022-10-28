package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.widgets

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcVoucherTargetWidgetBinding
import com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertarget.MerchantVoucherTargetAdapter
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.VoucherTargetUiModel

class VoucherTargetViewHolder(itemView: View) : AbstractViewHolder<VoucherTargetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_target_widget
    }

    private var binding: MvcVoucherTargetWidgetBinding? by viewBinding()

    override fun bind(element: VoucherTargetUiModel) {
        binding?.apply {
            voucherTargetWidgetRecyclerView.layoutManager = LinearLayoutManager(root.context)
            voucherTargetWidgetRecyclerView.adapter = MerchantVoucherTargetAdapter(
                    element.voucherTargetList,
                    ::onRequestNotify,
                    element.onShouldShowBottomSheet,
                    element.onSetActiveVoucherTargetType,
                    element.onRadioButtonClicked,
                    element.onChangePromoButtonClicked,
                    element.isEditVoucher)
        }
    }

    private fun onRequestNotify() {
        binding?.voucherTargetWidgetRecyclerView?.run {
            post {
                adapter?.notifyDataSetChanged()
            }
        }
    }
}