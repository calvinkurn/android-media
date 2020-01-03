package com.tokopedia.basemvvm.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    fun doOnStart() {}

    open fun doOnCreate() {}

    open fun doOnPause() {}

    fun doOnResume() {}

    open fun doOnStop() {}

    open fun doOnDestroy() {}


}
