package com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.databinding.SocialProofOrangeChipItemBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

class SocialProofOrangeChipViewHolder(
    private val binding: SocialProofOrangeChipItemBinding,
    private val listener: DynamicProductDetailListener
) : SocialProofTypeViewHolder(binding.root) {

    override fun bind(socialProof: SocialProofData, trackData: ComponentTrackDataModel?) = with(binding) {
        renderUI(socialProof = socialProof)
        eventClick(appLink = socialProof.appLink.appLink)
    }

    private fun SocialProofOrangeChipItemBinding.renderUI(socialProof: SocialProofData) {
        renderIcon(iconLink = socialProof.icon)
        renderTitle(title = socialProof.title)
        renderSubTitle(subTitle = socialProof.subtitle)
    }

    private fun SocialProofOrangeChipItemBinding.renderIcon(iconLink: String) {
        socialProofChipIcon.showIfWithBlock(iconLink.isNotBlank()) {
            loadImage(iconLink)
        }
    }

    private fun SocialProofOrangeChipItemBinding.renderTitle(title: String) {
        socialProofChipTitle.showIfWithBlock(title.isNotBlank()) {
            text = title
        }
    }

    private fun SocialProofOrangeChipItemBinding.renderSubTitle(subTitle: String) {
        socialProofChipSubtitle.showIfWithBlock(subTitle.isNotBlank()) {
            text = subTitle
        }
    }

    private fun eventClick(appLink: String) {
        if (appLink.isNotBlank()) {
            binding.root.setOnClickListener {
                listener.goToApplink(appLink)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: DynamicProductDetailListener
        ): SocialProofOrangeChipViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = SocialProofOrangeChipItemBinding.inflate(inflate, parent, false)
            return SocialProofOrangeChipViewHolder(binding = binding, listener = listener)
        }
    }
}
