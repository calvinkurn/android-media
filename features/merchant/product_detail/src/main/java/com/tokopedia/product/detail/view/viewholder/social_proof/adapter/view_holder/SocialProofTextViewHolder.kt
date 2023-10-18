package com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.databinding.SocialProofTextItemBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

class SocialProofTextViewHolder(
    private val binding: SocialProofTextItemBinding,
    private val listener: DynamicProductDetailListener
) : SocialProofTypeViewHolder(binding.root) {

    override fun bind(uiModel: SocialProofUiModel, trackData: ComponentTrackDataModel?) = with(binding) {
        renderTitle(title = uiModel.title)
        eventClick(uiModel = uiModel, trackData = trackData)
    }

    private fun SocialProofTextItemBinding.renderTitle(title: String) {
        socialProofTitle.showIfWithBlock(title.isNotBlank()) {
            text = title
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
        ): SocialProofTextViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = SocialProofTextItemBinding.inflate(inflate, parent, false)
            return SocialProofTextViewHolder(binding = binding, listener = listener)
        }
    }
}
