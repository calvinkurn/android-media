package com.tokopedia.layanan_finansial.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.viewModel.LayananFinansialViewModel
import javax.inject.Inject

class LayananFragment : BaseDaggerFragment() {

    @Inject
    lateinit var factory: ViewModelFactory
    val viewModel by lazy { ViewModelProviders.of(this,factory)[LayananFinansialViewModel::class.java] }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layanan_fragment,container,false)
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {

    }
}