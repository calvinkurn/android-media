package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetBuyerOrderDetailDataRequestState {
    val getP0DataRequestState: GetP0DataRequestState
    val getP1DataRequestState: GetP1DataRequestState

    data class Requesting(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState

    data class Complete(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState
    ) : GetBuyerOrderDetailDataRequestState {
        fun getThrowable(): Throwable? {
            return getThrowableFromGetP0DataUseCase() ?: getThrowableFromGetP1DataUseCase()
        }

        private fun getThrowableFromGetP0DataUseCase(): Throwable? {
            return getP0DataRequestState.let {
                if (it is GetP0DataRequestState.Complete) {
                    it.getThrowable()
                } else {
                    null
                }
            }
        }

        private fun getThrowableFromGetP1DataUseCase(): Throwable? {
            return getP1DataRequestState.let {
                if (it is GetP1DataRequestState.Complete) {
                    it.getThrowable()
                } else {
                    null
                }
            }
        }
    }
}

