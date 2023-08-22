package com.tokopedia.minicart.bmgm.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.minicart.databinding.FragmentBmgmMiniCartWidgetBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartView : ConstraintLayout, BmgmMiniCartAdapter.Listener, DefaultLifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: FragmentBmgmMiniCartWidgetBinding? = null
    private var footerBinding: ViewBmgmMiniCartSubTotalBinding? = null
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter(this) }
    private val viewModel by lazy {
        ViewModelProvider(
            ViewTreeViewModelStoreOwner.get(this)!!, viewModelFactory
        )[BmgmMiniCartViewModel::class.java]
    }
    private var param = BmgmParamModel()

    init {
        binding = FragmentBmgmMiniCartWidgetBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            footerBinding = ViewBmgmMiniCartSubTotalBinding.bind(root)
        }
        setAsLifecycleObserver()
        setupView()
    }

    private fun setupView() {
        setupRecyclerView()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        initInjector()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        observeCartData()
        observeSetCarChecklistStatus()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        viewModel.clearCartDataLocalCache()
        binding = null
        super.onDestroy(owner)
    }

    override fun setOnItemClickedListener() {
        RouteManager.route(context, ApplinkConstInternalGlobal.BMGM_MINI_CART)
    }

    fun fetchData(offerIds: List<Long>, offerJsonData: String, warehouseIds: List<String>) {
        this.param = BmgmParamModel(
            offerIds = offerIds,
            offerJsonData = offerJsonData,
            warehouseIds = warehouseIds
        )
        viewModel.getMiniCartData(param)
    }

    private fun setAsLifecycleObserver() {
        getLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    private fun observeCartData() {
        getLifecycleOwner()?.let { lifecycleOwner ->
            lifecycleOwner.observe(viewModel.cartData) {
                when (it) {
                    is BmgmState.Loading -> showMiniCartLoadingState()
                    is BmgmState.Success -> setOnSuccessGetCartData(it.data)
                    is BmgmState.Error -> showErrorState()
                }
            }
        }
    }

    private fun showMiniCartLoadingState() {
        dismissErrorState()
        binding?.run {
            loadingStateGroup.visible()
            rvBmgmMiniCart.gone()
            tvBmgmCartDiscount.gone()
        }
    }

    private fun dismissMiniCartLoadingState() {
        binding?.loadingStateGroup?.gone()
    }

    private fun observeSetCarChecklistStatus() {
        getLifecycleOwner()?.let { liveCycleOwner ->
            viewModel.setCheckListState.observe(liveCycleOwner) {
                when (it) {
                    is BmgmState.Loading -> showLoadingButton()
                    is BmgmState.Success, is BmgmState.Error -> openCartPage()
                }
            }
        }
    }

    private fun showErrorState() {
        dismissMiniCartLoadingState()
        binding?.run {
            errorStateGroup.visible()
            rvBmgmMiniCart.gone()
            tvBmgmCartDiscount.gone()
            containerSubTotal.gone()
            icBmgmReload.setOnClickListener {
                viewModel.getMiniCartData(param)
            }
        }
    }

    private fun dismissErrorState() {
        binding?.errorStateGroup?.gone()
    }

    private fun openCartPage() {
        dismissLoadingButton()
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun showLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = true
    }

    private fun dismissLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = false
    }

    private fun getLifecycleOwner(): LifecycleOwner? {
        return context as? LifecycleOwner
    }

    private fun setOnSuccessGetCartData(data: BmgmMiniCartDataUiModel) {
        dismissMiniCartLoadingState()
        dismissErrorState()
        setupTiersApplied(data)
        setupFooterView(data)
    }

    private fun setupTiersApplied(data: BmgmMiniCartDataUiModel) {
        binding?.run {
            rvBmgmMiniCart.visible()
            tvBmgmCartDiscount.visible()

            if (data.tiersApplied.isNotEmpty()) {
                tvBmgmCartDiscount.text = data.offerMessage.parseAsHtml()
                tvBmgmCartDiscount.visible()
                rvBmgmMiniCart.visible()
            } else {
                tvBmgmCartDiscount.gone()
                rvBmgmMiniCart.gone()
            }

            miniCartAdapter.clearAllElements()
            miniCartAdapter.addElement(getProductList(data))
        }
    }

    private fun setupFooterView(data: BmgmMiniCartDataUiModel) {
        binding?.containerSubTotal?.visible()
        footerBinding?.run {
            if (data.tiersApplied.isEmpty()) {
                tvBmgmFinalPrice.text =
                    context.getString(R.string.mini_cart_widget_label_total_price_unavailable_default)
                btnBmgmOpenCart.isEnabled = false
            } else {
                btnBmgmOpenCart.isEnabled = true
                tvBmgmFinalPrice.text = data.getPriceAfterDiscountStr()
                tvBmgmPriceBeforeDiscount.text = data.getPriceBeforeDiscountStr()

                btnBmgmOpenCart.setOnClickListener {
                    viewModel.setCartListCheckboxState(getCartIds(data.tiersApplied))
                }
            }
        }
    }

    private fun getCartIds(tiersApplied: List<BmgmMiniCartVisitable.TierUiModel>): List<String> {
        return tiersApplied.map { it.products }.flatten().map { it.cartId }
    }

    private fun getProductList(data: BmgmMiniCartDataUiModel): List<BmgmMiniCartVisitable> {
        val productList = mutableListOf<BmgmMiniCartVisitable>()
        val hasReachMaxDiscount = data.hasReachMaxDiscount
        data.tiersApplied.forEach { t ->
            if (t.isDiscountTier()) {
                productList.add(t)
            } else {
                t.products.forEach { p ->
                    productList.add(p)
                }
                if (!hasReachMaxDiscount) {
                    productList.add(BmgmMiniCartVisitable.PlaceholderUiModel)
                }
            }
        }
        return productList
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmMiniCartItemDecoration())
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = miniCartAdapter
        }
    }

    private fun initInjector() {
        DaggerBmgmComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }
}