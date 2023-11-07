package com.tokopedia.content.analytic.impression

/**
 * Created by kenny.hadisaputra on 06/11/23
 */
interface ImpressionManager<Key : Any?> {

    fun contains(key: Key): Boolean

    fun impress(key: Key, onImpress: () -> Unit)
}
