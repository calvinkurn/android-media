package com.tokopedia.vouchergame.list.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
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
import com.tokopedia.common.topupbills.utils.AnalyticUtils
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
        voucherGameViewModel.voucherGameMenuDetail.observe(this, Observer {
            it.run {
                togglePromoBanner(true)
                when(it) {
                    is Success -> {
                        with (it.data) {
                            if (catalog.label.isNotEmpty()) {
                                (activity as BaseVoucherGameActivity).updateTitle(catalog.label)
                                voucherGameAnalytics.categoryName = catalog.label
                            }

                            if (banners.isEmpty()) promo_banner.visibility = View.GONE
                            else {
                                renderBanners(banners)
                            }
                        }
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
            voucherGameViewModel.getVoucherGameMenuDetail(GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_menu_detail),
                    voucherGameViewModel.createMenuDetailParams(it))
        }
        voucherGameAnalytics.eventPDPLanding()
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

        recycler_view.addItemDecoration(VoucherGameListDecorator(resources.getDimensionPixelOffset(ITEM_DECORATOR_SIZE)))
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val operatorList = voucherGameViewModel.voucherGameList.value
                    if (operatorList is Success) {
                        val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recycler_view)
                        with(operatorList.data) {
                            if (searchInputView.searchText.isNotEmpty()) {
                                voucherGameAnalytics.impressionOperatorCardSearchResult(searchInputView.searchText,
                                        operators.subList(visibleIndexes.first, visibleIndexes.second + 1))
                            } else {
                                voucherGameAnalytics.impressionOperatorCard(
                                        operators.subList(visibleIndexes.first,
                                        visibleIndexes.second + 1))
                            }
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
        searchInputView.setSearchHint(data.text)

        if (data.operators.isEmpty()) {
            adapter.clearAllElements()
            showEmpty()
        }
        else {
            checkAutoSelectOperator(data.operators)
            renderList(data.operators)

            recycler_view.post {
                val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recycler_view)
                if (searchInputView.searchText.isEmpty()) {
                    voucherGameAnalytics.impressionOperatorCard(
                            data.operators.subList(visibleIndexes.first, visibleIndexes.second + 1))
                }
            }
        }
    }

    private fun renderBanners(data: List<TopupBillsBanner>) {
        promo_banner.setPromoList(data.map { it.imageUrl })
        promo_banner.setOnPromoClickListener {
            val banner = data[it]
            voucherGameAnalytics.eventClickBanner(banner, it)
            RouteManager.route(context, banner.applinkUrl) }
        promo_banner.setOnPromoScrolledListener { voucherGameAnalytics.impressionBanner(data[it], it) }
        promo_banner.setOnPromoAllClickListener {
            voucherGameAnalytics.eventClickViewAllBanner()
            RouteManager.route(context, ApplinkConst.PROMO_LIST)
        }
        context?.let {
            promo_banner.setBannerSeeAllTextColor(ContextCompat.getColor(it, com.tokopedia.design.R.color.unify_G500))
        }
        promo_banner.setBannerIndicator(Indicator.GREEN)

        val seeAllText = promo_banner.bannerSeeAll
        seeAllText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(BANNER_SEE_ALL_TEXT_SIZE))

        promo_banner.buildView()
    }

    override fun getAdapterTypeFactory(): VoucherGameListAdapterFactory {
        return VoucherGameListAdapterFactory(this)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(VoucherGameListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            voucherGameViewModel.getVoucherGameOperators(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                    voucherGameViewModel.createParams(it), "", true)
        }
    }

    override fun onItemClicked(item: Visitable<*>) {

    }

    override fun onItemClicked(operator: VoucherGameOperator) {
        if (searchInputView.searchText.isNotEmpty()) {
            voucherGameAnalytics.eventClickSearchResult(searchInputView.searchText)

            val operatorList = voucherGameViewModel.voucherGameList.value
            if (operatorList is Success) {
                val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recycler_view)
                voucherGameAnalytics.impressionOperatorCardSearchResult(searchInputView.searchText,
                        operatorList.data.operators.subList(visibleIndexes.first, visibleIndexes.second + 1))
            }

            voucherGameAnalytics.eventClickOperatorCardSearchResult(operator)
        } else {
            voucherGameAnalytics.eventClickOperatorCard(operator)
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
        hideSnackBarRetry()
        swipeToRefresh.isRefreshing = true
        searchInputView.searchText = ""
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                return when (adapter.getItemViewType(p0)) {
                    VoucherGameListViewHolder.LAYOUT -> FULL_SCREEN_SPAN_SIZE
                    else -> OPERATOR_ITEM_SPAN_SIZE
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
        text?.let { if(text.isNotEmpty()) voucherGameAnalytics.eventClickSearchResult(it) }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let { searchVoucherGame(it) }
    }

    override fun onSearchReset() {
        voucherGameAnalytics.eventClearSearchBox()
        searchVoucherGame("")
    }

    private fun searchVoucherGame(query: String) {
        voucherGameViewModel.getVoucherGameOperators(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                voucherGameViewModel.createParams(voucherGameExtraParam.menuId.toInt()), query)
    }

    fun onBackPressed() {
        voucherGameAnalytics.eventClickBackButton()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun getSearchInputViewResourceId(): Int {
        return R.id.search_input_view
    }

    companion object {

        val BANNER_SEE_ALL_TEXT_SIZE = com.tokopedia.design.R.dimen.sp_16
        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        const val FULL_SCREEN_SPAN_SIZE = 1
        const val OPERATOR_ITEM_SPAN_SIZE = 3

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