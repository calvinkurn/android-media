package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.mps.analytics.MPSTracking
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot

const val MPSSuccessJSON = "mps/mps.json"
const val MPSLoadMoreSuccessJSON = "mps/mps-load-more.json"

abstract class MultiProductSearchTestFixtures {

    protected val mpsFirstPageUseCase = mockk<UseCase<MPSModel>>(relaxed = true)
    protected val mpsLoadMoreUseCase = mockk<UseCase<MPSModel>>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val mpsLocalCache = mockk<MpsLocalCache>(relaxed = true)
    protected val mpsTracking = mockk<MPSTracking>(relaxed = true)

    protected val requestParamsSlot = slot<RequestParams>()
    protected val requestParams by lazy { requestParamsSlot.captured }
    protected val requestParamParameters
        get() = requestParams.parameters as Map<String, String>

    fun mpsViewModel(state: MPSState = MPSState()): MPSViewModel = MPSViewModel(
        mpsState = state,
        mpsFirstPageUseCase = mpsFirstPageUseCase,
        mpsLoadMoreUseCase = mpsLoadMoreUseCase,
        addToCartUseCase = addToCartUseCase,
        chooseAddressWrapper = chooseAddressWrapper,
        getDynamicFilterUseCase = getDynamicFilterUseCase,
        mpsLocalCache = mpsLocalCache,
        userSession = userSession,
        mpsTracking = mpsTracking,
    )

    val MPSViewModel.stateValue: MPSState
        get() = this.stateFlow.value

    val MPSViewModel.visitableList: List<Visitable<*>>
        get() = this.stateValue.visitableList

    protected fun `Given MPS Use Case success`(
        mpsModel: MPSModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        mpsFirstPageUseCase.stubExecute(requestParamsSlot) returns mpsModel
    }

    protected fun `Given MPS load more use case success`(
        mpsModel: MPSModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        mpsLoadMoreUseCase.stubExecute(requestParamsSlot) returns mpsModel
    }
}
