package com.tokopedia.tokofood.home.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodMerchantListUseCase
import javax.inject.Inject

class TokoFoodCategoryViewModel @Inject constructor(
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

}