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
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageFragment
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
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
    private var currentPlaceId: String? = ""

    private var composite = CompositeSubscription()

    private var binding by autoClearedNullable<FragmentPinpointNewBinding> {
        it.mapViews.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentPinpointNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMap(savedInstanceState)
        prepareLayout()
//        setViewListener()

        currentPlaceId?.let { viewModel.getDistrictLocation(it) }

        initObserver()
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        this.googleMap?.uiSettings?.setAllGesturesEnabled(false)
        activity?.let { MapsInitializer.initialize(activity) }
        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

//        this.googleMap?.setOnCameraMoveStartedListener { _ -> showLoading() }
        this.googleMap?.let {
            rxPinPoint(it).subscribe(object : Subscriber<Boolean>() {
                override fun onNext(t: Boolean?) {
//                    getAutofill()
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                }
            }).toCompositeSubs(composite)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.mapViews?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding?.mapViews?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapViews?.onStop()
    }

    override fun onPause() {
        binding?.mapViews?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding?.mapViews?.onDestroy()
        composite.unsubscribe()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapViews?.onLowMemory()
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

        viewModel.districtLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessPlaceGetDistrict(it.data)
                }
            }
        })
    }

    private fun onSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        if (data.postalCode.isEmpty() || data.districtId == 0) {
            currentLat = data.latitude.toDouble()
            currentLong = data.longitude.toDouble()
            moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
//            showNotFoundLocation()
        } else {
            doAfterSuccessPlaceGetDistrict(data)
        }
    }


    private fun doAfterSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
//        showDistrictBottomSheet()

        currentLat = data.latitude.toDouble()
        currentLong = data.longitude.toDouble()
//        isGetDistrict = true
        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

/*        continueWithLocation = true
        val savedModel = saveAddressMapper.map(getDistrictDataUiModel, zipCodes)
        presenter.setAddress(savedModel)
        with(getDistrictDataUiModel.errMessage) {
            if (this != null && this.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE)) {
                showLocationNotFoundCTA()
            } else updateGetDistrictBottomSheet(savedModel)
        }*/
    }


    private fun prepareMap(savedInstanceState: Bundle?) {
        binding?.mapViews?.onCreate(savedInstanceState)
        binding?.mapViews?.getMapAsync(this)
    }

    private fun prepareLayout() {
        //prepare bottomsheet etc

    }

/*
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
*/

    private fun moveMap(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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

        fun newInstance(extra: Bundle): PinpointNewPageFragment {
            return PinpointNewPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PLACE_ID, extra.getString(EXTRA_PLACE_ID))
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentPlaceId = it.getString(EXTRA_PLACE_ID)
        }
    }

}