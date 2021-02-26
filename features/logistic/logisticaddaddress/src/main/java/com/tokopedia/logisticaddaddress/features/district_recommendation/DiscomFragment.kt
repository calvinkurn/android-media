package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictAdapterTypeFactory
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictTypeFactory
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.IS_LOCALIZATION
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class DiscomFragment : BaseSearchListFragment<Address, DistrictTypeFactory>(), DiscomContract.View,
PopularCityAdapter.ActionListener {

    private var mToken: Token? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var tvMessage: TextView? = null
    private var analytics: ActionListener? = null
    private var llDiscomPopularCity: LinearLayout? = null
    private var rvChipsPopularCity: RecyclerView? = null
    private var popularCityAdapter: PopularCityAdapter? = null
    private var rlCurrLocation: RelativeLayout? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var dividerCurrLocation: View? = null
    private var isLocalization: Boolean? = null

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
        val view = inflater.inflate(R.layout.fragment_district_recommendation, container, false)
        tvMessage = view.findViewById(R.id.tv_message)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        llDiscomPopularCity = view.findViewById(R.id.ll_discom_popular_city)
        rvChipsPopularCity = view.findViewById(R.id.rv_discom_chips_popular_city)
        rlCurrLocation = view.findViewById(R.id.rl_discom_current_location)
        dividerCurrLocation = view.findViewById(R.id.discom_current_location_divider)
        return view
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
        swipeRefreshLayout!!.isEnabled = false

        if (isLocalization == true) {
            rlCurrLocation?.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    ChooseAddressTracking.onClickGunakanLokasiIni(userSession.userId)
                    if (AddNewAddressUtils.isGpsEnabled(it.context)) {
                        requestLocation()
                    }
                }
            }
            dividerCurrLocation?.visibility = View.VISIBLE
            llDiscomPopularCity?.visibility = View.VISIBLE
            fusedLocationClient = FusedLocationProviderClient(requireActivity())
            searchInputView.setOnClickListener {
                ChooseAddressTracking.onClickFieldSearchKotaKecamatan(userSession.userId)
            }

            val cityList = resources.getStringArray(R.array.cityList)
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            rvChipsPopularCity?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
            popularCityAdapter = PopularCityAdapter(context, this)
            popularCityAdapter?.cityList = cityList.toMutableList()

            rvChipsPopularCity?.apply {
                val dist = context?.resources?.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
                layoutManager = chipsLayoutManager
                adapter = popularCityAdapter
                dist?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            }
        } else {
            rlCurrLocation?.visibility = View.GONE
            dividerCurrLocation?.visibility = View.GONE
            llDiscomPopularCity?.visibility = View.GONE
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
        setBackAddressResult(address, "", "")
        if (isLocalization == true) {
            ChooseAddressTracking.onClickSuggestionKotaKecamatan(userSession.userId)
        }
    }

    private fun setBackAddressResult(address: Address, latitude: String, longitude: String) {
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
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE, latitude)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE, longitude)
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
        llDiscomPopularCity?.visibility = View.GONE

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
        tvMessage!!.text = getString(R.string.message_search_address_no_result)
        setSwipeRefreshSection(false)
    }

    private fun showInitialLoadMessage() {
        setMessageSection(isLocalization != true)
        setSwipeRefreshSection(false)

        if (isLocalization == true) {
            llDiscomPopularCity?.visibility = View.VISIBLE
        }
    }

    private fun setMessageSection(active: Boolean) {
        tvMessage?.visibility = if (active) View.VISIBLE else View.GONE
    }

    private fun setSwipeRefreshSection(active: Boolean) {
        swipeRefreshLayout?.visibility = if (active) View.VISIBLE else View.GONE
    }

    fun requestLocation() {
        activity?.let {
            permissionCheckerHelper?.checkPermissions(it, getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            ChooseAddressTracking.onClickDontAllowLocationKotaKecamatan(userSession.userId)
                            permissionCheckerHelper?.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper?.onNeverAskAgain(it, permissionText)
                        }

                        @SuppressLint("MissingPermission")
                        override fun onPermissionGranted() {
                            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                                if (data != null) {
                                    ChooseAddressTracking.onClickAllowLocationKotaKecamatan(userSession.userId)
                                    setBackAddressResult(Address(), data.latitude.toString(), data.longitude.toString())
                                }
                            }
                        }

                    }, it.getString(R.string.rationale_need_location))
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
}
