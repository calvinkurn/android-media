package com.tokopedia.deals.pdp.ui.fragment

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.deals.pdp.widget.WidgetDealsPDPCarousel
import com.tokopedia.graphql.util.Const
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class DealsPDPFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var productId: String = ""
    private var binding by autoClearedNullable<FragmentDealsDetailBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DealsPDPViewModel::class.java)
    }

    private var progressBar: LoaderUnify? = null
    private var progressBarLayout: FrameLayout? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EXTRA_PRODUCT_ID, "") ?: ""
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
        callPDP()
    }

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    private fun setupUi() {
        view?.apply {
            progressBar = binding?.progressBar
            progressBarLayout = binding?.progressBarLayout
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
            updateCollapsingToolbar()
        }
    }

    private fun observeFlowData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowPDP.collect {
                when (it) {
                    is Success -> {
                        hideLoading()
                        showPDPData(it.data.eventProductDetail.productDetailData)
                    }

                    is Fail -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun callPDP() {
        showLoading()
        viewModel.setPDP(productId)
    }

    private fun showLoading() {
        progressBar?.show()
        progressBarLayout?.show()
        imageCarousel?.shimmering()
    }

    private fun hideLoading() {
        progressBar?.hide()
        progressBarLayout?.hide()
    }

    private fun showPDPData(data: ProductDetailData) {
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
        if (data.outlets.size.isMoreThanZero()){
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
                val imgMaps = context?.resources?.getDrawable(com.tokopedia.deals.R.drawable.ic_see_location)
                tgViewMap?.show()
                tgViewMap?.setCompoundDrawablesWithIntrinsicBounds(imgMaps, null, null, null)
            }

            tgBrandVenue?.text = outlet.name
            tgBrandAddress?.text = outlet.district
            tgBrandName?.text = data.brand.title
            tgNumberOfLocation?.text = String.format(
                getString(com.tokopedia.deals.R.string.deals_pdp_number_of_items,
                data.outlets.size))
            ivBrandLogo?.loadImage(data.brand.featuredThumbnailImage)

            ivBrandLogo?.setOnClickListener {
                val url = ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE + "?" + data.brand.seoUrl
                RouteManager.route(context, url)
            }

            tgViewMap?.setOnClickListener {
                //todo goto maps
            }

            tgAllLocation?.setOnClickListener {
                //todo show all outlets
            }

        } else {
            clOutlets?.hide()
        }
    }

    private fun showDescription(data: ProductDetailData) {
        val desc = getExpandableItemText(data.longRichDesc)
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
                //todo show fragment
            }
        }
    }

    private fun showTnc(data: ProductDetailData) {
        val tnc = getExpandableItemText(data.tnc)
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
                //todo show fragment
            }
        }
    }

    private fun showRedeemInstruction(data: ProductDetailData) {
        clRedeemInstruction?.setOnClickListener {
            //todo redeem instruction
        }
    }

    private fun showCheckout(data: ProductDetailData) {
        context?.let { context ->
            cardCheckout?.show()
            val currentTime = Calendar.getInstance().time
            if (data.saleStartDate.toIntSafely() > (currentTime.time / 1000)) {
                btnCheckout?.text = context.resources.getString(com.tokopedia.deals.R.string.deals_pdp_product_not_started)
                btnCheckout?.setClickable(false)
            } else if (data.saleEndDate.toIntSafely() < (currentTime.time / 1000)) {
                btnCheckout?.text = context.resources.getString(com.tokopedia.deals.R.string.deals_pdp_product_ended)
                btnCheckout?.setClickable(false)
            } else {
                btnCheckout?.setClickable(true)
                btnCheckout?.text = context.resources.getString(com.tokopedia.deals.R.string.deals_pdp_buy_now)
                btnCheckout?.setOnClickListener {
                    //todo goto checkout
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
        tgExpiredDate?.text = String.format(getString(com.tokopedia.deals.R.string.deals_pdp_valid_through,
            DealsUtils.convertEpochToString(maxEndDate.toIntSafely())))
    }

    private fun updateImageFavorite() {
        //todo image likes
        //imgFavorite
        //tgFavorite
    }

    private fun showImageCarousel(data: ProductDetailData) {
        imageCarousel?.setImages(viewModel.productImagesMapper(data))
        imageCarousel?.buildView()
    }

    private fun showPDPOptionsMenu(displayName: String) {
        appBarLayout?.let { mainAppBarLayout ->
            setHasOptionsMenu(true)
            setToolbar()
            mainAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                context?.let { context ->
                    val menuSize = toolbar?.menu?.size() ?: Int.ZERO
                    if(menuSize > Int.ZERO) {
                        var colorInt = Int.ZERO
                        val appVerticalOffset = Math.abs(verticalOffset)
                        val difference =
                            mainAppBarLayout.totalScrollRange - (toolbar?.height ?: Int.ZERO)
                        if (appVerticalOffset >= difference) {
                            if (displayName.isNotEmpty()) {
                                collapsingToolbarLayout?.title = displayName
                            }
                            colorInt = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N400
                            )
                        } else {
                            collapsingToolbarLayout?.title = ""
                            colorInt = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N0
                            )
                        }

                        toolbar?.let { toolbar ->
                            setDrawableColorFilter(
                                toolbar.navigationIcon,
                                colorInt
                            )
                            setDrawableColorFilter(
                                toolbar.menu.getItem(Int.ZERO)?.icon,
                                colorInt
                            )
                        }
                    }
                }
            })
        }
    }

    private fun setToolbar() {
        toolbar?.let { toolbar ->
            (activity as DealsPDPActivity).setSupportActionBar(toolbar)
            (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as DealsPDPActivity).supportActionBar?.title = ""
            context?.let {
               toolbar.navigationIcon = ContextCompat.getDrawable(it, com.tokopedia.abstraction.R.drawable.ic_action_back)
            }
        }
    }

    private fun updateCollapsingToolbar() {
        context?.let {
            collapsingToolbarLayout?.setExpandedTitleColor(it.resources.getColor(android.R.color.transparent))
            collapsingToolbarLayout?.title = ""
        }
    }

    private fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    private fun showShareButton() {
        if (menuPDP != null) {
            val item = menuPDP?.findItem(com.tokopedia.deals.R.id.action_menu_share)
            item?.setVisible(true)
        }
    }

    private fun getExpandableItemText(texts: String): String? {
        return if (texts.isNotEmpty()) {
            val splitArray = texts.split("~").toTypedArray()
            val tncBuffer = StringBuilder()
            for (i in splitArray.indices) {
                val line = splitArray[i]
                if (i < splitArray.size - Int.ONE) tncBuffer.append(" ").append(DOT_HTML).append("  ")
                    .append(line.trim { it <= ' ' }).append(ENTER_HTML)
                else tncBuffer.append(" ").append(DOT_HTML).append("  ").append(line.trim { it <= ' ' })
            }
            tncBuffer.toString()
        } else {
            null
        }
    }

    companion object {

        private const val ZERO_PERCENT = "0%"
        private const val MAX_LINE = 10
        private const val TIME_LAPSE = 100L
        private const val ENTER_HTML = "<br><br>"
        private const val DOT_HTML = "\u2022"

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}