package com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.LoadingDelegateAdapter
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.entity.LoadingItem
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetListProductSseSubmissionErrorBinding
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.delegate.FlashSaleProductListSseSubmissionErrorItemDelegate
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.delegate.FlashSaleProductListSseSubmissionErrorItemShimmeringDelegate
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.item.ProductSseSubmissionErrorShimmeringItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FlashSaleProductListSseSubmissionErrorBottomSheet :
    BottomSheetUnify(),
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        private val TAG = FlashSaleProductListSseSubmissionErrorBottomSheet::class.java.simpleName
        private const val KEY_CAMPAIGN_ID = "campaign_id"
        private const val PRODUCT_PER_PAGE = 10

        fun createInstance(campaignId: String): FlashSaleProductListSseSubmissionErrorBottomSheet =
            FlashSaleProductListSseSubmissionErrorBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_CAMPAIGN_ID, campaignId)
                }
            }
    }

    private var binding by autoClearedNullable<StfsBottomsheetListProductSseSubmissionErrorBinding>()

    private val productSseErrorSubmissionAdapter by lazy {
        CompositeAdapter.Builder()
            .add(FlashSaleProductListSseSubmissionErrorItemDelegate())
            .add(FlashSaleProductListSseSubmissionErrorItemShimmeringDelegate())
            .add(LoadingDelegateAdapter())
            .build()
    }

    private var campaignId: String = ""
    private var currentProductOffset: Int = Int.ZERO

    @Inject
    lateinit var viewModel: FlashSaleProductListSseSubmissionErrorBottomSheetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        getArgumentData(arguments)
    }

    private fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun getArgumentData(arguments: Bundle?) {
        arguments?.let{
            campaignId = arguments.getString(KEY_CAMPAIGN_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        getInitialProductListSseSubmissionError()
        observeProductSubmissionSseErrorLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun showProductListShimmering(){
        productSseErrorSubmissionAdapter.addItem(ProductSseSubmissionErrorShimmeringItem)
    }

    private fun hideProductListShimmering(){
        productSseErrorSubmissionAdapter.removeItem(ProductSseSubmissionErrorShimmeringItem)
    }

    private fun observeProductSubmissionSseErrorLiveData() {
        viewModel.productSubmissionSseError.observe(viewLifecycleOwner) {
            hideProductListShimmering()
            notifyLoadResult(it.isHasNextProduct)
            productSseErrorSubmissionAdapter.addItems(it.listCampaignProductError)
        }
    }

    private fun acknowledgeProductSubmissionSse() {
        viewModel.acknowledgeProductSubmissionSse(campaignId)
    }

    override fun onDismiss(dialog: DialogInterface) {
        acknowledgeProductSubmissionSse()
        super.onDismiss(dialog)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding =
            StfsBottomsheetListProductSseSubmissionErrorBinding.inflate(inflater, container, false)
        clearContentPadding = true
        isFullpage = true
        setChild(binding?.root)
        setTitle(getString(R.string.stfs_bottomsheet_error_product_submission_sse_list_title))
        setTickerDescription()
        setupRecyclerView()
    }

    private fun setTickerDescription() {
        binding?.tickerSseErrorProductList?.setTextDescription(getString(R.string.stfs_bottomsheet_error_product_submission_sse_list_ticker_description))
    }

    private fun getInitialProductListSseSubmissionError() {
        currentProductOffset = Int.ZERO
        showProductListShimmering()
        getProductListSseSubmissionErrorData(currentProductOffset)
    }

    private fun getNextProductListSseSubmissionError(page: Int) {
        currentProductOffset = page
        getProductListSseSubmissionErrorData(currentProductOffset)
    }

    private fun getProductListSseSubmissionErrorData(page: Int) {
        viewModel.getProductListSseSubmissionError(campaignId, page)
    }

    private fun setupRecyclerView() {
        val pagingConfig = HasPaginatedList.Config(
            pageSize = PRODUCT_PER_PAGE,
            onLoadNextPage = {
                productSseErrorSubmissionAdapter.addItem(LoadingItem)
            }, onLoadNextPageFinished = {
                productSseErrorSubmissionAdapter.removeItem(LoadingItem)
            })
        binding?.rvResult?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = productSseErrorSubmissionAdapter
            attachPaging(this, pagingConfig) { _, offset ->
                getNextProductListSseSubmissionError(offset)
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}
