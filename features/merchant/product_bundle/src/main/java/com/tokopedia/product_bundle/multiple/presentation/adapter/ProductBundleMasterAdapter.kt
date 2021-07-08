package com.tokopedia.product_bundle.multiple.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder.ProductBundleChipState
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder.ProductBundleChipState.NORMAL
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder.ProductBundleChipState.SELECTED

class ProductBundleMasterAdapter(private val clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.Adapter<ProductBundleMasterViewHolder>() {

    interface ProductBundleMasterItemClickListener {
        fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster)
    }

    private var productBundleMasterList: List<ProductBundleMaster> = listOf()
    private var productBundleChipsStates: ArrayList<ProductBundleChipState> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleMasterViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_master_item, parent, false)
        return ProductBundleMasterViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return productBundleMasterList.size
    }

    override fun onBindViewHolder(holderBundle: ProductBundleMasterViewHolder, position: Int) {
        holderBundle.bindData(productBundleMasterList[position], productBundleChipsStates[position])
    }

    fun setProductBundleMasterList(productBundleMasterList: List<ProductBundleMaster>) {
        this.productBundleMasterList = productBundleMasterList
        this.productBundleChipsStates = ArrayList(productBundleMasterList.map { NORMAL })
        notifyDataSetChanged()
    }

    fun deselectUnselectedItems(adapterPosition: Int) {
        this.productBundleChipsStates[adapterPosition] = SELECTED
        this.productBundleChipsStates.forEachIndexed { index, _ ->
            if (index != adapterPosition) productBundleChipsStates[index] = NORMAL
        }
        notifyDataSetChanged()
    }
}