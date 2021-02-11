package com.tokopedia.feedback_form.drawonpicture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 01/10/2020
 */
class ProdDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Main
    override fun ui(): CoroutineDispatcher = Dispatchers.Default
}