package com.tokopedia.product.detail.view.viewmodel.slicing

import kotlinx.coroutines.CoroutineScope

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/
abstract class ViewModelSlice {

    protected lateinit var scope: CoroutineScope

    operator fun invoke(scope: CoroutineScope) {
        this.scope = scope
        afterInvoke()
    }

    open fun afterInvoke() {}
}
