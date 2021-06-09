package com.tokopedia.home.account.presentation.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel
import com.tokopedia.home.account.presentation.widget.TagRoundedSpan
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object GeneralSettingMenuLabel {
    const val LABEL_NEW = " BARU"
    const val LABEL_BETA = " BETA"

    fun generateSpannableTitle(
            context: Context,
            item: SettingItemViewModel,
            labelType: String
    ): SpannableString {
        val title = item.title
        val boxColor = getBoxColor(context, title)
        val hide = if(labelType == LABEL_NEW) {
            hasBeenOneMonth(context, title)
        } else {
            false
        }
        return createLabel(context, boxColor, hide, title, labelType)
    }

    private fun getBoxColor(context: Context, title: String): Int {
        val notificationTitle: String = context.resources
                .getString(R.string.title_notification_setting)
        val mediaTitle: String = context.resources
                .getString(R.string.image_quality_setting_screen)
        val darkModeTitle: String = context.resources
                .getString(R.string.title_dark_mode)

        if (title == notificationTitle || title == darkModeTitle) {
            return R.color.Unify_R400
        } else if (title == mediaTitle) {
             return R.color.Unify_R500
        }

        return -1
    }

    private fun createLabel(
        context: Context,
        boxColor: Int,
        hide: Boolean,
        originalTitle: String,
        labelType: String
    ): SpannableString {
        var title = originalTitle

        if (boxColor > -1 && !hide) {
            val startPosition = title.length + 1
            val endPosition = startPosition + labelType.length - 1
            title += labelType
            val spannable = SpannableString(title)
            val newTag = TagRoundedSpan(
                    context,
                    4,
                    boxColor,
                    R.color.Unify_N0
            )
            spannable.setSpan(
                    RelativeSizeSpan(0.57f),
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                    newTag,
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannable
        } else {
            return SpannableString(title)
        }
    }

    private fun hasBeenOneMonth(context: Context, title: String): Boolean {
        val key = "$title.NewTag"
        val prefKey = this.javaClass.name + ".pref"
        val dayOffset = 30
        val now = Date().time
        val preferences: SharedPreferences = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        if (!preferences.contains(key)) {
            preferences.edit().putLong(key, now).apply()
            return false
        }
        val firstTimeSeenDate = preferences.getLong(key, -1)
        val duration = abs(firstTimeSeenDate - now)
        val dayPassed = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS)
        return dayPassed >= dayOffset
    }
}