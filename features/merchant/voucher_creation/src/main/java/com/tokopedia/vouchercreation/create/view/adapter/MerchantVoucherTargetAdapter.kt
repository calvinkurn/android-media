package com.tokopedia.vouchercreation.create.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.create.view.customview.VoucherTargetCardItemView
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel
import kotlinx.android.synthetic.main.mvc_promo_code_info.view.*
import kotlinx.android.synthetic.main.mvc_voucher_target_item.view.*

class MerchantVoucherTargetAdapter(private val merchantVoucherTargetList: List<VoucherTargetItemUiModel>,
                                   private val onRequestNotifyLambda: () -> Unit = {},
                                   private val onShouldOpenBottomSheet: (CreateVoucherBottomSheetType, VoucherTargetCardType?) -> Unit = { _,_ -> }) : RecyclerView.Adapter<MerchantVoucherTargetAdapter.MerchantVoucherTargetViewHolder>() {

    class MerchantVoucherTargetViewHolder(itemView: VoucherTargetCardItemView) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantVoucherTargetViewHolder {
        val view = VoucherTargetCardItemView(parent.context)
        return MerchantVoucherTargetViewHolder(view)
    }

    override fun getItemCount(): Int = merchantVoucherTargetList.size

    override fun onBindViewHolder(holder: MerchantVoucherTargetViewHolder, position: Int) {
        merchantVoucherTargetList[position].let { uiModel ->
            (holder.itemView as? VoucherTargetCardItemView)?.run {
                val canShowBottomSheet = uiModel.voucherTargetType == VoucherTargetCardType.PRIVATE && !uiModel.isHavePromoCard
                setupCurrentView(uiModel.voucherTargetType, uiModel.isEnabled, uiModel.isHavePromoCard, uiModel.promoCode)
                voucherTargetItemRadioButton?.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onItemEnabled(position)
                        if (canShowBottomSheet) {
                            onShouldOpenBottomSheet(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, null)
                        }
                    }
                }
                voucherTargetPromoCodeInfo?.run {
                    if (isChangeEnabled && uiModel.isHavePromoCard) {
                        voucherCreatePromoCodeChange?.setOnClickListener {
                            onShouldOpenBottomSheet(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, null)
                        }
                    } else {
                        voucherCreatePromoCodeChange?.setOnClickListener(null)
                    }
                }
                voucherTargetDisplayVoucherText?.setOnClickListener {
                    onShouldOpenBottomSheet(CreateVoucherBottomSheetType.VOUCHER_DISPLAY, uiModel.voucherTargetType)
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