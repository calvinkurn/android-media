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
import com.tokopedia.shopdiscount.utils.extension.showError
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
        fun deleteParentProduct(productId: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun observeLiveData() {
        observeProductDetailListLiveData()
        observeReserveProduct()
        observeDeleteProductDiscount()
    }

    private fun observeDeleteProductDiscount() {
        viewModel.deleteProductDiscount.observe(viewLifecycleOwner, {
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
        })
    }

    private fun updateProductList() {
        adapter.updateProductList()
    }

    private fun deleteProductFromList(variantProductId: String) {
        adapter.deleteProductFromList(variantProductId)
        if (adapter.getTotalProduct().isZero()) {
            dismiss()
            listener?.deleteParentProduct(productParentId)
        }
    }

    private fun observeReserveProduct() {
        viewModel.reserveProduct.observe(viewLifecycleOwner, {
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
        })
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
                startActivity(intent)
            }
        }
    }

    private fun observeProductDetailListLiveData() {
        viewModel.productDetailListLiveData.observe(viewLifecycleOwner, {
            hideLoading()
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        updateProductList()
                        val errorMessage = it.data.responseHeader.errorMessages.joinToString()
                        showErrorState(Throwable(errorMessage))
                    } else {
                        val totalProductData = it.data.listProductDetailData.size
                        showHeaderSection(totalProductData)
                        addListProductDetailData(it.data.listProductDetailData)
                    }
                }
                is Fail -> {
                    updateProductList()
                    showErrorState(it.throwable)
                }
            }
        })
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
            updateProductDiscount(productParentId, productParentPosition)
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
        val selectedProductVariantId = model.productId
        updateProductDiscount(productParentId, productParentPosition, selectedProductVariantId)
    }

    override fun onClickDeleteProduct(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        showDialogDeleteProduct(uiModel)
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