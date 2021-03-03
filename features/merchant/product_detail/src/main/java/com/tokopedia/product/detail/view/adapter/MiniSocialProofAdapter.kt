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

class MiniSocialProofAdapter(
        private val socialProof: MutableList<ProductMiniSocialProofItemDataModel>,
        private val listener: DynamicProductDetailListener,
        private val componentTrackDataModel: ComponentTrackDataModel
) : RecyclerView.Adapter<ProductMiniSocialProofChipViewHolder>() {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_CHIP = 1
    }

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
        holder.bind(socialProof[position], componentTrackDataModel)
    }


    override fun getItemViewType(position: Int): Int {
        if (socialProof[position].type == ProductMiniSocialProofItemType.ProductMiniSocialProofChip) return TYPE_CHIP
        return TYPE_TEXT
    }

}