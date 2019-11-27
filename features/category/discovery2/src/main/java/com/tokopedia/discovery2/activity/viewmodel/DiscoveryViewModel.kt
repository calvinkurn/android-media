package com.tokopedia.discovery2.activity.viewmodel

import android.app.Application
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class DiscoveryViewModel(application: Application) : BaseViewModel(application), CoroutineScope {
    override fun doOnCreate() {
        super.doOnCreate()
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}