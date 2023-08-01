package com.tokopedia.seller.menu.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SellerMenuViewModel @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getSellerMenuNotifications: GetSellerNotificationUseCase,
    private val getShopScoreLevelUseCase: GetShopScoreLevelUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopAgeState = MutableStateFlow(0L)

    private val _isNewSeller = MutableStateFlow(false)
}
