package com.tokopedia.product_ar.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ProductArActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = ProductArFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data

        uri?.let {
            Log.e("pidnya", it.lastPathSegment.toString())
        }
    }
}