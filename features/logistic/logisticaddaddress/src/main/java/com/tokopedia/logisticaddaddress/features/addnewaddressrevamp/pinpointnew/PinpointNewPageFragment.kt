package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LATITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LONGITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class PinpointNewPageFragment: BaseDaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var saveAddressMapper: SaveAddressMapper


    private val viewModel: PinpointNewPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PinpointNewPageViewModel::class.java)
    }

    private var googleMap: GoogleMap? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentPlaceId: String? = ""
    private var zipCodes: MutableList<String>? = null
    private var bottomSheetInfo: BottomSheetUnify? = null

    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

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
        setViewListener()
        initData()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1599 && resultCode == Activity.RESULT_OK) {
            val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            finishActivity(newAddress)
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, data)
            })
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

        this.googleMap?.setOnCameraMoveStartedListener { _ ->
            showLoading()
        }
        this.googleMap?.let {
            rxPinPoint(it).subscribe(object : Subscriber<Boolean>() {
                override fun onNext(t: Boolean?) {
                    getAutofill()
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

    private fun initData() {
        arguments?.let {
            currentPlaceId = it.getString(EXTRA_PLACE_ID)
            currentLat = it.getDouble(EXTRA_LATITUDE)
            currentLong = it.getDouble(EXTRA_LONGITUDE)
            saveAddressDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            zipCodes = saveAddressDataModel?.zipCodes?.toMutableList()
        }

        if (!currentPlaceId.isNullOrEmpty()) {
            currentPlaceId?.let { viewModel.getDistrictLocation(it) }
        } else {
            viewModel.getDistrictData(currentLat, currentLong)
        }
    }

    private fun initObserver() {
        viewModel.autofillDistrictData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.messageError.isEmpty()) onSuccessAutofill(it.data.data)
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

    private fun showLoading() {
        binding?.bottomsheetLocation?.run {
            wholeLoadingContainer.visibility = View.VISIBLE
            districtLayout.visibility = View.GONE
        }
    }

    private fun showDistrictBottomSheet() {
        binding?.bottomsheetLocation?.run {
            wholeLoadingContainer.visibility = View.GONE
            districtLayout.visibility = View.VISIBLE
        }
    }


    private fun getAutofill() {
        val target: LatLng? = this.googleMap?.cameraPosition?.target
        val latTarget = target?.latitude ?: 0.0
        val longTarget = target?.longitude ?: 0.0

        viewModel.getDistrictData(latTarget, longTarget)
    }

    private fun onSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        if (data.postalCode.isEmpty() || data.districtId == 0) {
            currentLat = data.latitude.toDouble()
            currentLong = data.longitude.toDouble()
            moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
            showNotFoundLocation()
        } else {
            doAfterSuccessPlaceGetDistrict(data)
        }
    }


    private fun doAfterSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        showDistrictBottomSheet()

        currentLat = data.latitude.toDouble()
        currentLong = data.longitude.toDouble()
        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

        val savedModel = saveAddressMapper.map(data, zipCodes)
        viewModel.setAddress(savedModel)
        with(data.errMessage) {
            if (this != null && this.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE)) {
                showLocationNotFoundCTA()
            } else updateGetDistrictBottomSheet(savedModel)
        }
    }

    private fun updateGetDistrictBottomSheet(data: SaveAddressDataModel ) {
        this.saveAddressDataModel = saveAddressDataModel
        setDefaultResultGetDistrict(data)
    }

    private fun setDefaultResultGetDistrict(data: SaveAddressDataModel) {
        showDistrictBottomSheet()

        binding?.bottomsheetLocation?.run {
            tvAddress.text = data.title
            tvAddressDesc.text = data.formattedAddress
        }
    }


    private fun prepareMap(savedInstanceState: Bundle?) {
        binding?.mapViews?.onCreate(savedInstanceState)
        binding?.mapViews?.getMapAsync(this)
    }

    private fun prepareLayout() {
        //prepare bottomsheet etc

    }

    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())

        binding?.run {

            bottomsheetLocation.btnInfo.setOnClickListener {
                bottomsheetLocation.root.hide()
                showBottomSheetInfo()
            }

            bottomsheetLocation.btnPrimary.setOnClickListener {
                goToAddressForm()
            }

            bottomsheetLocation.btnSecondary.setOnClickListener {
                Toast.makeText(context, "This feature is under development", Toast.LENGTH_SHORT).show()
                //go-to ANA Negative
            }

            chipsCurrentLoc.setOnClickListener {
                if (allPermissionsGranted()) {
                    hasRequestedLocation = true
                    getLocation()
                } else {
                    hasRequestedLocation = false
                    requestPermissionLocation()
                }
            }

            chipsSearch.setOnClickListener {
                goToSearchPage()
            }
        }

    }

    private fun requestPermissionLocation() {
        requestPermissions(requiredPermissions, 9876)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                if (data != null) {
                    viewModel.getDistrictData(data.latitude, data.longitude)
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                            createLocationCallback(), null)
                }

            }
        } else {
            //bottomsheet blm aktifin GPS
        }
    }

    fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    //send to maps
                    hasRequestedLocation = true
                }
            }
        }
    }

    private fun showBottomSheetInfo() {
        bottomSheetInfo = BottomSheetUnify()
        val viewBinding = BottomsheetLocationUnmatchedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetInfo(viewBinding)

        bottomSheetInfo?.apply {
            setCloseClickListener {
                binding?.bottomsheetLocation?.root?.show()
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                binding?.bottomsheetLocation?.root?.show()
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetInfo?.show(it, "")
        }
    }

    private fun setupBottomSheetInfo(viewBinding: BottomsheetLocationUnmatchedBinding) {
        viewBinding.run {
            btnClose.setOnClickListener {
                binding?.bottomsheetLocation?.root?.show()
                bottomSheetInfo?.dismiss()
            }
        }
    }

    private fun moveMap(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun onSuccessAutofill(data: Data) {
        doAfterSuccessAutofill(data)
    }

    private fun doAfterSuccessAutofill(data: Data) {
        updateAfterOnSuccessAutofill(data)
    }

    private fun updateAfterOnSuccessAutofill(data: Data) {
        showDistrictBottomSheet()

        val saveAddress = saveAddressMapper.map(data, zipCodes)
        viewModel.setAddress(saveAddress)
        updateGetDistrictBottomSheet(saveAddress)
    }

    private fun showOutOfReachBottomSheet() {

    }

    private fun showLocationNotFoundCTA() {

    }

    private fun showNotFoundLocation() {

    }

    private fun goToSearchPage() {
        requireActivity().onBackPressed()
   /*     val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
        startActivity(intent)
        activity?.finish()*/
    }

    private fun goToAddressForm() {
        val saveModel = viewModel.getAddress()
        Intent(context, AddressFormActivity::class.java).apply {
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
            startActivityForResult(this, 1599)
        }

    }

    companion object {
        private const val ZOOM_LEVEL = 16f

        const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"

        fun newInstance(extra: Bundle): PinpointNewPageFragment {
            return PinpointNewPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PLACE_ID, extra.getString(EXTRA_PLACE_ID))
                    putDouble(EXTRA_LATITUDE, extra.getDouble(EXTRA_LATITUDE))
                    putDouble(EXTRA_LONGITUDE, extra.getDouble(EXTRA_LONGITUDE))
                }
            }
        }
    }

}