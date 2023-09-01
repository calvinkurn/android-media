package com.tokopedia.minicart.bmgm.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 21/08/23.
 */

class BmgmMiniCartDetailViewModel @Inject constructor(
    setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get())