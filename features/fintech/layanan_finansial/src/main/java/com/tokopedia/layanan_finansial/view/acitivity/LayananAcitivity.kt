package com.tokopedia.layanan_finansial.view.acitivity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.layanan_finansial.view.fragment.LayananFragment

class LayananAcitivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
      return LayananFragment()
    }
}