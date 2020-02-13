package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

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
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.Dialog
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressComponent
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.utils.getLatLng
import com.tokopedia.logisticaddaddress.utils.rxPinPoint
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import kotlinx.android.synthetic.main.fragment_pinpoint_map.*
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class PinpointMapFragment : BaseDaggerFragment(), PinpointMapListener, OnMapReadyCallback,
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
    private var continueWithLocation: Boolean? = false
    private var isFullFlow: Boolean = true //

    private var composite = CompositeSubscription()

    @Inject
    lateinit var presenter: PinpointMapPresenter

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
            presenter.setPermissionChecker(permissionCheckerHelper)
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
                }
                permissionCheckerHelper = PermissionCheckerHelper()
            }
        }
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
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true) //
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pinpoint_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareMap(savedInstanceState)
        prepareLayout()
        setViewListener()

        val zoom = googleMap?.cameraPosition?.zoom ?: 0f
        presenter.autofill(currentLat, currentLong, zoom)
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
    }

    private fun prepareLayout() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet_getdistrict)
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.VISIBLE
        if (!isMismatch) et_detail_address?.setText(saveAddressDataModel?.editDetailAddress)
    }

    private fun setViewListener() {
        back_button?.setOnClickListener {
            // hide keyboard
            val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
            (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.rootView?.windowToken, 0)

            map_view?.onPause()
            if(isFullFlow) AddNewAddressAnalytics.eventClickBackArrowOnPinPoint(eventLabel = LOGISTIC_LABEL)
            else AddNewAddressAnalytics.eventClickBackArrowOnPinPoint(eventLabel = NON_LOGISTIC_LABEL)
            activity?.finish()
        }

        ic_search_btn?.setOnClickListener {
            showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
        }

        et_detail_address?.run {
            setOnClickListener {
                getdistrict_container?.findViewById<ButtonCompat>(R.id.btn_choose_location)?.requestFocusFromTouch()
                getdistrict_container?.findViewById<EditText>(R.id.et_detail_address)?.requestFocusFromTouch()
            }

            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
        }

        ic_current_location?.setOnClickListener {
            if(isFullFlow) AddNewAddressAnalytics.eventClickButtonPilihLokasi(eventLabel = LOGISTIC_LABEL)
            else AddNewAddressAnalytics.eventClickButtonPilihLokasi(eventLabel = NON_LOGISTIC_LABEL)
            doUseCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper?.checkPermissions(it, getPermissions(),
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper?.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper?.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                googleMap?.isMyLocationEnabled = true
                            }

                        },
                        it.getString(R.string.rationale_need_location))
            } else {
                googleMap?.isMyLocationEnabled = true
            }
        }
        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(getLatLng(currentLat, currentLong))

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
            }).toCompositeSubs()
        }
    }

    override fun showLoading() {
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.VISIBLE
    }

    private fun getAutofill() {
        if (!isGetDistrict) {
            val target: LatLng? = this.googleMap?.cameraPosition?.target
            val zoomLevel = this.googleMap?.cameraPosition?.zoom ?: 0f
            val latTarget = target?.latitude ?: 0.0
            val longTarget = target?.longitude ?: 0.0

            presenter.clearCacheAutofill()
            presenter.autofill(latTarget, longTarget, zoomLevel)
        } else {
            whole_loading_container?.visibility = View.GONE
            invalid_container?.visibility = View.GONE
            getdistrict_container?.visibility = View.VISIBLE
        }
        isGetDistrict = false
    }

    override fun showAutoComplete(lat: Double, long: Double) {
        if (isShowingAutocomplete) {
            handler.postDelayed({
                showAutocompleteGeocodeBottomSheet(lat, long, "")
            }, 500)
        }

        var zoomLevel = 16f
        if (lat == 0.0 && long == 0.0) {
            currentLat = DEFAULT_LAT
            currentLong = DEFAULT_LONG
        } else {
            currentLat = lat
            currentLong = long
        }
        moveMap(getLatLng(currentLat, currentLong), zoomLevel)
    }

    private fun moveMap(latLng: LatLng, zoomLevel: Float = 16f) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
        if ((currentLat == 0.0 && currentLong == 0.0) || currentLat == DEFAULT_LAT && currentLong == DEFAULT_LONG) {
            presenter.requestLocation(requireActivity())
        }
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            ic_current_location.setImageResource(R.drawable.ic_gps_enable)
        } else {
            ic_current_location.setImageResource(R.drawable.ic_gps_disable)
        }
    }

    private fun doUseCurrentLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            activity?.let { presenter.requestLocation(it) }
        } else {
            showLocationInfoBottomSheet()
        }
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    private fun showAutocompleteGeocodeBottomSheet(lat: Double, long: Double, search: String) {
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance(lat, long, search)
        autocompleteGeocodeBottomSheetFragment.setActionListener(this)
        fragmentManager?.run {
            autocompleteGeocodeBottomSheetFragment.show(this, "")
        }
        isShowingAutocomplete = false
    }

    override fun onGetPlaceId(placeId: String) {

        presenter.clearCacheGetDistrict()
        presenter.getDistrict(placeId)
    }
    //start from here
    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        if (!isFullFlow) {
            if (getDistrictDataUiModel.postalCode.isEmpty() || getDistrictDataUiModel.districtId == 0) {
                currentLat = getDistrictDataUiModel.latitude.toDouble()
                currentLong = getDistrictDataUiModel.longitude.toDouble()
                moveMap(getLatLng(currentLat, currentLong))
                showNotFoundLocation("")
            } else {
                doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel)
            }
            //this is normal flow
        } else doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel)
    }

    private fun doAfterSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        invalid_container?.visibility = View.GONE

        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        isGetDistrict = true
        moveMap(getLatLng(currentLat, currentLong))

        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.VISIBLE
        continueWithLocation = true
        updateGetDistrictBottomSheet(presenter.convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel, zipCodes))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel, errMsg: String) {
        if (!isFullFlow) {
            if (errMsg.isNotEmpty() || autofillDataUiModel.postalCode.isEmpty() || autofillDataUiModel.districtId == 0) {
                showNotFoundLocation(errMsg)
            } else {
                doAfterSuccessAutofill(autofillDataUiModel)
            }
        } else doAfterSuccessAutofill(autofillDataUiModel)
    }

    private fun showNotFoundLocation(errMsg: String) {
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.VISIBLE

        var errorMessage = errMsg
        if (errorMessage.isEmpty()) errorMessage = getString(R.string.not_found_location)
        invalid_title?.text = errorMessage
        invalid_desc?.visibility = View.GONE
        invalid_img?.setImageResource(R.drawable.tokopedia_konslet)
        invalid_button?.visibility = View.GONE

        invalid_ic_search_btn?.setOnClickListener {
            showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
        }
    }

    private fun doAfterSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        if (isShowingAutocomplete) {
            handler.postDelayed({
                updateAfterOnSuccessAutofill(autofillDataUiModel)
            }, 2000)
        } else {
            updateAfterOnSuccessAutofill(autofillDataUiModel)
        }
    }

    private fun updateAfterOnSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.VISIBLE

        if (this.isPolygon) {
            if (autofillDataUiModel.districtId != districtId) {
                continueWithLocation = false
                view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                if (isFullFlow) AddNewAddressAnalytics.eventViewToasterAlamatTidakSesuaiDenganPeta(eventLabel = LOGISTIC_LABEL)
                else AddNewAddressAnalytics.eventViewToasterAlamatTidakSesuaiDenganPeta(eventLabel = NON_LOGISTIC_LABEL)
            } else {
                continueWithLocation = true
                updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
            }
        } else {
            continueWithLocation = true
            updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
        }

    }

    override fun showOutOfReachDialog() {
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.VISIBLE

        invalid_title?.text = getString(R.string.out_of_indonesia_title)
        invalid_desc?.text = getString(R.string.out_of_indonesia_desc)
        invalid_img?.setImageResource(R.drawable.tokopedia_out_of_indonesia)
        invalid_button?.visibility = View.GONE

        invalid_ic_search_btn?.setOnClickListener {
            showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
        }
    }

    override fun showUndetectedDialog() {
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.VISIBLE

        invalid_title?.text = getString(R.string.undetected_title)
        invalid_desc?.text = getString(R.string.undetected_desc)
        invalid_img?.setImageResource(R.drawable.tokopedia_konslet)
        invalid_button?.visibility = View.GONE

        invalid_ic_search_btn?.setOnClickListener {
            showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
        }
    }

    private fun updateGetDistrictBottomSheet(saveAddressDataModel: SaveAddressDataModel) {
        this.saveAddressDataModel = saveAddressDataModel
        if (isFullFlow) {
            if (saveAddressDataModel.title.equals(UNNAMED_ROAD, true)) {
                whole_loading_container?.visibility = View.GONE
                getdistrict_container?.visibility = View.GONE
                invalid_container?.visibility = View.VISIBLE

                invalid_title?.text = getString(R.string.invalid_title)
                invalid_desc?.text = getString(R.string.invalid_desc)
                invalid_img?.setImageResource(R.drawable.ic_invalid_location)
                invalid_button?.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        if(isFullFlow) AddNewAddressAnalytics.eventClickButtonUnnamedRoad(eventLabel = LOGISTIC_LABEL)
                        else AddNewAddressAnalytics.eventClickButtonUnnamedRoad(eventLabel =  NON_LOGISTIC_LABEL)
                        goToAddEditActivity(isMismatch = true, isMismatchSolved = false, isUnnamedRoad = true, isZipCodeNull = false)
                    }
                }

                invalid_ic_search_btn?.setOnClickListener {
                    showAutocompleteGeocodeBottomSheet(currentLat, currentLong, "")
                }
                if(isFullFlow) AddNewAddressAnalytics.eventViewErrorAlamatTidakValid(eventLabel = LOGISTIC_LABEL)
                else AddNewAddressAnalytics.eventViewErrorAlamatTidakValid(eventLabel = NON_LOGISTIC_LABEL)

            } else {
                setDefaultResultGetDistrict(saveAddressDataModel)
            }
        } else {
            setDefaultResultGetDistrict(saveAddressDataModel)
        }
    }

    private fun setDefaultResultGetDistrict(saveAddressDataModel: SaveAddressDataModel) {
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.VISIBLE

        ic_search_btn?.setOnClickListener {
            if (isFullFlow) AddNewAddressAnalytics.eventClickMagnifier(eventLabel = LOGISTIC_LABEL)
            else AddNewAddressAnalytics.eventClickMagnifier(eventLabel = NON_LOGISTIC_LABEL)
            showAutoCompleteBottomSheet("")
        }

        et_detail_address?.apply {
            setOnClickListener {
             if(isFullFlow) AddNewAddressAnalytics.eventClickFieldDetailAlamat(eventLabel = LOGISTIC_LABEL)
             else AddNewAddressAnalytics.eventClickFieldDetailAlamat(eventLabel = NON_LOGISTIC_LABEL)}
            setupClearButtonWithAction()
            addTextChangedListener(setDetailAlamatWatcher())
        }

        tv_title_getdistrict?.apply {
            text = saveAddressDataModel.title
            setOnClickListener {
                if(isFullFlow) AddNewAddressAnalytics.eventClickFieldCariLokasi(eventLabel = LOGISTIC_LABEL)
                else AddNewAddressAnalytics.eventClickFieldCariLokasi(eventLabel = NON_LOGISTIC_LABEL)
                showAutoCompleteBottomSheet(saveAddressDataModel.title)
            }
        }

        tv_address_getdistrict?.apply {
            text = saveAddressDataModel.formattedAddress
            setOnClickListener {
                if(isFullFlow) AddNewAddressAnalytics.eventClickFieldCariLokasi(eventLabel = LOGISTIC_LABEL)
                else AddNewAddressAnalytics.eventClickFieldCariLokasi(eventLabel = NON_LOGISTIC_LABEL)
                showAutoCompleteBottomSheet(saveAddressDataModel.title)
            }
        }

        btn_choose_location?.setOnClickListener {
            continueWithLocation?.let {
                if (it) {
                    if (isFullFlow) {
                        doLoadAddEdit()
                        AddNewAddressAnalytics.eventClickButtonPilihLokasi(eventLabel = LOGISTIC_LABEL)

                    }
                    else {
                        setResultPinpoint()
                        AddNewAddressAnalytics.eventClickButtonPilihLokasi(eventLabel = NON_LOGISTIC_LABEL)
                    }
                } else {
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                }
            }
        }
    }

    private fun setResultPinpoint() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_MODEL, saveAddressDataModel)
            })
            finish()
    }
    }


    private fun showAutoCompleteBottomSheet(searchStr: String) {
        showAutocompleteGeocodeBottomSheet(currentLat, currentLong, searchStr)
    }

    private fun doLoadAddEdit() {
        saveAddressDataModel?.editDetailAddress = et_detail_address.text.toString()
        if (this.isPolygon) {
            isMismatchSolved = true
        }

        presenter.loadAddEdit(isMismatchSolved, isChangesRequested)
    }

    override fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            tkpdDialog.dismiss()
            goToAddEditActivity(isMismatch = true, isMismatchSolved = false, isUnnamedRoad = false, isZipCodeNull = false)
        }
        tkpdDialog.show()
        if(isFullFlow) AddNewAddressAnalytics.eventViewFailedPinPointNotification(eventLabel = LOGISTIC_LABEL)
        else  AddNewAddressAnalytics.eventViewFailedPinPointNotification(eventLabel = NON_LOGISTIC_LABEL)
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean, isUnnamedRoad: Boolean, isZipCodeNull: Boolean) {
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
            startActivityForResult(this, FINISH_FLAG)
        }
    }

    override fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean) {
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
                    et_detail_address.setText(latestDetailAddress)
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

    override fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel) {
        this.googleMap?.addPolygon(PolygonOptions()
                .addAll(districtBoundaryGeometryUiModel.listCoordinates)
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
        doUseCurrentLocation()
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

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun showLocationInfoBottomSheet() {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance()
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
                    val countCharLeft = 60 - s.toString().length
                    when (countCharLeft) {
                        60 -> {
                            tv_detail_address_counter.text = "0/60"
                        }
                        else -> {
                            tv_detail_address_counter.text = "$countCharLeft/60"
                        }
                    }
                } else {
                    tv_detail_address_counter.text = "60/60"
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
        composite.unsubscribe()
    }

    private fun Subscription.toCompositeSubs() {
        composite.add(this)
    }
}