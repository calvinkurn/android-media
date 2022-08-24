package com.tokopedia.tokopedianow.recipehome.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import javax.inject.Inject

class TokoNowRecipeHomeViewModel @Inject constructor(
    getRecipeListUseCase: GetRecipeListUseCase,
    addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowRecipeListViewModel(getRecipeListUseCase, addressData, dispatchers)