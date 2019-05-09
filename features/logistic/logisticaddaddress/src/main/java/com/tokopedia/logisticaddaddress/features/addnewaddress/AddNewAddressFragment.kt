package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.AddressModule
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import kotlinx.android.synthetic.main.fragment_add_new_address.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class AddNewAddressFragment: BaseDaggerFragment(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, AddNewAddressView {
    private var googleMap: GoogleMap? = null
    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private val monasLatLong: LatLng by lazy { LatLng(defaultLat, defaultLong) }

    @Inject
    lateinit var presenter: AddNewAddressPresenter

    override fun getScreenName(): String {
        return "AddNewAddressFragment"
    }

    override fun initInjector() {
        val appComponent = (activity!!.application as BaseMainApplication).baseAppComponent
        DaggerAddressComponent.builder()
                .baseAppComponent(appComponent)
                .addressModule(AddressModule())
                .build().inject(this)
    }

    fun newInstance(): Fragment {
        /*val fragment = AddNewAddressFragment()
        val args = Bundle()
        args.putParcelable(ARGUMENT_GEOLOCATION_DATA, locationPass)
        fragment.arguments = args*/
        return AddNewAddressFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_new_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapview.getMapAsync(this)
        presenter.connectGoogleApi(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.uiSettings?.isMapToolbarEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        MapsInitializer.initialize(activity!!)
        moveMap(monasLatLong)
    }

    override fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(15f)
                .bearing(0f)
                .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onConnected(bundle: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}