package com.tokopedia.product.manage.feature.list.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductManageMoreMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.model.ProductMoreMenuModel

class ProductManageMoreMenuAdapter(
        val context: Context,
        val listener: ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener
) : RecyclerView.Adapter<ProductManageMoreMenuViewHolder>() {
    
    private var moreMenuList = arrayListOf(
            ProductMoreMenuModel(context.resources?.getString(R.string.product_manage_shop_showcase_more_menu_text))
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductManageMoreMenuViewHolder {
        return ProductManageMoreMenuViewHolder(
                LayoutInflater.from(context).inflate(ProductManageMoreMenuViewHolder.LAYOUT, parent, false),
                listener
        )
    }

    override fun getItemCount(): Int {
        return moreMenuList.size
    }

    override fun onBindViewHolder(holder: ProductManageMoreMenuViewHolder, position: Int) {
        holder.bind(moreMenuList[position])
    }

}