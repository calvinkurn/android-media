package com.tokopedia.product.detail.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.productView.PartialButtonActionView
import com.tokopedia.product.detail.view.fragment.productView.PartialHeaderView
import com.tokopedia.product.detail.view.fragment.productView.PartialProductDescrFullView
import com.tokopedia.product.detail.view.fragment.productView.PartialProductStatisticView
import com.tokopedia.product.detail.view.util.AppBarState
import com.tokopedia.product.detail.view.util.AppBarStateChangeListener
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_detail.*
import javax.inject.Inject

class ProductDetailFragment: BaseDaggerFragment() {
    private var productParams: ProductParams? = null
    lateinit var headerView: PartialHeaderView
    lateinit var productStatsView: PartialProductStatisticView
    lateinit var productDescrView: PartialProductDescrFullView
    lateinit var actionButtonView: PartialButtonActionView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel

    private var isAppBarCollapsed = false
    private var menu: Menu? = null

    var productInfo: ProductInfo? = null

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
        setHasOptionsMenu(true)
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

        if (!::headerView.isInitialized){
            headerView = PartialHeaderView.build(view)
        }

        if (!::productStatsView.isInitialized){
            productStatsView = PartialProductStatisticView.build(view)
        }

        if (!::productDescrView.isInitialized){
            productDescrView = PartialProductDescrFullView.build(view)
        }

        if (!::actionButtonView.isInitialized){
            actionButtonView = PartialButtonActionView.build(view)
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
        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: Int) {
                when(state){
                    AppBarState.EXPANDED -> {
                        isAppBarCollapsed = false
                        expandedAppBar()
                    }
                    AppBarState.COLLAPSED -> {
                        isAppBarCollapsed = true
                        collapsedAppBar()
                    }
                    AppBarState.IDLE -> {}
                }
            }
        })

    }

    private fun collapsedAppBar() {
        initStatusBarLight()
        initToolbarLight()
        fab_detail.hide()
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                if (menu != null && menu!!.size() > 2) {
                    menu!!.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                    //val cartCount = (activity!!.applicationContext as PdpRouter).getCartCount(getActivityContext())
                    menu!!.findItem(R.id.action_cart).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_dark)
                    /*if (cartCount > 0) {
                        setDrawableCount(context, cartCount)
                    }*/
                }
                toolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
            }
        }
    }

    private fun initStatusBarLight() {
        activity?.run {
            if (window == null) return@run
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP  && isAdded) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.green_600)
            }
        }
    }

    private fun expandedAppBar() {
        initStatusBarDark()
        initToolbarTransparent()
        fab_detail.show()
    }

    private fun initToolbarTransparent() {
        activity?.run {
            if (isAdded) {
                collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
                collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
                toolbar.background = ContextCompat.getDrawable(this, R.drawable.gradient_shadow_black_vertical)
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_light)
                if (menu != null && menu!!.size() > 1) {
                    menu!!.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_light)
                    //val cartCount = (activity!!.applicationContext as PdpRouter).getCartCount(getActivityContext())
                    menu!!.findItem(R.id.action_cart).icon = (ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_light))
                    /*if (cartCount > 0) {
                        setDrawableCount(context, cartCount)
                    }*/
                }
                toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_product_more_light)
            }
        }
    }

    private fun initStatusBarDark() {
        activity?.run {
            if (window == null) return@run
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP  && isAdded) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent_dark_40)
            }
        }
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
        productInfo = data
        headerView.renderData(data)
        view_picture.renderData(data.pictures)
        productStatsView.renderData(data)
        productDescrView.renderData(data)
        actionButtonView.renderData(data.basic.status, productInfoViewModel.isShopOwner(data.shop.id), data.preorder)
        activity?.invalidateOptionsMenu()
    }

    private fun showBasicProductData() {
        headerView.renderDataTemp(productParams!!)
        view_picture.renderDataTemp(productParams!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(if (isAppBarCollapsed) R.menu.menu_product_detail_dark else
            R.menu.menu_product_detail_light, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        activity?.let {
            handlingMenuPreparation(menu)
            // handling cart counter
        }
    }

    private fun handlingMenuPreparation(menu: Menu?){
        if (menu == null) return

        val menuShare = menu.findItem(R.id.action_share)
        val menuCart = menu.findItem(R.id.action_cart)
        val menuReport = menu.findItem(R.id.action_report)
        val menuWarehouse = menu.findItem(R.id.action_warehouse)
        val menuEtalase = menu.findItem(R.id.action_etalase)

        if (productInfo == null){
            menuShare.isVisible = false
            menuShare.isEnabled = false
            menuCart.isVisible = false
            menuCart.isEnabled = false
            menuReport.isVisible = false
            menuReport.isEnabled = false
            menuWarehouse.isVisible = false
            menuWarehouse.isEnabled = false
            menuEtalase.isEnabled = false
            menuEtalase.isVisible = false
        } else {
            menuShare.isVisible = true
            menuShare.isEnabled = true

            // handled on P2 when shop is loaded
            menuWarehouse.isVisible = false
            menuWarehouse.isEnabled = false
            menuEtalase.isEnabled = false
            menuEtalase.isVisible = false

            val isOwned = productInfoViewModel.isShopOwner(productInfo!!.shop.id)
            val isSellerApp = GlobalConfig.isSellerApp()
            val isWareHousing = productInfo!!.basic.status == ProductDetailConstant.PRD_STATE_WAREHOUSE

            menuCart.isVisible = !isOwned && !isSellerApp
            menuCart.isEnabled = !isOwned && !isSellerApp

            menuReport.isVisible = !isOwned && !isWareHousing
            menuReport.isEnabled = !isOwned && !isWareHousing
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            android.R.id.home -> { activity?.onBackPressed(); true }
            R.id.action_share -> { shareProduct(); true }
            R.id.action_cart -> { gotoCart(); true}
            R.id.action_report -> { reportProduct(); true }
            R.id.action_warehouse -> { gotoWarehouse(); true }
            R.id.action_etalase -> { gotoEtalase(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gotoEtalase() {

    }

    private fun gotoWarehouse() {

    }

    private fun reportProduct() {

    }

    private fun gotoCart() {

    }

    private fun shareProduct() {

    }

    private fun allProductParamsSet(): Boolean = productParams != null &&
            !productParams!!.productName.isNullOrEmpty() &&
            !productParams!!.productPrice.isNullOrEmpty()
}