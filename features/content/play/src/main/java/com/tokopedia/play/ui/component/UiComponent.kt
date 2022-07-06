package com.tokopedia.play.ui.component

import com.tokopedia.play.util.CachedState

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
interface UiComponent<T> {

    fun render(state: CachedState<T>)
}