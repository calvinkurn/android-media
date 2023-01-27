package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class GotoKycMainViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
//    private val state: SavedStateHandle
) : BaseViewModel(dispatcher.main)  {
}
