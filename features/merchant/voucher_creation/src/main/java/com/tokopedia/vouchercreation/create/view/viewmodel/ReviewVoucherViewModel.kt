package com.tokopedia.vouchercreation.create.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class ReviewVoucherViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {



}