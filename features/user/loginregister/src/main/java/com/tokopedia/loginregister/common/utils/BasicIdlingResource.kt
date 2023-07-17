package com.tokopedia.loginregister.common.utils

interface BasicIdlingResource {
    fun increment()
    fun decrement()

    companion object {
        fun emptyInstance() = object : BasicIdlingResource {
            override fun increment() {
                // no-op
            }

            override fun decrement() {
                // no-op
            }
        }
    }
}
