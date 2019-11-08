package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.widget.SquareHFrameLayout
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import javax.inject.Inject

class DynamicProductDetailFragment : BaseListFragment<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>() {

    companion object {

    }

    private var shouldShowCartAnimation = false
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DynamicProductDetailViewModel::class.java)
    }

    //Listener function
    private lateinit var initToolBarMethod: () -> Unit

    //Data
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private lateinit var remoteConfig: RemoteConfig
    private var useVariant = true

    //View
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(::onPictureProductClicked, childFragmentManager) }
    private var menu: Menu? = null
    private lateinit var varToolbar: Toolbar
    private lateinit var actionButtonView: PartialButtonActionView
    private lateinit var stickyLoginView: StickyLoginView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dynamic_product_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePartialView()
        initView()
        initializeSearchToolbar()
        initializeStickyLogin(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.run {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_pdp
    }

    override fun getAdapterTypeFactory(): DynamicProductDetailAdapterFactoryImpl = adapterFactory

    override fun onItemClicked(t: DynamicPDPDataModel) {
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerProductDetailComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        }
    }

    override fun loadData(page: Int) {
        viewModel.getProductP1()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod.invoke()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.productLayout.observe(this, Observer {
            when(it) {
                is Success ->{
                    Log.e("datanya","${it.data}")
                }
                is Fail -> {
                    Log.e("datanya","${it.throwable.message}")
                }
            }
        })

        viewModel.productSnapshotDataModel.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderList(listOf(it.data))
                }
                is Fail -> {

                }
            }
        })
    }

    private fun initView() {
        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        renderPartialData()
        activity?.let {
            varToolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initializeStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        stickyLoginView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), ProductDetailFragment.REQUEST_CODE_LOGIN)
            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.PDP)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            stickyLoginView.dismiss(StickyLoginConstant.Page.PDP)
            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.PDP)
            updateStickyState()
        })

        updateStickyContent()
    }

    private fun updateStickyState() {
        if (this.tickerDetail == null) {
            stickyLoginView.visibility = View.GONE
            return
        }

        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_PDP, true)
        if (!isCanShowing) {
            stickyLoginView.visibility = View.GONE
            return
        }

        val userSession = UserSession(activity)
        if (userSession.isLoggedIn) {
            stickyLoginView.visibility = View.GONE
            return
        }

        this.tickerDetail?.let { stickyLoginView.setContent(it) }
        stickyLoginView.show(StickyLoginConstant.Page.PDP)
        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.PDP)
    }

    private fun updateStickyContent() {
        viewModel.getStickyLoginContent(
                onSuccess = {
                    this.tickerDetail = it
                    updateStickyState()
                    updateActionButtonShadow()
                },
                onError = {
                    stickyLoginView.visibility = View.GONE
                }
        )
    }


    private fun initializeSearchToolbar() {
        et_search.setOnClickListener {
            productDetailTracking.eventClickSearchBar()
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")
    }

    private fun renderPartialData() {
        actionButtonView.visibility = true
        actionButtonView.renderData(false)
    }

    private fun initializePartialView() {
        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
        }
    }

    private val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_favorite -> onShopFavoriteClick()
            R.id.send_msg_shop, R.id.btn_topchat -> onShopChatClicked()
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail()
            R.id.btn_apply_leasing -> onApplyLeasingClicked()
            else -> {
            }
        }
    }

    private fun onApplyLeasingClicked() {
//        productInfo?.run {
//            productDetailTracking.eventClickApplyLeasing(
//                    parentProductId,
//                    variant.isVariant
//            )
//            goToNormalCheckout(APPLY_CREDIT)
//        }
    }

    private fun gotoShopDetail() {
//        activity?.let {
//            val shopId = productInfo?.basic?.shopID?.toString() ?: return
//            startActivityForResult(RouteManager.getIntent(it,
//                    ApplinkConst.SHOP, shopId),
//                    ProductDetailFragment.REQUEST_CODE_SHOP_INFO)
//        }
    }

    private fun onShopFavoriteClick() {
//        val shop = shopInfo ?: return
//        activity?.let {
//            if (productInfoViewModel.isUserSessionActive()) {
//                productShopView.toggleClickableFavoriteBtn(false)
//                productInfoViewModel.toggleFavorite(shop.shopCore.shopID,
//                        this::onSuccessFavoriteShop, this::onFailFavoriteShop)
//            } else {
//                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
//                        ProductDetailFragment.REQUEST_CODE_LOGIN)
//            }
//        }
    }

    private fun onShopChatClicked() {
//        val shop = shopInfo ?: return
//        val product = productInfo ?: return
//        activity?.let {
//            if (productInfoViewModel.isUserSessionActive()) {
//                val intent = RouteManager.getIntent(it,
//                        ApplinkConst.TOPCHAT_ASKSELLER,
//                        shop.shopCore.shopID, "",
//                        "product", shop.shopCore.name, shop.shopAssets.avatar)
//                productInfoViewModel.putChatProductInfoTo(intent, productId, productInfo, userInputVariant)
//                startActivity(intent)
//            } else {
//                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
//                        ProductDetailFragment.REQUEST_CODE_LOGIN)
//            }
//        }
//        productDetailTracking.eventSendMessage()
//        productDetailTracking.eventSendChat(productId ?: "")
    }


    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                varToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                varToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }

                varToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
            }
        }
    }

    private fun setBadgeMenuCart(menuCart: MenuItem) {
        activity?.run {
            val actionView = menuCart.actionView
            val cartImageView = actionView.cart_image_view
            val lottieCartView = actionView.cart_lottie_view
            cartImageView.setOnClickListener {
                gotoCart()
            }
            if (shouldShowCartAnimation) {
                if (actionView is SquareHFrameLayout) {
                    if (lottieCartView.visibility != View.VISIBLE) {
                        lottieCartView.addAnimatorListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator?) {}

                            override fun onAnimationEnd(animator: Animator?) {
                                showBadgeMenuCart(cartImageView, lottieCartView, true)
                                shouldShowCartAnimation = false
                            }

                            override fun onAnimationCancel(animator: Animator?) {}

                            override fun onAnimationStart(animator: Animator?) {}
                        })
                        cartImageView.visibility = View.INVISIBLE
                        lottieCartView.visibility = View.VISIBLE
                        if (!lottieCartView.isAnimating) {
                            lottieCartView.playAnimation()
                        }
                    }
                }
            } else {
                showBadgeMenuCart(cartImageView, lottieCartView, false)
            }
        }
    }

    private fun gotoCart() {

    }

    private fun showBadgeMenuCart(cartImageView: ImageView, lottieCartView: LottieAnimationView, animate: Boolean) {
        activity?.run {
            val localCacheHandler = LocalCacheHandler(context, "CART")
            val cartCount = localCacheHandler.getInt("CACHE_TOTAL_CART", 0)

            val icon = ContextCompat.getDrawable(this, cartImageView.tag as Int)
            if (icon is LayerDrawable) {
                val badge = CountDrawable(this)
                badge.setCount(if (cartCount > ProductDetailFragment.CART_MAX_COUNT) {
                    getString(R.string.pdp_label_cart_count_max)
                } else {
                    cartCount.toString()
                })
                icon.mutate()
                icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                cartImageView.setImageDrawable(icon)
                if (animate) {
                    val alphaAnimation = AlphaAnimation(ProductDetailFragment.CART_ALPHA_ANIMATION_FROM, ProductDetailFragment.CART_ALPHA_ANIMATION_TO)
                    val scaleAnimation = ScaleAnimation(ProductDetailFragment.CART_SCALE_ANIMATION_FROM, ProductDetailFragment.CART_SCALE_ANIMATION_TO, ProductDetailFragment.CART_SCALE_ANIMATION_FROM, ProductDetailFragment.CART_SCALE_ANIMATION_TO, Animation.RELATIVE_TO_SELF, ProductDetailFragment.CART_SCALE_ANIMATION_PIVOT, Animation.RELATIVE_TO_SELF, ProductDetailFragment.CART_SCALE_ANIMATION_PIVOT)
                    scaleAnimation.fillAfter = false
                    val animationSet = AnimationSet(false)
                    animationSet.addAnimation(alphaAnimation)
                    animationSet.addAnimation(scaleAnimation)
                    animationSet.duration = ProductDetailFragment.CART_ANIMATION_DURATION
                    animationSet.fillAfter = false
                    animationSet.fillBefore = false
                    animationSet.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {}

                        override fun onAnimationEnd(p0: Animation?) {
                            lottieCartView.clearAnimation()
                            cartImageView.clearAnimation()
                            lottieCartView.visibility = View.INVISIBLE
                            cartImageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationStart(p0: Animation?) {}
                    })
                    lottieCartView.startAnimation(animationSet)
                } else {
                    lottieCartView.visibility = View.INVISIBLE
                    cartImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateActionButtonShadow() {
        if (stickyLoginView.isShowing()) {
            actionButtonView.setBackground(R.color.white)
        } else {
            val drawable = context?.let { _context -> ContextCompat.getDrawable(_context, R.drawable.bg_shadow_top) }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }


    private fun onPictureProductClicked(position: Int) {
        startActivity(ImagePreviewActivity.getCallingIntent(context!!,
                viewModel.getImageUriPaths(),
                null,
                position))
    }
}