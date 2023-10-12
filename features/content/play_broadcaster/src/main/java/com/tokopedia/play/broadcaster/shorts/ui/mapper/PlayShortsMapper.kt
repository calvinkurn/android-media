package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.shorts.domain.model.BroadcasterCheckAffiliateResponseModel
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateResponseModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.BroadcasterCheckAffiliateResponseUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.OnboardAffiliateUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsMapper {

    fun mapAuthorList(response: GetCheckWhitelistResponse): List<ContentAccountUiModel>

    fun mapShortsConfig(response: GetBroadcasterAuthorConfigResponse): PlayShortsConfigUiModel

    fun mapTagRecommendation(response: GetRecommendedChannelTagsResponse): PlayTagUiModel

    fun mapBroadcasterCheckAffiliate(response: BroadcasterCheckAffiliateResponseModel): BroadcasterCheckAffiliateResponseUiModel

    fun mapOnboardAffiliate(response: OnboardAffiliateResponseModel): OnboardAffiliateUiModel
}
