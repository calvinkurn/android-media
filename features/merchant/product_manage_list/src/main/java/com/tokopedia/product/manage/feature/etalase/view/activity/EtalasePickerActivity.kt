package com.tokopedia.product.manage.feature.etalase.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment

class EtalasePickerActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return EtalasePickerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)
    }
}