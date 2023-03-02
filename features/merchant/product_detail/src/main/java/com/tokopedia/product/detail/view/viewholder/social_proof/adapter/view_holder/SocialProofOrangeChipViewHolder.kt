package com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
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

    override fun bind(uiModel: SocialProofUiModel, trackData: ComponentTrackDataModel?) = with(binding) {
        renderUI(uiModel = uiModel)
        eventClick(uiModel = uiModel, trackData = trackData)
        setImpression(uiModel = uiModel)
    }

    private fun SocialProofOrangeChipItemBinding.renderUI(uiModel: SocialProofUiModel) {
        renderIcon(iconLink = uiModel.icon)
        renderTitle(title = uiModel.title)
        renderSubTitle(subTitle = uiModel.subtitle)
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

    private fun SocialProofOrangeChipItemBinding.setImpression(uiModel: SocialProofUiModel) {
        root.addOnImpressionListener(uiModel.impressHolder) {
            listener.onSocialProofItemImpression(socialProof = uiModel)
        }
    }

    private fun eventClick(uiModel: SocialProofUiModel, trackData: ComponentTrackDataModel?) {
        val appLink = uiModel.appLink
        val id = uiModel.identifier

        if (appLink.isNotBlank()) {
            binding.root.setOnClickDebounceListener {
                listener.goToApplink(appLink)
                listener.onSocialProofItemClickTracking(identifier = id, trackData = trackData)
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
