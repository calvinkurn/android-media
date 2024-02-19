package com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountProductDetailBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountDetailReserveProductUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.utils.rv_decoration.ShopDiscountDividerItemDecoration
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailAdapter
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactoryImpl
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailItemViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailListGlobalErrorViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.viewmodel.ShopDiscountProductDetailBottomSheetViewModel
import com.tokopedia.shopdiscount.subsidy.model.mapper.ShopDiscountProgramInformationDetailMapper
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProgramInformationDetailUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountOptOutMultipleProductSubsidyBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountOptOutSingleProductSubsidyBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountSubsidyOptOutReasonBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountSubsidyProgramInformationBottomSheet
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopDiscountProductDetailBottomSheet : BottomSheetUnify(),
    ShopDiscountProductDetailItemViewHolder.Listener,
    ShopDiscountProductDetailListGlobalErrorViewHolder.Listener {

    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountProductDetailBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var tracker: ShopDiscountTracker
    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore
    private var coachMarkSubsidyInfo: CoachMark2? = null
    private var optOutSuccessMessage: String = ""
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(
            ShopDiscountProductDetailBottomSheetViewModel::class.java
        )
    }
    private var rvProductList: RecyclerView? = null
    private var textProductName: Typography? = null
    private var textChangeDiscount: Typography? = null
    private var divider: View? = null
    private var productParentId: String = ""
    private var status: Int = 0
    private var productParentName: String = ""
    private var productParentPosition: Int = 0
    private var listener: Listener? = null
    private val adapter by lazy {
        ShopDiscountProductDetailAdapter(
            typeFactory = ShopDiscountProductDetailTypeFactoryImpl(
                this,
                this
            )
        )
    }

    companion object {
        private const val PARAM_PRODUCT_PARENT_ID = "param_product_parent_id"
        private const val PARAM_PRODUCT_PARENT_NAME = "param_product_parent_name"
        private const val PARAM_STATUS = "param_status"
        private const val PARAM_PARENT_PRODUCT_POSITION = "param_parent_product_position"
        private const val MARGIN_TOP_BOTTOM_VALUE_DIVIDER = 16
        //need to add delay to fix delay data from BE
        private const val DELAY_SLASH_PRICE_OPT_OUT = 1000L

        fun newInstance(
            productId: String,
            productParentName: String,
            status: Int,
            productPosition: Int
        ): ShopDiscountProductDetailBottomSheet {
            return ShopDiscountProductDetailBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_PRODUCT_PARENT_ID, productId)
                bundle.putString(PARAM_PRODUCT_PARENT_NAME, productParentName)
                bundle.putInt(PARAM_STATUS, status)
                bundle.putInt(PARAM_PARENT_PRODUCT_POSITION, productPosition)
                arguments = bundle
            }
        }
    }

    interface Listener {
        fun deleteParentProduct(productId: String, message: String = "" )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun observeLiveData() {
        observeProductDetailListLiveData()
        observeReserveProduct()
        observeDeleteProductDiscount()
        observeManageProductSubsidyUiModelLiveData()
    }

    private fun observeManageProductSubsidyUiModelLiveData() {
        viewModel.manageProductSubsidyUiModelLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Success -> {
                        onSuccessGetListProductSubsidy(it.data)
                    }
                    is Fail -> {
                        onErrorGetListProductSubsidy(it.throwable)
                    }
                }
            }
        }
    }

    private fun onErrorGetListProductSubsidy(throwable: Throwable) {
        showToasterError(ErrorHandler.getErrorMessage(context, throwable))
    }

    private fun onSuccessGetListProductSubsidy(data: ShopDiscountManageProductSubsidyUiModel) {
        when(data.mode){
            ShopDiscountManageDiscountMode.DELETE, ShopDiscountManageDiscountMode.UPDATE -> {
                if(data.getTotalProductWithSubsidy() == Int.ONE){
                    showOptOutSingleProductSubsidyBottomSheet(data)
                } else {
                    showOptOutMultipleProductSubsidyBottomSheet(data)
                }
            }
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY -> {
                showBottomSheetOptOutReason(data)
            }
        }

    }

    private fun sendClickOptOutSubsidyTracker(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        tracker.sendClickOptOutSubsidyVariantEvent(uiModel.productId)
    }

    private fun observeDeleteProductDiscount() {
        viewModel.deleteProductDiscount.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        val errorMessage = ErrorHandler.getErrorMessage(context, null)
                        showToasterError(errorMessage)
                    } else {
                        deleteProductFromList(it.data.productId)
                    }
                }
                is Fail -> {
                    updateProductList()
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        }
    }

    private fun updateProductList() {
        adapter.updateProductList()
    }

    private fun deleteProductFromList(variantProductId: String) {
        adapter.deleteProductFromList(variantProductId)
        if (adapter.getTotalProduct().isZero()) {
            dismiss()
            listener?.deleteParentProduct(productParentId, getString(R.string.sd_discount_deleted))
        } else {
            showToaster(getString(R.string.sd_discount_deleted))
        }
    }

    private fun showToaster(message: String) {
        viewBinding?.root showToaster message
    }

    private fun observeReserveProduct() {
        viewModel.reserveProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        val errorMessage = ErrorHandler.getErrorMessage(context, null)
                        showToasterError(errorMessage)
                    } else {
                        redirectToManageDiscountPage(it.data)
                    }
                }

                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        }
    }

    private fun redirectToManageDiscountPage(uiModel: ShopDiscountDetailReserveProductUiModel) {
        CoroutineScope(Dispatchers.Main).launch {
            context?.let {
                val intent = RouteManager.getIntent(
                    it,
                    ApplinkConstInternalSellerapp.SHOP_DISCOUNT_MANAGE_DISCOUNT
                )
                intent.putExtra(
                    ShopDiscountManageActivity.REQUEST_ID_PARAM,
                    uiModel.requestId
                )
                intent.putExtra(ShopDiscountManageActivity.STATUS_PARAM, status)
                intent.putExtra(
                    ShopDiscountManageActivity.MODE_PARAM,
                    ShopDiscountManageDiscountMode.UPDATE
                )
                intent.putExtra(
                    ShopDiscountManageActivity.SELECTED_PRODUCT_VARIANT_ID_PARAM,
                    uiModel.selectedProductVariantId
                )
                intent.putExtra(
                    ShopDiscountManageActivity.OPT_OUT_SUCCESS_MESSAGE_PARAM,
                    optOutSuccessMessage
                )
                startActivity(intent)
            }
        }
    }

    private fun observeProductDetailListLiveData() {
        viewModel.productDetailListLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        updateProductList()
                        val errorMessage = it.data.responseHeader.errorMessages.joinToString()
                        showErrorState(Throwable(errorMessage))
                    } else {
                        val totalProductData = it.data.listProductDetailData.size
                        if (totalProductData == Int.ZERO) {
                            dismiss()
                            listener?.deleteParentProduct(productParentId, "")
                        } else {
                            showHeaderSection(totalProductData)
                            addListProductDetailData(it.data.listProductDetailData)
                        }
                    }
                }
                is Fail -> {
                    updateProductList()
                    showErrorState(it.throwable)
                }
            }
        }
    }

    private fun showToasterError(message: String) {
        view?.rootView?.showError(message)
    }

    private fun showHeaderSection(totalProductData: Int) {
        val bottomSheetTitle = if (totalProductData > 1) {
            String.format(
                getString(R.string.product_detail_multiple_product_title),
                totalProductData
            )
        } else {
            getString(R.string.product_detail_single_product_title)
        }
        setTitle(bottomSheetTitle)
        textProductName?.show()
        textChangeDiscount?.show()
        divider?.show()
    }

    private fun showErrorState(throwable: Throwable) {
        adapter.showGlobalErrorView(throwable)
    }

    private fun addListProductDetailData(data: List<ShopDiscountProductDetailUiModel.ProductDetailData>) {
        adapter.addListProductDetailData(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        init()
        observeLiveData()
        setupProductParentNameSection()
        setupRecyclerView()
        rvProductList?.post {
            showLoading()
            getProductListData()
        }
        initCoachMark()
    }

    private fun initCoachMark() {
        coachMarkSubsidyInfo = context?.let { CoachMark2(it) }
    }

    private fun init() {
        rvProductList = viewBinding?.rvProductList
        textProductName = viewBinding?.textProductParentName
        textChangeDiscount = viewBinding?.textChangeDiscount
        divider = viewBinding?.divider
    }

    private fun setupProductParentNameSection() {
        textProductName?.text = productParentName
        textChangeDiscount?.setOnClickListener {
            if (adapter.anySubsidyProduct()) {
                getListProductSubsidy(
                    adapter.getProductListData(),
                    ShopDiscountManageDiscountMode.UPDATE
                )
            } else {
                updateProductDiscount(productParentId, productParentPosition)
            }
        }
    }

    private fun showOptOutMultipleProductSubsidyBottomSheet(
        data: ShopDiscountManageProductSubsidyUiModel,
    ) {
        val bottomSheet = ShopDiscountOptOutMultipleProductSubsidyBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(dataModel, optOutSuccessMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showBottomSheetOptOutReason(data: ShopDiscountManageProductSubsidyUiModel) {
        val bottomSheet = ShopDiscountSubsidyOptOutReasonBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(dataModel, optOutSuccessMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onOptOutProductSubsidyBottomSheetSuccess(
        dataModel: ShopDiscountManageProductSubsidyUiModel,
        optOutSuccessMessage: String
    ) {
        this.optOutSuccessMessage = optOutSuccessMessage
        when (dataModel.mode) {
            ShopDiscountManageDiscountMode.DELETE -> {
                showLoading()
                deleteSelectedProductDiscount(dataModel.getListProductIdVariantNonSubsidy().firstOrNull().orEmpty())
            }

            ShopDiscountManageDiscountMode.UPDATE -> {
                if (dataModel.isAllSelectedProductFullSubsidy()) {
                    showToaster(getString(R.string.sd_discount_deleted))
                    showLoading()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(DELAY_SLASH_PRICE_OPT_OUT)
                        getProductListData()
                    }
                } else {
                    val selectedProductId = if (dataModel.getListProductIdVariant().size == Int.ONE) {
                        dataModel.getListProductIdVariant().firstOrNull().orEmpty()
                    } else {
                        ""
                    }
                    updateProductDiscount(productParentId, productParentPosition, selectedProductId)
                }
            }

            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY -> {
                showLoading()
                showToaster(optOutSuccessMessage)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY_SLASH_PRICE_OPT_OUT)
                    getProductListData()
                }
            }
        }
    }

    private fun getProductListData() {
        viewModel.getProductDetailListData(productParentId, status)
    }

    private fun showLoading() {
        adapter.showLoading()
    }

    private fun hideLoading() {
        adapter.hideLoading()
    }

    private fun setupRecyclerView() {
        rvProductList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ShopDiscountProductDetailBottomSheet.adapter
            itemAnimator = null
            setDecoration()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    checkShouldShowCoachMarkSubsidy()
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun checkShouldShowCoachMarkSubsidy() {
        val layoutManager = rvProductList?.layoutManager as? LinearLayoutManager
        val startIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val endIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        for (i in startIndex..endIndex) {
            val product = adapter.getProductListData().getOrNull(i)
            if (product?.isSubsidy == true && product.parentId != Int.ZERO.toString()) {
                val viewHolder = rvProductList?.findViewHolderForAdapterPosition(i)
                if (viewHolder is ShopDiscountProductDetailItemViewHolder) {
                    val anchoredView = viewHolder.itemView.findViewById<View>(R.id.text_subsidy_status)
                    anchoredView.isVisibleOnTheScreen(
                        onViewVisible = {
                            showCoachMarkSubsidyInfo(anchoredView, product)
                        },
                        onViewNotVisible = {
                            if(coachMarkSubsidyInfo?.isShowing == true) {
                                coachMarkSubsidyInfo?.dismissCoachMark()
                            }
                        }
                    )
                }
                break
            }
        }
    }

    private fun RecyclerView.setDecoration() {
        val dividerDrawable = MethodChecker.getDrawable(
            context,
            R.drawable.shop_discount_bg_line_separator_thin
        )
        val drawableInset = InsetDrawable(
            dividerDrawable,
            Int.ZERO,
            MARGIN_TOP_BOTTOM_VALUE_DIVIDER.toPx(),
            Int.ZERO,
            MARGIN_TOP_BOTTOM_VALUE_DIVIDER.toPx()
        )
        val dividerItemDecoration = ShopDiscountDividerItemDecoration(
            drawableInset
        )
        if (itemDecorationCount > 0)
            removeItemDecorationAt(0)
        addItemDecoration(dividerItemDecoration)
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountProductDetailBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            setChild(this.root)
            setCloseClickListener {
                dismiss()
            }
        }
        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheet.requestLayout()
                        bottomSheet.invalidate()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
        }
    }

    private fun getArgumentData() {
        productParentId = arguments?.getString(PARAM_PRODUCT_PARENT_ID).orEmpty()
        status = arguments?.getInt(PARAM_STATUS).orZero()
        productParentName = arguments?.getString(PARAM_PRODUCT_PARENT_NAME).orEmpty()
        productParentPosition = arguments?.getInt(PARAM_PARENT_PRODUCT_POSITION).orZero()
    }

    override fun onClickEditProduct(
        model: ShopDiscountProductDetailUiModel.ProductDetailData,
        position: Int
    ) {
        if (model.isSubsidy) {
            getListProductSubsidy(
                listOf(model),
                ShopDiscountManageDiscountMode.UPDATE
            )
        } else {
            val selectedProductVariantId = model.productId
            updateProductDiscount(productParentId, productParentPosition, selectedProductVariantId)
        }
    }

    private fun showOptOutSingleProductSubsidyBottomSheet(
        data: ShopDiscountManageProductSubsidyUiModel
    ) {
        val bottomSheet = ShopDiscountOptOutSingleProductSubsidyBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(dataModel, optOutSuccessMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun onClickDeleteProduct(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        if (uiModel.isSubsidy) {
            getListProductSubsidy(
                listOf(uiModel),
                ShopDiscountManageDiscountMode.DELETE
            )
        } else {
            showDialogDeleteProduct(uiModel)
        }
    }

    override fun onClickOptOutSubsidy(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        sendClickOptOutSubsidyTracker(uiModel)
        getListProductSubsidy(
            listOf(uiModel),
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY
        )
    }

    private fun getListProductSubsidy(
        listProductDetailData: List<ShopDiscountProductDetailUiModel.ProductDetailData>,
        mode: String
    ) {
        viewModel.getListProductDetailForManageSubsidy(listProductDetailData, mode)
    }

    override fun onClickSubsidyInfo(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        sendSlashPriceClickSubsidyInformation(uiModel)
        showSubsidyProgramInformationBottomSheet(uiModel)
    }

    private fun sendSlashPriceClickSubsidyInformation(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        tracker.sendSlashPriceClickSubsidyInformationBottomSheetEvent(
            uiModel.parentId != Int.ZERO.toString(),
            uiModel.productId,
        )
    }

    private fun showCoachMarkSubsidyInfo(view: View, uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        if(!preferenceDataStore.isCoachMarkSubsidyInfoOnVariantAlreadyShown()) {
            val coachMarks = ArrayList<CoachMark2Item>()
            val coachMarkDesc = getString(R.string.sd_subsidy_coach_mark_non_variant_desc)
            coachMarks.add(
                CoachMark2Item(
                    view,
                    "",
                    coachMarkDesc
                )
            )
            coachMarkSubsidyInfo?.showCoachMark(coachMarks)
            sendSlashPriceSubsidyImpressionCoachMark(uiModel)
            preferenceDataStore.setCoachMarkSubsidyInfoOnVariantAlreadyShown()
        }
    }

    private fun sendSlashPriceSubsidyImpressionCoachMark(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        tracker.sendImpressionSlashPriceSubsidyCoachMarkBottomSheetEvent(uiModel.productId)
    }

    private fun showSubsidyProgramInformationBottomSheet(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        val programDetailInfo = getProgramDetailInfoModel(uiModel)
        val bottomSheet = ShopDiscountSubsidyProgramInformationBottomSheet.newInstance(
            programDetailInfo
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun getProgramDetailInfoModel(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData): ShopDiscountProgramInformationDetailUiModel {
        return ShopDiscountProgramInformationDetailMapper.map(
            isVariant = uiModel.isVariant,
            formattedOriginalPrice = uiModel.maxOriginalPrice.getCurrencyFormatted(),
            formattedFinalDiscountedPrice = uiModel.maxPriceDiscounted.getCurrencyFormatted(),
            formattedFinalDiscountedPercentage = String.format(
                getString(R.string.shop_discount_product_detail_percent_format_non_range),
                uiModel.maxDiscount
            ),
            mainStock = uiModel.stock,
            maxOrder = uiModel.maxOrder,
            productId = uiModel.productId,
            isBottomSheet = true,
            subsidyInfo = uiModel.subsidyInfo,
            isMultiWarehouse = uiModel.isMultiWarehouse
        )
    }
    private fun showDialogDeleteProduct(
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title =
                    getString(R.string.shop_discount_manage_discount_delete_product_dialog_title)
                val description =
                    getString(R.string.shop_discount_product_detail_delete_product_dialog_description)
                val primaryCtaText =
                    getString(R.string.shop_discount_manage_discount_delete_button_cta)
                val secondaryCtaText =
                    getString(R.string.shop_discount_manage_discount_cancel_button_cta)
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setSecondaryCTAText(secondaryCtaText)
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    dismiss()
                    showLoading()
                    deleteSelectedProductDiscount(uiModel.productId)
                }
                show()
            }
        }
    }

    private fun deleteSelectedProductDiscount(productId: String) {
        viewModel.deleteSelectedProductDiscount(productId, status)
    }

    private fun updateProductDiscount(
        productParentId: String,
        productParentPosition: Int,
        selectedProductVariantId: String = ""
    ) {
        viewModel.reserveProduct(productParentId, productParentPosition, selectedProductVariantId)
    }

    override fun onGlobalErrorActionClickRetry() {
        showLoading()
        getProductListData()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

}
