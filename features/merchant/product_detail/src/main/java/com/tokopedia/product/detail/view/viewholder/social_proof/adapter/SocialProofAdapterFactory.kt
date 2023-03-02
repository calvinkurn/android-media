package com.tokopedia.product.detail.view.viewholder.social_proof.adapter

import android.view.ViewGroup
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofChipViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofLoadingViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofOrangeChipViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTextViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTypeViewHolder

class SocialProofAdapterFactory(
    private val listener: DynamicProductDetailListener
) {

    fun getItemViewType(type: SocialProofUiModel.Type): Int = type.hashCode()

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialProofTypeViewHolder = when (viewType) {
        SocialProofUiModel.Type.Chip.hashCode() -> {
            SocialProofChipViewHolder.create(parent = parent, listener = listener)
        }
        SocialProofUiModel.Type.OrangeChip.hashCode() -> {
            SocialProofOrangeChipViewHolder.create(parent = parent, listener = listener)
        }
        SocialProofUiModel.Type.Text.hashCode() -> {
            SocialProofTextViewHolder.create(parent = parent, listener = listener)
        }
        else -> {
            SocialProofLoadingViewHolder.create(parent = parent)
        }
    }
}
