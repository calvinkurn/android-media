package com.tokopedia.shareexperience.ui.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel

fun ShareExBottomSheetModel.map(position: Int = 0): List<Visitable<in ShareExTypeFactory>> {
    val result = arrayListOf<Visitable<in ShareExTypeFactory>>()

    // Subtitle UI
    val subtitleUiModel = ShareExSubtitleUiModel(this.subtitle)
    result.add(subtitleUiModel)

    // Chip UI
    val listChipUiModel = arrayListOf<ShareExChipUiModel>()
    this.body.listChip.forEach {
        val chipUiModel = ShareExChipUiModel(it)
        listChipUiModel.add(chipUiModel)
    }
    if (listChipUiModel.isNotEmpty()) { // Only add when chip is not empty
        val selectedChipUiModel = listChipUiModel[position].copy(isSelected = true)
        listChipUiModel[position] = selectedChipUiModel

        val chipsUiModel = ShareExChipsUiModel(listChipUiModel)
        result.add(chipsUiModel)
    }

    // Only add when property is not null
    this.body.listShareProperty.getOrNull(position)?.let { shareExPropertyModel ->
        // Image Carousel UI
        val listImageUiModel = shareExPropertyModel.listImage.mapIndexed { index, imageUrl ->
            ShareExImageUiModel(imageUrl = imageUrl, isSelected = index == 0)
        }
        if (listImageUiModel.isNotEmpty()) {
            val imageCarouselUiModel = ShareExImageCarouselUiModel(listImageUiModel)
            result.add(imageCarouselUiModel)
        }

        // Link Share Card UI
        // TODO: Ask BE if this copy can be from them
        val commissionText = if (shareExPropertyModel.affiliate.commission.isNotBlank()) {
            "<b>Komisi ${shareExPropertyModel.affiliate.commission}</b> / barang dijual"
        } else {
            ""
        }

        if (shareExPropertyModel.affiliate.eligibility != null) {
            val linkShareUiModel = ShareExLinkShareUiModel(
                shareExPropertyModel.title,
                commissionText,
                "tokopedia.link",
                listImageUiModel.firstOrNull()?.imageUrl.toString(),
                shareExPropertyModel.affiliate.label,
                shareExPropertyModel.affiliate.date
            )
            result.add(linkShareUiModel)
        }

        // Separator Ui
        val separator = ShareExSeparatorUiModel()
        result.add(separator)

        // Affliate Registration Ui
        if (shareExPropertyModel.affiliate.registration != null &&
            shareExPropertyModel.affiliate.registration.hasEnoughData()
        ) {
            val affiliateRegistrationUiModel = ShareExAffiliateRegistrationUiModel(
                shareExPropertyModel.affiliate.registration.icon,
                shareExPropertyModel.affiliate.registration.title,
                shareExPropertyModel.affiliate.registration.description,
                shareExPropertyModel.affiliate.registration.label,
                shareExPropertyModel.affiliate.registration.appLink
            )
            result.add(affiliateRegistrationUiModel)
        }
    }

    // Channel Ui
    val socialChannelUiModel = ShareExSocialChannelUiModel(this.body.socialChannel)
    if (socialChannelUiModel.socialChannel.listChannel.isNotEmpty()) {
        result.add(socialChannelUiModel)
    }
    val commonChannelUiModel = ShareExCommonChannelUiModel(this.body.commonChannel)
    if (commonChannelUiModel.commonChannel.listChannel.isNotEmpty()) {
        result.add(commonChannelUiModel)
    }

    return result
}

private fun ShareExAffiliateRegistrationModel.hasEnoughData(): Boolean {
    return this.icon.isNotBlank() &&
        this.title.isNotBlank() &&
        this.description.isNotBlank() &&
        this.appLink.isNotBlank()
}
