package com.tokopedia.sellerfeedback.presentation.activity

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.seller.active.common.features.sellerfeedback.SuccessToasterHelper
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment.Companion.EXTRA_URI_IMAGE

class SellerFeedbackActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val colorId = ContextCompat.getColor(
            this@SellerFeedbackActivity,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        )

        setupToolbar(colorId)
        setupStatusBar(colorId)
    }

    override fun getNewFragment(): Fragment {
        val uri = intent.getParcelableExtra<Uri>(EXTRA_URI_IMAGE)
        val shouldShowSetting = intent.getBooleanExtra(SuccessToasterHelper.SHOW_SETTING_BOTTOM_SHEET, false)
        val activityName = intent.getStringExtra(SellerFeedbackFragment.EXTRA_ACTIVITY_NAME).orEmpty()
        return SellerFeedbackFragment.createInstance(uri, shouldShowSetting, activityName)
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
}