package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MvcChooseProductViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {
    val isUserLogin: Boolean
        get() = userSessionInterface.isLoggedIn

}