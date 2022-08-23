package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.VoucherItemCardEventsBinding
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle

/**
 * created by @bayazidnasir on 23/8/2022
 */

class EventsViewHolder(itemView: View): AbstractViewHolder<ItemsEvents>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_events

        private const val IS_ENTERTAIN = 1
    }

    override fun bind(element: ItemsEvents) {
        val binding = VoucherItemCardEventsBinding.bind(itemView)
        val metadata = getMetadata(element.item)

        renderProducts(binding, metadata, element.item)
        renderAddress(binding, metadata)
        renderTime(binding, metadata)
    }

    private fun renderProducts(
        binding: VoucherItemCardEventsBinding,
        metadata: MetaDataInfo,
        item: Items,
    ) {
        if (metadata.productImage.isNotEmpty()) {
            binding.ivDeal.loadImageCircle(metadata.productImage)
        } else {
            binding.ivDeal.loadImageCircle(metadata.entityImage)
        }

        if (metadata.entityProductName.isNotEmpty()) {
            binding.tvDealIntro.text = metadata.entityProductName
        } else {
            binding.tvDealIntro.text = item.title
        }
    }

    private fun renderAddress(
        binding: VoucherItemCardEventsBinding,
        metadata: MetaDataInfo
    ){
        if (metadata.locationName.isNotEmpty()){
            binding.cityEvent.text = metadata.locationName
        }

        if (metadata.locationDesc.isNotEmpty()){
            binding.addressEvent.text = metadata.locationDesc
        }
    }

    private fun renderTime(
        binding: VoucherItemCardEventsBinding,
        metadata: MetaDataInfo
    ){
        if (metadata.isHiburan == IS_ENTERTAIN) {
            if (metadata.endTime.isNotEmpty()){
                binding.tanggalEventsTitle.visible()
                binding.tanggalEventsTitle.text = itemView.context.getString(R.string.text_valid_till)
                binding.tanggalEvents.text = metadata.endTime
            }
        } else {
            if (metadata.endTime.isNotEmpty() && metadata.startTime.isNotEmpty()){
                binding.tanggalEventsTitle.visible()
                binding.tanggalEventsTitle.text = itemView.context.getString(R.string.tanggal_events)
                binding.tanggalEvents.text = itemView.context.getString(R.string.event_date_label, metadata.startTime, metadata.endTime)
            }
        }
    }

    private fun getMetadata(item: Items): MetaDataInfo {
        val gson = Gson()
        return gson.fromJson(item.metaData, MetaDataInfo::class.java)
    }
}