package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass

/**
 * Created by fwidjaja on 2019-05-08.
 */
class AddNewAddressFragment: BaseDaggerFragment() {
    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun newInstance(locationPass: LocationPass): Fragment {
        val fragment = AddNewAddressFragment()
        val args = Bundle()
        args.putParcelable(ARGUMENT_GEOLOCATION_DATA, locationPass)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_google_map, container, false)
    }
}