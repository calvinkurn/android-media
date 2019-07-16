package com.tokopedia.topupbills.telco.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topupbills.R
import kotlinx.android.synthetic.main.activity_digital_telco.*

open abstract class BaseTelcoActivity : BaseSimpleActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_telco
    }

    override fun updateTitle(title: String?) {
        title?.run {
            toolbar_title.text = this
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.elevation = 0f
    }
}