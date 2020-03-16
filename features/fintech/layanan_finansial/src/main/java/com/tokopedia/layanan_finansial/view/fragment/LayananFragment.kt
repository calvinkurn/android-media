package com.tokopedia.layanan_finansial.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.layanan_finansial.R

class LayananFragment : BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layanan_fragment,container,false)
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}