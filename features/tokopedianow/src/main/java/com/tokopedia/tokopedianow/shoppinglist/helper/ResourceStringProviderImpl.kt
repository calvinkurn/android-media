package com.tokopedia.tokopedianow.shoppinglist.helper

import android.content.Context

class ResourceStringProviderImpl(
    private val context: Context
): ResourceStringProvider {
    override fun getString(id: Int): String = context.getString(id)
}

