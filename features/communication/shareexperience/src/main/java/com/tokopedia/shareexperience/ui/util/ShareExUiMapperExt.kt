package com.tokopedia.shareexperience.ui.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel

fun ShareExBottomSheetModel.getSelectedChipPosition(
    selectedIdChip: String
): Int {
    return this.bottomSheetPage.listChip.findIndexIgnoreCase(selectedIdChip).coerceAtLeast(0)
}

fun ShareExBottomSheetModel.map(
    position: Int,
    selectedImageUrl: String? = null
): List<Visitable<in ShareExTypeFactory>> {
    val result = arrayListOf<Visitable<in ShareExTypeFactory>>()

    // Subtitle UI
    if (this.subtitle.isNotBlank()) {
        val subtitleUiModel = ShareExSubtitleUiModel(this.subtitle)
        result.add(subtitleUiModel)
    }

    // Chip UI
    // List chip and List Share Property always have same size
    val listChipUiModel = arrayListOf<ShareExChipUiModel>()
    this.bottomSheetPage.listChip.forEach {
        val chipUiModel = ShareExChipUiModel(it)
        listChipUiModel.add(chipUiModel)
    }
    if (listChipUiModel.size > 1) { // Only add when chip size is more than 1
        val selectedChipUiModel = listChipUiModel[position].copy(isSelected = true)
        listChipUiModel[position] = selectedChipUiModel

        val chipsUiModel = ShareExChipsUiModel(listChipUiModel)
        result.add(chipsUiModel)
    }

    // Only add when property is not null
    this.bottomSheetPage.listShareProperty.getOrNull(position)?.let { shareExPropertyModel ->
        // Image Carousel UI
        val selectedImagePosition = shareExPropertyModel.listImage.indexOf(
            selectedImageUrl.orEmpty()
        ).coerceAtLeast(0)
        val listImageUiModel = shareExPropertyModel.listImage.mapIndexed { index, imageUrl ->
            ShareExImageUiModel(imageUrl = imageUrl, isSelected = index == selectedImagePosition)
        }
        if (listImageUiModel.size > 1) {
            val imageCarouselUiModel = ShareExImageCarouselUiModel(listImageUiModel)
            result.add(imageCarouselUiModel)
        }

        // Link Share Card UI
        val linkShareUiModel = ShareExLinkShareUiModel(
            shareExPropertyModel.title,
            shareExPropertyModel.affiliate.eligibility.message,
            "tokopedia.link",
            shareExPropertyModel.listImage.getOrNull(selectedImagePosition).orEmpty(),
            shareExPropertyModel.affiliate.eligibility.label,
            shareExPropertyModel.affiliate.eligibility.expiredDate
        )
        result.add(linkShareUiModel)

        // Separator Ui
        val separator = ShareExSeparatorUiModel()
        result.add(separator)

        // Affliate Registration Ui
        if (shareExPropertyModel.affiliate.registration != null &&
            shareExPropertyModel.affiliate.registration.hasEnoughData()
        ) {
            val affiliateRegistrationUiModel = ShareExAffiliateRegistrationUiModel(
                shareExPropertyModel.affiliate.registration
            )
            result.add(affiliateRegistrationUiModel)
        }

        // Channel Ui
        val socialChannelUiModel = ShareExSocialChannelUiModel(shareExPropertyModel.socialChannel)
        if (socialChannelUiModel.socialChannel.listChannel.isNotEmpty()) {
            result.add(socialChannelUiModel)
        }
        val commonChannelUiModel = ShareExCommonChannelUiModel(shareExPropertyModel.commonChannel)
        if (commonChannelUiModel.commonChannel.listChannel.isNotEmpty()) {
            result.add(commonChannelUiModel)
        }
    }
    return result
}

private fun ShareExAffiliateRegistrationModel.hasEnoughData(): Boolean {
    return this.icon.isNotBlank() &&
        this.title.isNotBlank() &&
        this.description.isNotBlank() &&
        this.appLink.isNotBlank()
}

fun ShareExBottomSheetModel.mapError(
    defaultUrl: String,
    throwable: Throwable
): List<Visitable<in ShareExTypeFactory>> {
    return if (defaultUrl.isBlank()) {
        listOf(ShareExErrorUiModel(throwable))
    } else {
        val result = arrayListOf<Visitable<in ShareExTypeFactory>>()
        // Only shows the channel for share, no share body
        // Channel Ui
        this.bottomSheetPage.listShareProperty.firstOrNull()?.let {
            val socialChannelUiModel = ShareExSocialChannelUiModel(it.socialChannel)
            if (socialChannelUiModel.socialChannel.listChannel.isNotEmpty()) {
                result.add(socialChannelUiModel)
            }
            val commonChannelUiModel = ShareExCommonChannelUiModel(it.commonChannel)
            if (commonChannelUiModel.commonChannel.listChannel.isNotEmpty()) {
                result.add(commonChannelUiModel)
            }
        }
        result
    }
}

fun ShareExBottomSheetModel.getImageGeneratorProperty(imageUrl: String): ShareExImageGeneratorPropertyModel? {
    return this.bottomSheetPage.listShareProperty
        .firstOrNull { it.listImage.findIndexIgnoreCase(imageUrl) >= 0 }
        ?.imageGenerator
}

fun ShareExBottomSheetModel.getImageGeneratorProperty(
    chipPosition: Int
): ShareExImageGeneratorPropertyModel? {
    return this.bottomSheetPage.listShareProperty.getOrNull(chipPosition)?.imageGenerator
}

fun ShareExBottomSheetModel.getSelectedImageUrl(chipPosition: Int, imagePosition: Int): String {
    return this.bottomSheetPage.listShareProperty.getOrNull(chipPosition)?.listImage?.getOrNull(imagePosition) ?: ""
}
