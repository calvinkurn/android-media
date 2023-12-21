package com.tokopedia.shareexperience.data.mapper

import com.tokopedia.shareexperience.data.dto.response.ShareExBottomSheetResponseDto
import com.tokopedia.shareexperience.data.dto.response.imagegenerator.ShareExImageGeneratorArgResponseDto
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBodyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import javax.inject.Inject

class ShareExPropertyMapper @Inject constructor(
    private val channelMapper: ShareExChannelMapper
) {
    fun map(dto: ShareExBottomSheetResponseDto): ShareExBottomSheetModel {
        val listShareProperty = arrayListOf<ShareExPropertyModel>()
        dto.properties.forEach {
            val affiliate = ShareExAffiliateModel(
                registration = ShareExAffiliateRegistrationModel(
                    icon = it.affiliateRegistrationWidget.icon,
                    title = it.affiliateRegistrationWidget.title,
                    label = it.affiliateRegistrationWidget.label,
                    description = it.affiliateRegistrationWidget.description,
                    appLink = it.affiliateRegistrationWidget.link
                ),
                eligibility = ShareExAffiliateEligibilityModel(
                    isEligible = it.affiliateEligibility.eligible,
                    commission = it.affiliateEligibility.commission
                )
            )
            val property = ShareExPropertyModel(
                title = it.shareBody.title,
                listImage = it.shareBody.thumbnailUrls,
                affiliate = affiliate
            )
            listShareProperty.add(property)
        }
        val body = ShareExBodyModel(
            listChip = dto.chips,
            listShareProperty = listShareProperty,
            socialChannel = channelMapper.generateSocialMediaChannel(),
            commonChannel = channelMapper.generateDefaultChannel()
        )
        val imageGeneratorModel = ShareExImageGeneratorModel(
            sourceId = dto.imageGeneratorPayload.sourceId,
            payload = dto.imageGeneratorPayload.args.mapToPayload()
        )
        return ShareExBottomSheetModel(
            title = dto.title,
            subtitle = dto.subtitle,
            body = body,
            imageGenerator = imageGeneratorModel
        )
    }

    private fun List<String>.mapToChip(): List<ShareExChipModel> {
        val result = arrayListOf<ShareExChipModel>()
        this.forEach {
            result.add(ShareExChipModel(it))
        }
        return result
    }

    private fun List<ShareExImageGeneratorArgResponseDto>.mapToPayload(): Map<String, String> {
        val result = mutableMapOf<String, String>()
        this.forEach {
            result[it.key] = it.value
        }
        return result
    }
}
