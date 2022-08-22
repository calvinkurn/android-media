package com.tokopedia.play.ui.promosheet.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayVoucherHeaderBinding
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

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
    RecyclerView.ViewHolder(view.root) {

       fun bind(item: PlayVoucherUiModel.InfoHeader){
            view.textView4.text = item.shopName
       }
}