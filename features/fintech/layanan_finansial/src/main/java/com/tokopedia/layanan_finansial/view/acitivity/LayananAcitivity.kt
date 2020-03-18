package com.tokopedia.layanan_finansial.view.acitivity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.layanan_finansial.di.DaggerLayananComponent
import com.tokopedia.layanan_finansial.di.LayananComponent
import com.tokopedia.layanan_finansial.di.LayananModule
import com.tokopedia.layanan_finansial.view.fragment.LayananFragment

class LayananAcitivity : BaseSimpleActivity() , HasComponent<LayananComponent>{

    private val layananComponent by lazy { DaggerLayananComponent.builder().baseAppComponent((application as BaseAppComponent)).layananModule(LayananModule(this)).build() }
    override fun getNewFragment(): Fragment {
      return LayananFragment()
    }

    override fun getComponent(): LayananComponent = layananComponent
}