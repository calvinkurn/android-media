package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceRegisterAffiliateCardItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExAffiliateRegistrationListener
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExAffiliateRegistrationViewHolder(
    itemView: View,
    affiliateRegistrationListener: ShareExAffiliateRegistrationListener
): AbstractViewHolder<ShareExAffiliateRegistrationUiModel>(itemView) {

    private val binding: ShareexperienceRegisterAffiliateCardItemBinding? by viewBinding()
    private var element: ShareExAffiliateRegistrationUiModel? = null

    init {
        binding?.root?.setOnClickListener {
            affiliateRegistrationListener.onAffiliateRegistrationCardClicked(
                element?.applink ?: ""
            )
        }
    }

    override fun bind(element: ShareExAffiliateRegistrationUiModel) {
        this.element = element
        binding?.shareexIvRegisterAffiliateIcon?.loadImage(element.icon)
        binding?.shareexTvRegisterAffiliateTitle?.text = element.title
        binding?.shareexTvRegisterAffiliateDesc?.text = element.description
        binding?.shareexLabelRegisterAffiliate?.text = element.label
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_register_affiliate_card_item
    }
}
