package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
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
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isEdit
import com.tokopedia.logisticaddaddress.common.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistcrictReccomendationRevampBinding
import com.tokopedia.logisticaddaddress.di.districtrecommendation.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.analytics.LogisticAddAddressAnalytics
import com.tokopedia.logisticaddaddress.features.analytics.LogisticEditAddressAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomAdapterRevamp
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.ZipCodeChipsAdapter
import com.tokopedia.logisticaddaddress.features.district_recommendation.uimodel.DiscomSource
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant
import com.tokopedia.logisticaddaddress.utils.AddNewAddressUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject
import com.tokopedia.logisticaddaddress.R as logisticaddaddressR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class DiscomBottomSheetRevamp :
    BottomSheetUnify(),
    ZipCodeChipsAdapter.ActionListener,
    PopularCityAdapter.ActionListener,
    DiscomAdapterRevamp.ActionListener {

    companion object {

        fun show(
            fm: FragmentManager?,
            listener: DiscomRevampListener,
            source: DiscomSource,
            isGmsAvailable: Boolean
        ): DiscomBottomSheetRevamp {
            val bottomSheet = DiscomBottomSheetRevamp()
            bottomSheet.source = source
            bottomSheet.isGmsAvailable = isGmsAvailable
            bottomSheet.discomRevampListener = listener
            fm?.run { bottomSheet.show(this) }
            return bottomSheet
        }

        private const val SUCCESS = "success"
        private const val NOT_SUCCESS = "not success"

        private const val MIN_TEXT_LENGTH = 4
        private const val DELAY_MILIS: Long = 200
        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
    }
    interface DiscomRevampListener {
        fun onGetDistrict(districtAddress: Address)
        fun onChooseZipcode(districtAddress: Address, zipCode: String, isPinpoint: Boolean)
    }

    private val viewModel: DiscomViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[DiscomViewModel::class.java]
    }

    private var viewBinding by autoClearedNullable<BottomsheetDistcrictReccomendationRevampBinding>()

    private val zipCodeChipsAdapter by lazy { ZipCodeChipsAdapter(this) }
    private val popularCityAdapter by lazy { PopularCityAdapter(this) }
    private val listDistrictAdapter by lazy { DiscomAdapterRevamp(this) }
    private var discomRevampListener: DiscomRevampListener? = null
    private var analytics: CheckoutAnalyticsChangeAddress = CheckoutAnalyticsChangeAddress()
    private var chipsLayoutManagerZipCode: ChipsLayoutManager? = null

    private var source: DiscomSource? = null
    private var isGmsAvailable: Boolean = true
    private var input: String = ""
    private var mIsInitialLoading: Boolean = false
    private var isKodePosShown: Boolean = false
    private var postalCode: String = ""
    private var districtAddressData: Address? = null
    private var page: Int = 1
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val searchHandler = Handler()
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            viewModel.loadData(input, page + 1)
        }
    }

    private var fm: FragmentManager? = null

    // get user current loc
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                stopLocationUpdate()
                viewModel.reverseGeoCode(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
            }
        }
    private val gpsResultResolutionContract = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (allPermissionsGranted()) {
                getLocation()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        isDragable = false
        isHideable = false
        showCloseIcon = true
        isFullpage = true
        isKeyboardOverlap = false
        clearContentPadding = true
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        initObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    onBackListener()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionCheckerHelper?.onRequestPermissionsResult(
            context,
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onCityChipClicked(city: String) {
        setupAnalyticOnCityChipClicked()
        viewBinding?.searchPageInput?.run {
            searchBarTextField.setText(city)
            searchBarTextField.setSelection(city.length)
        }
    }

    override fun onDistrictItemRevampClicked(districtModel: Address) {
        setAnalyticOnDistrictItemClicked(districtModel.districtName)
        onDistrictChosen(districtModel)
    }

    override fun onZipCodeClicked(zipCode: String) {
        setupOnClickZipCode()
        viewBinding?.rvKodeposChips?.visibility = View.GONE
        viewBinding?.etKodepos?.textFieldInput?.run {
            setText(zipCode)
        }
    }

    private fun onBackListener() {
        if (isKodePosShown) {
            setupAnalyticOnBackPressedKodePos()
            setTitle(getString(logisticaddaddressR.string.kota_kecamatan))
            hideZipCode()
            setViewListener()
        } else {
            setupAnalyticOnBackPressedDistrict()
            dismiss()
            activity?.finish()
        }
    }

    private fun initObserver() {
        viewModel.autoFill.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setResultDistrict(it.data.data, it.data.lat, it.data.long)
                }

                is Fail -> {
                    showToasterError(it.throwable.message.orEmpty())
                }
            }
        }

        viewModel.districtRecommendation.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.addresses.isNotEmpty()) {
                        renderData(it.data.addresses, it.data.isNextAvailable)
                    } else {
                        showEmpty()
                    }
                }

                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            setLoadingState(it)
        }
    }

    private fun initInjector() {
        DaggerDistrictRecommendationComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initLayout() {
        viewBinding = BottomsheetDistcrictReccomendationRevampBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        setupDiscomBottomsheet(viewBinding)
        setChild(viewBinding?.root)
        setTitle(getString(logisticaddaddressR.string.kota_kecamatan))
        setCloseClickListener {
            onBackListener()
        }
        setOnDismissListener {
            dismiss()
        }
    }

    private fun setupDiscomBottomsheet(viewBinding: BottomsheetDistcrictReccomendationRevampBinding?) {
        context?.let {
            val cityList = it.resources.getStringArray(logisticaddaddressR.array.cityList)
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(viewBinding?.root?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

            chipsLayoutManagerZipCode = ChipsLayoutManager.newBuilder(viewBinding?.root?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

            viewBinding?.rvChips?.let {
                ViewCompat.setLayoutDirection(
                    it,
                    ViewCompat.LAYOUT_DIRECTION_LTR
                )
            }

            popularCityAdapter.cityList = cityList.toMutableList()

            viewBinding?.run {
                rvListDistrict.visibility = View.GONE
                llZipCode.visibility = View.GONE
                btnChooseZipcode.visibility = View.GONE

                rvChips.apply {
                    val dist =
                        context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.unify_space_8)
                    layoutManager = chipsLayoutManager
                    adapter = popularCityAdapter
                    addItemDecoration(ChipsItemDecoration(dist))
                }

                rvListDistrict.apply {
                    layoutManager = mLayoutManager
                    adapter = listDistrictAdapter
                }

                showCurrentLocLayout()
                setCurrentLocationProvider()
            }
        }
    }

    private fun showCurrentLocLayout() {
        viewBinding?.run {
            if (isGmsAvailable) {
                source?.let {
                    when (it) {
                        is DiscomSource.LCA -> {
                            layoutUseCurrentLoc.visibility = View.VISIBLE
                            dividerUseCurrentLocation.visibility = View.VISIBLE
                        }

                        is DiscomSource.UserAddress -> {
                            if (it.state.isEdit()) {
                                layoutUseCurrentLoc.visibility = View.VISIBLE
                                dividerUseCurrentLocation.visibility = View.VISIBLE
                            } else {
                                layoutUseCurrentLoc.visibility = View.GONE
                                dividerUseCurrentLocation.visibility = View.GONE
                            }
                        }

                        is DiscomSource.ShopAddress -> {
                            layoutUseCurrentLoc.visibility = View.GONE
                            dividerUseCurrentLocation.visibility = View.GONE
                        }
                    }
                }
            } else {
                layoutUseCurrentLoc.visibility = View.GONE
                dividerUseCurrentLocation.visibility = View.GONE
            }
        }
    }

    private fun setViewListener() {
        viewBinding?.searchPageInput?.searchBarTextField?.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setupAnalyticOnClickFieldCariKotaKecamatan()
                }
            }
            setOnClickListener {
                setupAnalyticOnClickFieldCariKotaKecamatan()
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
                    if (viewBinding?.searchPageInput?.searchBarTextField?.text.toString()
                        .isEmpty()
                    ) {
                        viewBinding?.tvDescInputDistrict?.visibility = View.GONE
                        viewBinding?.emptyStateDistrict?.visibility = View.GONE
                        viewBinding?.llPopularCity?.visibility = View.VISIBLE
                        viewBinding?.rvListDistrict?.visibility = View.GONE
                        showCurrentLocLayout()
                        popularCityAdapter.notifyDataSetChanged()
                    } else {
                        input = viewBinding?.searchPageInput?.searchBarTextField?.text.toString()
                        mIsInitialLoading = true
                        searchHandler.postDelayed({
                            viewModel.loadData(input, page)
                        }, DELAY_MILIS)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // no-op
                }
            })
        }

        viewBinding?.searchPageInput?.clearListener = {
            source?.takeIf { it is DiscomSource.LCA }?.run {
                analytics.eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress()
            }
        }

        viewBinding?.searchPageInput?.searchBarPlaceholder =
            getString(logisticaddaddressR.string.hint_district_recommendation_search)

        viewBinding?.rvListDistrict?.addOnScrollListener(mEndlessListener)

        viewBinding?.btnChooseZipcode?.setOnClickListener {
            if (viewBinding?.etKodepos?.textFieldInput?.text.toString().length < MIN_TEXT_LENGTH) {
                setupAnalyticOnErrorPilihKodePos()
                Toaster.build(
                    it,
                    getString(logisticaddaddressR.string.postal_code_field_error),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR
                ).show()
            } else {
                setupAnalyticOnPilihKodePos()
                viewBinding?.etKodepos?.textFieldInput?.text?.let { input ->
                    this.postalCode = input.toString()
                }
                districtAddressData?.let { data ->
                    discomRevampListener?.onChooseZipcode(
                        data,
                        postalCode,
                        (source as? DiscomSource.UserAddress)?.isPinpoint ?: false
                    )
                }
                dismiss()
            }
        }
        viewBinding?.layoutUseCurrentLoc?.setOnClickListener {
            setupAnalyticOnClickUseCurrentLocation()
            requestPermissionLocation()
        }
    }

    private fun renderData(list: List<Address>, hasNextPage: Boolean) {
        viewBinding?.run {
            llPopularCity.visibility = View.GONE
            rvListDistrict.visibility = View.VISIBLE
            tvDescInputDistrict.visibility = View.VISIBLE
            emptyStateDistrict.visibility = View.GONE
            layoutUseCurrentLoc.visibility = View.GONE
            dividerUseCurrentLocation.visibility = View.GONE
            tvDescInputDistrict.setText(logisticaddaddressR.string.hint_advice_search_address)

            if (mIsInitialLoading) {
                listDistrictAdapter.setData(
                    list,
                    viewBinding?.searchPageInput?.searchBarTextField?.text.toString()
                )
                mEndlessListener.resetState()
                mIsInitialLoading = false
            } else {
                listDistrictAdapter.appendData(
                    list,
                    viewBinding?.searchPageInput?.searchBarTextField?.text.toString()
                )
                mEndlessListener.updateStateAfterGetData()
            }
            mEndlessListener.setHasNextPage(hasNextPage)
        }
    }

    private fun showGetListError(throwable: Throwable) {
        val msg = ErrorHandler.getErrorMessage(context, throwable)
        viewBinding?.root?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun setLoadingState(active: Boolean) {
        viewBinding?.run {
            if (active) {
                loadingDiscom.visible()
            } else {
                loadingDiscom.gone()
            }
        }
    }

    private fun showEmpty() {
        viewBinding?.run {
            tvDescInputDistrict.visibility = View.GONE
            showCurrentLocLayout()
            emptyStateDistrict.let {
                it.visibility = View.VISIBLE
                it.setImageUrl(AddAddressConstant.LOCATION_NOT_FOUND)
            }
            llPopularCity.visibility = View.GONE
            rvListDistrict.visibility = View.GONE
        }
    }

    private fun setResultDistrict(data: Data, lat: Double, long: Double) {
        val districtModel = Address()
        districtModel.districtId = data.districtId
        districtModel.districtName = data.districtName
        districtModel.cityId = data.cityId
        districtModel.cityName = data.cityName
        districtModel.provinceId = data.provinceId
        districtModel.provinceName = data.provinceName
        districtModel.zipCodes = arrayListOf(data.postalCode)
        districtModel.lat = lat
        districtModel.long = long
        onDistrictChosen(districtModel)
    }

    private fun onDistrictChosen(districtModel: Address) {
        context?.let {
            if (source is DiscomSource.UserAddress) {
                setTitle(getString(logisticaddaddressR.string.title_post_code))
                isKodePosShown = true
                setupRvZipCodeChips()
                getDistrict(districtModel)
            } else {
                discomRevampListener?.onGetDistrict(districtModel)
            }
        }
    }

    private fun showToasterError(message: String) {
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

    private fun getDistrict(data: Address) {
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
                        setupAnalyticOnClickKodePosTextField()
                        openSoftKeyboard()
                        showZipCodes(data)
                    }
                }
                setOnClickListener {
                    setupAnalyticOnClickKodePosTextField()
                    openSoftKeyboard()
                    showZipCodes(data)
                }
            }
        }
    }

    // zip code section

    private fun setupRvZipCodeChips() {
        viewBinding?.rvKodeposChips?.apply {
            visibility = View.GONE
            layoutManager = chipsLayoutManagerZipCode
            adapter = zipCodeChipsAdapter
            val dist =
                context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.unify_space_8)
            addItemDecoration(ChipsItemDecoration(dist))
        }
    }

    private fun openSoftKeyboard() {
        viewBinding?.etKodepos?.textFieldInput.let {
            (it?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                it,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    private fun showZipCodes(data: Address) {
        viewBinding?.rvKodeposChips?.let {
            ViewCompat.setLayoutDirection(
                it,
                ViewCompat.LAYOUT_DIRECTION_LTR
            )
        }
        data.zipCodes.let {
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
            showCurrentLocLayout()
            rvListDistrict.visibility = View.VISIBLE
            llPopularCity.visibility = View.GONE
            isKodePosShown = false
        }
    }

    // current location section

    private fun requestPermissionLocation() {
        permissionCheckerHelper?.checkPermissions(
            this,
            getPermissions(),
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    source?.takeIf { it is DiscomSource.LCA }
                        ?.run {
                            ChooseAddressTracking.onClickDontAllowLocationKotaKecamatan(
                                userSession.userId
                            )
                        }
                    if (!AddNewAddressUtils.isGpsEnabled(requireActivity())) {
                        showDialogAskGps()
                    }
                }

                override fun onNeverAskAgain(permissionText: String) {
                    // no op
                }

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    source?.takeIf { it is DiscomSource.LCA }
                        ?.run { ChooseAddressTracking.onClickAllowLocationKotaKecamatan(userSession.userId) }
                    if (AddNewAddressUtils.isGpsEnabled(requireActivity())) {
                        getLocation()
                    }
                }
            },
            getString(logisticaddaddressR.string.rationale_need_location)
        )
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getPermissions()) {
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
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                source?.takeIf { it is DiscomSource.LCA }
                    ?.run { ChooseAddressTracking.onClickAllowLocationKotaKecamatan(userSession.userId) }
                viewModel.reverseGeoCode(data.latitude, data.longitude)
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

    private fun showDialogAskGps() {
        context?.let {
            val dialog =
                DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(getString(logisticaddaddressR.string.txt_location_not_detected))
                    setDescription(getString(logisticaddaddressR.string.discom_on_deny_location_subtitle))
                    setPrimaryCTAText(getString(logisticaddaddressR.string.btn_ok))
                    setCancelable(true)
                    setPrimaryCTAClickListener {
                        dismiss()
                        turnGPSOn(it)
                    }
                    setSecondaryCTAText(getString(logisticaddaddressR.string.tv_discom_dialog_secondary))
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

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun setCurrentLocationProvider() {
        context?.let {
            fusedLocationClient = FusedLocationProviderClient(it)
            permissionCheckerHelper = PermissionCheckerHelper()
        }
    }

    // analytic sections
    private fun setupAnalyticOnBackPressedKodePos() {
        source?.let {
            if (it is DiscomSource.UserAddress) {
                when (it.state) {
                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickBackArrowKodePos(userSession.userId)
                    }

                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickBackArrowKodePos(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }

    private fun setupAnalyticOnBackPressedDistrict() {
        source?.let {
            when (it) {
                is DiscomSource.UserAddress -> {
                    when (it.state) {
                        AddressUiState.AddAddress -> {
                            LogisticAddAddressAnalytics.onClickBackArrowDiscom(userSession.userId)
                        }

                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickBackArrowDiscom(userSession.userId)
                        }

                        else -> {
                            // no op
                        }
                    }
                }

                is DiscomSource.LCA -> {
                    ChooseAddressTracking.onClickCloseKotaKecamatan(userSession.userId)
                }

                else -> {
                    analytics.eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress()
                }
            }
        }
    }

    private fun setupAnalyticOnClickFieldCariKotaKecamatan() {
        source?.let {
            when (it) {
                is DiscomSource.UserAddress -> {
                    when (it.state) {
                        AddressUiState.AddAddress -> {
                            LogisticAddAddressAnalytics.onClickFieldCariKotaKecamatanNegative(
                                userSession.userId
                            )
                        }

                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickFieldCariKotaKecamatan(userSession.userId)
                        }

                        else -> {
                            // no op
                        }
                    }
                }

                is DiscomSource.LCA -> {
                    ChooseAddressTracking.onClickFieldSearchKotaKecamatan(userSession.userId)
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun setupAnalyticOnPilihKodePos() {
        source?.let {
            if (it is DiscomSource.UserAddress) {
                when (it.state) {
                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickPilihKodePos(userSession.userId, SUCCESS)
                    }

                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickPilihKodePos(userSession.userId, true)
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }

    private fun setupAnalyticOnErrorPilihKodePos() {
        source?.let {
            if (it is DiscomSource.UserAddress) {
                when (it.state) {
                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onViewErrorToasterPilih(userSession.userId)
                        LogisticAddAddressAnalytics.onClickPilihKodePos(
                            userSession.userId,
                            NOT_SUCCESS
                        )
                    }

                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onViewErrorToaster(userSession.userId)
                        LogisticEditAddressAnalytics.onClickPilihKodePos(userSession.userId, false)
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }

    private fun setupAnalyticOnClickUseCurrentLocation() {
        source?.let {
            when (it) {
                is DiscomSource.UserAddress -> {
                    LogisticEditAddressAnalytics.onClickGunakanLokasiIni(userSession.userId)
                }

                is DiscomSource.LCA -> {
                    ChooseAddressTracking.onClickGunakanLokasiIni(userSession.userId)
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun setupOnClickZipCode() {
        source?.let {
            if (it is DiscomSource.UserAddress) {
                when (it.state) {
                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickChipsKodePosNegative(userSession.userId)
                    }

                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickChipsKodePos(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }

    private fun setupAnalyticOnCityChipClicked() {
        source?.let {
            when (it) {
                is DiscomSource.UserAddress -> {
                    when (it.state) {
                        AddressUiState.AddAddress -> {
                            LogisticAddAddressAnalytics.onClickChipsKotaKecamatanNegative(
                                userSession.userId
                            )
                        }

                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickChipsKotaKecamatan(userSession.userId)
                        }

                        else -> {
                            // no op
                        }
                    }
                }

                is DiscomSource.LCA -> {
                    ChooseAddressTracking.onClickChipsKotaPopuler(userSession.userId)
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun setAnalyticOnDistrictItemClicked(districtName: String) {
        source?.let {
            when (it) {
                is DiscomSource.UserAddress -> {
                    when (it.state) {
                        AddressUiState.AddAddress -> {
                            LogisticAddAddressAnalytics.onClickDropDownSuggestionKotaNegative(
                                userSession.userId
                            )
                        }

                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickDropDownSuggestionKota(userSession.userId)
                        }

                        else -> {
                            // no op
                        }
                    }
                }

                is DiscomSource.LCA -> {
                    analytics.eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(
                        districtName
                    )
                    ChooseAddressTracking.onClickSuggestionKotaKecamatan(userSession.userId)
                }

                else -> {
                    analytics.eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(
                        districtName
                    )
                }
            }
        }
    }

    private fun setupAnalyticOnClickKodePosTextField() {
        source?.let {
            if (it is DiscomSource.UserAddress) {
                when (it.state) {
                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickFieldKodePosNegative(userSession.userId)
                    }

                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickFieldKodePos(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }
}
