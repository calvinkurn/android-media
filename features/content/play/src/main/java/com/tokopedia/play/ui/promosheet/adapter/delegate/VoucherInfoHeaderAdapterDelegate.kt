package com.tokopedia.play.ui.promosheet.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayVoucherHeaderBinding
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.util.extension.bold
import com.tokopedia.play_common.util.extension.buildSpannedString

/**
 * @author by astidhiyaa on 22/08/22
 */

class VoucherInfoHeaderAdapterDelegate :
    TypedAdapterDelegate<PlayVoucherUiModel.InfoHeader, PlayVoucherUiModel, VoucherInfoHeaderViewHolder>(
        R.layout.item_play_voucher_header
    ) {
   override fun onBindViewHolder(
      item: PlayVoucherUiModel.InfoHeader,
      holder: VoucherInfoHeaderViewHolder
   ) {
      holder.bind(item)
   }

   override fun onCreateViewHolder(
      parent: ViewGroup,
      basicView: View
   ): VoucherInfoHeaderViewHolder {
      val binding = ItemPlayVoucherHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return VoucherInfoHeaderViewHolder(binding)
   }

}

class VoucherInfoHeaderViewHolder(private val view: ItemPlayVoucherHeaderBinding) :
    BaseViewHolder(view.root) {

       fun bind(item: PlayVoucherUiModel.InfoHeader){
            view.tvHeader.text = buildSpannedString {
                append(getString(R.string.play_voucher_header))
                append(" ")
                bold {
                    append(item.shopName)
                }
            }
       }
}