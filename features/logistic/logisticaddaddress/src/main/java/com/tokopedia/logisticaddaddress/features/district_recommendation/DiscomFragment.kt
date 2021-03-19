package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FragmentDistrictRecommendationBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictAdapterTypeFactory
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictTypeFactory
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DiscomFragment : BaseSearchListFragment<Address, DistrictTypeFactory>(), DiscomContract.View {

    private var mToken: Token? = null
    private var analytics: ActionListener? = null

    private var binding by autoCleared<FragmentDistrictRecommendationBinding>()

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
        }
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
        binding.tvMessage.text = getString(R.string.message_search_address_no_result)
        setMessageSection(true)
    }

    private fun showInitialLoadMessage() {
        binding.tvMessage.text = getString(R.string.message_advice_search_address)
        setMessageSection(true)
    }

    private fun setMessageSection(active: Boolean) {
        binding.tvMessage.visibility = if (active) View.VISIBLE else View.GONE
        binding.swipeRefreshLayout.visibility = if (active) View.GONE else View.VISIBLE
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
