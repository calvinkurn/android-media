package com.tokopedia.pdp.fintech.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechDetailViewModel @Inject constructor(@CoroutineMainDispatcher dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {
}