package com.tokopedia.sellerfeedback.presentation.view

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SellerFeedbackToolbar(private val activity: Activity) {

    private val toolbar by lazy { (activity as? AppCompatActivity)?.supportActionBar }
    private val window by lazy { (activity as? AppCompatActivity)?.window }
    private val context by lazy { (activity as? AppCompatActivity) }

    fun setupBackground(@ColorRes colorId: Int) {
        context?.let {
            val color = ContextCompat.getColor(it, colorId)
            toolbar?.setBackgroundDrawable(ColorDrawable(color))
            setStatusBarColor(color)
        }
    }

    private fun setStatusBarColor(@ColorInt colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = colorId
        }
    }
}