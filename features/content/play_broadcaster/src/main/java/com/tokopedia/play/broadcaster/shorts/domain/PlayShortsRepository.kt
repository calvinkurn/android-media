package com.tokopedia.play.broadcaster.shorts.domain

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateRequestModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.BroadcasterCheckAffiliateResponseUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.OnboardAffiliateUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsRepository {

    suspend fun getAccountList(): List<ContentAccountUiModel>

    suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String,
    ): PlayShortsConfigUiModel

    suspend fun createShorts(
        authorId: String,
        authorType: String,
    ): String

    suspend fun uploadTitle(
        title: String,
        shortsId: String,
        authorId: String,
    )

    suspend fun getTagRecommendation(
        creationId: String,
    ): Set<PlayTagUiModel>

    suspend fun saveTag(
        shortsId: String,
        tags: Set<String>
    ): Boolean

    suspend fun getBroadcasterCheckAffiliate(): BroadcasterCheckAffiliateResponseUiModel

    suspend fun submitOnboardAffiliateTnc(request: OnboardAffiliateRequestModel): OnboardAffiliateUiModel

}
