package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent

class PinpointNewPageFragment: BaseDaggerFragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this?.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        /*see onMapReady*/
    }

}