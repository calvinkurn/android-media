package com.tokopedia.content.common.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import okhttp3.internal.toHexString

/**
 * @author by astidhiyaa on 09/03/23
 */

fun Int.hexToString(
    ctx: Context
): String {
    val number = MethodChecker.getColor(ctx, this).toHexString()
    return "#$number"
}
