package com.tokopedia.home.beranda.data.balance

import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HomeHeaderUseCase @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
) {

    private var currentHeaderDataModel: HomeHeaderDataModel? = null
    private var previousHeaderDataModel: HomeHeaderDataModel? = null

    val flow: StateFlow<HomeHeaderDataModel>
        get() = _flow
    private val _flow: MutableStateFlow<HomeHeaderDataModel> = MutableStateFlow(
        HomeHeaderDataModel(
            headerDataModel = HeaderDataModel(
                isUserLogin = userSessionInterface.isLoggedIn
            )
        )
    )

    suspend fun updateBalanceWidget() {
        if (currentHeaderDataModel == null) {
            currentHeaderDataModel =
                homeBalanceWidgetUseCase.onGetBalanceWidgetData(
                    previousHeaderDataModel
                )
            previousHeaderDataModel = currentHeaderDataModel
            currentHeaderDataModel?.let {
                _flow.emit(it)
            }
        }
    }
}
