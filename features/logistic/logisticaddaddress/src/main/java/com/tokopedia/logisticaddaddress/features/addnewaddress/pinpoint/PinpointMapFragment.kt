package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.component.Dialog
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointMapBinding
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressComponent
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.utils.RequestPermissionUtil
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class PinpointMapFragment : BaseDaggerFragment(), PinpointMapView, OnMapReadyCallback,
        AutocompleteBottomSheetFragment.ActionListener, HasComponent<AddNewAddressComponent> {

    private var googleMap: GoogleMap? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var isShowingAutocomplete: Boolean = true
    private var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>? = null
    private val handler = Handler()
    private var UNNAMED_ROAD: String = "Unnamed Road"
    private var isGetDistrict = false
    private val FINISH_FLAG = 1212
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private var token: Token? = null
    private var isPolygon: Boolean = false
    private var districtId: Int? = null
    private val GREEN_ARGB = 0x40388E3C
    private var isMismatchSolved: Boolean = false
    private var isMismatch: Boolean = false
    private var zipCodes: MutableList<String>? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var addNewAddressComponent: AddNewAddressComponent? = null
    private var isChangesRequested: Boolean = false
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var continueWithLocation: Boolean? = false
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var isCircuitBreaker: Boolean = false
    private var isGpsEnable: Boolean = true
    private var warehouseDataModel: Warehouse? = null
    private var isEditWarehouse: Boolean = false
    private var isReqLocation: Boolean = false

    private var binding by autoClearedNullable<FragmentPinpointMapBinding> {
        // Set map view on destroy before binding is null
        it.mapView.onDestroy()
    }

    private var composite = CompositeSubscription()

    @Inject
    lateinit var presenter: PinpointMapPresenter

    @Inject
    lateinit var saveAddressMapper: SaveAddressMapper

    override fun getScreenName(): String {
        return PinpointMapFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@PinpointMapFragment)
            presenter.attachView(this@PinpointMapFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(extra: Bundle): PinpointMapFragment {
            return PinpointMapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_LAT, extra.getDouble(EXTRA_LAT, DEFAULT_LAT))
                    putDouble(EXTRA_LONG, extra.getDouble(EXTRA_LONG, DEFAULT_LONG))
                    putBoolean(EXTRA_SHOW_AUTOCOMPLETE, extra.getBoolean(EXTRA_SHOW_AUTOCOMPLETE, true))
                    putParcelable(KERO_TOKEN, extra.getParcelable(KERO_TOKEN))
                    putBoolean(EXTRA_IS_POLYGON, extra.getBoolean(EXTRA_IS_POLYGON))
                    putBoolean(EXTRA_IS_MISMATCH_SOLVED, extra.getBoolean(EXTRA_IS_MISMATCH_SOLVED))
                    putBoolean(EXTRA_IS_MISMATCH, extra.getBoolean(EXTRA_IS_MISMATCH))
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_CHANGES_REQUESTED, extra.getBoolean(EXTRA_IS_CHANGES_REQUESTED))
                    putBoolean(EXTRA_IS_FULL_FLOW, extra.getBoolean(EXTRA_IS_FULL_FLOW, true))
                    putBoolean(EXTRA_IS_LOGISTIC_LABEL, extra.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true))
                    putBoolean(EXTRA_IS_CIRCUIT_BREAKER, extra.getBoolean(EXTRA_IS_CIRCUIT_BREAKER, false))
                    putParcelable(EXTRA_WAREHOUSE_DATA, extra.getParcelable(EXTRA_WAREHOUSE_DATA))
                    putBoolean(EXTRA_IS_EDIT_WAREHOUSE, extra.getBoolean(EXTRA_IS_EDIT_WAREHOUSE, false))
                }
                permissionCheckerHelper = PermissionCheckerHelper()
            }
        }

        private const val ADDRESS_KONSLET = "tokopedia_konslet.png"
        private const val ADDRESS_OUT_OF_INDONESIA = "tokopedia_out_of_indonesia.png"
        private const val ADDRESS_INVALID = "ic_invalid_location.png"
        private const val ZOOM_LEVEL = 16f

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentLat = it.getDouble(EXTRA_LAT, DEFAULT_LAT)
            currentLong = it.getDouble(EXTRA_LONG, DEFAULT_LONG)
            isShowingAutocomplete = it.getBoolean(EXTRA_SHOW_AUTOCOMPLETE, true)
            token = it.getParcelable(KERO_TOKEN)
            isPolygon = it.getBoolean(EXTRA_IS_POLYGON, false)
            isMismatchSolved = it.getBoolean(EXTRA_IS_MISMATCH_SOLVED, false)
            isMismatch = it.getBoolean(EXTRA_IS_MISMATCH, false)
            saveAddressDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            isChangesRequested = it.getBoolean(EXTRA_IS_CHANGES_REQUESTED, false)
            zipCodes = saveAddressDataModel?.zipCodes?.toMutableList()
            districtId = saveAddressDataModel?.districtId
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
            isCircuitBreaker = it.getBoolean(EXTRA_IS_CIRCUIT_BREAKER, false)
            warehouseDataModel = it.getParcelable(EXTRA_WAREHOUSE_DATA)
            isEditWarehouse = it.getBoolean(EXTRA_IS_EDIT_WAREHOUSE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPinpointMapBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareMap(savedInstanceState)
        prepareLayout()
        setViewListener()

        val zoom = googleMap?.cameraPosition?.zoom ?: 0f
        presenter.autoFill(currentLat, currentLong, zoom)
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        binding?.mapView?.run {
            onCreate(savedInstanceState)
            getMapAsync(this@PinpointMapFragment)
        }
    }

    private fun prepareLayout() {
        binding?.bottomSheetGetDistrict?.run {
            bottomSheetBehavior = BottomSheetBehavior.from(this.bottomSheetGetDistrict)
            getdistrictContainer.visibility = View.GONE
            invalidContainer.visibility = View.GONE
            wholeLoadingContainer.visibility = View.VISIBLE
            if (!isMismatch) etDetailAddress.setText(saveAddressDataModel?.editDetailAddress)
            if (isEditWarehouse) {
                detailAddressLayout.visibility = View.GONE
            } else {
                detailAddressLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setViewListener() {
        binding?.run {
            backButton.setOnClickListener {
                // hide keyboard
                val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
                (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.rootView?.windowToken, 0)

                AddNewAddressAnalytics.eventClickBackArrowOnPinPoint(isFullFlow, isLogisticLabel)
                activity?.finish()
            }

            bottomSheetGetDistrict.icSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }

            bottomSheetGetDistrict.etDetailAddress.run {
                setOnClickListener {
                    bottomSheetGetDistrict.getdistrictContainer.requestFocusFromTouch()
                    bottomSheetGetDistrict.etDetailAddress.requestFocusFromTouch()
                }

                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }

            icCurrentLocation.setOnClickListener {
                AddNewAddressAnalytics.eventClickButtonPilihLokasi(isFullFlow, isLogisticLabel)
                doUseCurrentLocation(isFullFlow)
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestLocation()
            } else {
                googleMap?.isMyLocationEnabled = true
            }
        }
        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

        if (this.isPolygon) {
            districtId?.let { districtId ->
                token?.let { token ->
                    presenter.getDistrictBoundary(districtId, token.districtRecommendation, token.ut)
                }
            }
        }

        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.isHideable = false
        this.googleMap?.setOnCameraMoveStartedListener { _ -> showLoading() }
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

    override fun showLoading() {
        binding?.bottomSheetGetDistrict?.run {
            getdistrictContainer.visibility = View.GONE
            invalidContainer.visibility = View.GONE
            wholeLoadingContainer.visibility = View.VISIBLE
        }
    }

    private fun getAutofill() {
        if (!isGetDistrict) {
            val target: LatLng? = this.googleMap?.cameraPosition?.target
            val zoomLevel = this.googleMap?.cameraPosition?.zoom ?: 0f
            val latTarget = target?.latitude ?: 0.0
            val longTarget = target?.longitude ?: 0.0

            presenter.autoFill(latTarget, longTarget, zoomLevel)
        }
        isGetDistrict = false
    }

    override fun showAutoComplete(lat: Double, long: Double) {
        if (isShowingAutocomplete) {
            SimpleIdlingResource.increment()
            handler.postDelayed({
                SimpleIdlingResource.decrement()
                showAutocompleteGeocodeBottomSheet(lat, long, "")
            }, 500)
        }

        if (lat == 0.0 && long == 0.0) {
            currentLat = DEFAULT_LAT
            currentLong = DEFAULT_LONG
        } else {
            currentLat = lat
            currentLong = long
        }
        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
    }

    private fun moveMap(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            binding?.icCurrentLocation?.setImageResource(R.drawable.ic_gps_enable)
        } else {
            isGpsEnable = false
            binding?.icCurrentLocation?.setImageResource(R.drawable.ic_gps_disable)
        }
        getLastLocationClient()
    }

    private fun getLastLocationClient() {
        if (!isGpsEnable && RequestPermissionUtil.checkHasPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            SimpleIdlingResource.increment()
            Handler().postDelayed({
                SimpleIdlingResource.decrement()
                showAutoCompleteBottomSheet("")
            }, 200)
        } else {
            fusedLocationClient?.lastLocation
                    ?.addOnSuccessListener {
                        if (it != null) {
                            if (!isEditWarehouse) showAutoComplete(it.latitude, it.longitude)
                            else showAutoComplete(currentLat, currentLong)
                        } else {
                            showAutoComplete(DEFAULT_LAT, DEFAULT_LONG)
                        }
                    }
        }
    }

    private fun doUseCurrentLocation(isFullFlow: Boolean) {
        isReqLocation = true
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            requestLocation()
        } else {
            showLocationInfoBottomSheet(isFullFlow)
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop()
    }

    override fun onDestroyView() {
        binding?.mapView?.onDestroy()
        composite.unsubscribe()
        presenter.detachView()
        super.onDestroyView()
    }


    private fun showAutocompleteGeocodeBottomSheet(lat: Double, long: Double, search: String) {
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance(lat, long, search, isLogisticLabel)
        autocompleteGeocodeBottomSheetFragment.setActionListener(this)
        fragmentManager?.run {
            autocompleteGeocodeBottomSheetFragment.show(this, "")
        }
        isShowingAutocomplete = false
    }

    override fun onGetPlaceId(placeId: String) {
        presenter.getDistrict(placeId)
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        if (getDistrictDataUiModel.errorCode == CIRCUIT_BREAKER_ON_CODE) {
            goToAddEditActivity(isMismatch = true, isMismatchSolved = false, isUnnamedRoad = false, isZipCodeNull = false, isFullFlow = isFullFlow, isLogisticLabel = isLogisticLabel, isCircuitBreaker = true)
        } else {
            if (!isFullFlow) {
                if (getDistrictDataUiModel.postalCode.isEmpty() || getDistrictDataUiModel.districtId == 0) {
                    currentLat = getDistrictDataUiModel.latitude.toDouble()
                    currentLong = getDistrictDataUiModel.longitude.toDouble()
                    moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
                    showNotFoundLocation()
                } else {
                    doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel)
                }
            } else doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel)
        }
    }

    private fun doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        showDistrictBottomSheet()

        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        isGetDistrict = true
        moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)

        continueWithLocation = true
        val savedModel = saveAddressMapper.map(getDistrictDataUiModel, zipCodes)
        presenter.setAddress(savedModel)
        with(getDistrictDataUiModel.errMessage) {
            if (this != null && this.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE)) {
                showLocationNotFoundCTA()
            } else updateGetDistrictBottomSheet(savedModel)
        }
    }

    override fun onSuccessAutofill(autofillDataUiModel: Data) {
        if (!isFullFlow) {
            if (autofillDataUiModel.postalCode.isEmpty() || autofillDataUiModel.districtId == 0) {
                showNotFoundLocation()
            } else {
                doAfterSuccessAutofill(autofillDataUiModel)
            }
        } else doAfterSuccessAutofill(autofillDataUiModel)
    }

    private fun showNotFoundLocation() {
        showInvalidBottomSheet()
        binding?.bottomSheetGetDistrict?.run {
            tvAddressGetdistrict.visibility = View.GONE
            invalidTitle.text = getString(R.string.not_found_location)
            invalidDesc.text = getString(R.string.not_found_location_desc)
            invalidImg.loadRemoteImageDrawable(ADDRESS_KONSLET)
            invalidButton.visibility = View.GONE
            invalidIcSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }
        }
    }

    private fun doAfterSuccessAutofill(autofillDataUiModel: Data) {
        if (isShowingAutocomplete) {
            handler.postDelayed({
                updateAfterOnSuccessAutofill(autofillDataUiModel)
            }, 2000)
        } else {
            updateAfterOnSuccessAutofill(autofillDataUiModel)
        }
    }

    private fun updateAfterOnSuccessAutofill(autofillDataUiModel: Data) {
        showDistrictBottomSheet()

        if (this.isPolygon) {
            if (autofillDataUiModel.districtId != districtId) {
                continueWithLocation = false
                view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                AddNewAddressAnalytics.eventViewToasterAlamatTidakSesuaiDenganPeta(isFullFlow, isLogisticLabel)
            } else {
                continueWithLocation = true
                val saveAddress = saveAddressMapper.map(autofillDataUiModel, zipCodes)
                presenter.setAddress(saveAddress)
                updateGetDistrictBottomSheet(saveAddress)
            }
        } else {
            continueWithLocation = true
            val saveAddress = saveAddressMapper.map(autofillDataUiModel, zipCodes)
            presenter.setAddress(saveAddress)
            updateGetDistrictBottomSheet(saveAddress)
        }

    }

    override fun showOutOfReachDialog() {
        showInvalidBottomSheet()

        binding?.bottomSheetGetDistrict?.run {
            invalidTitle.text = getString(R.string.out_of_indonesia_title)
            invalidDesc.text = getString(R.string.out_of_indonesia_desc)
            invalidImg.loadRemoteImageDrawable(ADDRESS_OUT_OF_INDONESIA)
            invalidButton.visibility = View.GONE
            invalidIcSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }
        }
    }

    override fun showUndetectedDialog() {
        showInvalidBottomSheet()

        binding?.bottomSheetGetDistrict?.run {
            invalidTitle.text = getString(R.string.undetected_title)
            invalidDesc.text = getString(R.string.undetected_desc)
            invalidImg.loadRemoteImageDrawable(ADDRESS_KONSLET)
            invalidButton.visibility = View.GONE
            invalidIcSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }
        }
    }

    override fun showLocationNotFoundCTA() {
        showInvalidBottomSheet()

        binding?.bottomSheetGetDistrict?.run {
            invalidTitle.text = getString(R.string.invalid_title)
            invalidDesc.text = getString(R.string.invalid_desc)
            invalidImg.loadRemoteImageDrawable(ADDRESS_INVALID)
            invalidButton.visibility = View.VISIBLE
            invalidButton.setOnClickListener {
                goToAddEditActivity(isMismatch = true, isMismatchSolved = false,
                        isUnnamedRoad = false, isZipCodeNull = false,
                        isLogisticLabel = isLogisticLabel, isFullFlow = isFullFlow,
                        isCircuitBreaker = false)
            }
            invalidIcSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }
        }
    }

    override fun goToAddNewAddressNegative() {
        goToAddEditActivity(isMismatch = true, isMismatchSolved = false, isUnnamedRoad = false, isZipCodeNull = false, isFullFlow = isFullFlow, isLogisticLabel = isLogisticLabel, isCircuitBreaker = true)
    }

    private fun showDialogForUnnamedRoad() {
        showInvalidBottomSheet()

        binding?.bottomSheetGetDistrict?.run {
            invalidTitle.text = getString(R.string.invalid_title)
            invalidDesc.text = getString(R.string.invalid_desc)
            invalidImg.loadRemoteImageDrawable(ADDRESS_INVALID)
            invalidButton.visibility = View.VISIBLE
            invalidButton.setOnClickListener {
                AddNewAddressAnalytics.eventClickButtonUnnamedRoad(isFullFlow, isLogisticLabel)
                goToAddEditActivity(isMismatch = true, isMismatchSolved = false,
                        isUnnamedRoad = true, isZipCodeNull = false, isFullFlow = isFullFlow,
                        isLogisticLabel = isLogisticLabel, isCircuitBreaker = false)
            }
            invalidIcSearchBtn.setOnClickListener {
                showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
            }
        }

        AddNewAddressAnalytics.eventViewErrorAlamatTidakValid(isFullFlow, isLogisticLabel)
    }

    private fun updateGetDistrictBottomSheet(saveAddressDataModel: SaveAddressDataModel) {
        this.saveAddressDataModel = saveAddressDataModel
        if (isFullFlow) {
            if (saveAddressDataModel.title.equals(UNNAMED_ROAD, true)) {
                showDialogForUnnamedRoad()
            } else {
                setDefaultResultGetDistrict(saveAddressDataModel)
            }
        } else {
            setDefaultResultGetDistrict(saveAddressDataModel)
        }
    }

    private fun setDefaultResultGetDistrict(saveAddressDataModel: SaveAddressDataModel) {
        showDistrictBottomSheet()

        binding?.bottomSheetGetDistrict?.run {
            icSearchBtn.setOnClickListener {
                AddNewAddressAnalytics.eventClickMagnifier(isFullFlow, isLogisticLabel)
                showAutoCompleteBottomSheet("")
            }

            etDetailAddress.apply {
                setOnClickListener {
                    AddNewAddressAnalytics.eventClickFieldDetailAlamat(isFullFlow, isLogisticLabel)
                }
                setupClearButtonWithAction()
                addTextChangedListener(setDetailAlamatWatcher())
            }

            tvTitleGetdistrict.apply {
                text = saveAddressDataModel.title
                setOnClickListener {
                    AddNewAddressAnalytics.eventClickFieldCariLokasi(isFullFlow, isLogisticLabel)
                    showAutoCompleteBottomSheet(saveAddressDataModel.title)
                }
            }

            tvAddressGetdistrict.apply {
                visibility = View.VISIBLE
                text = saveAddressDataModel.formattedAddress
                setOnClickListener {
                    AddNewAddressAnalytics.eventClickFieldCariLokasi(isFullFlow, isLogisticLabel)
                    showAutoCompleteBottomSheet(saveAddressDataModel.title)
                }
            }
            btnChooseLocation.setOnClickListener {
                continueWithLocation?.let {
                    if (it) {
                        if (isFullFlow) {
                            doLoadAddEdit()
                            AddNewAddressAnalytics.eventClickButtonPilihLokasi(isFullFlow, isLogisticLabel)
                        } else {
                            setResultPinpoint()
                            AddNewAddressAnalytics.eventClickButtonPilihLokasi(isFullFlow, isLogisticLabel)
                        }
                    } else {
                        view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                    }
                }
            }
        }

    }

    private fun setResultPinpoint() {
        saveAddressDataModel?.editDetailAddress = binding?.bottomSheetGetDistrict?.etDetailAddress?.text?.toString() ?: ""
        if (isEditWarehouse && saveAddressDataModel?.districtId != warehouseDataModel?.districtId) {
            view?.let { Toaster.build(it, getString(R.string.toaster_not_avail_shop_loc), Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
        } else {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ADDRESS_MODEL, saveAddressDataModel)
                })
                finish()
            }
        }
    }

    private fun showDistrictBottomSheet() {
        binding?.bottomSheetGetDistrict?.run {
            invalidContainer.visibility = View.GONE
            wholeLoadingContainer.visibility = View.GONE
            getdistrictContainer.visibility = View.VISIBLE
        }
    }

    private fun showInvalidBottomSheet() {
        binding?.bottomSheetGetDistrict?.run {
            invalidContainer.visibility = View.VISIBLE
            wholeLoadingContainer.visibility = View.GONE
            getdistrictContainer.visibility = View.GONE
        }
    }


    private fun showAutoCompleteBottomSheet(searchStr: String) {
        showAutocompleteGeocodeBottomSheet(currentLat, currentLong, searchStr)
    }

    private fun doLoadAddEdit() {
        saveAddressDataModel?.editDetailAddress = binding?.bottomSheetGetDistrict?.etDetailAddress?.text?.toString() ?: ""
        if (this.isPolygon) {
            isMismatchSolved = true
        }

        val addressModel = presenter.getSaveAddressDataModel()
        if (addressModel.districtId == 0 && addressModel.postalCode.isEmpty()) {
            showFailedDialog()

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniNotSuccess(isFullFlow, isLogisticLabel)
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeFailed(isFullFlow, isLogisticLabel)
        } else if (addressModel.postalCode.isEmpty()) {
            goToAddEditActivity(true, isMismatchSolved, false, true, isFullFlow, isLogisticLabel, false)
        } else {
            if (isChangesRequested) {
                finishBackToAddEdit(false, isMismatchSolved)
            } else {
                goToAddEditActivity(false, isMismatchSolved, false, false, isFullFlow, isLogisticLabel, false)
            }

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniSuccess(isFullFlow, isLogisticLabel)
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeSuccess(isFullFlow, isLogisticLabel)
        }
    }

    fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            tkpdDialog.dismiss()
            goToAddEditActivity(isMismatch = true, isMismatchSolved = false, isUnnamedRoad = false, isZipCodeNull = false, isFullFlow = isFullFlow, isLogisticLabel = isLogisticLabel, isCircuitBreaker = false)
        }
        tkpdDialog.show()
        AddNewAddressAnalytics.eventViewFailedPinPointNotification(isFullFlow, isLogisticLabel)
    }

    private fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean, isUnnamedRoad: Boolean, isZipCodeNull: Boolean, isFullFlow: Boolean, isLogisticLabel: Boolean, isCircuitBreaker: Boolean) {
        val saveModel = if (isUnnamedRoad) presenter.getUnnamedRoadModelFormat() else presenter.getSaveAddressDataModel()
        Intent(context, AddEditAddressActivity::class.java).apply {
            if (isMismatch && !isMismatchSolved) {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            putExtra(EXTRA_IS_MISMATCH, isMismatch)
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
            putExtra(KERO_TOKEN, token)
            putExtra(EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
            putExtra(EXTRA_IS_UNNAMED_ROAD, isUnnamedRoad)
            putExtra(EXTRA_IS_NULL_ZIPCODE, isZipCodeNull)
            putExtra(EXTRA_IS_FULL_FLOW, isFullFlow)
            putExtra(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
            putExtra(EXTRA_IS_CIRCUIT_BREAKER, isCircuitBreaker)
            startActivityForResult(this, FINISH_FLAG)
        }
    }

    fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_IS_MISMATCH, isMismatch)
                putExtra(EXTRA_SAVE_DATA_UI_MODEL, presenter.getSaveAddressDataModel())
                putExtra(KERO_TOKEN, token)
                putExtra(EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
            })
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FINISH_FLAG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(EXTRA_ADDRESS_NEW)) {
                    val newAddress = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
                    finishActivity(newAddress)
                } else if (data.hasExtra(EXTRA_DETAIL_ADDRESS_LATEST)) {
                    val latestDetailAddress = data.getStringExtra(EXTRA_DETAIL_ADDRESS_LATEST)
                    binding?.bottomSheetGetDistrict?.etDetailAddress?.setText(latestDetailAddress)
                }
            }
        }
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

    override fun showBoundaries(boundaries: List<LatLng>) {
        this.googleMap?.addPolygon(PolygonOptions()
                .addAll(boundaries)
                .fillColor(GREEN_ARGB)
                .strokeWidth(3F))
    }

    override fun getComponent(): AddNewAddressComponent? {
        if (addNewAddressComponent == null) initInjector()
        return addNewAddressComponent
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper?.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    override fun useCurrentLocation() {
        doUseCurrentLocation(isFullFlow)
    }

    private fun EditText.setupClearButtonWithAction() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_close_btn else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    setText("")
                    v.performClick()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun showLocationInfoBottomSheet(isFullFlow: Boolean) {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance(isFullFlow, isLogisticLabel)
        fragmentManager?.run {
            locationInfoBottomSheetFragment.show(this, "")
        }
    }

    private fun setDetailAlamatWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    when (val countCharLeft = 60 - s.toString().length) {
                        60 -> {
                            binding?.bottomSheetGetDistrict?.tvDetailAddressCounter?.text = "0/60"
                        }
                        else -> {
                            binding?.bottomSheetGetDistrict?.tvDetailAddressCounter?.text = "$countCharLeft/60"
                        }
                    }
                } else {
                    binding?.bottomSheetGetDistrict?.tvDetailAddressCounter?.text = "60/60"
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        activity?.let {
            permissionCheckerHelper?.checkPermissions(it, getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            fusedLocationClient?.lastLocation?.addOnFailureListener { showAutoComplete(DEFAULT_LAT, DEFAULT_LONG) }
                            permissionCheckerHelper?.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper?.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                                if (data != null) {
                                    if (isEditWarehouse && !isReqLocation) moveMap(getLatLng(currentLat, currentLong), ZOOM_LEVEL)
                                    else moveMap(getLatLng(data.latitude, data.longitude), ZOOM_LEVEL)
                                }
                            }
                            googleMap?.isMyLocationEnabled = true
                        }

                    }, it.getString(R.string.rationale_need_location))
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

}