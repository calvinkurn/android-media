package com.tokopedia.sellerhome.settings.view.uimodel.socialmedialinks

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class SocialMediaLinkUiModel(@DrawableRes private val iconDrawableRes: Int,
                             @StringRes private val titleRes: Int,
                             @StringRes private val descriptionRes: Int,
                             val ctaLink: String,
                             val fallbackUrl: String,
                             val eventAction: String) {

    fun getDrawable(context: Context?): Drawable? {
        return context?.let {
            try {
                ContextCompat.getDrawable(it, iconDrawableRes)
            } catch (ex: Exception) {
                null
            }
        }
    }

    fun getTitle(context: Context?): String = context?.getString(titleRes).orEmpty()

    fun getDescription(context: Context?): String = context?.getString(descriptionRes).orEmpty()

}