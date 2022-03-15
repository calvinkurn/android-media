package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleSinglePackageViewHolder

class ShopHomeProductBundleSingleAdapter(
        private val singleBundleVariantSelectedListener: SingleBundleVariantSelectedListener
): RecyclerView.Adapter<ShopHomeProductBundleSinglePackageViewHolder>() {

    private var bundleDetails: List<ShopHomeProductBundleDetailUiModel> = listOf()
    private var lastSelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeProductBundleSinglePackageViewHolder {
        return ShopHomeProductBundleSinglePackageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        ShopHomeProductBundleSinglePackageViewHolder.LAYOUT,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ShopHomeProductBundleSinglePackageViewHolder, position: Int) {
        val currentBundle = bundleDetails.getOrNull(position) ?: ShopHomeProductBundleDetailUiModel()
        holder.bind(currentBundle)
        holder.itemView.apply {
            setOnClickListener {
                // deselect last selected bundle
                val lastSelectedBundle = bundleDetails.getOrNull(lastSelectedPosition) ?: ShopHomeProductBundleDetailUiModel()
                lastSelectedBundle.isSelected = false
                notifyItemChanged(lastSelectedPosition)

                // select new bundle
                currentBundle.isSelected = !currentBundle.isSelected
                lastSelectedPosition = position
                notifyItemChanged(position)
                singleBundleVariantSelectedListener.onSingleVariantSelected(currentBundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return bundleDetails.size
    }

    fun updateDataSet(newList: List<ShopHomeProductBundleDetailUiModel>) {
        bundleDetails = newList

        // set first bundle as default selected
        if (bundleDetails.isNotEmpty()) {
            bundleDetails.firstOrNull()?.isSelected = true
            lastSelectedPosition = 0
        }
        notifyDataSetChanged()
    }

}

interface SingleBundleVariantSelectedListener {
    fun onSingleVariantSelected(selectedBundle: ShopHomeProductBundleDetailUiModel)
}