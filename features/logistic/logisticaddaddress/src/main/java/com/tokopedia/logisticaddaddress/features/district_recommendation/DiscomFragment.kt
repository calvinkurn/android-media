package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper
import com.tokopedia.logisticdata.data.entity.address.Token

import javax.inject.Inject

import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS

class DiscomFragment : BaseSearchListFragment<AddressViewModel, DistrictRecommendationTypeFactory>(), DiscomContract.View {

    private var mToken: Token? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var tvMessage: TextView? = null
    private var analytics: DiscomAnalytics? = null

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DiscomAnalytics) {
            analytics = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mToken = it.getParcelable(ARGUMENT_DATA_TOKEN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_district_recommendation, container, false)
        tvMessage = view.findViewById(R.id.tv_message)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInitialLoadMessage()
        searchInputView.setSearchHint(getString(R.string.hint_district_recommendation_search))
        searchInputView.setDelayTextChanged(DEBOUNCE_DELAY_IN_MILIS)
        searchInputView.closeImageButton.setOnClickListener { v ->
            searchInputView.searchText = ""
            analytics?.sendAnalyticsOnClearTextDistrictRecommendationInput()
        }
        swipeRefreshLayout!!.isEnabled = false
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

    override fun getAdapterTypeFactory(): DistrictRecommendationTypeFactory {
        return DistrictRecommendationAdapterTypeFactory()
    }

    override fun onItemClicked(addressViewModel: AddressViewModel) {
        analytics?.sendAnalyticsOnDistrictDropdownSelectionItemClicked(addressViewModel.address.districtName)
        activity?.let {
            val resultIntent = Intent().apply {
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS, addressMapper.convertAddress(addressViewModel.address))

                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, addressViewModel.address.districtId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, addressViewModel.address.districtName)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, addressViewModel.address.cityId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, addressViewModel.address.cityName)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, addressViewModel.address.provinceId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, addressViewModel.address.provinceName)
                putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, addressViewModel.address.zipCodes)
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

    override fun renderData(list: List<AddressViewModel>, hasNextPage: Boolean) {
        super.renderList(list, hasNextPage)
        if (currentPage == defaultInitialPage && hasNextPage) {
            val page = currentPage + defaultInitialPage
            loadData(page)
        }
    }

    override fun setLoadingState(active: Boolean) {
        setMessageSection(false)
        if (active)
            super.showLoading()
        else
            super.hideLoading()
    }

    override fun showEmpty() {
        tvMessage!!.text = getString(R.string.message_search_address_no_result)
        setMessageSection(true)
    }

    private fun showInitialLoadMessage() {
        tvMessage!!.text = getString(R.string.message_advice_search_address)
        setMessageSection(true)
    }

    private fun setMessageSection(active: Boolean) {
        tvMessage!!.visibility = if (active) View.VISIBLE else View.GONE
        swipeRefreshLayout!!.visibility = if (active) View.GONE else View.VISIBLE
    }

    companion object {

        private val ARGUMENT_DATA_TOKEN = "token"
        private val DEBOUNCE_DELAY_IN_MILIS: Long = 700
        private val MINIMUM_SEARCH_KEYWORD_CHAR = 3

        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "city_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "city_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "province_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "province_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "zipcodes"

        @JvmStatic
        fun newInstance(): DiscomFragment = DiscomFragment()

        @JvmStatic
        fun newInstance(token: Token): DiscomFragment = DiscomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA_TOKEN, token)
            }
        }

    }
}
