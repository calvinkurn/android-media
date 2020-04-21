package com.tokopedia.vouchercreation.create.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.view.customview.VoucherTargetCardItemView
import kotlinx.android.synthetic.main.mvc_voucher_target_item.view.*

class MerchantVoucherTargetAdapter(private val onRequestNotifyLambda: () -> Unit = {},
                                   private val onShouldOpenBottomSheet: () -> Unit = {}) : RecyclerView.Adapter<MerchantVoucherTargetAdapter.MerchantVoucherTargetViewHolder>() {

    class MerchantVoucherTargetViewHolder(itemView: VoucherTargetCardItemView) : RecyclerView.ViewHolder(itemView)

    private var merchantVoucherTargetList = VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantVoucherTargetViewHolder {
        val view = VoucherTargetCardItemView(parent.context)
        return MerchantVoucherTargetViewHolder(view)
    }

    override fun getItemCount(): Int = merchantVoucherTargetList.size

    override fun onBindViewHolder(holder: MerchantVoucherTargetViewHolder, position: Int) {
        merchantVoucherTargetList[position].let { uiModel ->
            (holder.itemView as? VoucherTargetCardItemView)?.run {
                setupCurrentView(uiModel.voucherTargetType, uiModel.isEnabled, uiModel.isHavePromoCard, uiModel.promoCode)
                voucherTargetItemRadioButton?.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onItemEnabled(position)
                        val canShowBottomSheet = uiModel.voucherTargetType == VoucherTargetCardItemView.TARGET_SPECIAL_TYPE && !uiModel.isHavePromoCard
                        if (canShowBottomSheet) {
                            onShouldOpenBottomSheet()
                        }
                    }
                }
            }
        }
    }

    private fun onItemEnabled(position: Int) {
        merchantVoucherTargetList.forEachIndexed { index, voucherTargetItemUiModel ->
            voucherTargetItemUiModel.isEnabled = index == position
        }
        onRequestNotifyLambda()
    }
}