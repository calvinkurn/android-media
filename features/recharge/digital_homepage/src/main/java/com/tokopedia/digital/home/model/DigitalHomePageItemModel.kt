package com.tokopedia.digital.home.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

abstract class DigitalHomePageItemModel(var isLoaded: Boolean = false,
                                        var isSuccess: Boolean = false,
                                        var isLoadFromCloud : Boolean = true): Visitable<DigitalHomePageAdapterFactory> {

    // Old homepage has only one section instance per type so each sections' ID is set as a constant
    abstract fun visitableId(): Int
    abstract fun equalsWith(b: Any?): Boolean

    fun setResult(isSuccess: Boolean) {
        this.isLoaded = true
        this.isSuccess = isSuccess
    }

    fun setFail() {
        this.setResult(false)
    }
}
