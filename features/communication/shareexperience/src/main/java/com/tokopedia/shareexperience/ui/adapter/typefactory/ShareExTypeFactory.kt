package com.tokopedia.shareexperience.ui.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel

interface ShareExTypeFactory: AdapterTypeFactory {
    fun type(uiModel: ShareExSubtitleUiModel): Int
    fun type(uiModel: ShareExChipsUiModel): Int
    fun type(uiModel: ShareExImageCarouselUiModel): Int
    fun type(uiModel: ShareExLinkShareUiModel): Int
    fun type(uiModel: ShareExSeparatorUiModel): Int
    fun type(uiModel: ShareExAffiliateRegistrationUiModel): Int
    fun type(uiModel: ShareExSocialChannelUiModel): Int
    fun type(uiModel: ShareExCommonChannelUiModel): Int
    fun type(uiModel: ShareExErrorUiModel): Int
}
