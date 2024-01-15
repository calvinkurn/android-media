package com.tokopedia.shareexperience.data.mapper

import com.tokopedia.shareexperience.data.dto.ShareExBottomSheetResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateRegistrationWidgetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorArgResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorResponseDto
import com.tokopedia.shareexperience.domain.ShareExConstants
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExFeatureEnum
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties
import javax.inject.Inject

class ShareExPropertyMapper @Inject constructor(
    private val channelMapper: ShareExChannelMapper
) {
    fun map(dto: ShareExBottomSheetResponseDto): ShareExBottomSheetModel {
        val listShareProperty = arrayListOf<ShareExPropertyModel>()
        val listChip = arrayListOf<String>()
        dto.properties.forEach {
            val affiliate = ShareExAffiliateModel(
                registration = it.affiliateRegistrationWidget.mapToDomainModel(),
                eligibility = it.affiliateEligibility.mapToDomainModel()
            )
            val property = ShareExPropertyModel(
                title = it.shareBody.title,
                listImage = it.shareBody.thumbnailUrls,
                affiliate = affiliate,
                linkProperties = ShareExLinkProperties(
                    message = it.generateLinkProperties.message,
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
                    androidMinVersion = it.generateLinkProperties.androidMinVersion,
                    iosMinVersion = it.generateLinkProperties.iosMinVersion,
                    feature = ShareExFeatureEnum.SHARE,
                    campaign = "" // will be changed later when user clicked channel
                ),
                imageGenerator = it.imageGeneratorPayload.mapToDomainModel()
            )
            listChip.add(it.chipTitle)
            listShareProperty.add(property)
        }
        val body = ShareExBottomSheetPageModel(
            listChip = listChip,
            listShareProperty = listShareProperty,
            socialChannel = channelMapper.generateSocialMediaChannel(),
            commonChannel = channelMapper.generateDefaultChannel()
        )
        return ShareExBottomSheetModel(
            title = dto.title,
            subtitle = dto.subtitle,
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

    fun mapDefault(
        defaultUrl: String,
        defaultImageUrl: String
    ): ShareExBottomSheetModel {
        val listShareProperty = arrayListOf<ShareExPropertyModel>()
        val property = ShareExPropertyModel(
            title = defaultUrl,
            listImage = listOf(defaultImageUrl)
        )
        listShareProperty.add(property)
        val body = ShareExBottomSheetPageModel(
            listShareProperty = listShareProperty,
            socialChannel = channelMapper.generateSocialMediaChannel(),
            commonChannel = channelMapper.generateDefaultChannel()
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
}
