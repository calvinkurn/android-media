package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleClickListener
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleMultiplePackageViewHolder

class ShopHomeProductBundleMultipleAdapter(
        private val multipleProductBundleClickListener: MultipleProductBundleClickListener
): RecyclerView.Adapter<ShopHomeProductBundleMultiplePackageViewHolder>() {

    private var bundleProducts: List<ShopHomeBundleProductUiModel> = listOf()
    private var multipleBundleDetail: ShopHomeProductBundleDetailUiModel = ShopHomeProductBundleDetailUiModel()
    private var multipleBundleParent: ShopHomeProductBundleItemUiModel = ShopHomeProductBundleItemUiModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeProductBundleMultiplePackageViewHolder {
        return ShopHomeProductBundleMultiplePackageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        ShopHomeProductBundleMultiplePackageViewHolder.LAYOUT,
                        parent,
                        false
                ),
                multipleProductBundleClickListener
        )
    }

    override fun onBindViewHolder(holder: ShopHomeProductBundleMultiplePackageViewHolder, position: Int) {
        holder.bind(
                bundleProducts[position],
                multipleBundleDetail,
                multipleBundleParent,
                position
        )
    }

    override fun getItemCount(): Int {
        return bundleProducts.size
    }

    fun updateDataSet(
            newList: List<ShopHomeBundleProductUiModel>,
            bundleDetail: ShopHomeProductBundleDetailUiModel,
            bundleParent: ShopHomeProductBundleItemUiModel
    ) {
        bundleProducts = newList
        multipleBundleDetail = bundleDetail
        multipleBundleParent = bundleParent
        notifyDataSetChanged()
    }

}