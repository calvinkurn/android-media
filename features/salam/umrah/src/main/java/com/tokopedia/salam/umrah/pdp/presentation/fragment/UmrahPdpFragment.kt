package com.tokopedia.salam.umrah.pdp.presentation.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahPdpTrackingUserAction
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingUtil
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDate
import com.tokopedia.salam.umrah.common.util.UmrahHotelRating.getAllHotelRatings
import com.tokopedia.salam.umrah.common.util.UmrahHotelVariant.getAllHotelVariants
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpFeaturedFacilityModel
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpGreenRectWidgetModel
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_VARIANT_ROOM
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.REQUEST_PDP_DETAIL
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpDetailActivity
import com.tokopedia.salam.umrah.pdp.presentation.adapter.*
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpDetailFragment.Companion.paramPurchase
import com.tokopedia.salam.umrah.pdp.presentation.itemdecoration.UmrahPdpFaqIndicator
import com.tokopedia.salam.umrah.pdp.presentation.viewmodel.UmrahPdpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheets_umrah_pdp_facilities.view.*
import kotlinx.android.synthetic.main.bottom_sheets_umrah_pdp_itinerary.view.*
import kotlinx.android.synthetic.main.fragment_umrah_pdp.*
import kotlinx.android.synthetic.main.partial_umrah_pdp_ad.*
import kotlinx.android.synthetic.main.widget_umrah_pdp_green_rect.view.*
import kotlinx.android.synthetic.main.widget_umrah_pdp_itinerary.view.*
import javax.inject.Inject

/**
 * @author by M on 30/10/19
 */
class UmrahPdpFragment : BaseDaggerFragment(), UmrahPdpActivity.OnBackListener {
    @Inject
    lateinit var umrahPdpViewModel: UmrahPdpViewModel
    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingUtil

