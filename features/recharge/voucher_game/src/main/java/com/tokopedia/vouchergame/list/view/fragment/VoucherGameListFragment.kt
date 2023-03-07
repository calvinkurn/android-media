package com.tokopedia.vouchergame.list.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.common.topupbills.utils.AnalyticUtils
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_VOUCHER_GAME
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.vouchergame.common.util.VoucherGameGqlQuery
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.databinding.FragmentVoucherGameListBinding
import com.tokopedia.vouchergame.detail.view.activity.VoucherGameDetailActivity
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.activity.VoucherGameListActivity.Companion.RECHARGE_PRODUCT_EXTRA
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListDecorator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListFragment :
    BaseListFragment<Visitable<VoucherGameListAdapterFactory>,
        VoucherGameListAdapterFactory>(),
    VoucherGameListViewHolder.OnClickListener {

    private var binding by autoClearedNullable<FragmentVoucherGameListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameListViewModel

    lateinit var voucherGameExtraParam: VoucherGameExtraParam

    @Inject
    lateinit var voucherGameAnalytics: VoucherGameAnalytics

    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    var rechargeProductFromSlice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameListViewModel::class.java)
        }

        arguments?.let {
            voucherGameExtraParam = it.getParcelable(EXTRA_PARAM_VOUCHER_GAME)
                ?: VoucherGameExtraParam()
            rechargeProductFromSlice = it.getString(RECHARGE_PRODUCT_EXTRA, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameList.observe(
            viewLifecycleOwner,
            Observer {
                it.run {
                    when (it) {
                        is Success -> {
                            renderOperators(it.data)
                        }
                        is Fail -> {
                            showGetListError(it.throwable)
                        }
                    }
                }
            }
        )
        voucherGameViewModel.voucherGameMenuDetail.observe(
            viewLifecycleOwner,
            Observer {
                it.run {
                    togglePromoBanner(true)
                    when (it) {
                        is Success -> {
                            with(it.data) {
                                if (catalog.label.isNotEmpty()) {
                                    val categoryName = catalog.label
                                    (activity as? BaseVoucherGameActivity)?.updateTitle(categoryName)
                                    voucherGameAnalytics.categoryName = categoryName
                                    voucherGameExtraParam.categoryId.toIntSafely().let { id ->
                                        rechargeAnalytics.eventOpenScreen(
                                            userSession.userId,
                                            categoryName,
                                            id.toString()
                                        )
                                    }
                                }

                                renderBanners(banners)
                                renderTickers(tickers)
                            }
                        }
                        is Fail -> {
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVoucherGameListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (voucherGameExtraParam.operatorId.isNotEmpty()) {
            navigateToProductList(CatalogOperatorAttributes())
            activity?.finish()
            return
        }

        if (rechargeProductFromSlice.isNotEmpty()) {
            rechargeAnalytics.onClickSliceRecharge(userSession.userId, rechargeProductFromSlice)
            rechargeAnalytics.onOpenPageFromSlice(TITLE_PAGE)
        }

        rechargeAnalytics.trackVisitRechargePushEventRecommendation(
            voucherGameExtraParam.categoryId.toIntSafely()
        )

        voucherGameExtraParam.menuId.toIntSafely().let {
            togglePromoBanner(false)
            voucherGameViewModel.getVoucherGameMenuDetail(
                com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery.catalogMenuDetail,
                voucherGameViewModel.createMenuDetailParams(it)
            )
        }
        initView()
        loadInitialData()
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
        voucherGameExtraParam.operatorId.let {
            operators.firstOrNull { opr -> opr.id == it }?.let {
                navigateToProductList(it.attributes)
            }
        }
    }

    override fun showGetListError(throwable: Throwable) {
        hideLoading()

        updateStateScrollListener()

        if (adapter.itemCount > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
                context,
                throwable,
                ErrorHandler.Builder().build()
            )
            val errorNetworkModel = ErrorNetworkModel()

            errorNetworkModel.run {
                errorMessage = errMsg
                subErrorMessage = "${
                getString(
                    com.tokopedia.kotlin.extensions.R.string.title_try_again
                )
                }. Kode Error: ($errCode)"
                onRetryListener = ErrorNetworkModel.OnRetryListener {
                    showLoading()
                    loadInitialData()
                }
            }
            adapter.run {
                setErrorNetworkModel(errorNetworkModel)
                showErrorNetwork()
            }
        }
    }

    private fun initView() {
        binding?.run {
            searchInputView.clearListener = {
                searchInputView.searchBarTextField.setText("")
                voucherGameAnalytics.eventClearSearchBox()
            }

            searchInputView.searchBarTextField.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(text: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        text?.let {
                            if (text.text.isNotEmpty()) voucherGameAnalytics.eventClickSearchResult(it.text.toString())
                        }
                        KeyboardHandler.hideSoftKeyboard(activity)
                        return true
                    }
                    return false
                }
            })

            searchInputView.searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.let { searchVoucherGame(it, swipeRefreshLayout.isRefreshing) }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            searchInputView.searchBarTextField.setOnClickListener { voucherGameAnalytics.eventClickSearchBox() }

            context?.let {
                recyclerView.addItemDecoration(VoucherGameListDecorator(it.resources.getDimensionPixelOffset(ITEM_DECORATOR_SIZE)))
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val operatorList = voucherGameViewModel.voucherGameList.value
                        if (operatorList is Success && operatorList.data.operators.isNotEmpty()) {
                            val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recyclerView)
                            with(operatorList.data) {
                                if (searchInputView.searchBarTextField.text.isNotEmpty()) {
                                    voucherGameAnalytics.impressionOperatorCardSearchResult(
                                        searchInputView.searchBarTextField.text.toString(),
                                        operators.subList(visibleIndexes.first, visibleIndexes.second + 1)
                                    )
                                } else {
                                    voucherGameAnalytics.impressionOperatorCard(
                                        operators.subList(
                                            visibleIndexes.first,
                                            visibleIndexes.second + 1
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun togglePromoBanner(state: Boolean) {
        binding?.run {
            promoBanner.visibility = if (state) View.VISIBLE else View.GONE
            promoBannerShimmering.root.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun renderOperators(data: VoucherGameListData) {
        binding?.run {
            searchInputView.searchBarTextField.setHint(data.text)

            if (data.operators.isEmpty()) {
                adapter.clearAllElements()
                showEmpty()
            } else {
                checkAutoSelectOperator(data.operators)
                clearAllData()
                renderList(data.operators)

                recyclerView.post {
                    try {
                        if (recyclerView != null) {
                            val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recyclerView)
                            if (searchInputView.searchBarTextField.text.isNullOrEmpty()) {
                                voucherGameAnalytics.impressionOperatorCard(
                                    data.operators.subList(
                                        visibleIndexes.first,
                                        visibleIndexes.second + 1
                                    )
                                )
                            }
                        }
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
            }
        }
    }

    private fun renderBanners(data: List<TopupBillsBanner>) {
        binding?.run {
            if (data.isNotEmpty()) {
                promoBanner.setPromoList(data.map { it.imageUrl })
                promoBanner.setOnPromoClickListener {
                    val banner = data[it]
                    voucherGameAnalytics.eventClickBanner(banner, it)
                    RouteManager.route(context, banner.applinkUrl)
                }
                promoBanner.setOnPromoScrolledListener { voucherGameAnalytics.impressionBanner(data[it], it) }
                promoBanner.setOnPromoAllClickListener {
                    voucherGameAnalytics.eventClickViewAllBanner()
                    RouteManager.route(context, ApplinkConst.PROMO_LIST)
                }
                context?.let {
                    promoBanner.setBannerSeeAllTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    promoBanner.setBannerIndicator(Indicator.GREEN)
                    promoBanner.bannerSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.resources.getDimension(BANNER_SEE_ALL_TEXT_SIZE))
                }
                promoBanner.buildView()
                promoBanner.visibility = View.VISIBLE
            } else {
                promoBanner.visibility = View.GONE
            }
        }
    }

    private fun renderTickers(tickers: List<TopupBillsTicker>) {
        binding?.run {
            if (tickers.isNotEmpty()) {
                val messages = mutableListOf<TickerData>()
                for (item in tickers) {
                    var description: String = item.content
                    if (item.actionText.isNotEmpty() && item.actionLink.isNotEmpty()) {
                        description += " [${item.actionText}]{${item.actionLink}}"
                    }
                    messages.add(
                        TickerData(
                            item.name,
                            description,
                            when (item.type) {
                                TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                                TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                                TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                                TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                                else -> Ticker.TYPE_INFORMATION
                            }
                        )
                    )
                }

                if (messages.size == 1) {
                    with(messages.first()) {
                        tickerView.tickerTitle = title
                        tickerView.setHtmlDescription(description)
                        tickerView.tickerType = type
                    }
                    tickerView.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                        }

                        override fun onDismiss() {
                        }
                    })
                } else {
                    context?.let { context ->
                        val tickerAdapter = TickerPagerAdapter(context, messages)
                        tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                            }
                        })
                        tickerView.addPagerView(tickerAdapter, messages)
                    }
                }

                tickerView.visibility = View.VISIBLE
            } else {
                tickerView.visibility = View.GONE
            }
        }
    }

    override fun getAdapterTypeFactory(): VoucherGameListAdapterFactory {
        return VoucherGameListAdapterFactory(this)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(VoucherGameListComponent::class.java).inject(this)
    }

    override fun loadInitialData() {
        binding?.searchInputView?.searchBarTextField?.setText("")
    }

    override fun loadData(page: Int) {
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun onItemClicked(item: Visitable<VoucherGameListAdapterFactory>) {
    }

    override fun onItemClicked(operator: VoucherGameOperator) {
        binding?.run {
            if (searchInputView.searchBarTextField.text.isNotEmpty()) {
                voucherGameAnalytics.eventClickSearchResult(searchInputView.searchBarTextField.text.toString())

                val operatorList = voucherGameViewModel.voucherGameList.value
                if (operatorList is Success && operatorList.data.operators.isNotEmpty()) {
                    val visibleIndexes = AnalyticUtils.getVisibleItemIndexes(recyclerView)
                    voucherGameAnalytics.impressionOperatorCardSearchResult(
                        searchInputView.searchBarTextField.text.toString(),
                        operatorList.data.operators.subList(visibleIndexes.first, visibleIndexes.second + 1)
                    )
                }

                voucherGameAnalytics.eventClickOperatorCardSearchResult(operator)
            } else {
                voucherGameAnalytics.eventClickOperatorCard(operator)
            }

            voucherGameExtraParam.operatorId = operator.id.toString()
            navigateToProductList(operator.attributes)
        }
    }

    private fun navigateToProductList(operatorData: CatalogOperatorAttributes) {
        context?.run {
            val intent = VoucherGameDetailActivity.newInstance(
                this,
                voucherGameExtraParam,
                operatorData
            )
            startActivityForResult(intent, REQUEST_VOUCHER_GAME_DETAIL)
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(context, VG_LIST_SPAN_COUNT, GridLayoutManager.VERTICAL, false)
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

    private fun searchVoucherGame(query: String, loadFromCloud: Boolean = false) {
        voucherGameViewModel.getVoucherGameOperators(
            VoucherGameGqlQuery.voucherGameProductList,
            voucherGameViewModel.createParams(voucherGameExtraParam.menuId.toIntSafely()),
            query,
            loadFromCloud
        )
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

    companion object {

        val BANNER_SEE_ALL_TEXT_SIZE = com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl3
        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1

        const val FULL_SCREEN_SPAN_SIZE = 1
        const val OPERATOR_ITEM_SPAN_SIZE = 3
        const val VG_LIST_SPAN_COUNT = 3

        const val REQUEST_VOUCHER_GAME_DETAIL = 300

        private const val TITLE_PAGE = "voucher game"

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam, rechargeProductFromSlice: String = ""): Fragment {
            val fragment = VoucherGameListFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
            bundle.putString(RECHARGE_PRODUCT_EXTRA, rechargeProductFromSlice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
