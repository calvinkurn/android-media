package com.tokopedia.deals.pdp.ui.fragment

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.deals.pdp.widget.WidgetDealsPDPCarousel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
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
        showContentDescription(data)
        showBrand(data)
    }

    private fun showHeader(displayName: String) {
        clHeader?.show()
        collapsingToolbarLayout?.title = displayName
    }

    private fun showContentDescription(data: ProductDetailData) {
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

        } else {
            clOutlets?.hide()
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

    companion object {

        private const val ZERO_PERCENT = "0%"

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}