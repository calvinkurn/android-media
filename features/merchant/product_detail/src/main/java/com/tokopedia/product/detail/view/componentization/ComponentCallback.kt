package com.tokopedia.product.detail.view.componentization

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

interface ComponentCallback<E : ComponentEvent> {

    fun event(event: E)
}
