package com.tokopedia.shop.info.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.info.view.adapter.viewholder.ShopShareBottomSheetViewHolder
import com.tokopedia.shop.info.view.model.SocialMediaShareModel

class ShopShareBottomSheetAdapter (
        val context: Context?,
        val listener: ShopShareBottomSheetViewHolder.ShopShareListener,
        val data: List<SocialMediaShareModel>
): RecyclerView.Adapter<ShopShareBottomSheetViewHolder>() {

    private var socialMediaList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShareBottomSheetViewHolder {
        return ShopShareBottomSheetViewHolder(
                View.inflate(context, ShopShareBottomSheetViewHolder.LAYOUT, null),
                listener
        )
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }

    override fun onBindViewHolder(holder: ShopShareBottomSheetViewHolder, position: Int) {
        holder.bind(socialMediaList[position])
    }

}