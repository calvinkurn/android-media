package com.tokopedia.play.ui.promosheet.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemShopCouponBinding
import com.tokopedia.play.ui.promosheet.viewholder.MerchantVoucherNewViewHolder
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapterDelegate(
        listener: MerchantVoucherNewViewHolder.Listener
) : TypedAdapterDelegate<PlayVoucherUiModel.Merchant, PlayVoucherUiModel, MerchantVoucherNewViewHolder>(R.layout.item_shop_coupon), MerchantVoucherNewViewHolder.Listener by listener {

    override fun onBindViewHolder(item: PlayVoucherUiModel.Merchant, holder: MerchantVoucherNewViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MerchantVoucherNewViewHolder {
        val binding = ItemShopCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MerchantVoucherNewViewHolder(binding, this)
    }
}