package com.tokopedia.product_bundle.multiple.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterBaseViewHolder
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterOneItemViewHolder
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterBaseViewHolder.ProductBundleChipState
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterBaseViewHolder.ProductBundleChipState.NORMAL
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterBaseViewHolder.ProductBundleChipState.SELECTED

class ProductBundleMasterAdapter(private val clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.Adapter<ProductBundleMasterBaseViewHolder>() {

    interface ProductBundleMasterItemClickListener {
        fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster)
    }

    private var productBundleMasterList: List<ProductBundleMaster> = listOf()
    private var productBundleChipsStates: ArrayList<ProductBundleChipState> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleMasterBaseViewHolder {
        return if (productBundleMasterList.singleOrNull() != null) {
            // layout for single item
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_master_one_item, parent, false)
            ProductBundleMasterOneItemViewHolder(rootView)
        } else {
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_master_item, parent, false)
            ProductBundleMasterViewHolder(rootView, clickListener)
        }
    }

    override fun getItemCount(): Int {
        return productBundleMasterList.size
    }

    override fun onBindViewHolder(holderBundle: ProductBundleMasterBaseViewHolder, position: Int) {
        holderBundle.bindData(productBundleMasterList[position], productBundleChipsStates[position])
    }

    fun setProductBundleMasters(productBundleMasterList: List<ProductBundleMaster>, bundleId: Long) {
        this.productBundleMasterList = productBundleMasterList
        this.productBundleChipsStates = ArrayList(productBundleMasterList.map { bundleMaster ->
            if (bundleMaster.bundleId == bundleId) SELECTED
            else NORMAL
        })
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