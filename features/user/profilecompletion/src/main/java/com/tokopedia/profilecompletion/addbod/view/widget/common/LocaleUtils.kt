package com.tokopedia.profilecompletion.addbod.view.widget.common

import android.content.Context
import android.os.Build
import java.util.*

/**
 * Created by Ade Fulki on 2019-07-22.
 * ade.hadian@tokopedia.com
 */

object LocaleUtils{
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }

    fun getIDLocale(): Locale = Locale("in", "ID")
}