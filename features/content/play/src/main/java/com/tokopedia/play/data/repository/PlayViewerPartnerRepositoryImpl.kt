package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 05/07/21
 */
class PlayViewerPartnerRepositoryImpl @Inject constructor(
    private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
    private val postFollowPartnerUseCase: PostFollowPartnerUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getFollowingKOLUseCase: GetFollowingKOLUseCase,
    private val postFollowKolUseCase: PostFollowKolUseCase,
    private val postUnfollowKolUseCase: PostUnfollowKolUseCase,
    private val mapper: PlayUiModelMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayViewerPartnerRepository {

    override suspend fun getIsFollowingPartner(partnerId: Long): Boolean = withContext(dispatchers.io) {
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

    /**
     * Get KOL encUserId to do post follow / un
     * */
    override suspend fun getProfileHeader(kolId: String): String = withContext(dispatchers.io){
        val encUserId = getProfileInfoUseCase.apply {
            setRequestParams(createParam(kolId))
        }.executeOnBackground()
        return@withContext mapper.mapProfileHeader(encUserId.response)
    }

    override suspend fun getFollowingKOL(followedKol: String): Boolean = withContext(dispatchers.io){
        val status = getFollowingKOLUseCase.apply {
            setRequestParams(createParam(followedKol))
        }.executeOnBackground()
        return@withContext mapper.mapFollowingKol(status.response.followedKOLInfo)
    }

    override suspend fun postFollowKol(followedKol: String, followAction: PartnerFollowAction): Boolean = withContext(dispatchers.io){
        return@withContext if (followAction == PartnerFollowAction.Follow){
            postFollowKolUseCase.apply {
                setRequestParams(createParam(followedKol))
            }.executeOnBackground().followedKOLInfo.errorCode.isEmpty()
        } else{
            postUnfollowKolUseCase.apply {
                setRequestParams(createParam(followedKol))
            }.executeOnBackground().unFollowedKOLInfo.data.isSuccess
        }
    }
}