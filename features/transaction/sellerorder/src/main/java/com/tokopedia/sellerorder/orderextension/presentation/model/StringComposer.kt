package com.tokopedia.sellerorder.orderextension.presentation.model

import android.content.Context

data class StringComposer(
    private val composer: (Context) -> CharSequence
) {
    fun compose(context: Context?): CharSequence = context?.let { composer(it) } ?: ""
}
