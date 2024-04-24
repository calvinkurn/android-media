package com.tokopedia.shareexperience.data.mapper

import com.tokopedia.shareexperience.data.dto.ShareExGenerateLinkMessagePropertiesResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExSharePropertiesResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.properties.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.properties.ShareExAffiliateRegistrationWidgetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorArgResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorResponseDto
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExFeatureEnum
import com.tokopedia.shareexperience.domain.model.ShareExMessagePlaceholderEnum
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkMessagePropertiesModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkPropertiesModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import timber.log.Timber
import javax.inject.Inject

class ShareExPropertyMapper @Inject constructor(
    private val channelMapper: ShareExChannelMapper
) {
    fun map(dto: ShareExSharePropertiesResponseDto): ShareExBottomSheetModel {
        val listShareProperty = arrayListOf<ShareExPropertyModel>()
        val listChip = arrayListOf<String>()
        dto.bottomSheet.properties.forEach {
            val affiliate = ShareExAffiliateModel(
                registration = it.affiliateRegistrationWidget.mapToDomainModel(),
                eligibility = it.affiliateEligibility.mapToDomainModel()
            )
            val property = ShareExPropertyModel(
                shareId = it.shareId,
                title = it.shareBody.title,
                listImage = it.shareBody.thumbnailUrls,
                affiliate = affiliate,
                linkProperties = ShareExLinkPropertiesModel(
                    messageObject = it.generateLinkProperties.messageObject.mapToMessageObject(),
                    ogTitle = it.generateLinkProperties.ogTitle,
                    ogDescription = it.generateLinkProperties.ogDescription,
                    ogType = it.generateLinkProperties.ogType,
                    ogImageUrl = it.generateLinkProperties.ogImageUrl,
                    ogVideo = it.generateLinkProperties.ogVideo,
                    originalUrl = it.generateLinkProperties.desktopUrl,
                    androidUrl = it.generateLinkProperties.androidUrl,
                    iosUrl = it.generateLinkProperties.iosUrl,
                    desktopUrl = it.generateLinkProperties.desktopUrl,
                    androidDeeplinkPath = it.generateLinkProperties.androidDeeplinkPath,
                    iosDeeplinkPath = it.generateLinkProperties.iosDeeplinkPath,
                    canonicalUrl = it.generateLinkProperties.canonicalUrl,
                    canonicalIdentifier = it.generateLinkProperties.canonicalIdentifier,
                    customMetaTags = it.generateLinkProperties.customMetaTags,
                    anMinVersion = it.generateLinkProperties.anMinVersion,
                    feature = ShareExFeatureEnum.SHARE,
                    campaign = "" // will be changed later when user clicked channel
                ),
                imageGenerator = it.imageGeneratorPayload.mapToDomainModel(),
                socialChannel = channelMapper.mapToSocialMediaChannel(it.channel),
                commonChannel = channelMapper.mapToCommonChannel(it.channel)
            )
            listChip.add(it.chipTitle)
            listShareProperty.add(property)
        }
        val body = ShareExBottomSheetPageModel(
            listChip = listChip,
            listShareProperty = listShareProperty
        )
        return ShareExBottomSheetModel(
            title = dto.bottomSheet.title,
            subtitle = dto.bottomSheet.subtitle,
            bottomSheetPage = body
        )
    }

    private fun List<ShareExPropertyImageGeneratorArgResponseDto>.toMap(): Map<String, String> {
        val result = mutableMapOf<String, String>()
        this.forEach {
            result[it.key] = it.value
        }
        return result
    }

    fun mapDefault(defaultImageUrl: String): ShareExBottomSheetModel {
        val body = ShareExBottomSheetPageModel(
            listShareProperty = listOf(
                ShareExPropertyModel(
                    socialChannel = channelMapper.generateSocialMediaChannel(defaultImageUrl.isBlank()),
                    commonChannel = channelMapper.generateDefaultChannel()
                )
            )
        )
        return ShareExBottomSheetModel(
            title = ShareExConstants.DefaultValue.DEFAULT_TITLE,
            bottomSheetPage = body
        )
    }

    private fun ShareExAffiliateEligibilityResponseDto?.mapToDomainModel(): ShareExAffiliateEligibilityModel {
        return ShareExAffiliateEligibilityModel(
            isEligible = this != null,
            message = this?.message ?: "",
            label = this?.badge ?: "",
            expiredDate = this?.expiredDate ?: ""
        )
    }

    private fun ShareExAffiliateRegistrationWidgetResponseDto?.mapToDomainModel(): ShareExAffiliateRegistrationModel? {
        return if (this == null) {
            null
        } else {
            ShareExAffiliateRegistrationModel(
                icon = this.icon,
                title = this.title,
                label = this.label,
                description = this.description,
                appLink = this.link
            )
        }
    }

    private fun ShareExPropertyImageGeneratorResponseDto?.mapToDomainModel(): ShareExImageGeneratorPropertyModel? {
        return if (this == null) {
            null
        } else {
            ShareExImageGeneratorPropertyModel(
                sourceId = this.sourceId,
                args = this.args.toMap()
            )
        }
    }

    private fun ShareExGenerateLinkMessagePropertiesResponseDto.mapToMessageObject(): ShareExLinkMessagePropertiesModel {
        val replacementMap = this.placeholders.mapNotNull(
            ShareExMessagePlaceholderEnum::fromValue
        ).associateWith {
            "" // set to empty first, then it will be replaced later when the value is ready
        }.toMutableMap()
        return ShareExLinkMessagePropertiesModel(
            message = this.message,
            replacementMap = replacementMap
        )
    }
}
