package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_ADD_OPTION
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.model.*
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationData
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsProductRecomAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsRecommendationFragment.Companion.HEIGHT
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsRecommendationFragment.Companion.PRODUCT_RECOM
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsRecomGroupBottomSheet
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 20/7/20.
 */

const val CLICK_GRUP_AKTIF_IKLANKAN = "click - iklankan - grup iklan aktif"
const val BUAT_GRUP_IKLANKAN = "click - iklankan - buat grup iklan"

class TopAdsInsightBaseProductFragment : BaseDaggerFragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var product_recom_title: Typography? = null
    private var product_recom_desc: Typography? = null
    private var selectedItems: Typography? = null
    private var divider: DividerUnify? = null
    private var checkBox: CheckboxUnify? = null
    private var rvRecomProduct: RecyclerView? = null

    private lateinit var adapter: TopadsProductRecomAdapter
    private var productRecommendData: ProductRecommendationData? = null
    var height: Int? = null

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    private val sheet: TopAdsRecomGroupBottomSheet by lazy {
        TopAdsRecomGroupBottomSheet.getInstance()
    }
    private var currentGroupName = ""
    private var currentGroupId = ""
    private var currentGroupType = ""

    companion object {
        fun createInstance(bundle: Bundle): TopAdsInsightBaseProductFragment {
            val fragment = TopAdsInsightBaseProductFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return TopAdsInsightBaseProductFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        adapter = TopadsProductRecomAdapter(userSession, ::itemCheckedUnchecked, ::enableButton)
        val dummyId: MutableList<String> = mutableListOf()
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions(PRODUCT, dummyId))
        topAdsDashboardPresenter.getBidInfo(suggestions, ::getMaxBid)
    }

    private fun getMaxBid(list: List<TopadsBidInfo.DataItem>) {
        adapter.setMaxValue(list.firstOrNull()?.maxBid ?: "0")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_dash_recom_product_list, container, false)
        rvRecomProduct = view.findViewById(R.id.rvProductRecom)
        checkBox = view.findViewById(R.id.cb_product_recom)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        product_recom_title = view.findViewById(R.id.product_recom_title)
        product_recom_desc = view.findViewById(R.id.product_recom_desc)
        selectedItems = view.findViewById(R.id.selectedItems)
        divider = view.findViewById(R.id.divider)
        setAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromArgument()
        rvRecomProduct?.setMargin(0, 0, 0, height ?: 0)
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        if (productRecommendData?.products?.isEmpty() == true) {
            setEmptyState()
        } else {
            setAdapterData(productRecommendData?.products)
        }
        checkUnchekAll()
    }

    private fun getDataFromArgument() {
        productRecommendData = arguments?.getParcelable(PRODUCT_RECOM)
        height = arguments?.getInt(HEIGHT)
    }

    private fun checkUnchekAll() {
        checkBox?.run {
            setOnClickListener {
                adapter.setAllChecked(isChecked)
                selectedItems?.text =
                    String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product),
                        adapter.getSelectedIds().size)
            }
        }
    }

    private fun calculateSetTitle(products: List<ProductRecommendation>?): Int {
        var totalClicks = 0
        products?.forEach {
            totalClicks += it.searchCount.toIntOrZero()
        }
        return totalClicks
    }

    private fun setAdapterData(products: List<ProductRecommendation>?) {
        adapter.items.clear()
        products?.forEach {
            adapter.items.add(it)
        }
        adapter.notifyDataSetChanged()
        checkBox?.isChecked = true
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 0)
        product_recom_desc?.text =
            Html.fromHtml(String.format(getString(R.string.topads_dash_recom_product_desc),
                products?.size,
                calculateSetTitle(products)))
        selectedItems?.text =
            String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product),
                adapter.itemCount)
    }

    private fun enableButton(enable: Boolean) {
        var insightFragmentIsDisplayed = checkCurrentFragmentIndex()
        if (adapter.getSelectedIds().size == 0) {
            (activity as TopAdsDashboardActivity).insightMultiActionButtonEnabled = false
            if(insightFragmentIsDisplayed) {
                (activity as TopAdsDashboardActivity).setMultiActionButtonEnabled(false)
            }
        }
        else {
            (activity as TopAdsDashboardActivity).insightMultiActionButtonEnabled = enable
            if(insightFragmentIsDisplayed) {
                (activity as TopAdsDashboardActivity).setMultiActionButtonEnabled(enable)
            }
        }
    }

    private fun checkCurrentFragmentIndex(): Boolean {
        return activity?.findViewById<ViewPager>(R.id.view_pager)?.currentItem == CONST_3
    }


    private fun setEmptyState() {
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 0)
        view?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        view?.findViewById<Typography>(R.id.text_title)?.text =
            getString(R.string.topads_dash_empty_product_recom_title)
        view?.findViewById<Typography>(R.id.text_desc)?.text =
            getString(R.string.topads_dash_empty_product_recom_desc)
        view?.findViewById<ConstraintLayout>(R.id.emptyViewProductRecommendation)?.visible()
        divider?.gone()
        rvRecomProduct?.gone()
        product_recom_title?.gone()
        product_recom_desc?.gone()
        checkBox?.gone()
        selectedItems?.gone()
        (parentFragment as TopAdsRecommendationFragment).setEmptyProduct()
        (activity as TopAdsDashboardActivity).hideButton(true)
    }

    private fun loadData() {
        topAdsDashboardPresenter.getProductRecommendation(::onSuccessProductRecommendation)
    }

    private fun setAdapter() {
        rvRecomProduct?.adapter = adapter
        rvRecomProduct?.layoutManager = LinearLayoutManager(context)
        rvRecomProduct?.isNestedScrollingEnabled = true
    }

    private fun onSuccessProductRecommendation(productRecommendationModel: ProductRecommendationModel) {
        if (productRecommendationModel.topadsGetProductRecommendation.data.products.isEmpty())
            setEmptyState()
        else
            setAdapterData(productRecommendationModel.topadsGetProductRecommendation.data.products)
        swipeRefreshLayout?.isRefreshing = false
    }

    fun openBottomSheet() {
        topAdsDashboardPresenter.getGroupList("", ::onSuccessGroupList)
    }

    private fun onSuccessGroupList(list: List<GroupListDataItem>) {
        sheet.show(childFragmentManager, list)
        sheet.onNewGroup = { groupName ->
            currentGroupName = groupName
            currentGroupId = ""
            getBidInfo()
            adapter.items?.forEach {
                if (it.isChecked) {
                    val eventLabel =
                        "${it.productId} - ${it.searchCount} - ${it.searchPercentage} - ${it.recomBid} - ${it.setCurrentBid}"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(
                        BUAT_GRUP_IKLANKAN,
                        eventLabel,
                        userSession.userId)
                }
            }
        }
        sheet.onItemClick = { groupIdAndType ->
            getBidInfo()
            currentGroupId = groupIdAndType.first
            currentGroupType = groupIdAndType.second
            adapter.items?.forEach {
                if (it.isChecked) {
                    val eventLabel =
                        "${it.productId} - ${it.searchCount} - ${it.searchPercentage} - ${it.recomBid} - ${it.setCurrentBid}"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(
                        CLICK_GRUP_AKTIF_IKLANKAN,
                        eventLabel,
                        userSession.userId)
                }
            }
        }

    }

    private fun getBidInfo() {
        val selectedProductIds: List<String>? = adapter.getSelectedIds().map {
            it
        }
        val suggestions =
            DataSuggestions(ParamObject.TYPE_HEADLINE_KEYWORD, ids = selectedProductIds)
        topAdsDashboardPresenter.getBidInfo(listOf(suggestions), this::onSuccessSuggestion)
    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        if (currentGroupId.isEmpty()) {
            val priceBid = data.firstOrNull()?.suggestionBid?.toDouble() ?: 0.0

            topAdsDashboardPresenter.createGroup(
                adapter.getSelectedIds(), currentGroupName, priceBid, priceBid) {
                if (it.isNullOrEmpty()) {
                    showSuccessToast()
                    loadData()
                } else {
                    onError(it)
                }
            }
        } else {
            val productList: MutableList<GroupEditInput.Group.AdOperationsItem>? = getAds()
            val priceBid = data.firstOrNull()?.minBid?.toFloatOrNull()
            topAdsDashboardPresenter.editBudgetThroughInsight(
                productList, priceBid, currentGroupId, null,
                ::onResultEdit, ::onError
            )
            currentGroupId = ""
        }
        sheet.dismiss()
    }

    private fun getAds(): MutableList<GroupEditInput.Group.AdOperationsItem>? {
        val ids = adapter.getSelectedIds()
        val list: MutableList<GroupEditInput.Group.AdOperationsItem>? = mutableListOf()
        ids.forEach {
            val adOperation = GroupEditInput.Group.AdOperationsItem()
            val ad = adOperation.ad
            ad.productId = it
            adOperation.ad = ad
            adOperation.action = PARAM_ADD_OPTION
            list?.add(adOperation)
        }
        return list
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok),
                View.OnClickListener {}).show()
        }
    }

    private fun onResultEdit(topadsManageGroupAds: FinalAdResponse.TopadsManageGroupAds) {
        if (topadsManageGroupAds.groupResponse.errors?.isEmpty() == false) {
            onError(topadsManageGroupAds.groupResponse.errors?.firstOrNull()?.detail ?: "")
        } else {
            loadData()
            showSuccessToast()
        }
    }

    private fun showSuccessToast() {
        view?.let {
            Toaster.build(it,
                String.format(getString(R.string.topads_dash_success_product_toast),
                    adapter.getSelectedIds().size),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok),
                View.OnClickListener {}).show()
        }
    }

    private fun itemCheckedUnchecked() {
        val selected = adapter.getSelectedIds().size
        selectedItems?.text =
            String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product),
                selected)
        checkBox?.isChecked = selected == adapter.itemCount
        if (selected == 0) {
            enableButton(false)
        } else {
            enableButton(true)
        }
    }
}




