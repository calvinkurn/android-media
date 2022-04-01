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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountProductDetailBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.product_detail.presentation.ShopDiscountProductDetailDividerItemDecoration
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailAdapter
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactoryImpl
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailItemViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailListGlobalErrorViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.viewmodel.ShopDiscountProductDetailBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
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
    private var productParentId: String = ""
    private var status: Int = 0
    private var productParentName: String = ""
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
        private const val PARAM_STATUS = "param_status"
        private const val PARAM_PRODUCT_PARENT_NAME = "param_product_parent_name"
        private const val MARGIN_TOP_BOTTOM_VALUE_DIVIDER = 16

        fun newInstance(
            productId: String,
            productParentName: String,
            status: Int,
        ): ShopDiscountProductDetailBottomSheet {
            return ShopDiscountProductDetailBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_PRODUCT_PARENT_ID, productId)
                bundle.putString(PARAM_PRODUCT_PARENT_NAME, productParentName)
                bundle.putInt(PARAM_STATUS, status)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun observeLiveData() {
        observeProductDetailListLiveData()
    }

    private fun observeProductDetailListLiveData() {
        viewModel.productDetailListLiveData.observe(viewLifecycleOwner, {
            hideLoading()
            when (it) {
                is Success -> {
                    addListProductDetailData(it.data)
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })
    }

    private fun showErrorState(throwable: Throwable) {
        adapter.showGlobalErrorView(throwable)
    }

    private fun addListProductDetailData(data: List<ShopDiscountProductDetailUiModel>) {
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
            getProductListData()
        }
    }

    private fun init() {
        rvProductList = viewBinding?.rvProductList
        textProductName = viewBinding?.textProductParentName
        textChangeDiscount = viewBinding?.textChangeDiscount
    }

    private fun setupProductParentNameSection() {
        textProductName?.text = productParentName
        textChangeDiscount?.setOnClickListener {

        }
    }

    private fun getProductListData() {
        showLoading()
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
        val dividerItemDecoration = ShopDiscountProductDetailDividerItemDecoration(
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
            setTitle("Title")
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
    }

    override fun onClickEditProduct(model: ShopDiscountProductDetailUiModel) {

    }

    override fun onGlobalErrorActionClickRetry() {
        getProductListData()
    }

}