package com.tokopedia.tradein.viewmodel

import com.tokopedia.tradein.usecase.GetMoneyInAddressUseCase
import com.tokopedia.tradein.usecase.GetMoneyInCourierUseCase
import com.tokopedia.tradein.usecase.GetMoneyInScheduleOptionUseCase
import com.tokopedia.tradein.usecase.MoneyInCheckoutMutationUseCase
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import javax.inject.Inject

class MoneyInCheckoutViewModel @Inject constructor(
        getMoneyInCourierUseCase: GetMoneyInCourierUseCase,
        moneyInCheckoutMutationUseCase: MoneyInCheckoutMutationUseCase,
        getMoneyInScheduleOptionUseCase: GetMoneyInScheduleOptionUseCase,
        getMoneyInAddressUseCase: GetMoneyInAddressUseCase): BaseViewModel() {
}