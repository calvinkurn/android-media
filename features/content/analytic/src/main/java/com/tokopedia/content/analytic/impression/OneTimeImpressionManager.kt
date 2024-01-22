package com.tokopedia.content.analytic.impression

/**
 * Created by kenny.hadisaputra on 06/11/23
 */
class OneTimeImpressionManager<Key : Any?> : ImpressionManager<Key> {

    private val impressionSet = mutableSetOf<Key>()

    override fun contains(key: Key): Boolean {
        return impressionSet.contains(key)
    }

    override fun impress(key: Key, onImpress: () -> Unit) {
        if (!contains(key)) onImpress()
        impressionSet.add(key)
    }

    fun clear(key: Key) {
        impressionSet.remove(key)
    }

    fun reset() {
        impressionSet.clear()
    }
}
