package com.tokopedia.search.result.mps

import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot

const val MPSSuccessJSON = "mps/mps.json"

abstract class MultiProductSearchTestFixtures {

    protected val mpsUseCase: UseCase<MPSModel> = mockk(relaxed = true)
    protected val chooseAddressWrapper: ChooseAddressWrapper = mockk(relaxed = true)

    fun mpsViewModel(state: MPSState = MPSState()) = MPSViewModel(
        mpsState = state,
        mpsUseCase = mpsUseCase,
        chooseAddressWrapper = chooseAddressWrapper,
    )

    val MPSViewModel.stateValue: MPSState
        get() = this.stateFlow.value

    protected fun `Given MPS Use Case success`(
        mpsModel: MPSModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        mpsUseCase.stubExecute(requestParamsSlot) returns mpsModel
    }
}
