package com.tokopedia.tokopedianow.common.helper

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class ResourceProviderImpl(
    private val context: Context
): ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
    override fun getString(resId: Int, arg: Int): String = context.getString(resId, arg)
    override fun getColor(resId: Int): Int = MethodChecker.getColor(context, resId)
}

