package com.tokopedia.shop.common.view.bottomsheet.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.bottomsheet.viewholder.ShopShareBottomsheetViewHolder
import com.tokopedia.shop.common.view.model.ShopShareModel

class ShopShareBottomSheetAdapter(
    val context: Context?,
    private val bottomsheetListener: ShopShareBottomsheetListener,
    data: List<ShopShareModel>
) : RecyclerView.Adapter<ShopShareBottomsheetViewHolder>() {

    private var socialMediaList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShareBottomsheetViewHolder {
        return ShopShareBottomsheetViewHolder(
            View.inflate(context, ShopShareBottomsheetViewHolder.LAYOUT, null),
            bottomsheetListener
        )
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }

    override fun onBindViewHolder(holder: ShopShareBottomsheetViewHolder, position: Int) {
        holder.bind(socialMediaList[position])
    }
}
