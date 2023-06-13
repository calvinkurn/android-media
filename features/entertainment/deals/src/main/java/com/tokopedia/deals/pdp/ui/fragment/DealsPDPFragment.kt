package com.tokopedia.deals.pdp.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.deals.R
import com.tokopedia.deals.R.string as stringDeals
import com.tokopedia.deals.R.drawable as drawableDeals
import com.tokopedia.deals.R.id as idDeals
import com.tokopedia.abstraction.R.drawable as abstractionDrawables
import com.tokopedia.unifyprinciples.R.color as colorUnify
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.analytics.DealsAnalyticsConstants.Event.EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL
import com.tokopedia.deals.common.analytics.DealsAnalyticsConstants.Event.EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL
import com.tokopedia.deals.common.analytics.DealsAnalyticsConstants.Event.EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL
import com.tokopedia.deals.common.analytics.DealsAnalyticsConstants.Event.EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL
import com.tokopedia.deals.common.model.response.EventProductDetail
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailBinding
import com.tokopedia.deals.pdp.common.DealsPDPIdlingResource
import com.tokopedia.deals.pdp.data.EventContentInnerData
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.share.DealsPDPShare
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.deals.pdp.ui.adapter.DealsRecommendationAdapter
import com.tokopedia.deals.pdp.ui.callback.DealsPDPCallbacks
import com.tokopedia.deals.pdp.ui.utils.DealsPDPMapper
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.deals.pdp.widget.WidgetDealsPDPCarousel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.ref.WeakReference
import java.util.Calendar
import java.util.regex.Pattern
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class DealsPDPFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: DealsAnalytics

    private lateinit var dealsSharePDP: DealsPDPShare

    private var productId: String? = null
    private var binding by autoClearedNullable<FragmentDealsDetailBinding>()
    private val viewModel by viewModels<DealsPDPViewModel> { viewModelFactory }
    private var dealsPDPCallbacks: DealsPDPCallbacks? = null

    private var progressBar: LoaderUnify? = null
    private var secondaryBarLayout: FrameLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var toolbar: Toolbar? = null
    private var menuPDP: Menu? = null
    private var clHeader: ConstraintLayout? = null
    private var imageCarousel: WidgetDealsPDPCarousel? = null
    private var clBaseMainContent: ConstraintLayout? = null
    private var tgDealsDetail: Typography? = null
    private var tgMrp: Typography? = null
    private var tgOff: Typography? = null
    private var tgSalesPrice: Typography? = null
    private var tgExpiredDate: Typography? = null
    private var clOutlets: ConstraintLayout? = null
    private var imgFavorite: ImageView? = null
    private var tgFavorite: Typography? = null
    private var tgAllLocation: Typography? = null
    private var tgViewMap: Typography? = null
    private var tgBrandVenue: Typography? = null
    private var tgBrandAddress: Typography? = null
    private var tgBrandName: Typography? = null
    private var tgNumberOfLocation: Typography? = null
    private var ivBrandLogo: ImageView? = null
    private var clDescription: ConstraintLayout? = null
    private var clTnc: ConstraintLayout? = null
    private var tgExpandableDesc: Typography? = null
    private var tgExpandableTnc: Typography? = null
    private var seeMoreButtonDesc: Typography? = null
    private var seeMoreButtonTnc: Typography? = null
    private var clRedeemInstruction: ConstraintLayout? = null
    private var cardCheckout: CardView? = null
    private var btnCheckout: UnifyButton? = null
    private var tgRecommendation: Typography? = null
    private var rvRecommendation: RecyclerView? = null
    private var globalError: GlobalError? = null
    private var productDetailData: ProductDetailData = ProductDetailData()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        dealsPDPCallbacks = activity as DealsPDPActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EXTRA_PRODUCT_ID)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeFlowData()
        getPDP()
    }

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId ?: "" == R.id.action_menu_share_pdp) {
            share(productDetailData)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUi() {
        view?.apply {
            progressBar = binding?.progressBar
            secondaryBarLayout = binding?.secondaryLayout
            appBarLayout = binding?.appBarLayout
            collapsingToolbarLayout = binding?.collapsingToolbar
            toolbar = binding?.toolbar
            tgDealsDetail = binding?.subView?.tgDealDetails
            clHeader = binding?.clHeader
            clBaseMainContent = binding?.subView?.baseMainContent
            imageCarousel = binding?.carouselDealsPdp
            tgMrp = binding?.subView?.tgMrp
            tgOff = binding?.subView?.tgOff
            tgSalesPrice = binding?.subView?.tgSalesPrice
            imgFavorite = binding?.subView?.ivWishList
            tgFavorite = binding?.subView?.tgFavourite
            tgExpiredDate = binding?.subView?.tgExpiryDate
            clOutlets = binding?.subView?.clOutlets
            tgAllLocation = binding?.subView?.tgSeeAllLocations
            tgViewMap = binding?.subView?.tgViewMap
            tgBrandVenue = binding?.subView?.tgBrandVenue
            tgBrandAddress = binding?.subView?.tgBrandAddress
            tgBrandName = binding?.subView?.tgBrandName
            tgNumberOfLocation = binding?.subView?.tgNumberOfLocations
            ivBrandLogo = binding?.subView?.imageViewBrand
            clDescription = binding?.subView?.clDescription
            clTnc = binding?.subView?.clTnc
            tgExpandableDesc = binding?.subView?.tgExpandableDescription
            tgExpandableTnc = binding?.subView?.tgExpandableTnc
            seeMoreButtonDesc = binding?.subView?.seemorebuttonDescription
            seeMoreButtonTnc = binding?.subView?.seemorebuttonTnc
            clRedeemInstruction = binding?.subView?.clRedeemInstructions
            cardCheckout = binding?.cvCheckout
            btnCheckout = binding?.btnBuynow
            tgRecommendation = binding?.subView?.tgRecommendedDeals
            rvRecommendation = binding?.subView?.recyclerView
            globalError = binding?.globalError
            updateCollapsingToolbar()
        }
    }

    private fun observeFlowData() {
        observePDP()
        observeContent()
        observeRecommendation()
        observeRating()
        observeUpdateRating()
        observeTrackerAPIRecommendation()
        observeTrackerAPIRecentSearch()
    }

    private fun observePDP() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowPDP.collect {
                when (it) {
                    is Success -> {
                        showPDPData(it.data.eventProductDetail.productDetailData)
                        getRecommendation(it.data.eventProductDetail.productDetailData.childCategoryIds)
                        getRating(it.data.eventProductDetail.productDetailData.id)
                    }

                    is Fail -> {
                        DealsPDPIdlingResource.decrement()
                        hideLoading()
                        showErrorState()
                    }
                }
            }
        }
    }

    private fun observeContent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowContent.collect {
                when (it) {
                    is Success -> {
                        showRedeemContent(it.data.eventContentById.data)
                    }

                    is Fail -> {
                        showErrorRedeem()
                    }
                }
            }
        }
    }

    private fun observeRecommendation() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRecommendation.collect {
                when (it) {
                    is Success -> {
                        showRecommendation(it.data.eventSearch.products)
                    }

                    is Fail -> {
                        hideRecommendation()
                    }
                }
            }
        }
    }

    private fun observeRating() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRating.collect {
                when (it) {
                    is Success -> {
                        DealsPDPIdlingResource.decrement()
                        hideLoading()
                        if (it.data.data.isNullOrEmpty()) {
                            setRating(ID_ZERO, Int.ZERO, false, isHideImageRating = true)
                        } else {
                            it.data.data?.first()?.let {
                                setRating(it.productId.toString(), it.totalLikes, it.isLiked)
                            }
                        }

                    }

                    is Fail -> {
                        DealsPDPIdlingResource.decrement()
                        hideLoading()
                        setRating(ID_ZERO, Int.ZERO, false, isHideImageRating = true)
                    }
                }
            }
        }
    }

    private fun observeUpdateRating() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowUpdateRating.collect {
                when (it) {
                    is Success -> {
                        it.data.data.let {
                            setRating(it.productId.toString(), getTotalLikes(), it.isLiked)
                        }
                    }

                    is Fail -> {
                        setRating(ID_ZERO, Int.ZERO, false, isHideImageRating = true)
                    }
                }
            }
        }
    }

    private fun observeTrackerAPIRecommendation() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRecommendationTracking.collect {
                //do nothing
            }
        }
    }

    private fun observeTrackerAPIRecentSearch() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowRecentSearchTracking.collect {
                //do nothing
            }
        }
    }

    private fun getPDP() {
        showLoading()
        productId?.let {
            DealsPDPIdlingResource.increment()
            viewModel.setPDP(it)
        }
    }

    private fun getRecommendation(childCategoryId: String) {
        viewModel.setRecommendation(childCategoryId)
    }

    private fun getRating(productId: String) {
        viewModel.setRating(productId)
    }

    private fun updateRating(productId: String, isLiked: Boolean) {
        if (userSession.isLoggedIn) {
            viewModel.updateRating(productId, userSession.userId, isLiked)
        } else {
            context?.let { context ->
                showToaster(
                    context.resources.getString(stringDeals.deals_pdp_login_likes),
                    Toaster.TYPE_NORMAL
                )
            }
        }
    }

    private fun setIsLiked(isLiked: Boolean) {
        viewModel.isLiked = isLiked
    }

    private fun getIsLiked(): Boolean {
        return viewModel.isLiked
    }

    private fun getTotalLikes(): Int {
        return viewModel.totalLikes
    }

    private fun setTotalLikes(totalLikes: Int) {
        viewModel.totalLikes = totalLikes
    }

    private fun showLoading() {
        progressBar?.show()
        secondaryBarLayout?.show()
        imageCarousel?.shimmering()
    }

    private fun hideLoading() {
        progressBar?.hide()
        secondaryBarLayout?.hide()
    }

    private fun showErrorState() {
        secondaryBarLayout?.show()
        globalError?.show()
        globalError?.setActionClickListener {
            hideErrorState()
            getPDP()
        }
        globalError?.errorSecondaryAction?.hide()
    }

    private fun hideErrorState() {
        globalError?.hide()
        secondaryBarLayout?.hide()
    }

    private fun showPDPData(data: ProductDetailData) {
        analytics.pdpSendScreenName()
        productDetailData = data
        trackPDP(data)
        showShareButton()
        showPDPOptionsMenu(data.displayName)
        showHeader(data.displayName)
        showImageCarousel(data)
        showContent(data)
        showBrand(data)
        showDescription(data)
        showTnc(data)
        showRedeemInstruction(data)
        showCheckout(data)
    }

    private fun trackPDP(data: ProductDetailData) {
        if (userSession.isLoggedIn) {
            viewModel.setTrackingRecommendation(data.id, userSession.userId)
            viewModel.setTrackingRecentSearch(data, userSession.userId)
        }
    }

    private fun showHeader(displayName: String) {
        clHeader?.show()
        collapsingToolbarLayout?.title = displayName
    }

    private fun showContent(data: ProductDetailData) {
        clBaseMainContent?.show()
        updateTitle(data.displayName)
        updateMrp(data.mrp)
        updateSavingPrecentage(data.savingPercentage)
        updateSalesPrice(data.salesPrice)
        updateExpiredDate(data.maxEndDate)
    }

    private fun showBrand(data: ProductDetailData) {
        if (data.outlets.size.isMoreThanZero()) {
            clOutlets?.show()
            if (data.outlets.size == Int.ONE) {
                tgAllLocation?.hide()
            } else {
                tgAllLocation?.show()
            }

            val outlet = data.outlets.first()

            if (outlet.coordinates.isNullOrEmpty()) {
                tgViewMap?.hide()
            } else {
                val imgMaps = MethodChecker.getDrawable(context, drawableDeals.ic_see_location)
                tgViewMap?.show()
                tgViewMap?.setCompoundDrawablesWithIntrinsicBounds(imgMaps, null, null, null)
            }

            tgBrandVenue?.text = outlet.name
            tgBrandAddress?.text = outlet.district
            tgBrandName?.text = data.brand.title
            tgNumberOfLocation?.text = String.format(
                getString(
                    stringDeals.deals_pdp_number_of_items,
                    data.outlets.size
                )
            )
            ivBrandLogo?.loadImage(data.brand.featuredThumbnailImage)

            ivBrandLogo?.setOnClickListener {
                val url = ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE + "?" + data.brand.seoUrl
                RouteManager.route(context, url)
            }

            tgViewMap?.setOnClickListener {
                context?.let {
                    openGoogleMaps(it, outlet.coordinates)
                }
            }

            tgAllLocation?.setOnClickListener {
                analytics.pdpClick(EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL, data.brand.title, data.displayName)
                dealsPDPCallbacks?.onShowAllLocation(data.outlets)
            }

            analytics.pdpViewProduct(data.id, data.salesPrice.toIntSafely().toLong(), data.displayName, data.brand.title)
        } else {
            clOutlets?.hide()
        }
    }

    private fun showDescription(data: ProductDetailData) {
        val desc = DealsUtils.getExpandableItemText(data.longRichDesc)
        if (desc.isNullOrEmpty()) {
            clDescription?.hide()
        } else {
            tgExpandableDesc?.text = Html.fromHtml(desc)
            Handler().postDelayed({
                tgExpandableDesc?.let {
                    if (it.lineCount >= MAX_LINE) {
                        seeMoreButtonDesc?.show()
                    } else {
                        seeMoreButtonDesc?.hide()
                    }
                }
            }, TIME_LAPSE)

            seeMoreButtonDesc?.setOnClickListener {
                analytics.pdpClick(EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL, data.brand.title, data.displayName)
                dealsPDPCallbacks?.onShowMoreDesc(
                    getString(
                        stringDeals.deals_pdp_show_description
                    ),
                    data.longRichDesc
                )
            }
        }
    }

    private fun showTnc(data: ProductDetailData) {
        val tnc = DealsUtils.getExpandableItemText(data.tnc)
        if (tnc.isNullOrEmpty()) {
            clTnc?.hide()
        } else {
            tgExpandableTnc?.text = Html.fromHtml(tnc)
            Handler().postDelayed({
                tgExpandableTnc?.let {
                    if (it.lineCount >= MAX_LINE) {
                        seeMoreButtonTnc?.show()
                    } else {
                        seeMoreButtonTnc?.hide()
                    }
                }
            }, TIME_LAPSE)
            seeMoreButtonTnc?.setOnClickListener {
                analytics.pdpClick(EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL, data.brand.title, data.displayName)
                dealsPDPCallbacks?.onShowMoreDesc(
                    getString(stringDeals.deals_pdp_show_tnc),
                    data.tnc
                )
            }
        }
    }

    private fun showRedeemInstruction(data: ProductDetailData) {
        clRedeemInstruction?.setOnClickListener {
            analytics.pdpClick(EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL, data.brand.title, data.displayName)
            if ((data.customText1.toIntSafely() and SALAM_VALUE) <= SALAM_INDICATOR) {
                showGeneralWebview(REDEEM_URL)
            } else {
                viewModel.setContent(data.id)
            }
        }
    }

    private fun showCheckout(data: ProductDetailData) {
        context?.let { context ->
            cardCheckout?.show()
            val currentTime = Calendar.getInstance().time
            if (data.saleStartDate.toIntSafely() > (currentTime.time / TIME_DIVIDER)) {
                btnCheckout?.text = context.resources.getString(stringDeals.deals_pdp_product_not_started)
                btnCheckout?.setClickable(false)
            } else if (data.saleEndDate.toIntSafely() < (currentTime.time / TIME_DIVIDER)) {
                btnCheckout?.text = context.resources.getString(stringDeals.deals_pdp_product_ended)
                btnCheckout?.setClickable(false)
            } else {
                btnCheckout?.setClickable(true)
                btnCheckout?.text = context.resources.getString(stringDeals.deals_pdp_buy_now)
                btnCheckout?.setOnClickListener {
                    analytics.pdpCheckout(
                        data.id,
                        data.categoryId,
                        data.salesPrice.toIntSafely().toLong(),
                        data.displayName,
                        data.brand.title
                    )
                    dealsPDPCallbacks?.onSelectQuantityProduct(data)
                }
            }
        }
    }

    private fun updateTitle(displayName: String) {
        tgDealsDetail?.text = displayName
        tgDealsDetail?.show()
    }

    private fun updateMrp(mrp: String) {
        tgMrp?.apply {
            if (mrp.isNotEmpty()) {
                show()
                text = DealsUtils.convertToCurrencyString(mrp.toLong())
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                hide()
            }
        }
    }

    private fun updateSavingPrecentage(savingPrecentage: String) {
        tgOff?.apply {
            if (savingPrecentage.isEmpty() || savingPrecentage.startsWith(ZERO_PERCENT)) {
                hide()
            } else {
                show()
                text = savingPrecentage
            }
        }
    }

    private fun updateSalesPrice(salesPrice: String) {
        tgSalesPrice?.text = DealsUtils.convertToCurrencyString(salesPrice.toLong())
    }

    private fun updateExpiredDate(maxEndDate: String) {
        tgExpiredDate?.text = String.format(
            getString(
                stringDeals.deals_pdp_valid_through,
                DealsUtils.convertEpochToString(maxEndDate.toIntSafely()
            )
            )
        )
    }

    private fun setRating(productId: String, likeCount: Int, isLiked: Boolean, isHideImageRating: Boolean = false) {
        if (isHideImageRating) {
            imgFavorite?.hide()
        } else {
            imgFavorite?.show()
            setIsLiked(isLiked)
            setTotalLikes(likeCount)
            imgFavorite?.setOnClickListener {
                if (!getIsLiked()) {
                    setTotalLikes(getTotalLikes() + Int.ONE)
                } else setTotalLikes(getTotalLikes() - Int.ONE)
                updateRating(productId, !getIsLiked())
            }
        }

        if (getIsLiked()) {
            imgFavorite?.setImageResource(drawableDeals.ic_wishlist_filled)
        } else {
            imgFavorite?.setImageResource(drawableDeals.ic_wishlist_unfilled)
        }

        if (likeCount.isZero()) {
            tgFavorite?.hide()
        } else {
            tgFavorite?.show()
            tgFavorite?.text = likeCount.toString()
        }
    }

    private fun showImageCarousel(data: ProductDetailData) {
        imageCarousel?.setImages(DealsPDPMapper.productImagesMapper(data))
        imageCarousel?.buildView()
    }

    private fun updateCollapsingToolbar() {
        context?.let {
            collapsingToolbarLayout?.setCollapsedTitleTypeface(Typography.getFontType(it, true, Typography.HEADING_3))
            collapsingToolbarLayout?.setExpandedTitleColor(it.resources.getColor(android.R.color.transparent))
            collapsingToolbarLayout?.title = ""
        }
    }

    private fun showPDPOptionsMenu(displayName: String) {
        appBarLayout?.let { mainAppBarLayout ->
            setHasOptionsMenu(true)
            toolbar?.let { toolbar ->
                (activity as DealsPDPActivity).setSupportActionBar(toolbar)
                (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (activity as DealsPDPActivity).supportActionBar?.title = ""
                context?.let {
                    toolbar.navigationIcon = ContextCompat.getDrawable(it, abstractionDrawables.ic_action_back)
                }
            }
            mainAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    context?.let { context ->
                        val menuSize = toolbar?.menu?.size() ?: Int.ZERO
                        if (menuSize > Int.ZERO) {
                            var colorInt = Int.ZERO
                            var headerTitle = ""
                            val appVerticalOffset = Math.abs(verticalOffset)
                            val difference = mainAppBarLayout.totalScrollRange - (toolbar?.height ?: Int.ZERO)
                            if (appVerticalOffset >= difference) {
                                if (displayName.isNotEmpty()) {
                                    headerTitle = displayName
                                }
                                colorInt = ContextCompat.getColor(
                                    context,
                                    colorUnify.Unify_N400
                                )
                            } else {
                                headerTitle = ""
                                colorInt = ContextCompat.getColor(
                                    context,
                                    colorUnify.Unify_N0
                                )
                            }

                            toolbar?.let { toolbar ->
                                collapsingToolbarLayout?.title = headerTitle
                                setDrawableColorFilter(toolbar.navigationIcon, colorInt)
                                setDrawableColorFilter(toolbar.menu.getItem(Int.ZERO)?.icon, colorInt)
                            }
                        }
                   }
                }
            })
        }
    }

    private fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    private fun showShareButton() {
        if (menuPDP != null) {
            val item = menuPDP?.findItem(idDeals.action_share_deals)
            item?.setVisible(true)
        }
    }

    private fun openGoogleMaps(context: Context, latLng: String) {
        val gmmIntentUri = Uri.parse("$URI_MAPS$latLng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(PACKAGE_MAPS)
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            showToaster(
                context.resources.getString(stringDeals.deals_pdp_cannot_find_application),
                Toaster.TYPE_NORMAL
            )
        }
    }

    private fun showGeneralWebview(url: String) {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, url)
        }
    }

    private fun showRecommendation(listProduct: List<EventProductDetail>) {
        if (listProduct.isNullOrEmpty()) {
            hideRecommendation()
        } else {
            context?.let { context ->
                tgRecommendation?.show()
                rvRecommendation?.show()

                val adapter = DealsRecommendationAdapter(object : DealsRecommendationAdapter.RecommendationListener {
                    override fun onClickDealsBrand(brandUrl: String) {
                        RouteManager.route(context, brandUrl)
                    }

                    override fun onClickDealsProduct(pdpUrl: String, productDetail: EventProductDetail, index: Int) {
                        analytics.pdpRecommendationClick(
                            productDetail.id,
                            (index + Int.ONE),
                            productDetail.salesPrice.toLong(),
                            productDetail.displayName,
                            productDetail.brand.title
                        )
                        RouteManager.route(context, pdpUrl)
                    }

                    override fun onImpressProduct(productDetail: EventProductDetail, index: Int) {
                        analytics.pdpRecommendationImpression(
                            productDetail.id,
                            (index + Int.ONE),
                            productDetail.salesPrice.toLong(),
                            productDetail.displayName,
                            productDetail.brand.title,
                            productDetail.category.firstOrNull()?.title
                        )
                    }
                })

                rvRecommendation?.adapter = adapter
                rvRecommendation?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter.addProducts(listProduct)
            }
        }
    }

    private fun hideRecommendation() {
        tgRecommendation?.hide()
        rvRecommendation?.hide()
    }

    private fun showRedeemContent(data: EventContentInnerData) {
        val valueText = data.sectionDatas.firstOrNull()?.contents?.firstOrNull()?.valueText
        if (valueText.isNullOrEmpty()) {
            showErrorRedeem()
        } else {
            val pattern = Pattern.compile(SALAM_REGEX_PATTERN)
            val matcher = pattern.matcher(valueText)
            if (matcher.find()) {
                matcher.group(URL_GROUP)?.let {
                    showGeneralWebview(it)
                }
            }
        }
    }

    private fun showErrorRedeem() {
        context?.let { context ->
            showToaster(
                context.resources.getString(stringDeals.deals_pdp_how_to_redeem_error),
                Toaster.TYPE_ERROR
            )
        }
    }

    private fun showToaster(text: String, type: Int) {
        view?.let { view ->
            Toaster.build(
                view,
                text,
                Toaster.LENGTH_LONG,
                type
            ).show()
        }
    }

    private fun share(productdetail: ProductDetailData) {
        activity?.let { activity ->
            val weakReference = WeakReference<Activity>(activity)
            if (!::dealsSharePDP.isInitialized) dealsSharePDP = DealsPDPShare(weakReference)
            dealsSharePDP.shareEvent(productdetail, productdetail.displayName, requireContext(), { showShareLoading() }, { hideShareLoading() })
        }
    }

    private fun hideShareLoading() {
        dealsPDPCallbacks?.onHideShareLoader()
    }

    private fun showShareLoading() {
        dealsPDPCallbacks?.onShowShareLoader()
    }

    companion object {
        private const val ID_ZERO = "0"
        private const val ZERO_PERCENT = "0%"
        private const val MAX_LINE = 10
        private const val TIME_LAPSE = 100L
        private const val PACKAGE_MAPS = "com.google.android.apps.maps"
        private const val URI_MAPS = "geo:0,0?q="
        private const val SALAM_VALUE = 32768
        private const val SALAM_INDICATOR = 0
        private const val REDEEM_URL = "https://www.tokopedia.com/help/article/st-1283-tokopedia-food-voucher"
        private const val SALAM_REGEX_PATTERN = "<a(?:[^>]+)?>(.*?)<\\/a>"
        private const val URL_GROUP = 1
        private const val TIME_DIVIDER = 1000

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
