package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_ADD_OPTION
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_SOURCE_RECOM
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.model.*
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.dashboard.R
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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import kotlinx.android.synthetic.main.topads_dash_recom_product_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Created by Pika on 20/7/20.
 */

const val CLICK_GRUP_AKTIF_IKLANKAN = "click - iklankan - grup iklan aktif"
const val BUAT_GRUP_IKLANKAN = "click - iklankan - buat grup iklan"
class TopAdsInsightBaseProductFragment : BaseDaggerFragment() {

    private lateinit var adapter: TopadsProductRecomAdapter
    private lateinit var checkBox: CheckboxUnify
    private var productRecommendData: ProductRecommendationData? = null
    var height: Int? = null

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    var rvRecomProduct: RecyclerView? = null

    private val sheet: TopAdsRecomGroupBottomSheet by lazy {
        TopAdsRecomGroupBottomSheet.getInstance()
    }
    private var currentGroupName = ""
    private var currentGroupId = -1

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
        adapter = TopadsProductRecomAdapter(::itemCheckedUnchecked, ::enableButton)
        val dummyId: MutableList<Long> = mutableListOf()
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions(PRODUCT, dummyId))
        topAdsDashboardPresenter.getBidInfo(suggestions, ::getMaxBid)
    }

    private fun getMaxBid(list: List<TopadsBidInfo.DataItem>) {
        adapter.setMaxValue(list.firstOrNull()?.maxBid ?: "0")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_dash_recom_product_list, container, false)
        rvRecomProduct = view.findViewById(R.id.rvProductRecom)
        checkBox = view.findViewById(R.id.cb_product_recom)
        setAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromArgument()
        rvProductRecom.setMargin(0, 0, 0, height ?: 0)
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
        checkBox.run {
            setOnClickListener {
                adapter.setAllChecked(isChecked)
                selectedItems?.text = String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product), adapter.getSelectedIds().size)
            }
        }
    }

    private fun calculateSetTitle(products: List<ProductRecommendation>?): Int {
        var totalClicks = 0
        products?.forEach {
            totalClicks += it.searchCount
        }
        return totalClicks
    }

    private fun setAdapterData(products: List<ProductRecommendation>?) {
        adapter.items.clear()
        products?.forEach {
            adapter.items.add(it)
        }
        adapter.notifyDataSetChanged()
        checkBox.isChecked = true
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 0)
        product_recom_desc.text = Html.fromHtml(String.format(getString(R.string.topads_dash_recom_product_desc), products?.size, calculateSetTitle(products)))
        selectedItems?.text = String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product), adapter.itemCount)
    }

    private fun enableButton(enable: Boolean) {
        if (adapter.getSelectedIds().size == 0)
            (activity as TopAdsDashboardActivity).enableRecommButton(false)
        else
            (activity as TopAdsDashboardActivity).enableRecommButton(enable)
    }

    private fun setEmptyState() {
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 0)
        emptyViewProductRecommendation?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        emptyViewProductRecommendation?.text_title?.text = getString(R.string.topads_dash_empty_product_recom_title)
        emptyViewProductRecommendation?.text_desc?.text = getString(R.string.topads_dash_empty_product_recom_desc)
        emptyViewProductRecommendation?.visible()
        divider?.gone()
        rvProductRecom?.gone()
        product_recom_title?.gone()
        product_recom_desc?.gone()
        checkBox.gone()
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
        swipeRefreshLayout.isRefreshing = false
    }

    fun openBottomSheet() {
        topAdsDashboardPresenter.getGroupList("", ::onSuccessGroupList)
    }

    private fun onSuccessGroupList(list: List<GroupListDataItem>) {
        sheet.show(childFragmentManager, list)
        sheet.onNewGroup = { groupName ->
            currentGroupName = groupName
            getBidInfo()
            adapter.items?.forEach {
                val eventLabel = "${it.productId} - ${it.searchCount} - ${it.searchPercentage} - ${it.recomBid} - ${it.setCurrentBid}"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(BUAT_GRUP_IKLANKAN, eventLabel, userSession.userId)
            }
        }
        sheet.onItemClick = { groupId ->
            getBidInfo()
            currentGroupId = groupId
            adapter.items?.forEach {
                val eventLabel = "${it.productId} - ${it.searchCount} - ${it.searchPercentage} - ${it.recomBid} - ${it.setCurrentBid}"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(CLICK_GRUP_AKTIF_IKLANKAN, eventLabel, userSession.userId)
            }
        }

    }

    private fun getBidInfo() {
        val selectedProductIds: List<Long>? = adapter.getSelectedIds().map {
            it.toLong()
        }
        val suggestions = DataSuggestions(ParamObject.TYPE_HEADLINE_KEYWORD, ids = selectedProductIds)
        topAdsDashboardPresenter.getBidInfo(listOf(suggestions), this::onSuccessSuggestion)
    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        if (currentGroupId == -1) {
            val param: HashMap<String, Any> = hashMapOf()
            val userSession = UserSession(context)
            param[INPUT] = InputCreateGroup().apply {
                shopID = userSession.shopId
                keywords = null
                group = Group(
                        groupName = currentGroupName,
                        ads = getAdsList(),
                        priceBid = data.firstOrNull()?.suggestionBid?.toDouble() ?: 0.0,
                        groupBudget = "0",
                        source = PARAM_SOURCE_RECOM,
                        suggestedBidValue = data.firstOrNull()?.suggestionBid?.toDouble() ?: 0.0
                )
            }
            topAdsDashboardPresenter.createGroup(param, ::onSuccessGroupCreation)

        } else {
            val productList: MutableList<GroupEditInput.Group.AdOperationsItem>? = getAds()
            val map = HashMap<String, Any?>()
            map[PARAM_GROUP_Id] = currentGroupId
            map[PARAM_PRICE_BID] = data.firstOrNull()?.minBid ?: 0
            topAdsDashboardPresenter.editBudgetThroughInsight(productList, map, ::onResultEdit, ::onError)
            currentGroupId = -1
        }
        sheet.dismiss()
    }

    private fun getAdsList(): List<AdsItem> {
        val ids = adapter.getSelectedIds()
        val adsList: MutableList<AdsItem> = mutableListOf()
        val ad = AdsItem()
        ids.forEach {
            ad.productID = it
            ad.source = PARAM_SOURCE_RECOM
            ad.ad = Ad().apply {
                adType = "1"
                adID = "0"
            }
            adsList.add(ad)
        }
        return adsList
    }

    private fun onSuccessGroupCreation(topadsCreateGroupAds: ResponseCreateGroup.TopadsCreateGroupAds) {
        if (topadsCreateGroupAds.errors.isEmpty()) {
            showSuccessToast()
            loadData()
        } else {
            onError(topadsCreateGroupAds.errors.firstOrNull()?.detail ?: "")
        }
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
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.topads.common.R.string.topads_common_text_ok), View.OnClickListener {}).show()
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
            Toaster.build(it, String.format(getString(R.string.topads_dash_success_product_toast), adapter.getSelectedIds().size), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_text_ok), View.OnClickListener {}).show()
        }
    }

    private fun itemCheckedUnchecked() {
        val selected = adapter.getSelectedIds().size
        selectedItems?.text = String.format(getString(com.tokopedia.topads.common.R.string.topads_common_selected_product), selected)
        checkBox.isChecked = selected == adapter.itemCount
        if (selected == 0) {
            enableButton(false)
        } else {
            enableButton(true)
        }
    }
}




