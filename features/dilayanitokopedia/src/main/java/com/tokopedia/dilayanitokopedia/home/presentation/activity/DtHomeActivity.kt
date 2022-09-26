package com.tokopedia.dilayanitokopedia.home.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.dilayanitokopedia.common.base.activity.BaseDtActivity
import com.tokopedia.dilayanitokopedia.home.presentation.fragment.DtHomeFragment

class DtHomeActivity : BaseDtActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getFragment(): Fragment {
        return DtHomeFragment()
    }

}