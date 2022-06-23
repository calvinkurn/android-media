package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentManageProductBinding
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.*
import com.tokopedia.shop.flashsale.presentation.creation.highlight.ManageHighlightedProductActivity
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationFragment
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ManageProductListAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.ProductDeleteDialog
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.shop.flashsale.presentation.list.list.CampaignListFragment
import com.tokopedia.shop.flashsale.presentation.list.list.listener.RecyclerViewScrollListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.GlobalScope
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class ManageProductFragment :
    BaseSimpleListFragment<ManageProductListAdapter, SellerCampaignProductList.Product>() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaignId"
        private const val SECOND_STEP = 2
        private const val PAGE_SIZE = 50
        private const val LIST_TYPE = 0
        private const val DELAY = 1000L
        private const val REQUEST_CODE = 123
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"
        private const val REDIRECT_TO_CAMPAIGN_LIST_PAGE_DELAY: Long = 1_500
        private const val SCROLL_ANIMATION_DELAY = 500L

        @JvmStatic
        fun newInstance(campaignId: Long): ManageProductFragment {
            return ManageProductFragment().apply {
                arguments = Bundle().apply {
                    putLong(ManageProductActivity.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductViewModel::class.java) }
    private var binding by autoClearedNullable<SsfsFragmentManageProductBinding>()
    private val loaderDialog by lazy { context?.let { LoaderDialog(it) } }
    private val campaignId by lazy { arguments?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero() }
    private val manageProductListAdapter by lazy {
        ManageProductListAdapter(
            onEditClicked = {
                null
            },
            onDeleteClicked = ::deleteProduct
        )
    }

    override fun getScreenName(): String = ManageProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentManageProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupView()
        observeProductList()
        observeRemoveProductsStatus()
        observeBannerType()
    }

    private fun setupView() {
        binding?.apply {
            header.headerSubTitle = String.format(
                getString(R.string.sfs_placeholder_step_counter),
                SECOND_STEP
            )
            header.setNavigationOnClickListener {
                activity?.finish()
            }
            tpgAddProduct.setOnClickListener {
                if (manageProductListAdapter.itemCount < PAGE_SIZE) {
                    showChooseProductPage()
                } else {
                    view.showErrorWithCta(
                        getString(R.string.manage_product_maximum_product_error),
                        getString(R.string.action_oke)
                    )
                }
            }
            btnSaveDraft.setOnClickListener {
                root showToaster getString(R.string.sfs_saved_as_draft)
                doOnDelayFinished(REDIRECT_TO_CAMPAIGN_LIST_PAGE_DELAY) {
                    routeToCampaignListPage()
                }
            }
            btnContinue.setOnClickListener {
                context?.let { it1 -> ManageHighlightedProductActivity.start(it1, campaignId) }
            }
        }
        setupScrollListener()
    }

    private fun observeProductList() {
        viewModel.products.observe(viewLifecycleOwner) { result ->
            showLoader()
            when (result) {
                is Success -> {
                    hideLoader()
                    if (result.data.productList.size.isMoreThanZero()) {
                        displayProducts(result.data)
                        hideEmptyState()
                    } else {
                        showEmptyState()
                        showChooseProductPage()
                    }
                }
                is Fail -> {
                    hideLoader()
                    showEmptyState()
                    result.throwable.localizedMessage?.let { view.showError(it) }
                }
            }
        }
    }

    private fun observeRemoveProductsStatus() {
        viewModel.removeProductsStatus.observe(viewLifecycleOwner) {
            if (it is Success) {
                doOnDelayFinished(DELAY) {
                    loadInitialData()
                }
            } else if (it is Fail) {
                view?.showError(it.throwable)
            }
        }
    }

    private fun observeBannerType() {
        viewModel.bannerType.observe(viewLifecycleOwner) { type ->
            when (type) {
                EMPTY.type -> {
                    showEmptyProductBanner()
                }
                ERROR.type -> {
                    showErrorProductBanner()
                }
                HIDE.type -> {
                    hideBanner()
                }
            }
        }
    }

    private fun displayProducts(productList: SellerCampaignProductList) {
        viewModel.setProductErrorMessage(productList)
        viewModel.setProductInfoCompletion(productList)
        viewModel.getBannerType(productList)
        manageProductListAdapter.clearAll()
        renderList(productList.productList, false)
        binding?.apply {
            tpgProductCount.text = String.format(
                getString(R.string.manage_product_placeholder_product_count),
                productList.totalProduct
            )
        }
    }

    private fun setupScrollListener() {
        binding?.apply {
            recyclerViewProduct.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        doOnDelayFinished(SCROLL_ANIMATION_DELAY) {
                            handleScrollDownEvent()
                        }
                    },
                    onScrollUp = {
                        doOnDelayFinished(SCROLL_ANIMATION_DELAY) {
                            handleScrollUpEvent()
                        }
                    }
                )
            )
        }
    }

    @SuppressLint("ResourcePackage")
    private fun showEmptyState() {
        binding?.apply {
            emptyState.visible()
            cardIncompleteProductInfo.gone()
            tpgProductCount.gone()
            tpgAddProduct.gone()
            recyclerViewProduct.gone()
            cardBottomButtonGroup.gone()

            emptyState.setImageUrl(EMPTY_STATE_IMAGE_URL)
            emptyState.setPrimaryCTAClickListener {
                if (manageProductListAdapter.itemCount < PAGE_SIZE) {
                    showChooseProductPage()
                } else {
                    view.showErrorWithCta(
                        getString(R.string.manage_product_maximum_product_error),
                        getString(R.string.action_oke)
                    )
                }
            }
        }
    }

    private fun hideEmptyState() {
        binding?.apply {
            emptyState.gone()
            tpgProductCount.visible()
            tpgAddProduct.visible()
            recyclerViewProduct.visible()
            cardBottomButtonGroup.visible()
        }
    }

    private fun showEmptyProductBanner() {
        binding?.apply {
            tickerErrorProductInfo.gone()
            cardIncompleteProductInfo.visible()
            btnContinue.enable()
        }
    }

    private fun showErrorProductBanner() {
        binding?.apply {
            tickerErrorProductInfo.visible()
            cardIncompleteProductInfo.gone()
            btnContinue.disable()
        }
    }

    private fun hideBanner() {
        binding?.apply {
            tickerErrorProductInfo.gone()
            cardIncompleteProductInfo.gone()
            btnContinue.enable()
        }
    }

    private fun showLoader() {
        binding?.apply {
            loader.visible()
            cardIncompleteProductInfo.gone()
            tpgProductCount.gone()
            tpgAddProduct.gone()
            emptyState.gone()
            recyclerViewProduct.gone()
            cardBottomButtonGroup.gone()
        }
    }

    private fun hideLoader() {
        binding?.apply {
            loader.gone()
        }
    }

    private fun showChooseProductPage() {
        val context = context ?: return
        val intent = Intent(context, ChooseProductActivity::class.java).apply {
            putExtra(ChooseProductActivity.BUNDLE_KEY_CAMPAIGN_ID, campaignId.toString())
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun deleteProduct(product: SellerCampaignProductList.Product) {
        ProductDeleteDialog().apply {
            setOnPrimaryActionClick {
                loaderDialog?.show()
                viewModel.removeProducts(campaignId, listOf(product))
            }
            show(context ?: return)
        }
    }

    private fun routeToCampaignListPage() {
        val context = context ?: return
        CampaignListActivity.start(context, isClearTop = true)
    }

    private fun handleScrollDownEvent() {
        binding?.apply {
            when (viewModel.bannerType.value) {
                EMPTY.type -> {
                    cardIncompleteProductInfo.slideDown()
                }
                ERROR.type -> {
                    tickerErrorProductInfo.slideDown()
                }
            }
            cardBottomButtonGroup.slideDown()
        }
    }

    private fun handleScrollUpEvent() {
        binding?.apply {
            when (viewModel.bannerType.value) {
                EMPTY.type -> {
                    cardIncompleteProductInfo.slideUp()
                }
                ERROR.type -> {
                    tickerErrorProductInfo.slideUp()
                }
            }
            cardBottomButtonGroup.slideUp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                showLoader()
                Timer("Retrieving", false).schedule(DELAY) {
                    viewModel.getProducts(campaignId, LIST_TYPE)
                }
            }
        }
    }

    override fun createAdapter(): ManageProductListAdapter {
        return manageProductListAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.recyclerViewProduct
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<SellerCampaignProductList.Product>) {
        adapter?.submit(list)
    }

    override fun loadData(page: Int) {
        viewModel.getProducts(campaignId, LIST_TYPE)
    }

    override fun clearAdapterData() {
        adapter?.clearAll()
    }

    override fun onShowLoading() {}

    override fun onHideLoading() {
        loaderDialog?.dialog?.hide()
    }

    override fun onDataEmpty() {
        showEmptyState()
    }

    override fun onGetListError(message: String) {
        view?.showError(message)
    }

    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {

    }
}