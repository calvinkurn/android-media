package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemType
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofChipViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofTextDividerViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofTextViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofTypeBaseViewHolder
import com.tokopedia.unifycomponents.toPx

class MiniSocialProofAdapter(
        private val listener: DynamicProductDetailListener
) : RecyclerView.Adapter<ProductMiniSocialProofTypeBaseViewHolder>() {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_CHIP = 1
        const val TYPE_TEXT_DIVIDER = 2
    }

    private var socialProof: MutableList<ProductMiniSocialProofItemDataModel> = mutableListOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMiniSocialProofTypeBaseViewHolder {
        return when (viewType) {
            TYPE_CHIP -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chip_social_proof_item, parent, false)
                ProductMiniSocialProofChipViewHolder(view, listener)
            }
            TYPE_TEXT_DIVIDER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.social_proof_stock_item, parent, false)
                ProductMiniSocialProofTextDividerViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.social_proof_item, parent, false)
                ProductMiniSocialProofTextViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return socialProof.size
    }

    override fun onBindViewHolder(holder: ProductMiniSocialProofTypeBaseViewHolder, position: Int) {
        holder.itemView.apply {
            if (position == socialProof.lastIndex && getItemViewType(position) == TYPE_CHIP) {
                setPadding(0, 0, 8.toPx(), 0)
            } else {
                setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
            }
        }
        holder.bind(socialProof[position], componentTrackDataModel)
    }


    override fun getItemViewType(position: Int): Int {
        return when (socialProof[position].type) {
            ProductMiniSocialProofItemType.ProductMiniSocialProofChip -> TYPE_CHIP
            ProductMiniSocialProofItemType.ProductMiniSocialProofTextDivider -> TYPE_TEXT_DIVIDER
            else -> TYPE_TEXT
        }
    }

    fun setData(socialProofData: MutableList<ProductMiniSocialProofItemDataModel>, tracker: ComponentTrackDataModel) {
        socialProof = socialProofData
        componentTrackDataModel = tracker
        notifyDataSetChanged()
    }

}