package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleSinglePackageViewHolder
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel

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
        val currentBundle = bundleDetails[position]
        holder.bind(currentBundle)
        holder.itemView.apply {
            setOnClickListener {
                // deselect last selected bundle
                bundleDetails[lastSelectedPosition].isSelected = false
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
            bundleDetails.first().isSelected = true
        }
        notifyDataSetChanged()
    }

}

interface SingleBundleVariantSelectedListener {
    fun onSingleVariantSelected(selectedBundle: ShopHomeProductBundleDetailUiModel)
}