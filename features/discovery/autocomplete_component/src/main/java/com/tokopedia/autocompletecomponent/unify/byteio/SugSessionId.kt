package com.tokopedia.autocompletecomponent.unify.byteio

import javax.inject.Inject

interface SugSessionId {

    fun generate(): String
}

class SugSessionIdImpl @Inject constructor(): SugSessionId {

    override fun generate(): String = System.currentTimeMillis().toString()
}
