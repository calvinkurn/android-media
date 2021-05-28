package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofTokoNowChipViewHolder
import com.tokopedia.unifycomponents.toPx

class MiniSocialProofTokoNowAdapter(
        private val listener: DynamicProductDetailListener
) : RecyclerView.Adapter<ProductMiniSocialProofTokoNowChipViewHolder>() {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_CHIP = 1
        const val TYPE_TEXT_DIVIDER = 2
    }

    private var socialProof: MutableList<ProductMiniSocialProofTokoNowItemDataModel> = mutableListOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMiniSocialProofTokoNowChipViewHolder {
        val view = when (viewType) {
            TYPE_CHIP -> {
                LayoutInflater.from(parent.context).inflate(R.layout.chip_social_proof_item, parent, false)
            }
            TYPE_TEXT_DIVIDER -> {
                LayoutInflater.from(parent.context).inflate(R.layout.social_proof_toko_now_item, parent, false)
            }
            else -> LayoutInflater.from(parent.context).inflate(R.layout.social_proof_item, parent, false)
        }

        return ProductMiniSocialProofTokoNowChipViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return socialProof.size
    }

    override fun onBindViewHolder(holder: ProductMiniSocialProofTokoNowChipViewHolder, position: Int) {
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
        return when(socialProof[position].type){
            is ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofChip -> TYPE_CHIP
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofTextWithDivider -> TYPE_TEXT_DIVIDER
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofSingleText -> TYPE_TEXT
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofText -> TYPE_TEXT
        }
    }

    fun setData(socialProofData: MutableList<ProductMiniSocialProofTokoNowItemDataModel>, tracker: ComponentTrackDataModel) {
        socialProof = socialProofData
        componentTrackDataModel = tracker
        notifyDataSetChanged()
    }

}