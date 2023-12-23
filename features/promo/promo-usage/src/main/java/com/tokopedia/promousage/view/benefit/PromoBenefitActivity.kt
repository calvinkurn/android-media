package com.tokopedia.promousage.view.benefit

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity

class PromoBenefitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PromoBenefitBottomSheet.newInstance(
            "1"
        ).show(supportFragmentManager, null)
    }
}
