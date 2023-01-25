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
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticCommon.data.constant.PinpointSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_NEGATIVE_FULL_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_RESET_TO_SEARCH_PAGE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.GPS_REQUEST
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
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
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.PinpointWebviewActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.IMAGE_OUTSIDE_INDONESIA
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.MAPS_EMPTY
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class PinpointNewPageFragment : BaseDaggerFragment(), OnMapReadyCallback {

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

    private var bottomSheetInfo: BottomSheetUnify? = null
    private var bottomSheetLocUndefined: BottomSheetUnify? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false

    private var permissionState: Int = PERMISSION_NOT_DEFINED
    private var isAccessAppPermissionFromSettings: Boolean = false

    private val requiredPermissions: Array<String>
        get() = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private var composite = CompositeSubscription()

    private var binding by autoClearedNullable<FragmentPinpointNewBinding> {
        it.mapViews.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinpointNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkMapsAvailability(savedInstanceState)
    }

    private fun checkMapsAvailability(savedInstanceState: Bundle?) {
        if (viewModel.isGmsAvailable) {
            prepareMap(savedInstanceState)
            fetchData()
            initView()
            setViewListener()
            initObserver()
        } else {
            context?.let { ctx -> goToLitePinpoint(ctx) }
        }
    }

    private fun goToLitePinpoint(context: Context) {
        val intent = getLitePinpointIntent(context)
        startActivityForResult(intent, REQUEST_CODE_PINPOINT_LITE)
    }

    private fun getLitePinpointIntent(context: Context): Intent {
        if (viewModel.isEdit) {
            if (viewModel.getAddress().hasPinpoint()) {
                return PinpointWebviewActivity.getIntent(
                    context = context,
                    saveAddressDataModel = viewModel.getAddress(),
                    lat = viewModel.getAddress().latitude.toDoubleOrZero(),
                    lng = viewModel.getAddress().longitude.toDoubleOrZero(),
                    source = PinpointSource.EDIT_ADDRESS
                )
            } else {
                return PinpointWebviewActivity.getIntent(
                    context = context,
                    saveAddressDataModel = viewModel.getAddress(),
                    districtId = viewModel.getAddress().districtId,
                    source = PinpointSource.EDIT_ADDRESS
                )
            }
        } else {
            val source =
                if (viewModel.isPositiveFlow) PinpointSource.ADD_ADDRESS_POSITIVE else PinpointSource.ADD_ADDRESS_NEGATIVE
            return PinpointWebviewActivity.getIntent(
                context = context,
                saveAddressDataModel = viewModel.getAddress(),
                districtId = viewModel.getAddress().districtId,
                lat = viewModel.getAddress().latitude.toDoubleOrZero(),
                lng = viewModel.getAddress().longitude.toDoubleOrZero(),
                source = source
            )
        }
    }

    private fun initView() {
        if (viewModel.isEdit) {
            binding?.bottomsheetLocation?.let {
                it.btnPrimary.text = getString(R.string.choose_this_location)
                it.btnSecondary.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ADDRESS_FORM_PAGE -> {
                    onResultFromAddressForm(data)
                }
                REQUEST_SEARCH_PAGE -> {
                    onResultFromSearchPage(data)
                }
                GPS_REQUEST -> {
                    onResultFromGpsRequest()
                }
                REQUEST_CODE_PINPOINT_LITE -> {
                    data?.let { it -> handlePinpointLite(it) }
                }
            }
        } else {
            if (requestCode == REQUEST_CODE_PINPOINT_LITE) {
                activity?.finish()
            }
        }
    }

    private fun onResultFromAddressForm(data: Intent?) {
        val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
        finishActivity(newAddress, false)
    }

    private fun onResultFromSearchPage(data: Intent?) {
        if (!viewModel.isEdit) {
            val newAddress =
                data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
            isFromAddressForm?.let { finishActivity(newAddress, it) }
        } else {
            viewModel.currentPlaceId = data?.getStringExtra(EXTRA_PLACE_ID)
            val currentLat = data?.getDoubleExtra(EXTRA_LAT, 0.0).orZero()
            val currentLong = data?.getDoubleExtra(EXTRA_LONG, 0.0).orZero()
            if (!viewModel.currentPlaceId.isNullOrEmpty()) {
                viewModel.currentPlaceId?.let { viewModel.getDistrictLocation(it) }
            } else if (currentLong != 0.0 && currentLat != 0.0) {
                moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
                viewModel.getDistrictData(currentLat, currentLong)
            } else {
                goToAddressForm()
            }
        }
    }

    private fun onResultFromGpsRequest() {
        bottomSheetLocUndefined?.dismiss()
        if (allPermissionsGranted()) {
            showLoading()
            Handler().postDelayed({ getLocation() }, GPS_DELAY)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (activity != null && context != null) {
            permissionState = AddNewAddressUtils.getPermissionStateFromResult(
                requireActivity(),
                requireContext(),
                permissions
            )
        }
        when (permissionState) {
            PERMISSION_GRANTED -> {
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickAllowLocationPinpoint(userSession.userId)
                }
            }
            PERMISSION_DENIED -> {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)
                }
            }
            PERMISSION_DONT_ASK_AGAIN -> {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)
                }
                showBottomSheetLocUndefined(true)
            }
        }
    }

    private fun handlePinpointLite(data: Intent) {
        data.run {
            getParcelableExtra<SaveAddressDataModel>(KEY_ADDRESS_DATA)?.let { addressData ->
                viewModel.setAddress(
                    addressData
                )
            }
            onChoosePinpoint()
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_ADDRESS_NEW, data)
                    putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                    putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
                    putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                }
            )
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(
            getLatLng(viewModel.getAddress().latitude, viewModel.getAddress().longitude),
            ZOOM_LEVEL
        )

        if (viewModel.isPolygon) {
            viewModel.getDistrictBoundaries()
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
        if (isAccessAppPermissionFromSettings) {
            isAccessAppPermissionFromSettings = false
            if (allPermissionsGranted()) {
                bottomSheetLocUndefined?.dismiss()
                permissionState = PERMISSION_GRANTED
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapViews?.onLowMemory()
    }

    private fun initData() {
        arguments?.let {
            viewModel.setDataFromArguments(
                currentPlaceId = it.getString(EXTRA_PLACE_ID),
                latitude = it.getDouble(EXTRA_LAT),
                longitude = it.getDouble(EXTRA_LONG),
                addressData = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL),
                isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW),
                isPolygon = it.getBoolean(EXTRA_IS_POLYGON, false),
                isFromAddressForm = it.getBoolean(EXTRA_FROM_ADDRESS_FORM),
                isEdit = it.getBoolean(EXTRA_IS_EDIT),
                source = it.getString(PARAM_SOURCE, "")
            )

            getGmsAvailability(it)
        }
    }

    private fun getGmsAvailability(bundle: Bundle) {
        viewModel.isGmsAvailable = if (bundle.containsKey(EXTRA_GMS_AVAILABILITY)) {
            bundle.getBoolean(
                EXTRA_GMS_AVAILABILITY,
                true
            )
        } else {
            context?.let { ctx -> MapsAvailabilityHelper.isMapsAvailable(ctx) } ?: true
        }
    }

    private fun fetchData() {
        if (!viewModel.currentPlaceId.isNullOrEmpty()) {
            viewModel.currentPlaceId?.let { viewModel.getDistrictLocation(it) }
        } else {
            if (viewModel.isPositiveFlow) {
                viewModel.getCurrentLocationDetail()
            } else {
                if (viewModel.getAddress().hasPinpoint()) {
                    // negative flow but already pinpoint before
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE

                    showDistrictBottomSheet()
                    moveMap(
                        getLatLng(
                            viewModel.getAddress().latitude,
                            viewModel.getAddress().longitude
                        ),
                        ZOOM_LEVEL
                    )
                    updateGetDistrictBottomSheet(viewModel.getAddress())
                } else {
                    viewModel.getDistrictCenter()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.autofillDistrictData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE
                    if (it.data.messageError.isEmpty()) {
                        onSuccessAutofill(it.data.data)
                    } else {
                        val msg = it.data.messageError.getOrNull(0)
                        msg?.let { error ->
                            when {
                                error.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                                error.contains(LOCATION_NOT_FOUND_MESSAGE) -> showNotFoundLocation()
                            }
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
        }

        viewModel.districtLocation.observe(viewLifecycleOwner) {
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
                            viewModel.showIllustrationMap = true
                            showNotFoundLocation()
                        }
                    }
                }
            }
        }

        viewModel.districtCenter.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    moveMap(getLatLng(it.data.latitude, it.data.longitude), ZOOM_LEVEL)
                    viewModel.getDistrictData(it.data.latitude, it.data.longitude)
                }
            }
        }

        viewModel.districtBoundary.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showBoundaries(it.data.geometry.listCoordinates)
                }
            }
        }
    }

    private fun showBoundaries(boundaries: List<LatLng>) {
        this.googleMap?.addPolygon(
            PolygonOptions()
                .addAll(boundaries)
                .strokeWidth(MAP_BOUNDARY_STROKE_WIDTH)
        )
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
        if ((data.postalCode.isEmpty() && viewModel.getAddress().postalCode.isEmpty()) || data.districtId == 0L) {
            viewModel.setLatLong(data.latitude.toDoubleOrZero(), data.longitude.toDoubleOrZero())
            moveMap(getLatLng(data.latitude, data.longitude), ZOOM_LEVEL)
            showNotFoundLocation()
        } else {
            doAfterSuccessPlaceGetDistrict(data)
        }
    }

    private fun doAfterSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        showDistrictBottomSheet()

        moveMap(getLatLng(data.latitude, data.longitude), ZOOM_LEVEL)

        val savedModel = saveAddressMapper.map(data, null, viewModel.getAddress())
        viewModel.setAddress(savedModel)
        with(data.errMessage) {
            if (this != null && this.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE)) {
                showNotFoundLocation()
            } else {
                updateGetDistrictBottomSheet(savedModel)
            }
        }
    }

    private fun updateGetDistrictBottomSheet(data: SaveAddressDataModel) {
        viewModel.setAddress(data)
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
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickIconQuestion(userSession.userId)
                }
                bottomsheetLocation.root.hide()
                showBottomSheetInfo()
            }

            bottomsheetLocation.btnPrimary.setOnClickListener {
                if (viewModel.isEdit) {
                    EditAddressRevampAnalytics.onClickPilihLokasiIni(userSession.userId)
                } else {
                    if (viewModel.isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickPilihLokasiPositive(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(
                            userSession.userId,
                            SUCCESS
                        )
                    }
                }
                onChoosePinpoint()
            }

            bottomsheetLocation.btnSecondary.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickIsiAlamatManual(userSession.userId)
                if (viewModel.isPositiveFlow) {
                    viewModel.isPositiveFlow = false
                    viewModel.setAddress(SaveAddressDataModel())
                    goToAddressForm()
                } else {
                    activity?.run {
                        setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                                putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                            }
                        )
                        finish()
                    }
                }
            }

            chipsCurrentLoc.chipImageResource =
                context?.let { getIconUnifyDrawable(it, IconUnify.TARGET) }
            chipsCurrentLoc.setOnClickListener {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
                }
                if (allPermissionsGranted()) {
                    permissionState = PERMISSION_GRANTED
                    if (AddNewAddressUtils.isGpsEnabled(context)) {
                        getLocation()
                    } else {
                        showBottomSheetLocUndefined(false)
                    }
                } else {
                    when (permissionState) {
                        PERMISSION_DENIED, PERMISSION_NOT_DEFINED -> {
                            requestPermissionLocation()
                        }
                        PERMISSION_DONT_ASK_AGAIN -> {
                            showBottomSheetLocUndefined(true)
                        }
                    }
                }
            }
            if (viewModel.isEdit) {
                chipsSearch.chipText = getString(R.string.pinpointpage_chips_search_edit)
            }
            chipsSearch.chipImageResource =
                context?.let { getIconUnifyDrawable(it, IconUnify.SEARCH) }
            chipsSearch.setOnClickListener {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickCariUlangAlamat(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickCariUlangAlamat(userSession.userId)
                }
                goToSearchPage()
            }
        }
    }

    private fun onChoosePinpoint() {
        if (viewModel.isEdit) {
            setResultAddressFormNegative()
        } else {
            if (viewModel.isPositiveFlow) {
                goToAddressForm()
            } else {
                setResultAddressFormNegative()
            }
        }
    }

    private fun requestPermissionLocation() {
        requestPermissions(requiredPermissions, REQUEST_CODE_PERMISSION)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        permission
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        showLoading()
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                moveMap(getLatLng(data.latitude, data.longitude), ZOOM_LEVEL)
                viewModel.getDistrictData(data.latitude, data.longitude)
            } else {
                fusedLocationClient?.requestLocationUpdates(
                    AddNewAddressUtils.getLocationRequest(),
                    locationCallback,
                    null
                )
            }
        }
    }

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean) {
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickXOnBlockGpsPinpoint(userSession.userId)
                }
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(
        viewBinding: BottomsheetLocationUndefinedBinding,
        isDontAskAgain: Boolean
    ) {
        viewBinding.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiPinpoint(userSession.userId)
                }
                if (!isDontAskAgain) {
                    goToSettingLocationDevice()
                } else {
                    goToSettingLocationApps()
                }
            }
        }
    }

    private fun goToSettingLocationDevice() {
        context?.let { turnGPSOn(it) }
    }

    private fun goToSettingLocationApps() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        isAccessAppPermissionFromSettings = true
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
    }

    private fun turnGPSOn(context: Context): Boolean {
        var isGpsOn = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL
        locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
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
                .addOnFailureListener(
                    context,
                    OnFailureListener { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    startIntentSenderForResult(
                                        rae.resolution.intentSender,
                                        GPS_REQUEST,
                                        null,
                                        0,
                                        0,
                                        0,
                                        null
                                    )
                                } catch (sie: IntentSender.SendIntentException) {
                                    sie.printStackTrace()
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage =
                                    "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
        }
        return isGpsOn
    }

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    // send to maps
                    hasRequestedLocation = true
                }
                stopLocationUpdate()
                moveMap(
                    getLatLng(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    ),
                    ZOOM_LEVEL
                )
                viewModel.getDistrictData(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
            }
        }

    private fun showBottomSheetInfo() {
        bottomSheetInfo = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUnmatchedBinding.inflate(LayoutInflater.from(context), null, false)
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

        if (viewModel.isPolygon) {
            if (data.districtId != viewModel.getAddress().districtId) {
                if (!viewModel.isEdit) {
                    AddNewAddressRevampAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
                }
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(
                            userSession.userId,
                            NOT_SUCCESS
                        )
                    }
                }
                view?.let { view ->
                    Toaster.build(
                        view,
                        getString(R.string.txt_toaster_pinpoint_unmatched),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            } else {
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(
                            userSession.userId,
                            SUCCESS
                        )
                    }
                    setResultAddressFormNegative()
                }
                val saveAddress = saveAddressMapper.map(data, null, viewModel.getAddress())
                viewModel.setAddress(saveAddress)
                updateGetDistrictBottomSheet(saveAddress)
            }
        } else {
            val saveAddress = saveAddressMapper.map(data, null, viewModel.getAddress())
            viewModel.setAddress(saveAddress)
            updateGetDistrictBottomSheet(saveAddress)
        }
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
            if (!viewModel.isEdit) {
                AddNewAddressRevampAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
            } else {
                EditAddressRevampAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
            }
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(IMAGE_OUTSIDE_INDONESIA)
                tvInvalidLoc.text = getString(R.string.out_of_indonesia_title)
                tvInvalidLocDetail.text = getString(R.string.out_of_indonesia_desc_new)
                if (viewModel.isEdit) {
                    btnAnaNegative.visibility = View.GONE
                }
            }
        } else {
            if (!viewModel.isEdit) {
                AddNewAddressRevampAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(userSession.userId)
            } else {
                EditAddressRevampAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(userSession.userId)
            }
            if (viewModel.showIllustrationMap) {
                binding?.mapsEmpty?.visibility = View.VISIBLE
                binding?.mapViews?.visibility = View.GONE
                binding?.imgMapsEmpty?.setImageUrl(MAPS_EMPTY)
                viewModel.showIllustrationMap = false
            }
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(LOCATION_NOT_FOUND)
                if (viewModel.isEdit) {
                    tvInvalidLoc.text = getString(R.string.undetected_location_new_edit)
                    if (viewModel.getAddress().hasPinpoint()) {
                        tvInvalidLocDetail.text =
                            getString(R.string.undetected_location_desc_edit_w_pinpoint)
                        btnAnaNegative.visibility = View.GONE
                    } else {
                        tvInvalidLocDetail.text =
                            getString(R.string.undetected_location_desc_edit_wo_pinpoint)
                        btnAnaNegative.let {
                            it.buttonVariant = UnifyButton.Variant.GHOST
                            it.buttonType = UnifyButton.Type.MAIN
                            it.text = getString(R.string.mismatch_btn_title)
                            it.setOnClickListener {
                                goToAddressForm()
                            }
                        }
                    }
                } else {
                    tvInvalidLoc.text = getString(R.string.undetected_location_new)
                    tvInvalidLocDetail.text = getString(R.string.undetected_location_desc_new)
                }
            }
        }

        binding?.bottomsheetLocation?.btnAnaNegative?.setOnClickListener {
            if (!viewModel.isEdit) {
                if (type == BOTTOMSHEET_OUT_OF_INDO) {
                    AddNewAddressRevampAnalytics.onClickIsiAlamatOutOfIndo(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickIsiAlamatManualUndetectedLocation(
                        userSession.userId
                    )
                }
            }
            viewModel.isPositiveFlow = false
            goToAddressForm()
        }
    }

    private fun goToSearchPage() {
        if (!viewModel.isEdit) {
            if (!viewModel.isPositiveFlow) {
                // back to addressform, reset ana state to search page
                activity?.run {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().apply {
                            putExtra(EXTRA_RESET_TO_SEARCH_PAGE, true)
                            putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                        }
                    )
                    finish()
                }
            } else {
                activity?.finish()
            }
        } else {
            context?.let {
                startActivityForResult(
                    Intent(it, SearchPageActivity::class.java).apply {
                        putExtra(EXTRA_IS_EDIT, viewModel.isEdit)
                        putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                    },
                    REQUEST_SEARCH_PAGE
                )
            }
        }
    }

    private fun goToAddressForm() {
        val saveModel = viewModel.getAddress()
        if (!viewModel.isEdit) {
            Intent(context, AddressFormActivity::class.java).apply {
                putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
                putExtra(EXTRA_IS_POSITIVE_FLOW, viewModel.isPositiveFlow)
                putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                putExtra(PARAM_SOURCE, viewModel.source)
                startActivityForResult(this, REQUEST_ADDRESS_FORM_PAGE)
            }
        } else {
            activity?.run {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(EXTRA_IS_EDIT, true)
                        putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                    }
                )
                finish()
            }
        }
    }

    private fun setResultAddressFormNegative() {
        val saveModel = viewModel.getAddress()
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
                    putExtra(EXTRA_NEGATIVE_FULL_FLOW, false)
                    putExtra(EXTRA_FROM_ADDRESS_FORM, viewModel.isFromAddressForm)
                    putExtra(EXTRA_IS_EDIT, viewModel.isEdit)
                    putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
                }
            )
            finish()
        }
    }

    companion object {
        private const val RESULT_PERMISSION_CODE = 1234
        private const val REQUEST_CODE_PERMISSION = 9876
        private const val REQUEST_ADDRESS_FORM_PAGE = 1599
        private const val REQUEST_SEARCH_PAGE = 1995
        private const val REQUEST_CODE_PINPOINT_LITE = 1986

        private const val ZOOM_LEVEL = 16f
        private const val MAP_BOUNDARY_STROKE_WIDTH = 3F
        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
        private const val GPS_DELAY = 1000L

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
                    putBoolean(EXTRA_IS_POLYGON, extra.getBoolean(EXTRA_IS_POLYGON))
                    putParcelable(
                        EXTRA_SAVE_DATA_UI_MODEL,
                        extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
                    )
                    putBoolean(EXTRA_FROM_ADDRESS_FORM, extra.getBoolean(EXTRA_FROM_ADDRESS_FORM))
                    putBoolean(EXTRA_IS_EDIT, extra.getBoolean(EXTRA_IS_EDIT))
                    putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE))
                }
            }
        }
    }
}
