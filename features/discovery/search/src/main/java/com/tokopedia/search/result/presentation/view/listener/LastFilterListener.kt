package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.LastFilterDataView

interface LastFilterListener {

    fun onImpressedLastFilter(lastFilterDataView: LastFilterDataView)

    fun applyLastFilter(lastFilterDataView: LastFilterDataView)

    fun closeLastFilter(lastFilterDataView: LastFilterDataView)
}