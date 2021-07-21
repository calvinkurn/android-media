package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.unifycomponents.ChipsUnify

class ProductBundleMasterViewHolder(itemView: View, clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var context: Context? = null
    private var productBundleChipView: ChipsUnify? = null
    private var recommendationCoachMark: CoachMark2? = null
    private val coachMarkItems: ArrayList<CoachMark2Item> = ArrayList()

    enum class ProductBundleChipState {
        SELECTED,
        NORMAL,
    }

    init {
        this.context = itemView.context
        this.productBundleChipView = itemView.findViewById(R.id.cu_product_bundle_master)
        this.productBundleChipView?.setOnClickListener {
            val productBundleMasterObj = it.getTag(R.id.product_bundle_master_tag)
            productBundleMasterObj?.let { obj ->
                val productBundleMaster = obj as ProductBundleMaster
                clickListener.onProductBundleMasterItemClicked(adapterPosition, productBundleMaster)
            }
        }
        context?.let { context -> productBundleChipView?.let { chipView -> setupRecommendationCoachMark(context, chipView) } }
    }

    fun bindData(productBundleMaster: ProductBundleMaster, state: ProductBundleChipState) {
        productBundleChipView?.setTag(R.id.product_bundle_master_tag, productBundleMaster)
        val isRecommendation = productBundleMaster.isRecommendation
        if (isRecommendation) {
            recommendationCoachMark?.showCoachMark(coachMarkItems)
            context?.let { productBundleChipView?.chipImageResource = ContextCompat.getDrawable(it, R.drawable.ic_thumb_filled) }
        }
        productBundleChipView?.chip_text?.text = productBundleMaster.bundleName
        when (state) {
            ProductBundleChipState.NORMAL ->
                productBundleChipView?.chipType = ChipsUnify.TYPE_NORMAL
            ProductBundleChipState.SELECTED ->
                productBundleChipView?.chipType = ChipsUnify.TYPE_SELECTED
        }
    }

    private fun setupRecommendationCoachMark(context: Context, chipView: ChipsUnify) {
        this.recommendationCoachMark = CoachMark2(context)
        coachMarkItems.add(CoachMark2Item(
                anchorView = chipView.chip_image_icon,
                title = "Rekomendasi",
                description = "Paket termurah dibanding paket lainnya")
        )
    }
}