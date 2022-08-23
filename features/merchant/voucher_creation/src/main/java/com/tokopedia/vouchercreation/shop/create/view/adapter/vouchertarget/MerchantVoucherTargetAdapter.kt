package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertarget

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.MvcVoucherTargetItemBinding
import com.tokopedia.vouchercreation.shop.create.view.customview.VoucherTargetCardItemView
import com.tokopedia.vouchercreation.shop.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel

class MerchantVoucherTargetAdapter(private val merchantVoucherTargetList: List<VoucherTargetItemUiModel>,
                                   private val onRequestNotifyLambda: () -> Unit,
                                   private val onShouldOpenBottomSheet: (CreateVoucherBottomSheetType, VoucherTargetCardType?) -> Unit,
                                   private val onSetVoucherTargetType: (Int) -> Unit,
                                   private val onRadioButtonClicked: (Int) -> Unit,
                                   private val onChangePromoCodeButtonClicked: () -> Unit,
                                   private val isEditVoucher: Boolean) : RecyclerView.Adapter<MerchantVoucherTargetAdapter.MerchantVoucherTargetViewHolder>() {

    class MerchantVoucherTargetViewHolder(itemView: VoucherTargetCardItemView) : RecyclerView.ViewHolder(itemView)

    private var binding: MvcVoucherTargetItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantVoucherTargetViewHolder {
        val view = VoucherTargetCardItemView(parent.context)
        binding = view.binding
        return MerchantVoucherTargetViewHolder(view)
    }

    override fun getItemCount(): Int = merchantVoucherTargetList.size

    override fun onBindViewHolder(holder: MerchantVoucherTargetViewHolder, position: Int) {
        merchantVoucherTargetList[position].let { uiModel ->
            (holder.itemView as? VoucherTargetCardItemView)?.run {
                val canShowBottomSheet = uiModel.voucherTargetType == VoucherTargetCardType.PRIVATE && !uiModel.isHavePromoCard
                setupCurrentView(uiModel.voucherTargetType, uiModel.isEnabled, uiModel.isHavePromoCard, uiModel.promoCode, !isEditVoucher)
                binding.voucherTargetItemRadioButton.run {
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            onItemEnabled(position)
                            if (canShowBottomSheet) {
                                onShouldOpenBottomSheet(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, null)
                            }
                        }
                    }
                    if (!isEditVoucher) {
                        setOnClickListener {
                            onRadioButtonClicked(uiModel.voucherTargetType.targetType)
                        }
                    } else {
                        isEnabled = false
                        isClickable = false
                    }
                }
                binding.voucherTargetPromoCodeInfo.run {
                    if (isChangeEnabled && uiModel.isHavePromoCard) {
                        binding.voucherCreatePromoCodeChange.setOnClickListener {
                            onChangePromoCodeButtonClicked()
                            onShouldOpenBottomSheet(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, null)
                        }
                    } else {
                        binding.voucherCreatePromoCodeChange.setOnClickListener(null)
                    }
                }
                binding.voucherTargetDisplayVoucherText.setOnClickListener {
                    onShouldOpenBottomSheet(CreateVoucherBottomSheetType.VOUCHER_DISPLAY, uiModel.voucherTargetType)
                }
            }
        }
    }

    private fun onItemEnabled(position: Int) {
        merchantVoucherTargetList.forEachIndexed { index, voucherTargetItemUiModel ->
            if (index == position) {
                onSetVoucherTargetType(voucherTargetItemUiModel.voucherTargetType.targetType)
                voucherTargetItemUiModel.isEnabled = true
            } else {
                voucherTargetItemUiModel.isEnabled = false

            }
        }
        onRequestNotifyLambda()
    }
}