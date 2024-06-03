package com.tokopedia.home.beranda.data.balance

import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HeaderInterface
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PullToRefreshDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.libra.LibraConst
import com.tokopedia.libra.LibraInstance
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.LibraState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HomeHeaderUseCase @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
    private val libraInstance: LibraInstance
) {

    private var currentHeaderDataModel: HomeHeaderDataModel? = null
    private var previousHeaderDataModel: HomeHeaderDataModel? = null

    val flow: StateFlow<HeaderInterface>
        get() = _flow
    private val _flow: MutableStateFlow<HeaderInterface> = MutableStateFlow(
        HomeHeaderDataModel(
            headerDataModel = HeaderDataModel(
                isUserLogin = userSessionInterface.isLoggedIn
            )
        )
    )

    suspend fun updateBalanceWidget(isRefresh: Boolean = false) {
        val state = libraInstance.variantAsState(LibraOwner.Home, LibraConst.HOME_REVAMP_3_TYPE)
        when(state) {
            is LibraState.Control -> {
                if (currentHeaderDataModel == null || isRefresh) {
                    currentHeaderDataModel =
                        homeBalanceWidgetUseCase.onGetBalanceWidgetData(
                            previousHeaderDataModel
                        )
                    previousHeaderDataModel = currentHeaderDataModel
                }
                currentHeaderDataModel?.let {
                    _flow.emit(it.copy())
                }
            }
            is LibraState.Variant -> {
                _flow.emit(PullToRefreshDataModel())
            }
        }
    }
}
