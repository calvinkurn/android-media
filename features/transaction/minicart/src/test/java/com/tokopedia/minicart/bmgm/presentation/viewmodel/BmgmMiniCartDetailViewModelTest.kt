package com.tokopedia.minicart.bmgm.presentation.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by @ilhamsuaib on 29/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BmgmMiniCartDetailViewModelTest :
    BaseCartCheckboxViewModelTest<BmgmMiniCartDetailViewModel>() {

    override fun createViewModel(): BmgmMiniCartDetailViewModel {
        return BmgmMiniCartDetailViewModel(
            { setCartListCheckboxStateUseCase },
            { coroutineTestRule.dispatchers }
        )
    }
}