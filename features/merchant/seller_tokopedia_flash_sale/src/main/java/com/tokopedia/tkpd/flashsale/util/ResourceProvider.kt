package com.tokopedia.tkpd.flashsale.util

import android.content.Context
import androidx.annotation.StringRes
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.seller_tokopedia_flash_sale.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {

    private fun getString(@StringRes id: Int, vararg args: Any): String? {
        return try {
            context?.getString(id, *args).orEmpty()
        } catch (t: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(t)
            null
        }
    }

    fun getTickerDescriptionFormat(content: String, link: String, textLink: String): String {
        return if (link.isNotEmpty()) {
            getString(
                R.string.stfs_ticker_description_format,
                content,
                link,
                textLink
            ).orEmpty()
        } else {
            content
        }
    }
}
