package com.tokopedia.promotionstarget

import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

class PromotionsTargetVM(@Named("Main")
                         val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {}