package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.domain.mapper.UserShopInfoMapper
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserShopInfoUseCase @Inject constructor(
    private val dateShopCreatedUseCase: DateShopCreatedUseCase,
    private val shopInfoByIdUseCase: ShopInfoByIdUseCase,
    private val pmProPeriodTypeUseCase: PMProPeriodTypeUseCase,
    private val goldGetPMOSStatusUseCase: GoldGetPMOSStatusUseCase,
    private val userShopInfoMapper: UserShopInfoMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
): UseCase<UserShopInfoWrapper>() {

    override suspend fun executeOnBackground(): UserShopInfoWrapper {
        return withContext(dispatchers.io) {
            val shopId = userSession.shopId.toLongOrZero()
            val dateShopCreatedDeferred = async { dateShopCreatedUseCase.execute() }
            val shopInfoByIdDeferred = async { shopInfoByIdUseCase.execute(shopId) }
            val pMProPeriodTypeDeferred = async { pmProPeriodTypeUseCase.execute(shopId) }
            val goldGetPMOSStatusDeferred = async { goldGetPMOSStatusUseCase.execute(shopId) }

            userShopInfoMapper.mapToUserShopInfoUiModel(
                dateShopCreatedDeferred.await(),
                shopInfoByIdDeferred.await(),
                pMProPeriodTypeDeferred.await(),
                goldGetPMOSStatusDeferred.await()
            )
        }
    }
}