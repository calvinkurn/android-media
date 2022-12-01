package com.tokopedia.mvc.presentation.creation.step1

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import javax.inject.Inject

class VoucherCreationStepOneViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase
) : BaseViewModel(dispatchers.main)
