package com.tokopedia.shareexperience.ui.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExAffiliateRegistrationViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExErrorViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExLineSeparatorViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExLinkShareViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExSubtitleViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.channel.ShareExCommonChannelsViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.channel.ShareExSocialChannelsViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.chip.ShareExChipsViewHolder
import com.tokopedia.shareexperience.ui.adapter.viewholder.image.ShareExImageCarouselViewHolder
import com.tokopedia.shareexperience.ui.listener.ShareExAffiliateRegistrationListener
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.shareexperience.ui.listener.ShareExChipsListener
import com.tokopedia.shareexperience.ui.listener.ShareExErrorListener
import com.tokopedia.shareexperience.ui.listener.ShareExImageGeneratorListener
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel

class ShareExTypeFactoryImpl(
    private val chipsListener: ShareExChipsListener,
    private val imageGeneratorListener: ShareExImageGeneratorListener,
    private val affiliateRegistrationListener: ShareExAffiliateRegistrationListener,
    private val channelListener: ShareExChannelListener,
    private val errorListener: ShareExErrorListener
) : BaseAdapterTypeFactory(), ShareExTypeFactory {
    override fun type(uiModel: ShareExSubtitleUiModel): Int {
        return ShareExSubtitleViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExChipsUiModel): Int {
        return ShareExChipsViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExImageCarouselUiModel): Int {
        return ShareExImageCarouselViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExLinkShareUiModel): Int {
        return ShareExLinkShareViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExSeparatorUiModel): Int {
        return ShareExLineSeparatorViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExAffiliateRegistrationUiModel): Int {
        return ShareExAffiliateRegistrationViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExSocialChannelUiModel): Int {
        return ShareExSocialChannelsViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExCommonChannelUiModel): Int {
        return ShareExCommonChannelsViewHolder.LAYOUT
    }

    override fun type(uiModel: ShareExErrorUiModel): Int {
        return ShareExErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShareExSubtitleViewHolder.LAYOUT -> ShareExSubtitleViewHolder(parent)
            ShareExChipsViewHolder.LAYOUT -> ShareExChipsViewHolder(parent, chipsListener)
            ShareExImageCarouselViewHolder.LAYOUT -> ShareExImageCarouselViewHolder(parent, imageGeneratorListener)
            ShareExLinkShareViewHolder.LAYOUT -> ShareExLinkShareViewHolder(parent)
            ShareExLineSeparatorViewHolder.LAYOUT -> ShareExLineSeparatorViewHolder(parent)
            ShareExAffiliateRegistrationViewHolder.LAYOUT -> ShareExAffiliateRegistrationViewHolder(parent, affiliateRegistrationListener)
            ShareExSocialChannelsViewHolder.LAYOUT -> ShareExSocialChannelsViewHolder(parent, channelListener)
            ShareExCommonChannelsViewHolder.LAYOUT -> ShareExCommonChannelsViewHolder(parent, channelListener)
            ShareExErrorViewHolder.LAYOUT -> ShareExErrorViewHolder(parent, errorListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
