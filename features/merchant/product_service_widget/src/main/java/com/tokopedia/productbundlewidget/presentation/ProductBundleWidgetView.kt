package com.tokopedia.productbundlewidget.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.ProductServiceWidgetConstant.BUNDLE_ID_DEFAULT_VALUE
import com.tokopedia.common.ProductServiceWidgetConstant.PRODUCT_BUNDLE_APPLINK_WITH_PARAM
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_service_widget.R
import com.tokopedia.productbundlewidget.adapter.ProductBundleWidgetAdapter
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.*
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class ProductBundleWidgetView : BaseCustomView, ProductBundleAdapterListener {

    @Inject
    lateinit var viewModel: ProductBundleWidgetViewModel

    private var tfTitle: Typography? = null
    private var pageSource: String = ""
    private val bundleAdapter = ProductBundleWidgetAdapter()
    private var listener: ProductBundleWidgetListener? = null

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    override fun onBundleProductClicked(
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        productItemPosition: Int
    ) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, selectedProduct.productId)
        listener?.onBundleProductClicked(bundle, selectedMultipleBundle, selectedProduct)
    }

    override fun onMultipleBundleActionButtonClicked(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    ) {
        RouteManager.route(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, BUNDLE_ID_DEFAULT_VALUE,
            selectedMultipleBundle.bundleId, pageSource)
        listener?.onMultipleBundleActionButtonClicked(selectedMultipleBundle, productDetails)
    }

    override fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
    ) {
        RouteManager.route(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, BUNDLE_ID_DEFAULT_VALUE,
            selectedBundle.bundleId, pageSource)
        listener?.onSingleBundleActionButtonClicked(selectedBundle, bundleProducts)
    }

    override fun onTrackSingleVariantChange(
        selectedProduct: BundleProductUiModel,
        selectedSingleBundle: BundleDetailUiModel,
        bundleName: String
    ) {
        listener?.onSingleBundleChipsSelected(selectedProduct, selectedSingleBundle, bundleName)
    }

    override fun impressionProductBundleSingle(
        selectedSingleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        listener?.impressionSingleBundle(selectedSingleBundle, selectedProduct, bundleName)
    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        listener?.impressionMultipleBundle(selectedMultipleBundle, bundlePosition)
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    ) {
        listener?.impressionMultipleBundleProduct(selectedProduct, selectedMultipleBundle)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_product_bundle_widget, this)
        val rvBundles: RecyclerView = view.findViewById(R.id.rv_bundles)
        tfTitle = view.findViewById(R.id.tf_title)
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
                val text = styledAttributes.getString(R.styleable.ProductBundleWidgetView_bundlewidget_title).orEmpty()
                setTitleText(text)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val lifecycleOwner = context as? LifecycleOwner
        lifecycleOwner?.run {
            viewModel.bundleUiModels.observe(this) {
                bundleAdapter.updateDataSet(it)
            }
        }
    }

    fun setTitleText(text: String) {
        tfTitle?.setTextAndCheckShow(text)
    }

    fun setListener(listener: ProductBundleWidgetListener) {
        this.listener = listener
    }

    fun getBundleData(param: GetBundleParam) {
        pageSource = param.pageSource
        param.apply {
            viewModel.getBundleInfo(param)
        }
    }

}
