package com.tokopedia.product.detail.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.productView.PartialHeaderView
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_detail.*
import javax.inject.Inject

class ProductDetailFragment: BaseDaggerFragment() {
    private var productParams: ProductParams? = null
    lateinit var headerView: PartialHeaderView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel

    companion object {
        private const val ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA"
        private const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"

        fun newInstance(data: ProductParams, isFromDeeplink: Boolean = false) =
                ProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, data)
                        putBoolean(ARG_FROM_DEEPLINK, isFromDeeplink)
                    }
                }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ProductDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            productParams = it.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA)
        }
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            productInfoViewModel = viewModelProvider.get(ProductInfoViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productInfoViewModel.productInfoResp.observe(this, Observer { when(it){
            is Success -> onSuccessGetProductInfo(it.data)
            is Fail -> onErrorGetProductInfo(it.throwable)
        } })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if (!::headerView.isInitialized){
            headerView = PartialHeaderView.build(view)
        }

        initView()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = FlingBehavior(nested_scroll)
        }

        loadProductData()
    }

    private fun initView() {
        collapsing_toolbar.title = ""
        toolbar.title = ""
        activity?.let {
            toolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupByConfiguration(resources.configuration)

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setupByConfiguration(newConfig)
    }

    private fun setupByConfiguration(configuration: Configuration?) {
        configuration?.let {
            val screenWidth = resources.displayMetrics.widthPixels
            if (it.orientation == Configuration.ORIENTATION_LANDSCAPE){
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = screenWidth / 3
                appbar.visibility = View.VISIBLE
            } else if (it.orientation == Configuration.ORIENTATION_PORTRAIT){
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = screenWidth
                appbar.visibility = View.VISIBLE
            }
        }
    }

    private fun loadProductData() {
        productParams?.let {
            if (allProductParamsSet())
                showBasicProductData()
            productInfoViewModel.getProductInfo(GraphqlHelper.loadRawString(resources, R.raw.gql_get_product_info), it, resources)
        }
    }

    override fun onDestroy() {
        productInfoViewModel.productInfoResp.removeObservers(this)
        productInfoViewModel.clear()
        super.onDestroy()
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {

    }

    private fun onSuccessGetProductInfo(data: ProductInfo) {
        headerView.renderData(data)
        view_picture.renderData(data.pictures)
    }

    private fun showBasicProductData() {
        headerView.renderDataTemp(productParams!!)
        view_picture.renderDataTemp(productParams!!)
    }

    private fun allProductParamsSet(): Boolean = productParams != null &&
            /*!productParams!!.productImage.isNullOrEmpty() &&*/ !productParams!!.productName.isNullOrEmpty() &&
            !productParams!!.productPrice.isNullOrEmpty()
}