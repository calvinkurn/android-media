package com.tokopedia.shop.showcase.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: CoroutineDispatchers,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase
) : BaseViewModel(dispatcherProvider.main) {


}