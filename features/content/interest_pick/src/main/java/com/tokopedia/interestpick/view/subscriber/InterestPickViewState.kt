package com.tokopedia.interestpick.view.subscriber

import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel

sealed class InterestPickViewState {
    object LoadingState : InterestPickViewState()

    object ProgressState : InterestPickViewState()

    data class GetInterestSuccess(val headerModel: String, val interestList: ArrayList<InterestPickDataViewModel>) : InterestPickViewState()

    object UpdateInterestSuccess : InterestPickViewState()

    data class Error(val onUpdate:Boolean, val error: String) : InterestPickViewState()
}