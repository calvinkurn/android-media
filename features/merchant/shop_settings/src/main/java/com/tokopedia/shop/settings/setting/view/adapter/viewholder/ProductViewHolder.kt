package com.tokopedia.shop.settings.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.databinding.ItemShopPageSettingProductBinding
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.utils.view.binding.viewBinding

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemShopPageSettingProductBinding? by viewBinding()

    fun bind(clickListener: ShopPageSettingAdapter.ProductItemClickListener) {
        binding?.apply {
            tvDisplayProducts.setOnClickListener { clickListener.onDisplayProductsClicked() }
            tvEditEtalase.setOnClickListener { clickListener.onEditEtalaseClicked() }
        }
    }
}