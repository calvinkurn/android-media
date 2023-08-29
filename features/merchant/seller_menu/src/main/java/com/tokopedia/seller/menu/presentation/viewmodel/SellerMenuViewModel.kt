package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuList
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SellerMenuViewModel @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getSellerMenuNotifications: GetSellerNotificationUseCase,
    private val getShopScoreLevelUseCase: GetShopScoreLevelUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: AdminPermissionMapper,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopAgeState = MutableStateFlow(0L)

    private val _isNewSeller = MutableStateFlow(false)

    private val _uiState = MutableStateFlow<SellerMenuUIState>(SellerMenuUIState.Idle)
    val uiState get() = _uiState.asStateFlow()

    fun onEvent(event: SellerMenuUIEvent) {
        viewModelScope.launch {
            when(event) {
                is SellerMenuUIEvent.GetInitialMenu -> {
                    getInitialMenu()
                }
            }
        }
    }

    private fun getInitialMenu() {
        val initialMenu = SellerMenuList.createInitialItems(userSession, mapper)
        _uiState.tryEmit(SellerMenuUIState.OnSuccessGetMenuList(initialMenu))
    }


}
