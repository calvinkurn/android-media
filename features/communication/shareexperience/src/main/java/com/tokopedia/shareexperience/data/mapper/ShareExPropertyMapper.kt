package com.tokopedia.shareexperience.data.mapper

import com.tokopedia.shareexperience.data.dto.ShareExBottomSheetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorArgResponseDto
import com.tokopedia.shareexperience.data.util.ShareExDefaultValue.DEFAULT_TITLE
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBodyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import javax.inject.Inject

class ShareExPropertyMapper @Inject constructor(
    private val channelMapper: ShareExChannelMapper
) {
    fun map(dto: ShareExBottomSheetResponseDto): ShareExBottomSheetModel {
        val listShareProperty = arrayListOf<ShareExPropertyModel>()
        val listChip = arrayListOf<String>()
        dto.properties.forEach {
            val affiliate = ShareExAffiliateModel(
                registration = ShareExAffiliateRegistrationModel(
                    icon = it.affiliateRegistrationWidget.icon,
                    title = it.affiliateRegistrationWidget.title,
                    label = it.affiliateRegistrationWidget.label,
                    description = it.affiliateRegistrationWidget.description,
                    appLink = it.affiliateRegistrationWidget.link
                ),
                commission = it.affiliateEligibility?.commission ?: "",
                label = it.affiliateEligibility?.badge ?: "",
                expiredDate = it.affiliateEligibility?.expiredDate ?: ""
            )
            val imageGeneratorModel = ShareExImageGeneratorModel(
                sourceId = it.imageGeneratorPayload.sourceId,
                payload = it.imageGeneratorPayload.args.mapToPayload()
            )
            val property = ShareExPropertyModel(
                title = it.shareBody.title,
                listImage = it.shareBody.thumbnailUrls,
                affiliate = affiliate,
                imageGenerator = imageGeneratorModel
            )
            listChip.add(it.chipTitle)
            listShareProperty.add(property)
        }
        val body = ShareExBodyModel(
            listChip = listChip,
            listShareProperty = listShareProperty,
            socialChannel = channelMapper.generateSocialMediaChannel(),
            commonChannel = channelMapper.generateDefaultChannel()
        )
        return ShareExBottomSheetModel(
            title = dto.title,
            subtitle = dto.subtitle,
            body = body
        )
    }

    private fun List<ShareExImageGeneratorArgResponseDto>.mapToPayload(): Map<String, String> {
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
        val body = ShareExBodyModel(
            listShareProperty = listShareProperty,
            socialChannel = channelMapper.generateSocialMediaChannel(),
            commonChannel = channelMapper.generateDefaultChannel()
        )

        return ShareExBottomSheetModel(
            title = DEFAULT_TITLE,
            body = body
        )
    }
}
