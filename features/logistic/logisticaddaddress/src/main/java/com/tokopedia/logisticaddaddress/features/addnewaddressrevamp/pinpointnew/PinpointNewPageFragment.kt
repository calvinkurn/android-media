package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.IMAGE_OUTSIDE_INDONESIA
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.MAPS_EMPTY
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class PinpointNewPageFragment: BaseDaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var userSession: UserSessionInterface

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
    private var currentDistrictName: String? = ""
    private var zipCodes: MutableList<String>? = null
    private var bottomSheetInfo: BottomSheetUnify? = null
    private var bottomSheetLocUndefined: BottomSheetUnify? = null

    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    /*to differentiate positive flow or negative flow*/
    private var isPositiveFlow: Boolean = true

    private var isPermissionAccessed: Boolean = false

    private var showIllustrationMap: Boolean = false

    private var isFromAddressForm: Boolean = false
    private var districtId: Int? = null
    private var currentKotaKecamatan: String? = ""
    private var currentPostalCode: String? = ""

    private var isPolygon: Boolean = false

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
        setViewListener()
        initData()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_ADDRESS_FORM_PAGE) {
                val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
                finishActivity(newAddress, false)
            } else if (requestCode == REQUEST_SEARCH_PAGE) {
                val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
                val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
                isFromAddressForm?.let { finishActivity(newAddress, it) }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var isAllowed = false
        for (permission in permissions) {
            if (!isPermissionAccessed) {
                if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission) } == true) {
                    //no-op
                } else {
                    if (activity?.let { ActivityCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_GRANTED) {
                        isAllowed = true
                        getLocation()
                        isPermissionAccessed = true
                    } else {
                        showBottomSheetLocUndefined(true)
                    }
                }
            }
        }

        if (isAllowed) {
            AddNewAddressRevampAnalytics.onClickAllowLocationPinpoint(userSession.userId)
        } else {
            AddNewAddressRevampAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)

        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, data)
                putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
                putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
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

        if (isPolygon) {
            districtId?.let { viewModel.getDistrictBoundaries(it) }
        }

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
            currentLat = it.getDouble(EXTRA_LAT)
            currentLong = it.getDouble(EXTRA_LONG)
            saveAddressDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW)
            currentDistrictName = it.getString(EXTRA_DISTRICT_NAME)
            districtId = saveAddressDataModel?.districtId
            if (districtId == null) {
                districtId = it.getInt(EXTRA_DISTRICT_ID)
            }
            isPolygon = it.getBoolean(EXTRA_IS_POLYGON, false)
            zipCodes = saveAddressDataModel?.zipCodes?.toMutableList()
            currentKotaKecamatan = it.getString(EXTRA_KOTA_KECAMATAN)
            currentPostalCode = it.getString(EXTRA_POSTAL_CODE)
            isFromAddressForm = it.getBoolean(EXTRA_FROM_ADDRESS_FORM)
        }

        if (!currentPlaceId.isNullOrEmpty()) {
            currentPlaceId?.let { viewModel.getDistrictLocation(it) }
        } else {
            if (isPositiveFlow) {
                viewModel.getDistrictData(currentLat, currentLong)
            } else {
                currentDistrictName?.let { viewModel.getAutoComplete(it) }
            }
        }
    }

    private fun initObserver() {
        viewModel.autofillDistrictData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE
                    if (it.data.messageError.isEmpty()) onSuccessAutofill(it.data.data)
                    else {
                        val msg = it.data.messageError[0]
                        when {
                            msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                            msg.contains(LOCATION_NOT_FOUND_MESSAGE) -> showNotFoundLocation()
                        }
                    }
                }

                is Fail -> {
                    val msg = it.throwable.message.toString()
                    when {
                        msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                        else -> showNotFoundLocation()
                    }
                }
            }
        })

        viewModel.districtLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE
                    onSuccessPlaceGetDistrict(it.data)
                }

                is Fail -> {
                    val msg = it.throwable.message.toString()
                    when {
                        msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                        else -> {
                            showIllustrationMap = true
                            showNotFoundLocation()
                        }
                    }
                }
            }
        })

        viewModel.autoCompleteData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    viewModel.getDistrictLocation(it.data.keroMapsAutocomplete.AData.predictions[0].placeId)
                }
            }
        })

        viewModel.districtBoundary.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showBoundaries(it.data.geometry.listCoordinates)
                }
            }
        })
    }

    private fun showBoundaries(boundaries: List<LatLng>) {
        this.googleMap?.addPolygon(PolygonOptions()
                .addAll(boundaries)
                .strokeWidth(3F))
    }

    private fun showLoading() {
        binding?.bottomsheetLocation?.run {
            wholeLoadingContainer.visibility = View.VISIBLE
            districtLayout.visibility = View.GONE
            invalidLayout.visibility = View.GONE
        }
    }

    private fun showDistrictBottomSheet() {
        binding?.bottomsheetLocation?.run {
            districtLayout.visibility = View.VISIBLE
            wholeLoadingContainer.visibility = View.GONE
            invalidLayout.visibility = View.GONE
        }
    }

    private fun showInvalidBottomSheet() {
        binding?.bottomsheetLocation?.run {
            invalidLayout.visibility = View.VISIBLE
            wholeLoadingContainer.visibility = View.GONE
            districtLayout.visibility = View.GONE
        }
    }


    private fun getAutofill() {
        val target: LatLng? = this.googleMap?.cameraPosition?.target
        val latTarget = target?.latitude ?: 0.0
        val longTarget = target?.longitude ?: 0.0

        viewModel.getDistrictData(latTarget, longTarget)
    }

    private fun onSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        if ((data.postalCode.isEmpty() && currentPostalCode.isNullOrEmpty()) || data.districtId == 0) {
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
                showNotFoundLocation()
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

    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())

        binding?.run {

            bottomsheetLocation.btnInfo.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickIconQuestion(userSession.userId)
                bottomsheetLocation.root.hide()
                showBottomSheetInfo()
            }

            bottomsheetLocation.btnPrimary.setOnClickListener {
                if (isPositiveFlow) {
                    AddNewAddressRevampAnalytics.onClickPilihLokasiPositive(userSession.userId, SUCCESS)
                    goToAddressForm()
                } else {
                    AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(userSession.userId, SUCCESS)
                    setResultAddressFormNegative()
                }
            }

            bottomsheetLocation.btnSecondary.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickIsiAlamatManual(userSession.userId)
                if (isPositiveFlow) {
                    isPositiveFlow = false
                    goToAddressForm()
                } else {
                    activity?.run {
                        setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
                            putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
                            putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                        })
                        finish()
                    }
                }

            }

            chipsCurrentLoc.chipImageResource = context?.let { getIconUnifyDrawable(it, IconUnify.TARGET) }
            chipsCurrentLoc.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    if (allPermissionsGranted()) {
                        hasRequestedLocation = true
                        getLocation()
                    } else {
                        hasRequestedLocation = false
                        requestPermissionLocation()
                    }
                } else {
                    requestPermissionLocation()
                }
            }

            chipsSearch.chipImageResource = context?.let { getIconUnifyDrawable(it, IconUnify.SEARCH) }
            chipsSearch.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickCariUlangAlamat(userSession.userId)
                goToSearchPage()
            }
        }

    }

    private fun requestPermissionLocation() {
        requestPermissions(requiredPermissions, REQUEST_CODE_PERMISSION)
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
                isPermissionAccessed = false
                if (data != null) {
                    moveMap(getLatLng(data.latitude, data.longitude), ZOOM_LEVEL)
                    viewModel.getDistrictData(data.latitude, data.longitude)
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                            createLocationCallback(), null)
                }

            }
        } else {
            showBottomSheetLocUndefined(false)
        }
    }

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean){
        isPermissionAccessed = true
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding = BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                isPermissionAccessed = false
                AddNewAddressRevampAnalytics.onClickXOnBlockGpsPinpoint(userSession.userId)
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                isPermissionAccessed = false
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(viewBinding: BottomsheetLocationUndefinedBinding, isDontAskAgain: Boolean) {
        viewBinding.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiPinpoint(userSession.userId)
                if (!isDontAskAgain) {
                    goToSettingLocationDevice()
                } else {
                    goToSettingLocationApps()
                }
            }
        }
    }

    private fun goToSettingLocationDevice() {
        if (context?.let { turnGPSOn(it) } == false) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }


    private fun goToSettingLocationApps() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
    }

    private fun turnGPSOn(context: Context): Boolean {
        var isGpsOn = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000
        locationRequest.fastestInterval = 2 * 1000
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGpsOn = true
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(context as Activity) {
                        //  GPS is already enable, callback GPS status through listener
                        isGpsOn = true
                    }
                    .addOnFailureListener(context, OnFailureListener { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(context, GPS_REQUEST)
                                } catch (sie: IntentSender.SendIntentException) {
                                    sie.printStackTrace()
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
        }
        return isGpsOn
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

        if (isPolygon) {
            if (data.districtId != districtId) {
                AddNewAddressRevampAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(userSession.userId, NOT_SUCCESS)
                }
                view?.let { view -> Toaster.build(view, getString(R.string.txt_toaster_pinpoint_unmatched), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
            } else {
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(userSession.userId, SUCCESS)
                    setResultAddressFormNegative()
                }
                val saveAddress = saveAddressMapper.map(data, zipCodes)
                viewModel.setAddress(saveAddress)
                updateGetDistrictBottomSheet(saveAddress)
            }
        }

        val saveAddress = saveAddressMapper.map(data, zipCodes)
        viewModel.setAddress(saveAddress)
        updateGetDistrictBottomSheet(saveAddress)
    }

    private fun showOutOfReachBottomSheet() {
        showInvalidBottomSheet()
        updateInvalidBottomSheetData(BOTTOMSHEET_OUT_OF_INDO)
    }


    private fun showNotFoundLocation() {
        showInvalidBottomSheet()
        updateInvalidBottomSheetData(BOTTOMSHEET_NOT_FOUND_LOC)
    }

    private fun updateInvalidBottomSheetData(type: Int) {
        if (type == BOTTOMSHEET_OUT_OF_INDO) {
            AddNewAddressRevampAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(IMAGE_OUTSIDE_INDONESIA)
                tvInvalidLoc.text = getString(R.string.out_of_indonesia_title)
                tvInvalidLocDetail.text = getString(R.string.out_of_indonesia_desc_new)
            }
        } else {
            AddNewAddressRevampAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(userSession.userId)
            if (showIllustrationMap) {
                binding?.mapsEmpty?.visibility = View.VISIBLE
                binding?.mapViews?.visibility = View.GONE
                binding?.imgMapsEmpty?.setImageUrl(MAPS_EMPTY)
                showIllustrationMap = false
            }
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(LOCATION_NOT_FOUND)
                tvInvalidLoc.text = getString(R.string.undetected_location_new)
                tvInvalidLocDetail.text = getString(R.string.undetected_location_desc_new)
            }
        }

        binding?.bottomsheetLocation?.btnAnaNegative?.setOnClickListener {
            if (type == BOTTOMSHEET_OUT_OF_INDO) AddNewAddressRevampAnalytics.onClickIsiAlamatOutOfIndo(userSession.userId)
            else AddNewAddressRevampAnalytics.onClickIsiAlamatManualUndetectedLocation(userSession.userId)
            isPositiveFlow = false
            goToAddressForm()
        }
    }

    private fun goToSearchPage() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
        if (!isPositiveFlow) {
            intent.putExtra(EXTRA_IS_POLYGON, isPolygon)
            intent.putExtra(EXTRA_FROM_PINPOINT, true)
            intent.putExtra(EXTRA_DISTRICT_ID, districtId)
        }
        intent.putExtra(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        intent.putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
        intent.putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
        startActivityForResult(intent, REQUEST_SEARCH_PAGE)
    }

    private fun goToAddressForm() {
        val saveModel = viewModel.getAddress()
        Intent(context, AddressFormActivity::class.java).apply {
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
            putExtra(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
            startActivityForResult(this, REQUEST_ADDRESS_FORM_PAGE)
        }
    }

    private fun setResultAddressFormNegative() {
        val saveModel = viewModel.getAddress()
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
                putExtra(EXTRA_NEGATIVE_FULL_FLOW, false)
                putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
                putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
            })
            finish()
        }
    }

    companion object {
        private const val RESULT_PERMISSION_CODE = 1234
        private const val REQUEST_CODE_PERMISSION = 9876
        private const val REQUEST_ADDRESS_FORM_PAGE = 1599
        private const val REQUEST_SEARCH_PAGE = 1995

        private const val ZOOM_LEVEL = 16f

        const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"
        const val BOTTOMSHEET_OUT_OF_INDO = 1
        const val BOTTOMSHEET_NOT_FOUND_LOC = 2

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        fun newInstance(extra: Bundle): PinpointNewPageFragment {
            return PinpointNewPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PLACE_ID, extra.getString(EXTRA_PLACE_ID))
                    putDouble(EXTRA_LAT, extra.getDouble(EXTRA_LAT))
                    putDouble(EXTRA_LONG, extra.getDouble(EXTRA_LONG))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putString(EXTRA_DISTRICT_NAME, extra.getString(EXTRA_DISTRICT_NAME))
                    putBoolean(EXTRA_IS_POLYGON, extra.getBoolean(EXTRA_IS_POLYGON))
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putString(EXTRA_KOTA_KECAMATAN, extra.getString(EXTRA_KOTA_KECAMATAN))
                    putBoolean(EXTRA_FROM_ADDRESS_FORM, extra.getBoolean(EXTRA_FROM_ADDRESS_FORM))
                    putInt(EXTRA_DISTRICT_ID, extra.getInt(EXTRA_DISTRICT_ID))
                    putString(EXTRA_POSTAL_CODE, extra.getString(EXTRA_POSTAL_CODE))
                }
            }
        }
    }

}