package com.tokopedia.vouchergame.list.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.utils.AnalyticUtils.Companion.getVisibleItems
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackImpressionItem
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_VOUCHER_GAME
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.view.activity.VoucherGameDetailActivity
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListDecorator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import com.tokopedia.vouchergame.list.view.model.VoucherGameOperatorAttributes
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import kotlinx.android.synthetic.main.fragment_voucher_game_list.*
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListFragment: BaseSearchListFragment<Visitable<*>,
        VoucherGameListAdapterFactory>(),
        SearchInputView.ResetListener,
        VoucherGameListViewHolder.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameListViewModel

    lateinit var voucherGameExtraParam: VoucherGameExtraParam

    @Inject
    lateinit var voucherGameAnalytics: VoucherGameAnalytics
    lateinit var operatorTrackingList: List<TopupBillsTrackImpressionItem<VoucherGameOperator>>
    lateinit var bannerTrackingList: List<TopupBillsTrackImpressionItem<TopupBillsBanner>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameListViewModel::class.java)
        }

        arguments?.let {
            voucherGameExtraParam = it.getParcelable(EXTRA_PARAM_VOUCHER_GAME) ?: VoucherGameExtraParam()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameList.observe(this, Observer {
            it.run {
                when(it) {
                    is Success -> {
                        renderOperators(it.data)
                    }
                    is Fail -> {
                        showGetListError(it.throwable)
                    }
                }
            }
        })
        voucherGameViewModel.voucherGameBanners.observe(this, Observer {
            it.run {
                togglePromoBanner(true)
                when(it) {
                    is Success -> {
                        if (it.data.isEmpty()) promo_banner.visibility = View.GONE
                        else renderBanners(it.data)
                    }
                    is Fail -> {
                        promo_banner.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_voucher_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            togglePromoBanner(false)
            voucherGameViewModel.getVoucherGameBanners(GraphqlHelper.loadRawString(resources, R.raw.query_menu_detail),
                    voucherGameViewModel.createBannerParams(it))
        }
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::voucherGameExtraParam.isInitialized) {
            // Reset operatorId
            voucherGameExtraParam.operatorId = ""
            outState.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
        }
    }

    private fun checkAutoSelectOperator(operators: List<VoucherGameOperator>) {
        voucherGameExtraParam.operatorId.toIntOrNull()?.let {
            operators.firstOrNull { opr -> opr.id == it }?.let {
                navigateToProductList(it.attributes)
            }
        }
    }

    private fun initView() {
        searchInputView.setResetListener(this)
        searchInputView.searchTextView.setOnClickListener { voucherGameAnalytics.eventClickSearchBox() }

        recycler_view.addItemDecoration(VoucherGameListDecorator(ITEM_DECORATOR_SIZE, resources))
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (::operatorTrackingList.isInitialized) {
                        if (searchInputView.searchText.isNotEmpty()) {
                            voucherGameAnalytics.impressionOperatorCardSearchResult(searchInputView.searchText,
                                    getVisibleItems(operatorTrackingList, recycler_view,
                                            this@VoucherGameListFragment::updateOperatorTrackingList))
                        } else {
                            voucherGameAnalytics.impressionOperatorCard(getVisibleItems(operatorTrackingList,
                                    recycler_view, this@VoucherGameListFragment::updateOperatorTrackingList))
                        }
                    }
                }
            }
        })
    }

    private fun togglePromoBanner(state: Boolean) {
        promo_banner.visibility = if (state) View.VISIBLE else View.GONE
        promo_banner_shimmering.visibility = if (state) View.GONE else View.VISIBLE
    }

    private fun renderOperators(data: VoucherGameListData) {
        (activity as BaseVoucherGameActivity).updateTitle(data.text)

        if (data.operators.isEmpty()) {
            adapter.clearAllElements()
            showEmpty()
        }
        else {
            checkAutoSelectOperator(data.operators)
            renderList(data.operators)

            operatorTrackingList = data.operators.mapIndexed { index, item ->
                TopupBillsTrackImpressionItem(item, index)
            }

            recycler_view.post {
                if (::operatorTrackingList.isInitialized) {
                    if (searchInputView.searchText.isNotEmpty()) {
                        voucherGameAnalytics.impressionOperatorCardSearchResult(searchInputView.searchText,
                                getVisibleItems(operatorTrackingList, recycler_view, this::updateOperatorTrackingList))
                    } else {
                        voucherGameAnalytics.impressionOperatorCard(getVisibleItems(operatorTrackingList,
                                recycler_view, this::updateOperatorTrackingList))
                    }
                }
            }
        }
    }

    private fun renderBanners(data: List<TopupBillsBanner>) {
        bannerTrackingList = data.mapIndexed { index, item ->
            TopupBillsTrackImpressionItem(item, index)
        }

        promo_banner.setPromoList(data.map { it.imageUrl })
        promo_banner.setOnPromoClickListener {
            val banner = data[it]
            voucherGameAnalytics.eventClickBanner(bannerTrackingList[it])
            RouteManager.route(context, banner.applinkUrl) }
        promo_banner.setOnPromoScrolledListener { voucherGameAnalytics.impressionBanner(listOf(bannerTrackingList[it])) }
        promo_banner.setOnPromoAllClickListener {
            voucherGameAnalytics.eventClickViewAllBanner()
            RouteManager.route(context, ApplinkConst.PROMO_LIST)
        }
        context?.let {
            promo_banner.setBannerSeeAllTextColor(ContextCompat.getColor(it, R.color.unify_G500))
        }
        promo_banner.setBannerIndicator(Indicator.GREEN)
        promo_banner.buildView()
    }

    override fun getAdapterTypeFactory(): VoucherGameListAdapterFactory {
        return VoucherGameListAdapterFactory(this)
    }

    override fun getScreenName(): String = getString(R.string.app_label)

    override fun initInjector() {
        getComponent(VoucherGameListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            voucherGameViewModel.getVoucherGameList(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                    voucherGameViewModel.createParams(it), "", true)
        }
    }

    override fun onItemClicked(item: Visitable<*>) {

    }

    override fun onItemClicked(operator: VoucherGameOperator) {
        val operatorIndex = operatorTrackingList.indexOfFirst { it.item == operator }
        if (searchInputView.searchText.isNotEmpty()) {
            voucherGameAnalytics.eventClickOperatorCardSearchResult(operator.attributes.name,
                    operatorIndex, operatorTrackingList[operatorIndex])
        } else {
            voucherGameAnalytics.eventClickOperatorCard(operator.attributes.name,
                    operatorIndex, operatorTrackingList[operatorIndex])
        }

        voucherGameExtraParam.operatorId = operator.id.toString()
        navigateToProductList(operator.attributes)
    }

    private fun navigateToProductList(operatorData: VoucherGameOperatorAttributes) {
        context?.run {
            val intent = VoucherGameDetailActivity.newInstance(this,
                    voucherGameExtraParam, operatorData)
            startActivityForResult(intent, REQUEST_VOUCHER_GAME_DETAIL)
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        searchInputView.searchText = ""
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                return when (adapter.getItemViewType(p0)) {
                    VoucherGameListViewHolder.LAYOUT -> 1
                    else -> 3
                }
            }
        }
        return layoutManager
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val model = EmptyModel()
        model.title = getString(R.string.vg_empty_state_title)
        model.description = getString(R.string.vg_empty_state_desc)
        return model
    }

    override fun onSearchSubmitted(text: String?) {
        text?.let {
            voucherGameAnalytics.eventClickSearchResult(it)
            searchVoucherGame(it)
        }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
//            if (text.length > 2)
            voucherGameAnalytics.eventClickSearchResult(it)
            searchVoucherGame(it)
        }
    }

    override fun onSearchReset() {
        voucherGameAnalytics.eventClearSearchBox()
        searchVoucherGame("")
    }

    private fun searchVoucherGame(query: String) {
        voucherGameViewModel.getVoucherGameList(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                voucherGameViewModel.createParams(voucherGameExtraParam.menuId.toInt()), query)
    }

    private fun updateOperatorTrackingList(data: List<TopupBillsTrackImpressionItem<VoucherGameOperator>>) {
        operatorTrackingList = data
    }

    fun onBackPressed() {
        voucherGameAnalytics.eventClickBackButton()
    }

    companion object {

        const val ITEM_DECORATOR_SIZE = 8

        const val REQUEST_VOUCHER_GAME_DETAIL = 300

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam): Fragment {
            val fragment = VoucherGameListFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}