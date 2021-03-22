package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FragmentDistrictRecommendationBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.IS_LOCALIZATION
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictAdapterTypeFactory
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictTypeFactory
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class DiscomFragment : BaseSearchListFragment<Address, DistrictTypeFactory>(), DiscomContract.View,
PopularCityAdapter.ActionListener {

    private var mToken: Token? = null
    private var analytics: ActionListener? = null
    private var popularCityAdapter: PopularCityAdapter? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var isLocalization: Boolean? = null
    private val REQUEST_LOCATION: Int = 288
    private var hasRequestedLocation: Boolean = false

    private var binding by autoCleared<FragmentDistrictRecommendationBinding>()

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var addressMapper: AddressMapper
    @Inject
    lateinit var presenter: DiscomContract.Presenter

    override fun initInjector() {
        val appComponent = getComponent(BaseAppComponent::class.java)
        val districtRecommendationComponent = DaggerDistrictRecommendationComponent.builder()
                .baseAppComponent(appComponent)
                .build()
        districtRecommendationComponent.inject(this)
        presenter.attach(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionListener) {
            analytics = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mToken = it.getParcelable(ARGUMENT_DATA_TOKEN)
            isLocalization = it.getBoolean(IS_LOCALIZATION, false)
        }

        if (isLocalization == true) permissionCheckerHelper = PermissionCheckerHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDistrictRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInitialLoadMessage()
        searchInputView.setSearchHint(getString(R.string.hint_district_recommendation_search))
        searchInputView.setDelayTextChanged(DEBOUNCE_DELAY_IN_MILIS)
        searchInputView.closeImageButton.setOnClickListener { v ->
            searchInputView.searchText = ""
            analytics?.gtmOnClearTextDistrictRecommendationInput()
        }
        binding.swipeRefreshLayout.isEnabled = false

        if (isLocalization == true) {
            binding.rlDiscomCurrentLocation.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    ChooseAddressTracking.onClickGunakanLokasiIni(userSession.userId)
                    requestPermissionLocation()
                }
            }
            binding.discomCurrentLocationDivider.visibility = View.VISIBLE
            binding.llDiscomPopularCity.visibility = View.VISIBLE
            fusedLocationClient = FusedLocationProviderClient(requireActivity())
            searchInputView.searchTextView.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) ChooseAddressTracking.onClickFieldSearchKotaKecamatan(userSession.userId)
                }
                setOnClickListener { ChooseAddressTracking.onClickFieldSearchKotaKecamatan(userSession.userId) }
            }

            val cityList = resources.getStringArray(R.array.cityList)
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            binding.rvDiscomChipsPopularCity.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
            popularCityAdapter = PopularCityAdapter(context, this)
            popularCityAdapter?.cityList = cityList.toMutableList()

            binding.rvDiscomChipsPopularCity.apply {
                val dist = context?.resources?.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
                layoutManager = chipsLayoutManager
                adapter = popularCityAdapter
                dist?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            }
        } else {
            binding.rlDiscomCurrentLocation.visibility = View.GONE
            binding.discomCurrentLocationDivider.visibility = View.GONE
            binding.llDiscomPopularCity.visibility = View.GONE
        }
    }

    override fun getSearchInputViewResourceId(): Int {
        return R.id.search_input_view
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onDetach() {
        analytics = null
        super.onDetach()
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun loadData(page: Int) {
        if (isAdded) {
            if (!TextUtils.isEmpty(searchInputView.searchText) && searchInputView.searchText.length >= MINIMUM_SEARCH_KEYWORD_CHAR) {
                if (mToken != null) {
                    presenter.loadData(searchInputView.searchText, page, mToken!!)
                } else {
                    presenter.loadData(searchInputView.searchText, page)
                }
            } else {
                showInitialLoadMessage()
            }
        }
    }

    override fun getAdapterTypeFactory(): DistrictTypeFactory {
        return DistrictAdapterTypeFactory()
    }

    override fun onItemClicked(address: Address) {
        setBackAddressResult(address)
        if (isLocalization == true) {
            ChooseAddressTracking.onClickSuggestionKotaKecamatan(userSession.userId)
        }
    }

    private fun setBackAddressResult(address: Address) {
        analytics?.gtmOnDistrictDropdownSelectionItemClicked(address.districtName)
        activity?.let {
            val resultIntent = Intent().apply {
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS, addressMapper.convertAddress(address))
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, address.districtId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, address.districtName)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, address.cityId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, address.cityName)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, address.provinceId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, address.provinceName)
                putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, address.zipCodes)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE, "")
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE, "")
            }
            it.setResult(Activity.RESULT_OK, resultIntent)
            it.finish()
        }
    }

    override fun getScreenName(): String? = null

    override fun onSearchSubmitted(text: String) {
        clearAllData()
        loadData(defaultInitialPage)
    }

    override fun onSearchTextChanged(text: String) {
        clearAllData()
        loadData(defaultInitialPage)
    }

    override fun renderData(list: List<Address>, hasNextPage: Boolean) {
        super.renderList(list, hasNextPage)

        setSwipeRefreshSection(true)
        binding.llDiscomPopularCity.visibility = View.GONE

        if (currentPage == defaultInitialPage && hasNextPage) {
            val page = currentPage + defaultInitialPage
            loadData(page)
        }
    }

    override fun setLoadingState(active: Boolean) {
        if (active)
            super.showLoading()
        else
            super.hideLoading()
    }

    override fun showEmpty() {
        binding.tvMessage.text = getString(R.string.message_search_address_no_result)
        setSwipeRefreshSection(false)
    }

    private fun showInitialLoadMessage() {
        setMessageSection(isLocalization != true)
        setSwipeRefreshSection(false)

        if (isLocalization == true) {
            binding.llDiscomPopularCity.visibility = View.VISIBLE
        }
    }

    private fun setMessageSection(active: Boolean) {
        binding.tvMessage.visibility = if (active) View.VISIBLE else View.GONE
    }

    private fun setSwipeRefreshSection(active: Boolean) {
        binding.swipeRefreshLayout.visibility = if (active) View.VISIBLE else View.GONE
    }

    fun requestPermissionLocation() {
            permissionCheckerHelper?.checkPermissions(this, getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            ChooseAddressTracking.onClickDontAllowLocationKotaKecamatan(userSession.userId)
                            hasRequestedLocation = false
                            showDialogAskGps()
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            // no op
                        }

                        @SuppressLint("MissingPermission")
                        override fun onPermissionGranted() {
                            ChooseAddressTracking.onClickAllowLocationKotaKecamatan(userSession.userId)
                            getLocation()
                        }

                    }, getString(R.string.rationale_need_location))
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                if (data != null) {
                    ChooseAddressTracking.onClickAllowLocationKotaKecamatan(userSession.userId)
                    presenter.autoFill(data.latitude, data.longitude)
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
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
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.undetected_location))
            dialog.setDescription(getString(R.string.undetected_location_desc_2))
            dialog.setPrimaryCTAText(getString(R.string.btn_ok))
            dialog.setSecondaryCTAText(getString(R.string.btn_lain_kali))
            dialog.setCancelable(true)
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
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

    interface ActionListener {
        fun gtmOnBackPressClicked()
        fun gtmOnDistrictDropdownSelectionItemClicked(districtName: String)
        fun gtmOnClearTextDistrictRecommendationInput()
    }

    companion object {

        const val ARGUMENT_DATA_TOKEN = "token"
        const val DEBOUNCE_DELAY_IN_MILIS: Long = 700
        const val MINIMUM_SEARCH_KEYWORD_CHAR = 3

        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "city_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "city_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "province_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "province_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "zipcodes"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE = "latitude"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE = "longitude"

        @JvmStatic
        fun newInstance(isLocalization: Boolean): DiscomFragment = DiscomFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_LOCALIZATION, isLocalization)
            }
        }

        @JvmStatic
        fun newInstance(token: Token, isLocalization: Boolean): DiscomFragment = DiscomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA_TOKEN, token)
                putBoolean(IS_LOCALIZATION, isLocalization)
            }
        }

    }

    override fun onCityChipClicked(city: String) {
        ChooseAddressTracking.onClickChipsKotaPopuler(userSession.userId)
        searchInputView.searchText = city
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
        val arrayListZipCodes = arrayListOf<String>()
        arrayListZipCodes.add(data.postalCode)
        analytics?.gtmOnDistrictDropdownSelectionItemClicked(data.districtName)
        activity?.let {
            val resultIntent = Intent().apply {
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS, addressMapper.convertAutofillResponse(data))
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, data.districtId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, data.districtName)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, data.cityId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, "")
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, data.provinceId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, "")
                putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, arrayListZipCodes)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE, lat)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE, long)
            }
            it.setResult(Activity.RESULT_OK, resultIntent)
            it.finish()
        }
    }

    override fun showToasterError() {
        val toaster = Toaster
        view?.let { v ->
            toaster.build(v, getString(R.string.toaster_failed_get_district), Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR, "").show()
        }
    }
}
