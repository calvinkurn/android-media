package com.tokopedia.buyerorder.unifiedhistory.list.data.model

import android.graphics.drawable.Drawable

/**
 * Created by fwidjaja on 22/07/20.
 */
data class UohEmptyState (
        val drawableSrc: Drawable,
        val title: String = "",
        val desc: String = "",
        val isBtnShow: Boolean = false,
        val btnStr: String = "")