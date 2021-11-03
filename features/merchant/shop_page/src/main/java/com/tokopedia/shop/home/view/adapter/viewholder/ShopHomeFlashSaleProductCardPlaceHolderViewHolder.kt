package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopHomeFlashSaleProductCardPlaceHolderViewHolder(
    itemView: View,
    listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var placeHolderTextView: Typography? = itemView.findViewById(R.id.tgp_placeholder_text)
    private var ctaSeeAllView: View? = itemView.findViewById(R.id.cta_see_all_view)

    init { setupClickListener(listener) }

    @SuppressLint("SetTextI18n")
    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.fsUiModel = fsUiModel
        placeHolderTextView?.text = uiModel.totalProductWording
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        ctaSeeAllView?.setOnClickListener {
            fsUiModel?.run {
                listener.onPlaceHolderClickSeeAll(this)
            }
        }
    }
}