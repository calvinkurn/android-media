package com.tokopedia.product.manage.feature.cashback.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.di.DaggerProductManageSetCashbackComponent
import com.tokopedia.product.manage.feature.cashback.di.ProductManageSetCashbackComponent
import com.tokopedia.product.manage.feature.cashback.di.ProductManageSetCashbackModule
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.SetCashbackAdapter
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.SetCashbackAdapterTypeFactory
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackViewModel
import com.tokopedia.product.manage.feature.cashback.presentation.viewmodel.ProductManageSetCashbackViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import kotlinx.android.synthetic.main.fragment_product_manage_set_cashback.*
import javax.inject.Inject

class ProductManageSetCashbackFragment : Fragment(), SelectClickListener,
        HasComponent<ProductManageSetCashbackComponent> {

    companion object {

        const val SET_CASHBACK_CACHE_MANAGER_KEY = "set_cashback_cache_id"
        const val SET_CASHBACK_PRODUCT = "set_cashback_product"
        const val ZERO_CASHBACK = 0
        const val THREE_PERCENT_CASHBACK = 3
        const val FOUR_PERCENT_CASHBACK = 4
        const val FIVE_PERCENT_CASHBACK = 5
        const val PERCENT = 100

        fun createInstance(cacheManagerId: String): ProductManageSetCashbackFragment{
            return ProductManageSetCashbackFragment().apply {
                arguments = Bundle().apply {
                    putString(SET_CASHBACK_CACHE_MANAGER_KEY, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageSetCashbackViewModel

    private var adapter: SetCashbackAdapter? = null

    override fun getComponent(): ProductManageSetCashbackComponent? {
        return activity?.run {
            DaggerProductManageSetCashbackComponent
                    .builder()
                    .productManageSetCashbackModule(ProductManageSetCashbackModule())
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        var cacheManagerId = ""
        arguments?.let {
            cacheManagerId = it.getString(SET_CASHBACK_CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val cacheManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        val product : ProductViewModel? = cacheManager?.get(SET_CASHBACK_PRODUCT, ProductViewModel::class.java)
        product?.let {
            viewModel.updateProduct(it)
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
        set_cashback_recycler_view.adapter = adapter
        set_cashback_recycler_view.layoutManager = LinearLayoutManager(this.context)
        initView()
        observeProduct()
    }

    override fun onSelectClick(element: SelectViewModel) {
        viewModel.updateCashback(element.value.toIntOrZero())
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.let {
            val cacheManagerId = it.id
            it.put(SET_CASHBACK_PRODUCT, viewModel.product.value)
            this.activity?.setResult(Activity.RESULT_OK, Intent().putExtra(SET_CASHBACK_CACHE_MANAGER_KEY, cacheManagerId))
            this.activity?.finish()
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        initHeader()
    }

    private fun initHeader() {
        context?.let {
            set_cashback_header.title = it.resources.getString(R.string.product_manage_set_cashback_header_title)
        }
        set_cashback_header.isShowBackButton = true
        set_cashback_header.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        set_cashback_header.isShowShadow = false
    }

    private fun initSetCashbackList(product: ProductViewModel) {
        val setCashbackList = mutableListOf<SetCashbackViewModel>()
        context?.let {
            it.resources.let { resources ->
                setCashbackList.addAll(
                        listOf(
                                SetCashbackViewModel(
                                        resources.getString(R.string.product_manage_set_cashback_no_cashback),
                                        ZERO_CASHBACK,
                                        product.cashBack == ZERO_CASHBACK),
                                SetCashbackViewModel(
                                        resources.getString(R.string.product_manage_set_cashback_three_percent,
                                                product.price?.let { price -> getCashbackPrice(price, THREE_PERCENT_CASHBACK) }),
                                        THREE_PERCENT_CASHBACK,
                                        product.cashBack == THREE_PERCENT_CASHBACK),
                                SetCashbackViewModel(
                                        resources.getString(R.string.product_manage_set_cashback_four_percent,
                                                product.price?.let { price -> getCashbackPrice(price, FOUR_PERCENT_CASHBACK) }),
                                        FOUR_PERCENT_CASHBACK,
                                        product.cashBack == FOUR_PERCENT_CASHBACK),
                                SetCashbackViewModel(
                                        resources.getString(R.string.product_manage_set_cashback_five_percent,
                                                product.price?.let { price -> getCashbackPrice(price, FIVE_PERCENT_CASHBACK) }),
                                        FIVE_PERCENT_CASHBACK,
                                        product.cashBack == FIVE_PERCENT_CASHBACK)
                        )

                )
            }
        }
        adapter?.updateCashback(setCashbackList)
    }

    private fun observeProduct() {
        viewModel.product.observe(this, Observer {
            updateView(it)
        })
    }

    private fun updateView(product: ProductViewModel) {
        set_cashback_header.subtitle = product.title.toString()
        initSetCashbackList(product)
    }

    private fun getCashbackPrice(price: String, cashback : Int): String {
        return (price.toIntOrZero()*cashback/ PERCENT).getCurrencyFormatted()
    }


}