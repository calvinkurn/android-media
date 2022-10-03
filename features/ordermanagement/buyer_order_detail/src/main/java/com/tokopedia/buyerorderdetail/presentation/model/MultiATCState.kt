package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.kotlin.extensions.view.ZERO
import java.io.Serializable

sealed class MultiATCState : Serializable {
    data class Success(
        val data: AtcMultiData
    ) : MultiATCState()

    data class Fail(
        val message: StringRes = StringRes(Int.ZERO),
        val throwable: Throwable? = null
    ) : MultiATCState()
}
