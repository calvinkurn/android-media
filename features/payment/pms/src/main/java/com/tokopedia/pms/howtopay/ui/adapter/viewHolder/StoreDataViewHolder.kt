package com.tokopedia.pms.howtopay.ui.adapter.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.databinding.PmsHwpItemStoreInfoBinding
import com.tokopedia.pms.howtopay.data.model.StoreData

class StoreDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PmsHwpItemStoreInfoBinding.bind(view)

    fun bind(storeData: StoreData) {
        binding.run {
            tvStoreName.text = storeData.storeName
            tvStoreFee.text = storeData.storeFee
            ivStoreImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
            ivStoreImage.setImageUrl(storeData.storeLogo)
        }
    }
}
