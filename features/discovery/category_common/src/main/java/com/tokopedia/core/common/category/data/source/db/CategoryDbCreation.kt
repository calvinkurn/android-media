package com.tokopedia.core.common.category.data.source.db

import android.content.Context

object CategoryDbCreation {

    @JvmStatic
    fun getCategoryDao(context: Context) = CategoryDB.getInstance(context).getCategoryDao()
}