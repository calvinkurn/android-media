package com.tokopedia.campaign.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.R
import com.tokopedia.campaign.databinding.FragmentBaseCampaignManageProductDetailBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailHeaderBinding
import com.tokopedia.campaign.widget.BaseCampaignManageProductListDividerItemDecoration
import com.tokopedia.campaign.widget.WidgetCampaignLabelBulkApply
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class BaseCampaignManageProductDetailFragment<RA : RecyclerView.Adapter<*>> :
    BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentBaseCampaignManageProductDetailBinding>()

    var headerUnify: HeaderUnify? = null
        private set
    var tickerHeader: Ticker? = null
        private set
    var loadingSpinner: View? = null
        private set
    var containerProductDataLayout: View? = null
        private set
    var layoutGlobalError: GlobalError? = null
        private set
    var imageProduct: ImageUnify? = null
        private set
    var textProductName: Typography? = null
        private set
    var textProductOriginalPrice: Typography? = null
        private set
    var labelTotalVariant: Label? = null
        private set
    var textTotalStock: Typography? = null
        private set
    var dividerProductHeader: DividerUnify? = null
        private set
    var textProductVariantName: Typography? = null
        private set
    var widgetBulkApply: WidgetCampaignLabelBulkApply? = null
        private set
    var imageIconProduct: ImageUnify? = null
        private set
    var dividerThick: DividerUnify? = null
        private set
    var rvManageProductDetail: RecyclerView? = null
        private set
    var buttonSubmit: UnifyButton? = null
        private set
    var adapter: RA? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentBaseCampaignManageProductDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupHeaderUnify()
        initRvAdapter()
        setupRecyclerView()
        setupSubmitButtonClickListener()
    }


    private fun initView() {
        viewBinding?.let {
            headerUnify = it.headerUnify
            tickerHeader = it.tickerHeader
            loadingSpinner = it.loadingSpinner
            containerProductDataLayout = it.containerProductDataLayout
            layoutGlobalError = it.globalError
            initViewProductHeader(it.campaignManageProductDetailHeader)
            dividerThick = it.dividerThick
            buttonSubmit = it.buttonSubmit
            rvManageProductDetail = it.rvManageProductDetail
        }
    }

    private fun initViewProductHeader(binding: LayoutCampaignManageProductDetailHeaderBinding) {
        imageProduct = binding.imgProduct
        textProductName = binding.textProductName
        textProductOriginalPrice = binding.textHeaderOriginalPrice
        labelTotalVariant = binding.labelTotalVariant
        imageIconProduct = binding.iconProduct
        textTotalStock = binding.textHeaderTotalStock
        dividerProductHeader = binding.dividerProductHeader
        textProductVariantName = binding.textProductVariantName
        widgetBulkApply = binding.widgetBulkApply
    }

    private fun setupHeaderUnify() {
        headerUnify?.apply {
            title = getHeaderUnifyTitle()
            setNavigationOnClickListener {
                onBackArrowClicked()
            }
        }
    }

    private fun initRvAdapter() {
        adapter = createAdapterInstance()
    }

    abstract fun createAdapterInstance(): RA

    private fun setupRecyclerView() {
        rvManageProductDetail?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@BaseCampaignManageProductDetailFragment.adapter
            itemAnimator = null
            setDecoration()
        }
    }

    /**
     * Can be override if you want to use different implementation
     */
    protected fun RecyclerView.setDecoration() {
        val dividerDrawable = MethodChecker.getDrawable(
            context,
            R.drawable.base_campaign_manage_product_list_bg_separator
        )
        val dividerItemDecoration =
            BaseCampaignManageProductListDividerItemDecoration(dividerDrawable)
        if (itemDecorationCount > 0)
            removeItemDecorationAt(0)
        addItemDecoration(dividerItemDecoration)
    }


    abstract fun onBackArrowClicked()

    abstract fun getHeaderUnifyTitle(): String

    /**
     * Can be used to set product header data
     */
    protected fun setupProductHeaderData(
        productImageUrl: String = "",
        productName: String = "",
        productOriginalPriceFormatted: String = "",
        productTotalVariantFormatted: String = "",
        productStockTextFormatted: String = "",
        productVariantName: String = "",
        isShowWidgetBulkApply: Boolean = false
    ) {
        imageProduct?.showIfWithBlock(productImageUrl.isNotEmpty()) {
            loadImage(productImageUrl)
        }
        textProductName?.showIfWithBlock(productName.isNotEmpty()) {
            text = productName
        }
        textProductOriginalPrice?.showIfWithBlock(productOriginalPriceFormatted.isNotEmpty()) {
            text = productOriginalPriceFormatted
        }
        labelTotalVariant?.showIfWithBlock(productTotalVariantFormatted.isNotEmpty()) {
            setLabel(productTotalVariantFormatted)
        }
        imageIconProduct?.showWithCondition(productStockTextFormatted.isNotEmpty())
        textTotalStock?.showIfWithBlock(productStockTextFormatted.isNotEmpty()) {
            text = productStockTextFormatted
        }
        dividerProductHeader?.showWithCondition(productVariantName.isNotEmpty() || isShowWidgetBulkApply)
        textProductVariantName?.showIfWithBlock(productVariantName.isNotEmpty()) {
            text = productVariantName
        }
        widgetBulkApply?.showWithCondition(isShowWidgetBulkApply)
        dividerThick?.show()
    }

    /**
     * Can be used to disable widget bulk apply
     */
    protected fun disableWidgetBulkApply() {
        widgetBulkApply?.apply {
            setOnClickListener(null)
            isEnabled = false
        }
    }

    /**
     * Can be used to enable widget bulk apply
     */
    protected fun enableWidgetBulkApply() {
        widgetBulkApply?.apply {
            setOnClickListener { onWidgetBulkApplyClicked() }
            isEnabled = true
        }
    }

    /**
     * You can override this function to define the action when clicking widget bulk apply
     */
    protected open fun onWidgetBulkApplyClicked() {}

    /**
     * Can be used to set widget bulk apply title
     */
    protected fun setWidgetBulkApplyText(labelText: String) {
        widgetBulkApply?.setTitle(labelText)
    }

    private fun setupSubmitButtonClickListener() {
        buttonSubmit?.setOnClickListener { onSubmitButtonClicked() }
    }

    abstract fun onSubmitButtonClicked()

    /**
     * Can be used to show global error state
     */
    protected fun showErrorState(errorMessage: String, onGlobalErrorRetryClicked: () -> Unit) {
        layoutGlobalError?.apply {
            show()
            errorSecondaryAction.hide()
            errorTitle.text = errorMessage
            setActionClickListener {
                onGlobalErrorRetryClicked.invoke()
            }
        }
    }

    /**
     * Can be used to hide global error state
     */
    protected fun hideErrorState() {
        layoutGlobalError?.hide()
    }

    /**
     * Can be used to show product content layout
     */
    protected fun showProductDataContent() {
        containerProductDataLayout?.show()
    }

    /**
     * Can be used to hide product content layout
     */
    protected fun hideProductDataContent() {
        containerProductDataLayout?.hide()
    }

    /**
     * Can be used to show loading spinner
     */
    protected fun showLoadingSpinner() {
        loadingSpinner?.hide()
    }

    /**
     * Can be used to hide loading spinner
     */
    protected fun hideLoadingSpinner() {
        loadingSpinner?.hide()
    }

    /**
     * Can be used to enable button submit
     */
    protected fun enableButtonSubmit() {
        buttonSubmit?.isEnabled = true
    }

    /**
     * Can be used to disable button submit
     */
    protected fun disableButtonSubmit() {
        buttonSubmit?.isEnabled = false
    }
}
