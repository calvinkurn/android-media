package com.tokopedia.wishlist.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMarkPreference

fun disableCoachmarkWishlistOnboarding(context: Context) {
    CoachMarkPreference.setShown(context, "coachmark-wishlist-onboarding", true)
}

internal inline fun <reified T : RecyclerView.Adapter<*>> RecyclerView?.adapter(): T {
    val castedAdapter = this?.adapter as? T
    checkNotNull(castedAdapter) {
        "Adapter is not instance of ${T::class.java.simpleName}"
    }
    return castedAdapter
}
