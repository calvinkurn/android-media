package com.tokopedia.sellerorder.common.presenter.activities

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker

abstract class BaseSomActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.run {
            decorView.setBackgroundColor(
                MethodChecker.getColor(
                    this@BaseSomActivity,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }
}