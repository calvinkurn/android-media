package com.tokopedia.topads.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.fragment.NoCreditFragment
import com.tokopedia.topads.view.fragment.OnSuccessFragment

class NoCreditActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return NoCreditFragment.newInstance()
    }

}
