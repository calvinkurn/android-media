package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistcrictReccomendationRevampBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.ZipCodeChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomAdapterRevamp
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class DiscomBottomSheetRevamp :
    BottomSheetUnify(),
    ZipCodeChipsAdapter.ActionListener,
    PopularCityAdapter.ActionListener,
    DiscomContract.View,
    DiscomAdapterRevamp.ActionListener {

    @Inject
    lateinit var presenter: DiscomContract.Presenter

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding by autoClearedNullable<BottomsheetDistcrictReccomendationRevampBinding>()

    private val zipCodeChipsAdapter by lazy { ZipCodeChipsAdapter(context, this) }
    private val popularCityAdapter by lazy { PopularCityAdapter(context, this) }
    private val listDistrictAdapter by lazy { DiscomAdapterRevamp(this) }
    private var discomRevampListener: DiscomRevampListener? = null
    private lateinit var chipsLayoutManagerZipCode: ChipsLayoutManager
    private var isPinpoint: Boolean = false
    private var isEdit: Boolean = false
    private var isGmsAvailable: Boolean = true
    private var input: String = ""
    private var mIsInitialLoading: Boolean = false
    private var isKodePosShown: Boolean = false
    private var postalCode: String = ""
    private var districtAddressData: Address? = null
    private var staticDimen8dp: Int? = 0
    private var page: Int = 1
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val searchHandler = Handler()
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter.loadData(input, page + 1)
        }
    }

    private var fm: FragmentManager? = null

    // get user current loc
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val gpsResultResolutionContract = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (allPermissionsGranted()) {
                getLocation()
            }
        }
    }

    interface DiscomRevampListener {
        fun onGetDistrict(districtAddress: Address)
        fun onChooseZipcode(districtAddress: Address, zipCode: String, isPinpoint: Boolean)
    }

    init {
        isDragable = false
        isHideable = false
        showCloseIcon = true
        isFullpage = true
        isKeyboardOverlap = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCurrentLocationProvider()
        setViewListener()
    }

    fun setData(isPinpoint: Boolean = false, gmsAvailable: Boolean, isEdit: Boolean) {
        this.isPinpoint = isPinpoint
        this.isGmsAvailable = gmsAvailable
        this.isEdit = isEdit
    }

    private fun setCurrentLocationProvider() {
        context?.let {
            if (isEdit && isGmsAvailable) {
                fusedLocationClient = FusedLocationProviderClient(it)
                permissionCheckerHelper = PermissionCheckerHelper()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (::presenter.isInitialized) {
            presenter.detach()
        }
    }

    private fun initInjector() {
        DaggerDistrictRecommendationComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
        presenter.attach(this)
    }

    private fun initLayout() {
        viewBinding = BottomsheetDistcrictReccomendationRevampBinding.inflate(LayoutInflater.from(context), null, false)
        setupDiscomBottomsheet(viewBinding)
        setChild(viewBinding?.root)
        setTitle(getString(R.string.kota_kecamatan))
        setCloseClickListener {
            if (isKodePosShown) {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickBackArrowKodePos(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickBackArrowKodePos(userSession.userId)
                }
                setTitle(getString(R.string.kota_kecamatan))
                hideZipCode()
                setViewListener()
            } else {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickBackArrowDiscom(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickBackArrowDiscom(userSession.userId)
                }
                dismiss()
            }
        }
        setOnDismissListener {
            dismiss()
        }
    }

    private fun setupDiscomBottomsheet(viewBinding: BottomsheetDistcrictReccomendationRevampBinding?) {
        context?.let {
            val cityList = it.resources.getStringArray(R.array.cityList)
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(viewBinding?.root?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

            chipsLayoutManagerZipCode = ChipsLayoutManager.newBuilder(viewBinding?.root?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

            viewBinding?.rvChips?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }

            popularCityAdapter.cityList = cityList.toMutableList()

            viewBinding?.run {
                rvListDistrict.visibility = View.GONE
                llZipCode.visibility = View.GONE
                btnChooseZipcode.visibility = View.GONE

                rvChips.apply {
                    val dist = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                    layoutManager = chipsLayoutManager
                    adapter = popularCityAdapter
                    addItemDecoration(ChipsItemDecoration(dist))
                }

                rvListDistrict.apply {
                    layoutManager = mLayoutManager
                    adapter = listDistrictAdapter
                }

                if (isEdit && isGmsAvailable) {
                    layoutUseCurrentLoc.visibility = View.VISIBLE
                    dividerUseCurrentLocation.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setViewListener() {
        viewBinding?.searchPageInput?.searchBarTextField?.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCariKotaKecamatanNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCariKotaKecamatan(userSession.userId)
                    }
                }
            }
            setOnClickListener {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickFieldCariKotaKecamatanNegative(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickFieldCariKotaKecamatan(userSession.userId)
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // no-op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (viewBinding?.searchPageInput?.searchBarTextField?.text.toString().isEmpty()) {
                        viewBinding?.tvDescInputDistrict?.visibility = View.GONE
                        viewBinding?.emptyStateDistrict?.visibility = View.GONE
                        viewBinding?.llPopularCity?.visibility = View.VISIBLE
                        viewBinding?.rvListDistrict?.visibility = View.GONE
                        if (isEdit) {
                            viewBinding?.layoutUseCurrentLoc?.visibility = View.VISIBLE
                            viewBinding?.dividerUseCurrentLocation?.visibility = View.VISIBLE
                        }
                        popularCityAdapter.notifyDataSetChanged()
                    } else {
                        input = viewBinding?.searchPageInput?.searchBarTextField?.text.toString()
                        mIsInitialLoading = true
                        searchHandler.postDelayed({
                            presenter.loadData(input, page)
                        }, DELAY_MILIS)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // no-op
                }
            })
        }

        viewBinding?.rvListDistrict?.addOnScrollListener(mEndlessListener)

        viewBinding?.btnChooseZipcode?.setOnClickListener {
            if (viewBinding?.etKodepos?.textFieldInput?.text.toString().length < MIN_TEXT_LENGTH) {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onViewErrorToasterPilih(userSession.userId)
                    AddNewAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, NOT_SUCCESS)
                } else {
                    EditAddressRevampAnalytics.onViewErrorToaster(userSession.userId)
                    EditAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, false)
                }
                Toaster.build(it, getString(R.string.postal_code_field_error), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
            } else {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, SUCCESS)
                } else {
                    EditAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, true)
                }
                viewBinding?.etKodepos?.textFieldInput?.text?.let { input -> this.postalCode = input.toString() }
                districtAddressData?.let { data -> discomRevampListener?.onChooseZipcode(data, postalCode, isPinpoint) }
                dismiss()
            }
        }
        viewBinding?.layoutUseCurrentLoc?.setOnClickListener {
            EditAddressRevampAnalytics.onClickGunakanLokasiIni(userSession.userId)
            requestPermissionLocation()
        }
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    fun setListener(listener: DiscomRevampListener) {
        this.discomRevampListener = listener
    }

    override fun onZipCodeClicked(zipCode: String) {
        if (!isEdit) {
            AddNewAddressRevampAnalytics.onClickChipsKodePosNegative(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickChipsKodePos(userSession.userId)
        }
        viewBinding?.rvKodeposChips?.visibility = View.GONE
        viewBinding?.etKodepos?.textFieldInput?.run {
            setText(zipCode)
        }
    }

    override fun onCityChipClicked(city: String) {
        if (!isEdit) {
            AddNewAddressRevampAnalytics.onClickChipsKotaKecamatanNegative(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickChipsKotaKecamatan(userSession.userId)
        }
        viewBinding?.searchPageInput?.run {
            searchBarTextField.setText(city)
            searchBarTextField.setSelection(city.length)
        }
    }

    override fun renderData(list: List<Address>, hasNextPage: Boolean) {
        viewBinding?.run {
            llPopularCity.visibility = View.GONE
            rvListDistrict.visibility = View.VISIBLE
            tvDescInputDistrict.visibility = View.VISIBLE
            emptyStateDistrict.visibility = View.GONE
            layoutUseCurrentLoc.visibility = View.GONE
            dividerUseCurrentLocation.visibility = View.GONE
            tvDescInputDistrict.setText(R.string.hint_advice_search_address)

            if (mIsInitialLoading) {
                listDistrictAdapter.setData(list, viewBinding?.searchPageInput?.searchBarTextField?.text.toString())
                mEndlessListener.resetState()
                mIsInitialLoading = false
            } else {
                listDistrictAdapter.appendData(list, viewBinding?.searchPageInput?.searchBarTextField?.text.toString())
                mEndlessListener.updateStateAfterGetData()
            }
            mEndlessListener.setHasNextPage(hasNextPage)
        }
    }

    override fun showGetListError(throwable: Throwable) {
        val msg = ErrorHandler.getErrorMessage(context, throwable)
        viewBinding?.root?.let { Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
    }

    override fun setLoadingState(active: Boolean) {
        // no-op
    }

    override fun showEmpty() {
        viewBinding?.run {
            tvDescInputDistrict.visibility = View.GONE
            if (isEdit) {
                layoutUseCurrentLoc.visibility = View.VISIBLE
                dividerUseCurrentLocation.visibility = View.VISIBLE
            }
            dividerUseCurrentLocation.visibility = View.VISIBLE
            emptyStateDistrict.let {
                it.visibility = View.VISIBLE
                it.setImageUrl(AddAddressConstant.LOCATION_NOT_FOUND)
            }
            llPopularCity.visibility = View.GONE
            rvListDistrict.visibility = View.GONE
        }
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
        setTitle(getString(R.string.title_post_code))
        isKodePosShown = true
        val districtModel = Address()
        districtModel.setDistrictId(data.districtId)
        districtModel.setDistrictName(data.districtName)
        districtModel.setCityId(data.cityId)
        districtModel.setCityName(data.cityName)
        districtModel.setProvinceId(data.provinceId)
        districtModel.setProvinceName(data.provinceName)
        districtModel.setZipCodes(arrayListOf(data.postalCode))
        discomRevampListener?.onGetDistrict(districtModel)
        setupRvZipCodeChips()
        getDistrict(districtModel)
    }

    override fun showToasterError(message: String) {
        val toaster = Toaster
        viewBinding?.root?.let { v ->
            toaster.build(
                v,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR,
                ""
            ).show()
        }
    }

    override fun onDistrictItemRevampClicked(districtModel: Address) {
        if (!isEdit) {
            AddNewAddressRevampAnalytics.onClickDropDownSuggestionKotaNegative(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickDropDownSuggestionKota(userSession.userId)
        }
        context?.let {
            setTitle(getString(R.string.title_post_code))
            isKodePosShown = true
            districtModel.run {
                discomRevampListener?.onGetDistrict(districtModel)
                setupRvZipCodeChips()
                getDistrict(districtModel)
            }
        }
    }

    private fun setupRvZipCodeChips() {
        viewBinding?.rvKodeposChips?.apply {
            visibility = View.GONE
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = chipsLayoutManagerZipCode
            adapter = zipCodeChipsAdapter
        }
    }

    fun getDistrict(data: Address) {
        districtAddressData = data
        viewBinding?.run {
            llZipCode.visibility = View.VISIBLE
            btnChooseZipcode.visibility = View.VISIBLE
            searchPageInput.visibility = View.GONE
            tvDescInputDistrict.visibility = View.GONE
            emptyStateDistrict.visibility = View.GONE
            rvListDistrict.visibility = View.GONE
            llPopularCity.visibility = View.GONE
            layoutUseCurrentLoc.visibility = View.GONE
            dividerUseCurrentLocation.visibility = View.GONE

            cardAddressDiscom.setAddressDistrict("${data.districtName}, ${data.cityName}, ${data.provinceName}")
            etKodepos.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        if (!isEdit) {
                            AddNewAddressRevampAnalytics.onClickFieldKodePosNegative(userSession.userId)
                        } else {
                            EditAddressRevampAnalytics.onClickFieldKodePos(userSession.userId)
                        }
                        openSoftKeyboard()
                        showZipCodes(data)
                    }
                }
                setOnClickListener {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldKodePosNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldKodePos(userSession.userId)
                    }
                    openSoftKeyboard()
                    showZipCodes(data)
                }
            }
        }
    }

    private fun openSoftKeyboard() {
        viewBinding?.etKodepos?.textFieldInput.let {
            (it?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showZipCodes(data: Address) {
        viewBinding?.rvKodeposChips?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
        data.zipCodes?.let {
            viewBinding?.rvKodeposChips?.visibility = View.VISIBLE
            zipCodeChipsAdapter.zipCodes = it.toMutableList()
            zipCodeChipsAdapter.notifyDataSetChanged()
        }
    }

    private fun hideZipCode() {
        viewBinding?.run {
            llZipCode.visibility = View.GONE
            btnChooseZipcode.visibility = View.GONE
            searchPageInput.visibility = View.VISIBLE
            tvDescInputDistrict.visibility = View.VISIBLE
            emptyStateDistrict.visibility = View.GONE
            if (isEdit) {
                layoutUseCurrentLoc.visibility = View.VISIBLE
                dividerUseCurrentLocation.visibility = View.VISIBLE
            }
            rvListDistrict.visibility = View.VISIBLE
            llPopularCity.visibility = View.VISIBLE
            isKodePosShown = false
        }
    }

    private fun requestPermissionLocation() {
        permissionCheckerHelper?.checkPermissions(
            this,
            getPermissions(),
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    if (!AddNewAddressUtils.isGpsEnabled(requireActivity())) {
                        showDialogAskGps()
                    }
                }

                override fun onNeverAskAgain(permissionText: String) {
                    // no op
                }

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    if (AddNewAddressUtils.isGpsEnabled(requireActivity())) {
                        getLocation()
                    }
                }
            },
            getString(R.string.rationale_need_location)
        )
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getPermissions()) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                presenter.autoFill(data.latitude, data.longitude)
            } else {
                fusedLocationClient?.requestLocationUpdates(
                    AddNewAddressUtils.getLocationRequest(),
                    locationCallback,
                    null
                )
            }
        }
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    private fun showDialogAskGps() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.txt_location_not_detected))
                setDescription(getString(R.string.discom_on_deny_location_subtitle))
                setPrimaryCTAText(getString(R.string.btn_ok))
                setCancelable(true)
                setPrimaryCTAClickListener {
                    dismiss()
                    turnGPSOn(it)
                }
                setSecondaryCTAText(getString(R.string.tv_discom_dialog_secondary))
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }
            dialog.show()
        }
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
                .addOnSuccessListener(requireActivity()) {
                    //  GPS is already enable, callback GPS status through listener
                    isGpsOn = true
                }
                .addOnFailureListener(
                    requireActivity()
                ) { e ->
                    if (e is ApiException) {
                        when (e.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    if (e is ResolvableApiException) {
                                        val intentSenderRequest =
                                            IntentSenderRequest.Builder(e.resolution.intentSender)
                                                .build()
                                        gpsResultResolutionContract.launch(intentSenderRequest)
                                    }
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
                }
        }
        return isGpsOn
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                stopLocationUpdate()
                presenter.autoFill(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
            }
        }

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    companion object {
        private const val SUCCESS = "success"
        private const val NOT_SUCCESS = "not success"

        private const val MIN_TEXT_LENGTH = 4
        private const val DELAY_MILIS: Long = 200
        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
    }
}
