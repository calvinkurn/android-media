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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_service_widget.R
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

class ProductBundleWidgetView : BaseCustomView, ProductBundleListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val bundleAdapter = ProductBundleWidgetAdapter()

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
        viewModel.bundleUiModels.observe(lifecycleOwner) {
            bundleAdapter.updateDataSet(it)
        }
        viewModel.getBundleInfo(2444029649, "", listOf())
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_product_bundle_widget, this)
        val rvBundles: RecyclerView = view.findViewById(R.id.rv_bundles)
        setupItems(rvBundles)
        defineCustomAttributes(attrs)
        initInjector()
    }

    private fun setupItems(rvBundles: RecyclerView) {
        rvBundles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bundleAdapter
            bundleAdapter.setListener(this@ProductBundleWidgetView)
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

    override fun onBundleProductClicked(
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        productItemPosition: Int
    ) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, selectedProduct.productId)
    }

    override fun addMultipleBundleToCart(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    ) {
        RouteManager.route(context, ApplinkConst.PRODUCT_BUNDLE, selectedMultipleBundle.bundleId)
    }

    override fun addSingleBundleToCart(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
    ) {
        RouteManager.route(context, ApplinkConst.PRODUCT_BUNDLE, selectedBundle.bundleId)
    }

    override fun onTrackSingleVariantChange(
        selectedProduct: BundleProductUiModel,
        selectedSingleBundle: BundleDetailUiModel,
        bundleName: String
    ) {
        println(bundleName)
    }

    override fun impressionProductBundleSingle(
        selectedSingleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        println(bundleName)
    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        println(selectedMultipleBundle)
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    ) {
        println(selectedProduct)
    }
}
