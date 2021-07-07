package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.GetPartnerInfoUseCase
import com.tokopedia.play.domain.PostFollowPartnerUseCase
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfo
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 05/07/21
 */
class PlayViewerPartnerRepositoryImpl @Inject constructor(
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val postFollowPartnerUseCase: PostFollowPartnerUseCase,
        private val mapper: PlayUiModelMapper,
        private val dispatchers: CoroutineDispatchers,
) : PlayViewerPartnerRepository {

    override suspend fun getPartnerFollowInfo(partnerId: Long): PlayPartnerFollowInfo = withContext(dispatchers.io) {
        val partnerInfo = getPartnerInfoUseCase.apply {
            params = GetPartnerInfoUseCase.createParam(partnerId)
        }.executeOnBackground()
        return@withContext mapper.mapPartnerInfo(partnerInfo)
    }

    override suspend fun postFollowStatus(shopId: String, followAction: PartnerFollowAction): Boolean = withContext(dispatchers.io) {
        return@withContext postFollowPartnerUseCase.apply {
            params = PostFollowPartnerUseCase.createParam(shopId, followAction)
        }.executeOnBackground()
    }
}