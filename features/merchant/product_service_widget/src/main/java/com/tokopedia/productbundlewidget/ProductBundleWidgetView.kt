package com.tokopedia.productbundlewidget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_service_widget.R
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleShopUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

class ProductBundleWidgetView : BaseCustomView {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    fun createViewModel(storeOwner: ViewModelStoreOwner, lifecycleOwner: LifecycleOwner) {
        val viewModel = ViewModelProvider(storeOwner, viewModelFactory).get(ProductBundleWidgetViewModel::class.java)
        viewModel.testdata.observe(lifecycleOwner) {
            it.forEach {
                println(it)
            }
        }
        viewModel.getBundleInfo(5995723895, "")
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_product_bundle_widget, this)
        val rvBundles: RecyclerView = view.findViewById(R.id.rv_bundles)
        setupItems(rvBundles)
        defineCustomAttributes(attrs)
        initInjector()
    }

    private fun setupItems(rvBundles: RecyclerView) {
        val bundleAdapter = ProductBundleWidgetAdapter()
        val product = listOf(
            BundleProductUiModel(productName = "satu", productImageUrl = "https://placekitten.com/50/50"),
            BundleProductUiModel(productName = "satu", productImageUrl = "https://placekitten.com/52/52"),
        )
        val shop = BundleShopUiModel(
            "", "Test Shop", "https://placekitten.com/30/30"
        )
        val bundle = listOf(
            BundleDetailUiModel(products = product, shopInfo = shop)
        )
        val listdata = listOf(
            BundleUiModel("Multi1", bundleDetails = bundle),
            BundleUiModel("Multi2", bundleDetails = bundle),
            BundleUiModel("Multi3", bundleDetails = bundle),
            BundleUiModel("Multi4", bundleDetails = bundle),
        )
        bundleAdapter.updateDataSet(listdata)
        rvBundles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bundleAdapter
        }
    }

    private fun initInjector() {
        DaggerProductBundleComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ProductBundleWidgetView, 0, 0)

            try {
            } finally {
                styledAttributes.recycle()
            }
        }
    }
}
