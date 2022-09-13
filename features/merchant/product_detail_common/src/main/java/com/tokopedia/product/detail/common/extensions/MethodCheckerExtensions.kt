package com.tokopedia.product.detail.common.extensions

import android.content.Context
import android.text.Spanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * Created by yovi.putra on 13/09/22"
 * Project name: android-tokopedia-core
 **/

fun String?.fromHtml(): Spanned = MethodChecker.fromHtml(
    this.orEmpty().replace(
        regex = "(\r\n|\n)".toRegex(),
        replacement = "<br />"
    )
)

fun Context.getColorChecker(id: Int) = MethodChecker.getColor(this, id)