package com.tokopedia.sellerfeedback.presentation.activity

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment

class SellerFeedbackActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val colorId = ContextCompat.getColor(this@SellerFeedbackActivity,
            com.tokopedia.unifyprinciples.R.color.Unify_N400)

        setupToolbar(colorId)
        setupStatusBar(colorId)
    }

    private fun setupToolbar(@ColorInt colorId: Int) {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(colorId))
            elevation = 0f
        }
    }

    private fun setupStatusBar(@ColorInt colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(colorId)
        }
    }

    override fun getNewFragment(): Fragment = SellerFeedbackFragment()
}