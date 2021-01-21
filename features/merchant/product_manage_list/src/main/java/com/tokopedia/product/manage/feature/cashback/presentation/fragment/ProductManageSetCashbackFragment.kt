package com.tokopedia.product.manage.feature.cashback.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.di.DaggerProductManageSetCashbackComponent
import com.tokopedia.product.manage.feature.cashback.di.ProductManageSetCashbackComponent
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.SetCashbackAdapter
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.SetCashbackAdapterTypeFactory
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel
import com.tokopedia.product.manage.feature.cashback.presentation.viewmodel.ProductManageSetCashbackViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_RESULT_STATUS
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_manage_set_cashback.*
import javax.inject.Inject

class ProductManageSetCashbackFragment : Fragment(), SelectClickListener,
        HasComponent<ProductManageSetCashbackComponent> {

    companion object {
        const val SET_CASHBACK_CACHE_MANAGER_KEY = "set_cashback_cache_id"
        const val SET_CASHBACK_RESULT = "set_cashback_result"
        const val SET_CASHBACK_RESULT_STATUS = "set_cashback_result_status"
        const val ZERO_CASHBACK = 0
        const val THREE_PERCENT_CASHBACK = 3
        const val FOUR_PERCENT_CASHBACK = 4
        const val FIVE_PERCENT_CASHBACK = 5
        const val PERCENT = 100
        const val SET_CASHBACK_PRODUCT_ID = "id"
        const val PARAM_SET_CASHBACK_VALUE = "cashback"
        const val SET_CASHBACK_PRODUCT_NAME = "product_name"
        const val PARAM_SET_CASHBACK_PRODUCT_PRICE = "price"
        const val PARAM_SET_CASHBACK_SHOP_ID = "shop_id"
        const val PARAM_SET_CASHBACK_IS_DRAFTING = "is_drafting"
        const val EXTRA_CASHBACK_SHOP_ID = "extra_shop_id"
        const val EXTRA_CASHBACK_IS_DRAFTING = "extra_is_drafting"

        fun createInstance(productId: String, cashback: Int, productName: String, price: String): ProductManageSetCashbackFragment{
            return ProductManageSetCashbackFragment().apply {
                arguments = Bundle().apply {
                    putString(SET_CASHBACK_PRODUCT_ID, productId)
                    putInt(PARAM_SET_CASHBACK_VALUE, cashback)
                    putString(SET_CASHBACK_PRODUCT_NAME, productName)
                    putString(PARAM_SET_CASHBACK_PRODUCT_PRICE, price)
                }
            }
        }

        fun createInstance(productId: String, cashback: Int, productName: String, price: String, shopId: String, isDrafting: Boolean): ProductManageSetCashbackFragment{
            return ProductManageSetCashbackFragment().apply {
                arguments = Bundle().apply {
                    putString(SET_CASHBACK_PRODUCT_ID, productId)
                    putInt(PARAM_SET_CASHBACK_VALUE, cashback)
                    putString(SET_CASHBACK_PRODUCT_NAME, productName)
                    putString(PARAM_SET_CASHBACK_PRODUCT_PRICE, price)
                    putString(PARAM_SET_CASHBACK_SHOP_ID, shopId)
                    putBoolean(PARAM_SET_CASHBACK_IS_DRAFTING, isDrafting)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageSetCashbackViewModel

    private var adapter: SetCashbackAdapter? = null
    private var productId = ""
    private var cashback = 0
    private var productName = ""
    private var price = ""
    private var shopId = ""
    private var isDrafting = false

    override fun getComponent(): ProductManageSetCashbackComponent? {
        return activity?.run {
            DaggerProductManageSetCashbackComponent
                    .builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            productId =  it.getString(SET_CASHBACK_PRODUCT_ID, "")
            cashback = it.getInt(PARAM_SET_CASHBACK_VALUE)
            productName = it.getString(SET_CASHBACK_PRODUCT_NAME, "")
            price = it.getString(PARAM_SET_CASHBACK_PRODUCT_PRICE, "")
            shopId = it.getString(PARAM_SET_CASHBACK_SHOP_ID, "")
            isDrafting = it.getBoolean(PARAM_SET_CASHBACK_IS_DRAFTING, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_manage_set_cashback, container, false)
        val adapterTypeFactory = SetCashbackAdapterTypeFactory(this)
        adapter = SetCashbackAdapter(adapterTypeFactory)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCashbackRecyclerView.adapter = adapter
        setCashbackRecyclerView.layoutManager = LinearLayoutManager(this.context)
        initView()
        observeSetCashback()
    }

    override fun onSelectClick(element: SelectUiModel) {
        cashback = element.value.toIntOrZero()
        setCashbackList()
        if(shopId.isNotBlank() && !isDrafting) {
            if(cashback != 0) {
                ProductManageTracking.eventClickCashbackValue(cashback, shopId)
            } else {
                ProductManageTracking.eventClickNoCashbackValue(shopId)
            }
        }
    }

    fun setTrackerOnBackPressed() {
        if(shopId.isNotBlank() && !isDrafting) {
            ProductManageTracking.eventClickBackOnPromotionPage(shopId)
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        initHeader()
        initButton()
        setCashbackList()
    }

    private fun initHeader() {
        context?.let {
            setCashbackHeader.title = it.resources.getString(R.string.product_manage_set_cashback_header_title)
        }
        setCashbackHeader.isShowBackButton = true
        setCashbackHeader.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        setCashbackHeader.isShowShadow = false
        setCashbackHeader.subtitle = productName
    }

    private fun initButton() {
        submitCashbackButton.setOnClickListener {
            viewModel.setCashback(productId, productName, cashback)
            if (!isDrafting) {
                if(shopId.isNotBlank()) {
                    ProductManageTracking.eventClickSavePromotion(shopId)
                } else {
                    ProductManageTracking.eventCashbackSettingsSave(productId)
                }
            }
        }
    }

    private fun setCashbackList() {
        val setCashbackList = mutableListOf<SetCashbackUiModel>()
        context?.let {
            setCashbackList.addAll(
                    listOf(
                            SetCashbackUiModel(
                                    it.resources.getString(R.string.product_manage_set_cashback_no_cashback),
                                    ZERO_CASHBACK,
                                    cashback == ZERO_CASHBACK),
                            SetCashbackUiModel(
                                    it.resources.getString(R.string.product_manage_set_cashback_three_percent,
                                            getCashbackPrice(price, THREE_PERCENT_CASHBACK)),
                                    THREE_PERCENT_CASHBACK,
                                    cashback == THREE_PERCENT_CASHBACK),
                            SetCashbackUiModel(
                                    it.resources.getString(R.string.product_manage_set_cashback_four_percent,
                                            getCashbackPrice(price, FOUR_PERCENT_CASHBACK)),
                                    FOUR_PERCENT_CASHBACK,
                                    cashback == FOUR_PERCENT_CASHBACK),
                            SetCashbackUiModel(
                                    it.resources.getString(R.string.product_manage_set_cashback_five_percent,
                                            getCashbackPrice(price, FIVE_PERCENT_CASHBACK)),
                                    FIVE_PERCENT_CASHBACK,
                                    cashback == FIVE_PERCENT_CASHBACK)
                    )
            )
        }
        adapter?.updateCashback(setCashbackList)
    }

    private fun getCashbackPrice(price: String, cashback : Int): String {
        return (price.toIntOrZero()*cashback/ PERCENT).getCurrencyFormatted()
    }

    private fun observeSetCashback() {
        observe(viewModel.setCashbackResult) {
            when (it) {
                is Success -> {
                    val extraCacheManagerId = activity?.intent?.data?.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID).orEmpty()
                    val cacheManager = if (extraCacheManagerId.isNotBlank()) {
                        context?.let { context -> SaveInstanceCacheManager(context, extraCacheManagerId) }
                    } else {
                        context?.let { context -> SaveInstanceCacheManager(context, true) }
                    }
                    cacheManager?.let { manager ->
                        val cacheManagerId = manager.id
                        manager.put(SET_CASHBACK_RESULT, it.data)
                        manager.put(EXTRA_RESULT_STATUS, Activity.RESULT_OK)
                        this.activity?.setResult(Activity.RESULT_OK, Intent().putExtra(SET_CASHBACK_CACHE_MANAGER_KEY, cacheManagerId))
                        this.activity?.finish()
                    }
                }
                is Fail -> {
                    onErrorSetCashback(it.throwable as SetCashbackResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun onErrorSetCashback(setCashbackResult: SetCashbackResult) {
        Toaster.build(setCashbackCoordinatorLayout, getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry),
                View.OnClickListener {
                    viewModel.setCashback(productId = setCashbackResult.productId,
                            productName = setCashbackResult.productName, cashback = setCashbackResult.cashback)
                }).show()
    }

}