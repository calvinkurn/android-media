package com.tokopedia.seller.search.common.util

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HTTPS_APP_LINK_PREFIX
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.WWW_APP_LINK_PREFIX
import com.tokopedia.unifyprinciples.Typography
import java.util.*

internal fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
    return if (!TextUtils.isEmpty(searchTerm)) {
        displayName.lowercase(Locale.getDefault())
            .indexOf(searchTerm.lowercase(Locale.getDefault()))
    } else {
        -1
    }
}

internal fun Typography.bindTitleText(title: String, keyword: String) {
    val startIndex = indexOfSearchQuery(title, keyword)
    text = if (startIndex == -1) {
        title
    } else {
        val highlightedTitle = SpannableString(title)
        highlightedTitle.safeSetSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            0,
            startIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        highlightedTitle.safeSetSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            startIndex + keyword.length.orZero(),
            title.length.orZero(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        highlightedTitle
    }
}

internal fun SpannableString.safeSetSpan(what: Any, start: Int, end: Int, flags: Int) {
    try {
        setSpan(what, start, end, flags)
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    }
}

internal fun View.setRippleEffect() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = with(TypedValue()) {
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                this,
                true
            )
            ContextCompat.getDrawable(context, resourceId)
        }
    }
}

internal val String.addWWWPrefix: String
    get() {
        return try {
            this.replace(HTTPS_APP_LINK_PREFIX, WWW_APP_LINK_PREFIX)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            this
        }
    }
