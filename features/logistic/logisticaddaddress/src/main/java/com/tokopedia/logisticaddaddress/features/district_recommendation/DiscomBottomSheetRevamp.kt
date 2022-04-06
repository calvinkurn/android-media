package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistcrictReccomendationRevampBinding
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
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

class DiscomBottomSheetRevamp(private var isPinpoint: Boolean = false, private var isEdit: Boolean): BottomSheetUnify(),
    ZipCodeChipsAdapter.ActionListener,
    PopularCityAdapter.ActionListener, DiscomContract.View, DiscomAdapterRevamp.ActionListener{

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
    // for edit revamp get user current loc
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var hasRequestedLocation: Boolean = false
    private var fusedLocationClient: FusedLocationProviderClient? = null

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
        initInjector()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEdit) {
            permissionCheckerHelper = PermissionCheckerHelper()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        setViewListener()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detach()
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
        val cityList = resources.getStringArray(R.array.cityList)
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

            if (isEdit) {
                layoutUseCurrentLoc.visibility = View.VISIBLE
                dividerUseCurrentLocation.visibility = View.VISIBLE
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

            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //no-op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (viewBinding?.searchPageInput?.searchBarTextField?.text.toString().isEmpty()) {
                        viewBinding?.tvDescInputDistrict?.visibility = View.GONE
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
                    //no-op
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
        if (isEdit) {
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
        //no-op
    }

    override fun showEmpty() {
        viewBinding?.run {
            tvDescInputDistrict.visibility = View.VISIBLE
            if (isEdit) {
                layoutUseCurrentLoc.visibility = View.VISIBLE
                dividerUseCurrentLocation.visibility = View.VISIBLE
            }
            tvDescInputDistrict.setText(R.string.hint_search_address_no_result)
            llPopularCity.visibility = View.VISIBLE
            rvListDistrict.visibility = View.GONE
        }
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
        setTitle("Kode Pos")
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
            toaster.build(v, message, Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR, "").show()
        }
    }

    override fun onDistrictItemRevampClicked(districtModel: Address) {
        if (!isEdit) {
            AddNewAddressRevampAnalytics.onClickDropDownSuggestionKotaNegative(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickDropDownSuggestionKota(userSession.userId)
        }
        context?.let {
            setTitle("Kode Pos")
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
            rvListDistrict.visibility = View.GONE
            llPopularCity.visibility = View.GONE
            layoutUseCurrentLoc.visibility = View.GONE
            dividerUseCurrentLocation.visibility = View.GONE

            cardAddress.addressDistrict.text = "${data.districtName}, ${data.cityName}, ${data.provinceName}"
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
            if (isEdit) {
                layoutUseCurrentLoc.visibility = View.VISIBLE
                dividerUseCurrentLocation.visibility = View.VISIBLE
            }
            rvListDistrict.visibility = View.VISIBLE
            llPopularCity.visibility = View.VISIBLE
            isKodePosShown = false
        }
    }

    fun requestPermissionLocation() {
        permissionCheckerHelper?.checkPermissions(this, getPermissions(),
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    hasRequestedLocation = false
                    showDialogAskGps()
                }

                override fun onNeverAskAgain(permissionText: String) {
                    // no op
                }

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    getLocation()
                }

            }, getString(R.string.rationale_need_location))
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                if (data != null) {
                    presenter.autoFill(data.latitude, data.longitude)
                } else {
                    fusedLocationClient?.requestLocationUpdates(
                        AddNewAddressUtils.getLocationRequest(),
                        createLocationCallback(), null)
                }
            }
        } else {
            hasRequestedLocation = false
            showDialogAskGps()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }


    private fun showDialogAskGps() {
        val bottomSheetLocUndefined = BottomSheetUnify()
        val bottomsheetLocationUndefinedBinding = BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
//        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)
        bottomsheetLocationUndefinedBinding.apply {
            imgLocUndefined.setImageUrl(AddAddressConstant.LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
        bottomSheetLocUndefined.apply {
            setCloseClickListener {
                dismiss()
            }
            setChild(bottomsheetLocationUndefinedBinding.root)
            setOnDismissListener {
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined.show(it, "")
        }
    }

    fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    presenter.autoFill(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                    hasRequestedLocation = true
                }
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

    companion object {
        private const val SUCCESS = "success"
        private const val NOT_SUCCESS = "not success"

        private const val MIN_TEXT_LENGTH = 4
        private const val DELAY_MILIS: Long = 200

    }
}