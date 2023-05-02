package com.tokopedia.wishlist.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

fun disableWishlistCoachmark(context: Context) {
    CoachMarkPreference.setShown(context, "coachmark-wishlist-onboarding", true)
    CoachMarkPreference.setShown(context, "coachmark-wishlist-sharing", true)
    CoachMarkPreference.setShown(context, "coachmark-wishlist", true)
}

internal inline fun <reified T : RecyclerView.Adapter<*>> RecyclerView?.adapter(): T {
    val castedAdapter = this?.adapter as? T
    checkNotNull(castedAdapter) {
        "Adapter is not instance of ${T::class.java.simpleName}"
    }
    return castedAdapter
}

private const val DISABLE_RECOM_USING_GQL_FED = "false"

fun setupRemoteConfig() {
    val remoteConfig = FirebaseRemoteConfigImpl(
        InstrumentationRegistry.getInstrumentation().context
    )
    remoteConfig.setString(RemoteConfigKey.RECOM_USE_GQL_FED_QUERY, DISABLE_RECOM_USING_GQL_FED)
}
