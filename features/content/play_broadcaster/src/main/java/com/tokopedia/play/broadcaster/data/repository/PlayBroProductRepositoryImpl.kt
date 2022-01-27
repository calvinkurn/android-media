package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroProductRepository
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductMapper
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val productMapper: PlayBroProductMapper,
    private val userSession: UserSessionInterface,
) : PlayBroProductRepository {

    override suspend fun getCampaignList(): List<CampaignUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val response = getCampaignListUseCase.apply {
            setRequestParams(GetCampaignListUseCase.createParams(shopId = userSession.shopId))
        }.executeOnBackground()

        return@withContext productMapper.mapCampaignList(response)
    }
}