package com.tokopedia.feedback_form.drawonpicture

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 01/10/2020
 */
interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
}