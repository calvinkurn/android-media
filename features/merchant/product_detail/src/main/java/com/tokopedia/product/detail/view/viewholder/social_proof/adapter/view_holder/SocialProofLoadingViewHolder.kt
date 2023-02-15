package com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.databinding.SocialProofLoadingItemBinding

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/


class SocialProofLoadingViewHolder(
    private val binding: SocialProofLoadingItemBinding
) : SocialProofTypeViewHolder(binding.root) {

    override fun bind(socialProof: SocialProofData, trackData: ComponentTrackDataModel?){
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): SocialProofLoadingViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = SocialProofLoadingItemBinding.inflate(inflate, parent, false)
            return SocialProofLoadingViewHolder(binding = binding)
        }
    }
}
