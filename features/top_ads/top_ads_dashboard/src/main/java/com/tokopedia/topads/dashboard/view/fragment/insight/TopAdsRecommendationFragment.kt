package com.tokopedia.topads.dashboard.view.fragment.insight

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DAILY_BUDGET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_CURRENT_TAB
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.InsightAdObj
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightShopKeywordRecommendationFragment.Companion.NOT_EXPANDED
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsInsightAdsTypeBottomSheet
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.viewmodel.TopAdsInsightViewModel
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_fragment_recommendation_layout.*
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.abs

/**
 * Created by Pika on 9/7/20.
 */

const val CLICK_PRODUK_BERPOTENSI = "click - produk berpotensi"
const val CLICK_ANGARRAN_HARIAN = "click - anggaran harian"
const val CLICK_IKLANKAN = "click - iklankan"

class TopAdsRecommendationFragment : BaseDaggerFragment() {

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: TopAdsHeadlineBaseFragment.AppBarActionHeadline? = null
    private var isAdTypeProdukSelected = true
    private val adTypeList by lazy { getAdsTypeList() }
    private val adsTypeBottomSheet by lazy {
        TopAdsInsightAdsTypeBottomSheet.getInstance(adTypeList, this::onAdTypeChanged)
    }
    private var productRecommendData: ProductRecommendationData? = null
    private var keywordRecommendData: InsightKeyData? = null
    private var dailyBudgetRecommendData: TopadsGetDailyBudgetRecommendation? = null
    private var recommendedKeywordData: RecommendedKeywordData? = null
    private var countKey = 0
    private var countProduct = 0
    private var countBid = 0
    private var index = 0
    private lateinit var emptyView: ConstraintLayout
    private lateinit var loderRecom: LoaderUnify

    companion object {
        private const val ADTYPE_PRODUK = 0
        private const val ADTYPE_TOKO = 1

        const val HEIGHT = "addp_bar_height"
        const val PRODUCT_RECOM = "productRecommendData"
        const val BUDGET_RECOM = "dailyBudgetRecommendData"
        fun createInstance(height: Int?, redirectToTabInsight: Int): TopAdsRecommendationFragment {
            val bundle = Bundle()
            bundle.putInt(HEIGHT, height ?: 0)
            bundle.putInt(PARAM_CURRENT_TAB, redirectToTabInsight)
            val fragment = TopAdsRecommendationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: TopAdsInsightViewModel

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_fragment_recommendation_layout,
            container,
            false
        )
        emptyView = view.findViewById(R.id.empty_view)
        loderRecom = view.findViewById(R.id.loderRecom)
        return view
    }

