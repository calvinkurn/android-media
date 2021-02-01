package com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.howtopay_native.data.model.StoreData
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.pms_hwp_item_store_info.view.*

class StoreDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvStoreName: Typography = itemView.tvStoreName
    private val tvStoreFee: Typography = itemView.tvStoreFee
    private val ivStoreImage = itemView.ivStoreImage

    fun bind(storeData: StoreData) {
        tvStoreName.text = storeData.storeName
        tvStoreFee.text = storeData.storeFee
        ivStoreImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        ivStoreImage.setImageUrl(storeData.storeLogo)
    }
}