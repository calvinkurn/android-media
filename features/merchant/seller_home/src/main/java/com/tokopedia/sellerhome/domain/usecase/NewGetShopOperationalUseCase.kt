package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.domain.mapper.ShopOperationalHourMapper
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Use old use case but create new data model
class NewGetShopOperationalUseCase @Inject constructor(
    private val getShopOperationalHourUseCase: GetShopOperationalHourUseCase,
    private val getShopClosedInfoUseCase: GetShopClosedInfoUseCase,
    private val authorizeAccessUseCase: AuthorizeAccessUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
): UseCase<ShopOperationalData>() {

    override suspend fun executeOnBackground(): ShopOperationalData {
        return withContext(dispatchers.io) {
            val shopOperationalResponse = async { getShopOperationalHourUseCase.execute(userSession.shopId) }
            val shopClosedInfoResponse = async { getShopClosedInfoUseCase.execute(userSession.shopId.toIntOrZero()) }
            val shopSettingsAccess = async { getSettingsAccess() }

            ShopOperationalHourMapper.mapToShopOperationalData(
                shopOperationalResponse.await(),
                shopClosedInfoResponse.await(),
                shopSettingsAccess.await()
            )
        }
    }

    private suspend fun getSettingsAccess(): Boolean {
        return if(!userSession.isShopOwner) {
            val requestParams = AuthorizeAccessUseCase.createRequestParams(
                userSession.shopId.toLongOrZero(),
                AccessId.SHOP_SETTING_INFO
            )
            authorizeAccessUseCase.execute(requestParams)
        } else {
            true
        }
    }
}