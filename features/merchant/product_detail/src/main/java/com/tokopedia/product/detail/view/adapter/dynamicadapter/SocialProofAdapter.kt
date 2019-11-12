package com.tokopedia.product.detail.view.adapter.dynamicadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.viewholder.BaseSocialProofViewHolder
import com.tokopedia.product.detail.view.viewholder.ItemAttributeViewHolder
import com.tokopedia.product.detail.view.viewholder.ItemRatingTalkCourierViewHolder

class SocialProofAdapter : RecyclerView.Adapter<BaseSocialProofViewHolder<*>>() {

    companion object {
        val LAYOUT_ATTRIBUTE = R.layout.partial_product_detail_visibility
        val LAYOUT_RATING_TALK_COURIER = R.layout.partial_product_rating_talk_courier
    }

    var socialProofData: ProductSocialProofDataModel = ProductSocialProofDataModel()

    fun updateSocialProofData(socialProofData: ProductSocialProofDataModel) {
        this.socialProofData = socialProofData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSocialProofViewHolder<*> {
        return when (viewType) {
            LAYOUT_RATING_TALK_COURIER -> {
                ItemRatingTalkCourierViewHolder(LayoutInflater.from(
                        parent.context).inflate(viewType, parent, false
                ))
            }
            LAYOUT_ATTRIBUTE -> {
                ItemAttributeViewHolder(LayoutInflater.from(
                        parent.context).inflate(viewType, parent, false
                ))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = socialProofData.dataLayout.size

    override fun onBindViewHolder(holder: BaseSocialProofViewHolder<*>, position: Int) {
        when (holder) {
            is ItemRatingTalkCourierViewHolder -> holder.bind(socialProofData, position)
            is ItemAttributeViewHolder -> holder.bind(socialProofData, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (socialProofData.dataLayout[position].row) {
            "top" -> {
                LAYOUT_RATING_TALK_COURIER
            }
            "bottom" -> {
                LAYOUT_ATTRIBUTE
            }
            else -> return super.getItemViewType(position)
        }
    }
}