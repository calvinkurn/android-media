package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemType
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofChipViewHolder
import com.tokopedia.unifycomponents.toPx

class MiniSocialProofAdapter(
        private val listener: DynamicProductDetailListener
) : RecyclerView.Adapter<ProductMiniSocialProofChipViewHolder>() {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_CHIP = 1
    }

    private var socialProof: MutableList<ProductMiniSocialProofItemDataModel> = mutableListOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMiniSocialProofChipViewHolder {
        val view = when (viewType) {
            TYPE_CHIP -> {
                LayoutInflater.from(parent.context).inflate(R.layout.chip_social_proof_item, parent, false)
            }
            else -> LayoutInflater.from(parent.context).inflate(R.layout.social_proof_item, parent, false)
        }

        return ProductMiniSocialProofChipViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return socialProof.size
    }

    override fun onBindViewHolder(holder: ProductMiniSocialProofChipViewHolder, position: Int) {
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
        if (socialProof[position].type == ProductMiniSocialProofItemType.ProductMiniSocialProofChip) return TYPE_CHIP
        return TYPE_TEXT
    }

    fun setData(socialProofData: MutableList<ProductMiniSocialProofItemDataModel>, tracker: ComponentTrackDataModel) {
        socialProof = socialProofData
        componentTrackDataModel = tracker
        notifyDataSetChanged()
    }

}