    override fun getScreenName(): String {
        return TopAdsRecommendationFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopAdsHeadlineBaseFragment.AppBarActionHeadline)
            collapseStateCallBack = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        setupSelectAdsTypeView(adTypeList[if (isAdTypeProdukSelected) ADTYPE_PRODUK else ADTYPE_TOKO])
        observeLiveData()
        app_bar_layout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED;
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE;
                }
            }
        })
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
    }

    private fun observeLiveData() {
        viewModel.recommendedKeyword.observe(viewLifecycleOwner, {
            recommendedKeywordData = it
            countKey = it.recommendedKeywordDetails?.size ?: 0
            checkAllData()
        })
    }

    private fun initListener() {
        changeAdType.setOnClickListener {
            adsTypeBottomSheet.show(childFragmentManager)
        }
    }

    fun loadShopData() {
        loderRecom.visibility = View.VISIBLE
        viewModel.getShopKeywords(userSession.shopId, arrayOf())
    }

    private fun loadProductData() {
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)
        topAdsDashboardPresenter.getProductRecommendation(::onSuccessProductRecommendation)
        topAdsDashboardPresenter.getDailyBudgetRecommendation(::onSuccessBudgetRecommendation)
    }

    private fun onSuccessBudgetRecommendation(dailyBudgetRecommendationModel: DailyBudgetRecommendationModel) {
        dailyBudgetRecommendData = dailyBudgetRecommendationModel.topadsGetDailyBudgetRecommendation
        countBid = dailyBudgetRecommendData?.data?.size ?: 0
        checkAllData()
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        keywordRecommendData = response
        val data: HashMap<String, KeywordInsightDataMain> = response.data
        countKey = data.size
        checkAllData()
    }

    private fun onSuccessProductRecommendation(productRecommendationModel: ProductRecommendationModel) {
        productRecommendData = productRecommendationModel.topadsGetProductRecommendation.data
        countProduct = productRecommendData?.products?.size ?: 0
        checkAllData()
    }

    private fun checkAllData() {
        if (isAdTypeProdukSelected && (productRecommendData == null || keywordRecommendData == null || dailyBudgetRecommendData == null))
            return
        if (!isAdTypeProdukSelected && recommendedKeywordData == null) return
        (activity as TopAdsDashboardActivity?)?.hideButton(countProduct == 0)
        if (isAdTypeProdukSelected && countProduct == 0 && countBid == 0 && countKey == 0) {
            setEmptyView()
        } else {
            showViews()
            initInsightTabAdapter()
            renderViewPager()
            topAdsInsightTabAdapter?.setTabTitles(
                resources, countProduct, countBid, countKey, isAdTypeProdukSelected
            )
        }
    }

    private fun setEmptyView() {
        loderRecom.visibility = View.GONE
        rvTabInsight?.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        view_pager?.visibility = View.GONE
    }

    private fun showViews() {
        rvTabInsight?.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        view_pager?.visibility = View.VISIBLE
    }

    private fun initInsightTabAdapter() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object :
            TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                if (position == 0 && checkFragmentPosition(CONST_0, PRODUK)) {
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(CLICK_PRODUK_BERPOTENSI, userSession.userId, userSession.userId)
                } else if ((position == 1) && checkFragmentPosition(CONST_1, DAILY_BUDGET) || (position == 0) && checkFragmentPosition(CONST_0, DAILY_BUDGET)) {
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(CLICK_ANGARRAN_HARIAN, userSession.userId, userSession.userId)
                }
                view_pager.currentItem = position
                if (position == 0 && topAdsInsightTabAdapter?.getTab()?.get(position)?.contains(PRODUK) == true && countProduct != 0) {
                    (activity as TopAdsDashboardActivity?)?.hideButton(false)
                } else {
                    (activity as TopAdsDashboardActivity?)?.hideButton(true)
                }
            }
        })
        rvTabInsight.adapter = topAdsInsightTabAdapter
        view_pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun renderViewPager() {
        loderRecom.visibility = View.GONE
        view_pager.adapter = getViewPagerAdapter()
        val page = arguments?.getInt(PARAM_CURRENT_TAB)
        if (ifPageAvailable(page)) {
            if (page == CONST_0)
                index = CONST_0
            if (page == CONST_1) {
                index = if (checkFragmentPosition(CONST_0, DAILY_BUDGET))
                    CONST_0
                else
                    CONST_1
            }
            if (page == CONST_2) {
                if (checkFragmentPosition(CONST_0, KATA_KUNCI))
                    index = CONST_0
                index = if (checkFragmentPosition(CONST_1, KATA_KUNCI))
                    CONST_1
                else
                    CONST_2
            }
        }
        view_pager?.currentItem = index
        topAdsInsightTabAdapter?.setSelectedTitle(index)
        view_pager.disableScroll(true)
    }

    private fun ifPageAvailable(page: Int?): Boolean {
        return when (page) {
            CONST_0 -> isProductAvailable()
            CONST_1 -> isDailyBudgetAvailable()
            CONST_2 -> isKeywordAvailable()
            else -> false
        }
    }

    private fun isKeywordAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return when (topAdsInsightTabAdapter?.itemCount) {
                CONST_1 -> {
                    checkFragmentPosition(CONST_0, KATA_KUNCI)
                }
                CONST_2 -> {
                    return !(!checkFragmentPosition(CONST_0, KATA_KUNCI) &&
                            !checkFragmentPosition(CONST_1, KATA_KUNCI))
                }
                else -> true
            }
        return false
    }

    private fun isDailyBudgetAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return if (topAdsInsightTabAdapter?.itemCount == CONST_1) {
                checkFragmentPosition(CONST_0, DAILY_BUDGET)
            } else {
                !(!checkFragmentPosition(CONST_0, DAILY_BUDGET)
                        && !checkFragmentPosition(CONST_1, DAILY_BUDGET))
            }
        return false
    }

    private fun checkFragmentPosition(index: Int, value: String): Boolean {
        return topAdsInsightTabAdapter?.getTab()?.get(index)?.contains(value) == true
    }

    private fun isProductAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return topAdsInsightTabAdapter?.getTab()?.get(CONST_0)?.contains(PRODUK) == true
        return false
    }

    fun setCount(count: Int, type: Int) {
        when (type) {
            CONST_0 -> topAdsInsightTabAdapter?.setTabTitles(resources, count, countBid, countKey)
            CONST_1 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, count, countKey)
            CONST_2 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, countBid, count)
        }
    }

    private fun getViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        if (countProduct != 0) {
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_RECOM, productRecommendData)
            bundle.putInt(HEIGHT, arguments?.getInt(HEIGHT) ?: 0)
            list.add(FragmentTabItem("", TopAdsInsightBaseProductFragment.createInstance(bundle)))
        }
        if (countBid != 0) {
            val bundle = Bundle()
            bundle.putParcelable(BUDGET_RECOM, dailyBudgetRecommendData)
            list.add(FragmentTabItem("", TopAdsInsightBaseBidFragment.createInstance(bundle)))
        }
        if (isAdTypeProdukSelected) {
            if (countKey != 0)
                list.add(FragmentTabItem("", TopadsInsightBaseKeywordFragment.createInstance()))
        } else {
            val instance =
                TopAdsInsightShopKeywordRecommendationFragment.createInstance(recommendedKeywordData)
            list.add(FragmentTabItem("", instance))
        }
        val pagerAdapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)

        pagerAdapter.setList(list)
        return pagerAdapter
    }

    fun setClick() {
        val fragments = (view_pager?.adapter as? TopAdsDashboardBasePagerAdapter)?.getList()
        if (fragments != null) {
            for (frag in fragments) {
                when (frag.fragment) {
                    is TopAdsInsightBaseProductFragment -> {
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(
                            CLICK_IKLANKAN,
                            "",
                            userSession.userId
                        )
                        (frag.fragment as TopAdsInsightBaseProductFragment).openBottomSheet()
                    }
                    is TopAdsInsightShopKeywordRecommendationFragment -> {
                        (frag.fragment as TopAdsInsightShopKeywordRecommendationFragment).applyKeywords()
                    }
                }
            }
        }
    }

    fun setEmptyProduct() {
        countProduct = 0
    }

    fun checkBtnVisibilityAndSetTracker() {
        if (productRecommendData != null)
            (activity as TopAdsDashboardActivity?)?.hideButton(countProduct == 0)
    }

    //methods for choosing ad type
    private fun setupSelectAdsTypeView(item: InsightAdObj) {
        txtSelectedAdType.text = item.adName
        if (isAdTypeProdukSelected) loadProductData() else loadShopData()
    }

    private fun onAdTypeChanged(position: Int, item: InsightAdObj) {
        adsTypeBottomSheet.dismiss()
        val produkSelected = position == ADTYPE_PRODUK
        if (produkSelected == isAdTypeProdukSelected) return
        countKey = 0
        countProduct = 0
        countBid = 0

        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(false)
        isAdTypeProdukSelected = produkSelected
        TopAdsInsightShopKeywordRecommendationFragment.expandedPosi = NOT_EXPANDED
        setupSelectAdsTypeView(item)
    }

    private fun getAdsTypeList(): List<InsightAdObj> {
        return listOf(
            InsightAdObj(
                resources.getString(R.string.topads_dashboard_ad_product_type_selection_title),
                isAdTypeProdukSelected,
            ),
            InsightAdObj(
                resources.getString(R.string.topads_dashboard_ad_headline_type_selection_title),
                !isAdTypeProdukSelected
            )
        )
    }
}