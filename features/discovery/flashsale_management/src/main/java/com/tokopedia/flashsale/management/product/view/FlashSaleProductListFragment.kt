package com.tokopedia.flashsale.management.product.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory
import com.tokopedia.flashsale.management.product.adapter.FlashSaleSubmitLabelAdapter
import com.tokopedia.flashsale.management.product.data.FlashSaleProductHeader
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.view.presenter.FlashSaleProductListPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_eligible_product.*
import javax.inject.Inject

class FlashSaleProductListFragment : BaseSearchListFragment<FlashSaleProductItem, FlashSaleProductAdapterTypeFactory>(),
        FlashSaleSubmitLabelAdapter.OnSellerStatusListAdapterListener {

    var filterIndex: Int = -1

    var campaignId: Int = 0
    var campaignSlug: String = ""
    var allowEditProducts: Boolean = false

    var progressDialog: ProgressDialog? = null

    @Inject
    lateinit var presenter: FlashSaleProductListPresenter

    var hasVisibleOnce = false
    var needLoadData = true

    var flashSaleSubmitLabelAdapter: FlashSaleSubmitLabelAdapter? = null

    override fun getAdapterTypeFactory() = FlashSaleProductAdapterTypeFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { GraphqlClient.init(it) }
        //TODO test, will replaced later
        campaignId = 44
        campaignSlug = "flash-sale-kamis"
//        campaignId = arguments?.getInt(EXTRA_PARAM_CAMPAIGN_ID, 0) ?: 0
//        campaignSlug = arguments?.getString(EXTRA_PARAM_CAMPAIGN_SLUG, "") ?: ""
        if (savedInstanceState != null) {
            filterIndex = savedInstanceState.getInt(SAVED_FILTER_INDEX)
        }
        super.onCreate(savedInstanceState)
        flashSaleSubmitLabelAdapter = FlashSaleSubmitLabelAdapter(filterIndex, 0, 0, this)
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
        recyclerViewLabel.adapter = flashSaleSubmitLabelAdapter
        searchInputView.setResetListener {
            onSearchSubmitted("")
        }
        refreshUI()
    }

    override fun loadInitialData() {
        hideSearchInputView()
        recyclerViewLabel.visibility = View.GONE
        vgBottom.visibility = View.GONE
        loadContent()
        loadBottomBar()
    }

    fun loadContent() {
        super.loadInitialData()
    }

    fun loadBottomBar() {
        loadSellerStatus()
        loadTnC()
    }

    private fun loadSellerStatus() {
        presenter.getSellerStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_get_seller_status),
                campaignSlug,
                onSuccess = {
                    context?.run {
                        if (it.submitStatus) {
                            btnSubmit.text = getString(R.string.flash_sale_update_submission)
                            btnSubmit.setOnClickListener {
                                onClickToUpdateSubmission()
                            }
                        } else {
                            btnSubmit.text = getString(R.string.flash_sale_list)
                            btnSubmit.setOnClickListener {
                                onClickFlashSaleList()
                            }
                        }
                    }
                    allowEditProducts = it.isEligible && it.isShopActive && !it.isGodSeller
                    vgBottom.visibility = View.VISIBLE
                },
                onError = {
                    vgBottom.visibility = View.GONE
                })
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage(getString(R.string.title_loading))
        }
        if (progressDialog!!.isShowing()) {
            progressDialog!!.dismiss()
        }
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    private fun onClickToUpdateSubmission() {
        showProgressDialog()
        presenter.submitSubmission(campaignId,
                onSuccess = {
                    hideProgressDialog()
                    activity?.run {
                        ToasterNormal.showClose(this, it)
                    }
                    loadInitialData()
                },
                onError = {
                    hideProgressDialog()
                    ToasterError.make(view, ErrorHandler.getErrorMessage(context,it), BaseToaster.LENGTH_INDEFINITE)
                            .setAction(R.string.retry_label) {
                                onClickToUpdateSubmission()
                            }
                })
    }

    private fun onClickFlashSaleList() {

    }

    private fun loadTnC() {
        presenter.getFlashSaleTnc(campaignSlug,
                onSuccess = {
                    context?.run {
                        val tncLongString = getString(R.string.with_click_button_you_agree_with_tnc)
                        val tncString = getString(R.string.flash_sale_term_and_condition)
                        val spannable = SpannableString(tncLongString)
                        val indexStart = tncLongString.indexOf(tncString)
                        val indexEnd = indexStart + tncString.length

                        val color = ContextCompat.getColor(context!!, R.color.tkpd_main_green)
                        spannable.setSpan(ForegroundColorSpan(color), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        val clickableSpan = object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                //TODO will change page
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.isUnderlineText = false
                                ds.color = color
                            }
                        }
                        spannable.setSpan(clickableSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        tvTnc.movementMethod = LinkMovementMethod.getInstance()
                        tvTnc.text = spannable
                    }
                },
                onError = {
                    tvTnc.text = getString(R.string.with_click_button_you_agree_with_tnc)
                })
    }

    override fun loadData(page: Int) {
        presenter.getEligibleProductList(campaignId,
                campaignSlug,
                (page - 1) * PER_PAGE, PER_PAGE,
                searchInputView.searchText,
                getFilterId(),
                onSuccess = {
                    onSuccessGetEligibleList(it)
                },
                onError = {
                    super.showGetListError(it)
                })
    }

    private fun getFilterId(): Int {
        return FlashSaleFilterProductListTypeDef.values()
                .firstOrNull() { it.index == flashSaleSubmitLabelAdapter?.selectedIndex }?.id
                ?: FlashSaleFilterProductListTypeDef.TYPE_ALL.id
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_box
        if (getFilterId() == FlashSaleFilterProductListTypeDef.TYPE_ALL.id &&
                searchInputView.searchText.isEmpty()) {
            emptyModel.title = getString(R.string.no_eligible_product_in_this_flash_sale)
            emptyModel.content = getString(R.string.no_worry_you_can_join_next_flash_sale)
        } else {
            //TODO wording empty for filter or search text
            emptyModel.title = ""
            emptyModel.content = ""
        }
        return emptyModel
    }

    private fun onSuccessGetEligibleList(flashSaleProductHeader: FlashSaleProductHeader) {
        super.renderList(flashSaleProductHeader.flashSaleProduct, hasNextPage(flashSaleProductHeader.flashSaleProduct))
        if (TextUtils.isEmpty(searchInputView.searchText) &&
                flashSaleProductHeader.flashSaleProduct.isEmpty() &&
                currentPage <= 1 &&
                (flashSaleSubmitLabelAdapter?.selectedIndex ?: -1) == -1) {
            hideSearchInputView()
        } else {
            showSearchInputView()
        }
        if (currentPage <= 1) {
            renderUILabel(flashSaleProductHeader.submittedCount, flashSaleProductHeader.pendingCount)
        }
    }

    private fun renderUILabel(submittedCount: Int, pendingCount: Int) {
        if (submittedCount + pendingCount == 0) {
            recyclerViewLabel.visibility = View.GONE
        } else {
            flashSaleSubmitLabelAdapter?.setData(submittedCount, pendingCount)
            recyclerViewLabel.visibility = View.VISIBLE
        }
    }

    private fun hasNextPage(list: List<Any>): Boolean {
        return list.isNotEmpty() && list.size >= PER_PAGE
    }

    override fun onSwipeRefresh() {
        presenter.clearCache()
        super.onSwipeRefresh()
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
            val intent = FlashSaleProductDetailActivity.createIntent(it, campaignId,
                    flashSaleProductItem, allowEditProducts)
            startActivityForResult(intent, REQUEST_CODE_FLASH_SALE_PRODUCT_DETAIL)
        }
    }

    override fun onStatusSelected(position: Int) {
        loadContent()
    }

    override fun onStatusCleared() {
        loadContent()
    }

    override fun onSearchSubmitted(text: String?) {
        loadContent()
    }

    override fun onSearchTextChanged(text: String?) {
        // no-op
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
        private const val SAVED_FILTER_INDEX = "saved_filter_id"
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        flashSaleSubmitLabelAdapter?.run {
            outState.putInt(SAVED_FILTER_INDEX, selectedIndex)
        }
    }
}