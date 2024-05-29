package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.content.common.model.Creation
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.shorts.domain.model.BroadcasterCheckAffiliateResponseModel
import com.tokopedia.play.broadcaster.shorts.domain.model.CheckProductCustomVideoResponse
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateResponseModel
import com.tokopedia.play.broadcaster.shorts.domain.model.PlayShortsConfig
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.ProductVideoUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.BroadcasterCheckAffiliateResponseUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.OnboardAffiliateUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsUiMapper @Inject constructor(
    private val gson: Gson
) : PlayShortsMapper {

    override fun mapAuthorList(response: Creation): List<ContentAccountUiModel> {
        return response.authors.map {
            ContentAccountUiModel(
                id = it.id,
                name = it.name,
                iconUrl = it.image,
                type = it.type,
                hasUsername = it.hasUsername,
                hasAcceptTnc = it.hasAcceptTnC,
                enable = response.isActive,
            )
        }
    }

    override fun mapShortsConfig(response: GetBroadcasterAuthorConfigResponse): PlayShortsConfigUiModel {
        val config = if (response.authorConfig.config.isEmpty()) {
            PlayShortsConfig()
        } else {
            gson.fromJson<PlayShortsConfig>(
                response.authorConfig.config,
                object : TypeToken<PlayShortsConfig>() {}.type
            )
        }

        return PlayShortsConfigUiModel(
            shortsId = if (config.draftShortsId == 0) "" else config.draftShortsId.toString(),
            shortsAllowed = response.authorConfig.shortVideoAllowed,
            isBanned = response.authorConfig.isBanned,
            hasContent = response.authorConfig.hasContent,
            tncList = response.authorConfig.tnc.map {
                TermsAndConditionUiModel(desc = it.description)
            },
            maxTitleCharacter = config.maxTitleCharacter,
            maxTaggedProduct = config.maxTaggedProduct,
            shortsVideoSourceId = config.shortVideoSourceId,
            eligibleInterspersing = config.eligibleInterspersing,
            productCountForInterspersing = config.productCountForInterspersing,
        )
    }

    override fun mapTagRecommendation(response: GetRecommendedChannelTagsResponse): PlayTagUiModel {
        return PlayTagUiModel(
            tags = response.recommendedTags.tags.map {
                PlayTagItem(
                    tag = it,
                    isChosen = false,
                    isActive = response.recommendedTags.maxTags != 0,
                )
            }.toSet(),
            minTags = response.recommendedTags.minTags,
            maxTags = response.recommendedTags.maxTags,
        )
    }

    override fun mapBroadcasterCheckAffiliate(response: BroadcasterCheckAffiliateResponseModel): BroadcasterCheckAffiliateResponseUiModel {
        val responseData = response.data
        return BroadcasterCheckAffiliateResponseUiModel(
            affiliateName = responseData.affiliateName,
            isAffiliate = responseData.isAffiliate,
        )
    }

    override fun mapOnboardAffiliate(response: OnboardAffiliateResponseModel): OnboardAffiliateUiModel {
        val responseData = response.data
        return OnboardAffiliateUiModel(
            errorMessage = if (responseData.status == 0) responseData.error.message
            else ""
        )
    }

    override fun mapProductVideo(response: CheckProductCustomVideoResponse): ProductVideoUiModel {
        return ProductVideoUiModel(
            hasVideo = response.data.hasVideo,
            videoUrl = response.data.videoUrl,
            coverUrl = response.data.coverUrl,
        )
    }
}
