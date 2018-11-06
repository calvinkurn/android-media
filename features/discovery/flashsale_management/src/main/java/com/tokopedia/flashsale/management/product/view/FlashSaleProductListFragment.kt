package com.tokopedia.flashsale.management.product.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory
import com.tokopedia.flashsale.management.product.adapter.SellerStatusListAdapter
import com.tokopedia.flashsale.management.product.data.FlashSaleProductHeader
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.view.presenter.FlashSaleProductListPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_eligible_product.*
import javax.inject.Inject

class FlashSaleProductListFragment : BaseSearchListFragment<FlashSaleProductItem, FlashSaleProductAdapterTypeFactory>(),
        SellerStatusListAdapter.OnSellerStatusListAdapterListener {


    var campaignId: Int = 0
    var campaignSlug: String = ""
    @Inject
    lateinit var presenter: FlashSaleProductListPresenter

    var hasVisibleOnce = false
    var needLoadData = true

    var sellerStatusListAdapter: SellerStatusListAdapter? = null

    override fun getAdapterTypeFactory() = FlashSaleProductAdapterTypeFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { GraphqlClient.init(it) }
        //TODO test, will replaced later
        campaignId = 44
        campaignSlug = "flash-sale-kamis"
//        campaignId = arguments?.getInt(EXTRA_PARAM_CAMPAIGN_ID, 0) ?: 0
//        campaignSlug = arguments?.getString(EXTRA_PARAM_CAMPAIGN_SLUG, "") ?: ""

        super.onCreate(savedInstanceState)
        sellerStatusListAdapter = SellerStatusListAdapter(arrayListOf(), this)
        context?.let {
            GraphqlClient.init(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_eligible_product, container, false)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.swipe_refresh_layout)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        val recyclerView = super.getRecyclerView(view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemAnimator = recyclerView.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewLabel.layoutManager = layoutManager
        val animator = recyclerViewLabel.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recyclerViewLabel.adapter = sellerStatusListAdapter
        searchInputView.setResetListener {
            onSearchSubmitted("")
        }
        refreshUI()
    }

    override fun loadInitialData() {
        loadContent()
    }

    fun loadContent() {
        super.loadInitialData()
    }

    override fun loadData(page: Int) {
        //TODO filter by chip (not always all), query
        presenter.getEligibleProductList(campaignId,
                (page - 1) * PER_PAGE, PER_PAGE,
                searchInputView.searchText, FlashSaleFilterProductListTypeDef.TYPE_ALL,
                onSuccess = {
                    onSuccessGetEligibleList(it)
                },
                onError = {
                    super.showGetListError(it)
                })
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_box
        emptyModel.title = getString(R.string.no_eligible_product_in_this_flash_sale)
        emptyModel.content = getString(R.string.no_worry_you_can_join_next_flash_sale)
        return emptyModel
    }

    private fun onSuccessGetEligibleList(flashSaleProductHeader: FlashSaleProductHeader) {
        if (TextUtils.isEmpty(searchInputView.searchText) &&
                flashSaleProductHeader.flashSaleProduct.isEmpty() &&
                currentPage == defaultInitialPage) {
            hideSearchInputView()
        } else {
            showSearchInputView()
        }
        super.renderList(flashSaleProductHeader.flashSaleProduct, hasNextPage(flashSaleProductHeader.flashSaleProduct))
    }

    private fun hasNextPage(list: List<Any>): Boolean {
        return list.isNotEmpty() && list.size >= PER_PAGE
    }

    private fun refreshUI() {
        if (hasVisibleOnce && needLoadData) {
            loadInitialData()
            needLoadData = false
        }
    }

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    override fun onItemClicked(flashSaleProductItem: FlashSaleProductItem) {
        context?.let {
            val intent = FlashSaleProductDetailActivity.createIntent(it, campaignId, flashSaleProductItem)
            startActivityForResult(intent, REQUEST_CODE_FLASH_SALE_PRODUCT_DETAIL)
        }
    }

    override fun onStatusClicked(string: String) {
        //TODO
        Log.i("Test", "test")
        loadContent()
    }


    override fun onSearchSubmitted(text: String?) {
        loadContent()
    }

    override fun onSearchTextChanged(text: String?) {
        //TODO("not implemented")
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_FLASH_SALE_PRODUCT_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK)
                //TODO refresh data
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_PARAM_CAMPAIGN_SLUG = "campaign_slug"
        private const val PER_PAGE = 20

        private const val REQUEST_CODE_FLASH_SALE_PRODUCT_DETAIL = 203

        fun createInstance(campaignId: Int, campaignSlug: String) = FlashSaleProductListFragment().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                putString(EXTRA_PARAM_CAMPAIGN_SLUG, campaignSlug)
            }
        }
    }

    override fun setUserVisibleHint(isFragmentVisible_: Boolean) {
        super.setUserVisibleHint(true)
        if (this.isVisible) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !hasVisibleOnce) {
                hasVisibleOnce = true
                refreshUI()
            }
        }
    }
}