    private val umrahPdpImageViewPagerAdapter by lazy { UmrahPdpImageViewPagerAdapter() }
    private val umrahPdpHotelAdapter by lazy { UmrahPdpHotelAdapter() }
    private val umrahPdpAirlineAdapter by lazy { UmrahPdpAirlineAdapter() }
    private val umrahPdpFeaturedFacilityAdapter by lazy { UmrahPdpFeaturedFacilityAdapter() }
    private val umrahPdpFacilityAdapter by lazy { UmrahPdpFacilityAdapter() }
    private val umrahPdpNonFacilityAdapter by lazy { UmrahPdpNonFacilityAdapter() }
    private val umrahPdpFaqAdapter by lazy { UmrahPdpFaqAdapter() }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahPdpComponent::class.java).inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahPdpViewModel.pdpResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError()
            }
        })
    }

    private fun showGetListError() {
        NetworkErrorHelper.showEmptyState(context, view?.rootView) { loadData() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PDP_DETAIL -> {
                if (data != null) {
                    paramPurchase.variant = data.getStringExtra(EXTRA_VARIANT_ROOM)
                    paramPurchase.totalPassenger = data.getIntExtra(EXTRA_TOTAL_PASSENGER, 1)
                    paramPurchase.totalPrice = data.getIntExtra(EXTRA_TOTAL_PRICE, 1)
                    setupHotelTypeItem()
                    setupPriceAndPurchaseButton()
                }
            }
        }
    }

    private fun loadData() {
        umrahPdpViewModel.getUmrahPdp(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_pdp_simple))
    }

    private fun onSuccessGetResult(umrahProduct: UmrahProductModel.UmrahProduct) {
        UmrahPdpFragment.umrahProduct = umrahProduct
        setupAll()
    }

    private fun setupAll() {
        setupCollapsingToolbar()
        setupTopImages()
        setupPdpHeader()
        setupTravelAgentItem()
        setupHotelTypeItem()
        setupCalendarItem()
        setupHotelItem()
        setupPlaneItem()
        setupRVHotels()
        setupRVAirlines()
        setupItineraries()
        setupRVFeaturedFacilities()
        setupRVNonFacilities()
        setupRVFaq()
        setupAdditionalInformation()
        checkPackageAvailability()
        showData()
    }

    private fun showData() {
        container_umrah_pdp.visibility = VISIBLE
        container_umrah_pdp_shimmering.visibility = GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        umrahPdpViewModel.slugName = arguments!!.getString(EXTRA_SLUG_NAME, "")
        if (umrahPdpViewModel.slugName != umrahProduct.slugName) {
            resetObject()
        }

    }

    private fun resetObject() {
        umrahProduct = UmrahProductModel.UmrahProduct()
        paramPurchase = ParamPurchase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_pdp, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAdContainerBg()

        if (umrahProduct.title != "") setupAll()
        else loadData()
    }

    private fun loadAdContainerBg() {
        val containerBg = context?.let { AppCompatResources.getDrawable(it, R.drawable.umrah_bg_pdp_ad) }
        cl_umrah_pdp_ad.background = containerBg
    }

    private fun setupCollapsingToolbar() {
        (activity as UmrahPdpActivity).setSupportActionBar(umrah_pdp_toolbar)
        (activity as UmrahPdpActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = umrah_pdp_toolbar.navigationIcon
        navIcon?.setColorFilter(ContextCompat.getColor(context!!, com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
        (activity as UmrahPdpActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        umrah_pdp_collapsing_toolbar.title = ""
        umrah_pdp_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    umrah_pdp_collapsing_toolbar.title = umrahProduct.title
                    navIcon?.setColorFilter(ContextCompat.getColor(context!!, com.tokopedia.design.R.color.black), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    umrah_pdp_collapsing_toolbar.title = ""
                    navIcon?.setColorFilter(ContextCompat.getColor(context!!, com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
                    isShow = false
                }
            }
        })
    }

    private fun setupTopImages() {
        umrahPdpImageViewPagerAdapter.apply {
            imageUrls = umrahProduct.banners
            onClickListener = object : UmrahPdpImageViewPagerAdapter.SetOnClickListener {
                override fun onClick(position: Int) {
                    umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_PACKAGE_PHOTOS)
                    context?.run {
                        startActivity(ImagePreviewSliderActivity.getCallingIntent(
                                this, umrahProduct.title, umrahPdpImageViewPagerAdapter.imageUrls, umrahPdpImageViewPagerAdapter.imageUrls, position
                        ))
                    }
                }
            }
        }
        vp_umrah_pdp_images_top.adapter = umrahPdpImageViewPagerAdapter
        tl_umrah_pdp_images_indicator.setupWithViewPager(vp_umrah_pdp_images_top, true)
    }

    private fun setupPdpHeader() {
        tg_umrah_pdp_title.text = umrahProduct.title
    }

    private fun setupTravelAgentItem() {
        val travelAgent = umrahProduct.travelAgent
        val umrahItemWidgetModel: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = travelAgent.name
            imageUri = travelAgent.imageUrl
            desc = travelAgent.permissionOfUmrah
        }
        iw_umrah_pdp_travel_agent.umrahItemWidgetModel = umrahItemWidgetModel
        iw_umrah_pdp_travel_agent.buildView()
    }

    private fun setupHotelTypeItem() {
        if (paramPurchase.variant == "") paramPurchase.variant = umrahProduct.variants[0].name
        val umrahPdpGreenRectWidgetModel = UmrahPdpGreenRectWidgetModel().apply {
            title = getString(R.string.umrah_pdp_hotel_type_x, paramPurchase.variant)
            imageFirstLineUrl = R.drawable.umrah_ic_pdp_person
            descFirstLine = getString(R.string.umrah_pdp_detail_total_passenger, paramPurchase.totalPassenger)
            imageActionUrl = R.drawable.umrah_ic_right
        }
        grw_umrah_pdp_hotel_type.iv_widget_umrah_pdp_green_rect_action.setPadding(3, 3, 3, 3)
        grw_umrah_pdp_hotel_type.umrahPdpGreenRectWidgetModel = umrahPdpGreenRectWidgetModel
        grw_umrah_pdp_hotel_type.buildView()
        grw_umrah_pdp_hotel_type.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CHOOSE_ROOM_TYPE_UP)
            startActivityForResult(context?.let { it1 -> UmrahPdpDetailActivity.createIntent(it1, umrahProduct.slugName) }, REQUEST_PDP_DETAIL)
        }
    }

    private fun setupCalendarItem() {
        val departureDate = getDate("dd MMM", umrahProduct.departureDate)
        val returningDate = getDate("dd MMM yyyy", umrahProduct.returningDate)
        val umrahPdpItemWidgetModel = UmrahItemWidgetModel()
        umrahPdpItemWidgetModel.apply {
            imageDrawable = R.drawable.umrah_ic_calendar
            title = getString(R.string.umrah_pdp_calendar, departureDate, returningDate)
            desc = getString(R.string.umrah_pdp_duration, umrahProduct.durationDays, umrahProduct.durationDays - 1)
        }
        iw_umrah_pdp_calendar.umrahItemWidgetModel = umrahPdpItemWidgetModel
        iw_umrah_pdp_calendar.buildView()
    }

    private fun setupHotelItem() {
        val hotelsRating = getAllHotelRatings(umrahProduct.hotels)
        val hotelsVariant = getAllHotelVariants(umrahProduct.variants)
        val umrahPdpItemWidgetModel = UmrahItemWidgetModel()
        umrahPdpItemWidgetModel.apply {
            imageDrawable = R.drawable.umrah_ic_hotel
            title = getString(R.string.umrah_search_hotel_rating_x, hotelsRating)
            desc = hotelsVariant
        }
        iw_umrah_pdp_hotel.umrahItemWidgetModel = umrahPdpItemWidgetModel
        iw_umrah_pdp_hotel.buildView()

    }

    private fun setupPlaneItem() {
        val umrahPdpItemWidgetModel = UmrahItemWidgetModel()
        umrahPdpItemWidgetModel.apply {
            imageDrawable = R.drawable.umrah_ic_plane
            title = umrahProduct.airlines[0].name
            desc = try {
                umrahProduct.transitCity.name
            } catch (e: NullPointerException) {
                getString(R.string.umrah_pdp_direct_flight)
            }
        }
        iw_umrah_pdp_plane.umrahItemWidgetModel = umrahPdpItemWidgetModel
        iw_umrah_pdp_plane.buildView()
    }

    private fun setupRVHotels() {
        val hotels = umrahProduct.hotels
        umrahPdpHotelAdapter.hotels = hotels
        rv_umrah_pdp_hotel.apply {
            adapter = umrahPdpHotelAdapter.apply {
                onImageListener = object : UmrahPdpHotelImagesAdapter.UmrahPdpHotelImagesListener {
                    override fun onImageClicked(itemPosition: Int, position: Int) {
                        umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_HOTEL_PHOTOS)
                        context?.run {
                            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                                    this, hotels[itemPosition].name, hotels[itemPosition].imageUrls, hotels[itemPosition].imageUrls, position
                            ))
                        }
                    }
                }
                onScrollListener = object : UmrahPdpHotelAdapter.OnScrollListener {
                    override fun onScrolled() {
                        umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLLING_HOTEL)
                    }

                }
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16),
                    LinearLayoutManager.VERTICAL))
        }
    }

    private fun setupRVAirlines() {
        umrahPdpAirlineAdapter.airlines = umrahProduct.airlines
        rv_umrah_pdp_airline.apply {
            adapter = umrahPdpAirlineAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_12),
                    LinearLayoutManager.HORIZONTAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLLING_PENERBANGAN)
                }
            })
        }
    }

    private fun setupItineraries() {
        iw_umrah_pdp_itineraries.setItem(umrahProduct.itineraries, 4)
        iw_umrah_pdp_itineraries.buildView()
        iw_umrah_pdp_itineraries.read_more.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_ITINERARY_SELENGKAPNYA)
            openBottomSheetItinerary()
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheetItinerary() {
        val itineraryBottomSheet = BottomSheetUnify()
        itineraryBottomSheet.setCloseClickListener { itineraryBottomSheet.dismiss() }
        itineraryBottomSheet.setTitle(resources.getString(R.string.umrah_pdp_itinerary_title))
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_pdp_itinerary, null)
        view.apply {
            iw_umrah_pdp_itineraries.setItem(umrahProduct.itineraries)
            iw_umrah_pdp_itineraries.buildView()
        }
        itineraryBottomSheet.setChild(view)
        itineraryBottomSheet.show(fragmentManager, "")
    }

    private fun setupRVFeaturedFacilities() {
        umrahPdpFeaturedFacilityAdapter.featuredFacilities = getHardcodedData()
        rv_umrah_pdp_facilities.apply {
            adapter = umrahPdpFeaturedFacilityAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                    LinearLayoutManager.HORIZONTAL))
        }
        tg_umrah_pdp_featured_facilities_see_all.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_SEMUA_FASILITAS)
            openBottomSheetFacilities()
        }
    }

    private fun getHardcodedData(): ArrayList<UmrahPdpFeaturedFacilityModel> {
        val icons = listOf(R.drawable.umrah_ic_pdp_visa,
                R.drawable.umrah_ic_pdp_insurance,
                R.drawable.umrah_ic_pdp_zam_zam,
                R.drawable.umrah_ic_pdp_food,
                R.drawable.umrah_ic_pdp_bus)
        val descriptions = listOf("Visa",
                "Insurance",
                "Air Zam-zam 5 Liter",
                "Makanan",
                "Bus")
        val featuredFacilities = arrayListOf<UmrahPdpFeaturedFacilityModel>()
        for (i in 0 until 5) {
            val umrahPdpFeaturedFacilityModel = UmrahPdpFeaturedFacilityModel("", icons[i], descriptions[i])
            featuredFacilities.add(umrahPdpFeaturedFacilityModel)
        }
        return featuredFacilities
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheetFacilities() {
        val facilitiesBottomSheet = BottomSheetUnify()
        facilitiesBottomSheet.setCloseClickListener { facilitiesBottomSheet.dismiss() }
        facilitiesBottomSheet.setTitle(resources.getString(R.string.umrah_pdp_facilities_title))
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_pdp_facilities, null)
        view.bs_rv_umrah_pdp_facilities.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = umrahPdpFacilityAdapter
        }
        umrahPdpFacilityAdapter.facilities = umrahProduct.facilities
        facilitiesBottomSheet.setChild(view)
        facilitiesBottomSheet.show(fragmentManager, "TEST")
    }

    private fun setupRVNonFacilities() {
        umrahPdpNonFacilityAdapter.nonFacilities = umrahProduct.nonFacilities
        rv_umrah_pdp_non_facilities.apply {
            adapter = umrahPdpNonFacilityAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun setupRVFaq() {
        umrahPdpFaqAdapter.faqs = umrahProduct.faqs.contents
        rv_umrah_pdp_faqs.apply {
            adapter = umrahPdpFaqAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8), LinearLayoutManager.HORIZONTAL))
            addItemDecoration(UmrahPdpFaqIndicator())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLL_FAQ)
                }
            })
        }
        umrahPdpFaqAdapter.onItemListener = object : UmrahPdpFaqAdapter.OnClickListener {
            override fun onClick(link: String) {
                umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_FAQ)
                RouteManager.route(context, link)
            }
        }
        tg_umrah_pdp_faq_see_all.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_SEMUA_FAQ)
            RouteManager.route(context, umrahProduct.faqs.allContentsLink)
        }
    }

    private fun setupAdditionalInformation() {
        var bulletData = "\u2022\t${umrahProduct.additionalInformation[0]}"
        for (i in 1 until umrahProduct.additionalInformation.size) {
            bulletData += "\n\u2022\t${umrahProduct.additionalInformation[i]}"
        }
        ticker_umrah_pdp_additional_info.setTextDescription(bulletData)
        ticker_umrah_pdp_additional_info.tickerTitle = getString(R.string.umrah_pdp_additional_information_title)
    }

    private fun setupPriceAndPurchaseButton() {
        if (paramPurchase.totalPrice == 0) paramPurchase.totalPrice = umrahProduct.originalPrice
        tg_umrah_pdp_price.text = getRupiahFormat(paramPurchase.totalPrice)
        tg_umrah_pdp_price_start_from.text = getSlashedPrice(resources, umrahProduct.slashPrice)
        bt_umrah_pdp_buy_package.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CHOOSE_ROOM_TYPE_DOWN)
            startActivityForResult(context?.let { it1 -> UmrahPdpDetailActivity.createIntent(it1, umrahProduct.slugName) }, REQUEST_PDP_DETAIL)
        }
    }

    private fun checkPackageAvailability() {
        if (umrahProduct.availableSeat != 0) setupPriceAndPurchaseButton()
        else showSoldOutPackage()
    }

    private fun showSoldOutPackage() {
        tg_umrah_pdp_price_start_from.visibility = GONE
        tg_umrah_pdp_price.visibility = GONE
        tg_umrah_pdp_empty_price_label.visibility = VISIBLE
        bt_umrah_pdp_buy_package.buttonType = UnifyButton.Type.MAIN
        bt_umrah_pdp_buy_package.text = getString(R.string.umrah_pdp_change_search)
        bt_umrah_pdp_buy_package.setOnClickListener {
            activity?.finish()
        }
    }

    companion object {
        var umrahProduct: UmrahProductModel.UmrahProduct = UmrahProductModel.UmrahProduct()

        fun getInstance(slugName: String) =
                UmrahPdpFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUG_NAME, slugName)
                    }
                }
    }

    override fun onBackPressed() {
        if (!isDetached) {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_BACK)
        }
    }
}