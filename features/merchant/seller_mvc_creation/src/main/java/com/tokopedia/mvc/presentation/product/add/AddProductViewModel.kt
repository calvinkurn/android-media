package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase
) : BaseViewModel(dispatchers.main) {

    fun getShopWarehouseLocations() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetShopWarehouseLocationUseCase.Param()
                val response = getShopWarehouseLocationUseCase.execute(param)
            },
            onError = { error ->

            }
        )

    }
}
