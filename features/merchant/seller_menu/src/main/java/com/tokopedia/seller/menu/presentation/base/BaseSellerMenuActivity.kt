package com.tokopedia.seller.menu.presentation.base

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

abstract class BaseSellerMenuActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackgroundColor()
    }

    private fun setBackgroundColor() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }
}