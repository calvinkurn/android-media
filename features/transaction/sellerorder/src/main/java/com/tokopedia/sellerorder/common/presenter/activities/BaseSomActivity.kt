package com.tokopedia.sellerorder.common.presenter.activities

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

abstract class BaseSomActivity: BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            statusBarColor = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ContextCompat.getColor(this@BaseSomActivity, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black)
            } else {
                ContextCompat.getColor(this@BaseSomActivity, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }
    }
}