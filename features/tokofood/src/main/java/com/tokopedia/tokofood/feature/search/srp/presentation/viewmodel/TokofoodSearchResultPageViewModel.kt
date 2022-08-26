package com.tokopedia.tokofood.feature.search.srp.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.feature.search.srp.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.srp.domain.usecase.TokofoodSearchMerchantUseCase
import javax.inject.Inject

class TokofoodSearchResultPageViewModel @Inject constructor(
    private val tokofoodSearchMerchantUseCase: TokofoodSearchMerchantUseCase,
    private val tokofoodFilterSortUseCase: TokofoodFilterSortUseCase,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    

}