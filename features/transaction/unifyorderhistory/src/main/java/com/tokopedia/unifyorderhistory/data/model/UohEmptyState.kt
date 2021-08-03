package com.tokopedia.unifyorderhistory.data.model

import android.graphics.drawable.Drawable

/**
 * Created by fwidjaja on 22/07/20.
 */
data class UohEmptyState (
        val imgUrl: String = "",
        val title: String = "",
        val desc: String = "",
        val isBtnShow: Boolean = false,
        val btnStr: String = "")