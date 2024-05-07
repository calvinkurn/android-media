package com.tokopedia.libra

interface Libra {

    fun getVariant(owner: LibraOwner, experiment: String): String
    fun cleanUp()
}
