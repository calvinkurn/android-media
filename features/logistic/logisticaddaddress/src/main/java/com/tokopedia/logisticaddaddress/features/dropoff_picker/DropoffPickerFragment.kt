package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.R

class DropoffPickerFragment: BaseDaggerFragment() {

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dropoff_picker, container, false)
    }

    companion object {
        fun newInstance(): Fragment = DropoffPickerFragment().apply {
            arguments = Bundle()
        }
    }
}