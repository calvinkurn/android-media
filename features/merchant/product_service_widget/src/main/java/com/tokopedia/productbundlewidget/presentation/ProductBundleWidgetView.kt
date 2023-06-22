package com.tokopedia.productbundlewidget.presentation

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.ProductServiceWidgetConstant.PRODUCT_BUNDLE_APPLINK_WITH_PARAM
import com.tokopedia.common.ProductServiceWidgetConstant.PRODUCT_BUNDLE_REQUEST_CODE
import com.tokopedia.common.ProductServiceWidgetConstant.PRODUCT_ID_DEFAULT_VALUE
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_service_widget.R
import com.tokopedia.productbundlewidget.adapter.ProductBundleWidgetAdapter
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParam
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class ProductBundleWidgetView : BaseCustomView, ProductBundleAdapterListener {

    companion object {
        private const val PADDING_START_ADJUSTMENT_RV = 6
        private const val INVALID_POSITION = -1
    }

    @Inject
    lateinit var viewModel: ProductBundleWidgetViewModel

    private var tfTitle: Typography? = null
    private var rvBundles: RecyclerView? = null
    private var pageSource: String = ""
    private var productId: String = ""
    private val bundleAdapter = ProductBundleWidgetAdapter()
    private var listener: ProductBundleWidgetListener? = null
    private var startActivityResult: ((intent: Intent, requestCode: Int) -> Unit)? = null

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
        itemPosition: Int
    ) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, selectedProduct.productId)
        listener?.onBundleProductClicked(bundle, selectedMultipleBundle, selectedProduct, itemPosition)
    }

    override fun onMultipleBundleActionButtonClicked(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>,
        bundlePosition: Int
    ) {
        goToProductPage(selectedMultipleBundle, productDetails, bundlePosition)
    }

    override fun onMultipleBundleMoreProductClicked(
        selectedMultipleBundle: BundleDetailUiModel,
        bundleProductGrouped: List<BundleProductUiModel>,
        bundleProductAll: List<BundleProductUiModel>
    ) {
        goToProductPage(selectedMultipleBundle, bundleProductAll)
    }

    override fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel,
        bundlePosition: Int
    ) {
        if (startActivityResult != null) {
            val intent = RouteManager.getIntent(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, PRODUCT_ID_DEFAULT_VALUE,
                selectedBundle.bundleId, pageSource)
            startActivityResult?.invoke(intent, PRODUCT_BUNDLE_REQUEST_CODE)
        } else {
            RouteManager.route(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, PRODUCT_ID_DEFAULT_VALUE,
                selectedBundle.bundleId, pageSource)
        }
        listener?.onSingleBundleActionButtonClicked(selectedBundle, bundleProducts, bundlePosition)
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
        listener?.impressionSingleBundle(selectedSingleBundle, selectedProduct, bundleName, bundlePosition)
    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        listener?.impressionMultipleBundle(selectedMultipleBundle, bundlePosition)
    }

    override fun impressionProductBundleMultiple(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        listener?.impressionMultipleBundle(bundle, selectedMultipleBundle, bundlePosition)
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    ) {
        listener?.impressionMultipleBundleProduct(selectedProduct, selectedMultipleBundle)
    }

    override fun impressionProductItemBundleMultiple(
        bundle: BundleUiModel,
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    ) {
        listener?.impressionMultipleBundleProduct(bundle, selectedProduct, selectedMultipleBundle)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val lifecycleOwner = context as? LifecycleOwner
        lifecycleOwner?.run {
            viewModel.bundleUiModels.observe(this) {
                bundleAdapter.updateDataSet(it)
            }
            viewModel.error.observe(this) {
                listener?.onError(it)
            }
            viewModel.isBundleEmpty.observe(this) {
                tfTitle?.isVisible = !it && tfTitle?.text?.isNotEmpty().orTrue()
                if (it) listener?.onBundleEmpty()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
        startActivityResult = null
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_product_bundle_widget, this)
        rvBundles = view.findViewById(R.id.rv_bundles)
        tfTitle = view.findViewById(R.id.tf_title)
        rvBundles?.let {
            setupItems(it)
            defineCustomAttributes(attrs)
            adjustPadding(it)
        }
        initInjector()
    }

    private fun setupItems(rvBundles: RecyclerView) {
        rvBundles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bundleAdapter
            bundleAdapter.setListener(this@ProductBundleWidgetView)
        }
    }

    private fun adjustPadding(rvBundles: RecyclerView) {
        tfTitle?.setMargin(paddingStart, paddingTop, paddingEnd, 0)
        rvBundles.setPadding(paddingStart - PADDING_START_ADJUSTMENT_RV.toPx(), 0, paddingEnd, paddingBottom)
        setPadding(0,0,0,0)
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

    private fun goToProductPage(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>,
        bundlePosition: Int = INVALID_POSITION
    ) {
        val fixedProductId = if (productId.isNotEmpty()) productId else PRODUCT_ID_DEFAULT_VALUE
        if (startActivityResult != null) {
            val intent = RouteManager.getIntent(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, fixedProductId,
                selectedMultipleBundle.bundleId, pageSource)
            startActivityResult?.invoke(intent, PRODUCT_BUNDLE_REQUEST_CODE)
        } else {
            RouteManager.route(context, PRODUCT_BUNDLE_APPLINK_WITH_PARAM, fixedProductId,
                selectedMultipleBundle.bundleId, pageSource)
        }
        if (bundlePosition == INVALID_POSITION) {
            listener?.onMultipleBundleActionButtonClicked(
                selectedBundle = selectedMultipleBundle,
                productDetails = productDetails
            )
        } else {
            listener?.onMultipleBundleActionButtonClicked(
                selectedBundle = selectedMultipleBundle,
                productDetails = productDetails,
                bundlePosition = bundlePosition
            )
        }
    }

    fun setTitleText(text: String) {
        tfTitle?.setTextAndCheckShow(text)
    }

    fun setTitleTextColor(@ColorRes color: Int) {
        tfTitle?.setTextColor(MethodChecker.getColor(context, color))
    }

    fun setListener(listener: ProductBundleWidgetListener) {
        this.listener = listener
    }

    fun startActivityResult(startActivityResult: (intent: Intent, requestCode: Int) -> Unit) {
        this.startActivityResult = startActivityResult
    }

    fun getBundleData(param: GetBundleParam) {
        pageSource = param.pageSource
        productId = param.productId
        param.apply {
            viewModel.getBundleInfo(param)
        }
    }

    fun setBundlingCarouselTopMargin(margin: Int) {
        try {
            rvBundles?.setMargin(paddingStart, margin, paddingEnd, paddingBottom)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

}
