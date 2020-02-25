package com.tokopedia.tradein.view.viewcontrollers

import android.app.Application
import android.content.res.Resources
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

abstract class BaseTradeInViewModel(application: Application) : BaseViewModel(application) {
    private var contextInterface: ContextInterface? = null

    fun setContextInterface(contextInterface: ContextInterface) {
        this.contextInterface = contextInterface
    }

    fun getResource(): Resources? {
        return contextInterface?.contextFromActivity?.resources
    }
}