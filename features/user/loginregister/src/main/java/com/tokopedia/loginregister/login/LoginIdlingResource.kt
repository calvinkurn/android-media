package com.tokopedia.loginregister.login

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created by Yoris Prayogo on 07/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
object LoginIdlingResource {
    private const val RESOURCE_NAME = "login_idling_resource"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        idlingResource = CountingIdlingResource(RESOURCE_NAME)
        return idlingResource!!
    }
}