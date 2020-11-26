package com.tokopedia.manageaddress.ui.shoplocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopLocationViewModel
@Inject constructor() : ViewModel(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

}