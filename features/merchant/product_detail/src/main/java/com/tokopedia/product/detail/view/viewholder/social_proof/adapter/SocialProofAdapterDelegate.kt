package com.tokopedia.product.detail.view.viewholder.social_proof.adapter

import android.view.ViewGroup
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofChipViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofLoadingViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofOrangeChipViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTextViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTypeViewHolder

class SocialProofAdapterDelegate(
    private val listener: DynamicProductDetailListener
) {

    fun getItemViewType(type: String): Int = type.hashCode()

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialProofTypeViewHolder = when (viewType) {
        SocialProofData.CHIP.hashCode() -> {
            SocialProofChipViewHolder.create(parent = parent, listener = listener)
        }
        SocialProofData.ORANGE_CHIP.hashCode() -> {
            SocialProofOrangeChipViewHolder.create(parent = parent, listener = listener)
        }
        SocialProofData.TEXT.hashCode() -> {
            SocialProofTextViewHolder.create(parent = parent, listener = listener)
        }
        else -> {
            SocialProofLoadingViewHolder.create(parent = parent)
        }
    }
}
