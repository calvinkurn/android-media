package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase.Companion.FOREIGN_COUNTRY_MESSAGE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PinpointNewPageFragment: BaseDaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PinpointNewPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PinpointNewPageViewModel::class.java)
    }

    private var googleMap: GoogleMap? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0

    private var binding by autoClearedNullable<FragmentPinpointNewBinding> {
        it.mapView.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentPinpointNewBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMap(savedInstanceState)
        prepareLayout()
        setViewListener()

        viewModel.getDistrictData(currentLat, currentLong)

        initObserver()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
    }

    private fun initObserver() {
        viewModel.autofillDistrictData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.messageError.isEmpty()) onSuccessAutofill()
                    else {
                        val msg = it.data.messageError[0]
                        when {
                            msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                            msg.contains(LOCATION_NOT_FOUND_MESSAGE) -> showLocationNotFoundCTA()
                        }
                    }
                }

                is Fail -> it.throwable.printStackTrace()
            }
        })
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        binding?.mapView?.run {
            onCreate(savedInstanceState)
            getMapAsync(this@PinpointNewPageFragment)
        }
    }

    private fun prepareLayout() {
        //prepare bottomsheet etc

    }

    private fun setViewListener() {
        binding?.run {

            bottomsheetLocation.btnPrimary.setOnClickListener {
                //go-to ANA posotive
            }

            bottomsheetLocation.btnSecondary.setOnClickListener {
                //go-to ANA Negative
            }
        }
    }

    private fun moveMap(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun onSuccessAutofill() {

    }

    private fun showOutOfReachBottomSheet() {

    }

    private fun showLocationNotFoundCTA() {

    }

    companion object {
        private const val ZOOM_LEVEL = 16f

        const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"
    }
}