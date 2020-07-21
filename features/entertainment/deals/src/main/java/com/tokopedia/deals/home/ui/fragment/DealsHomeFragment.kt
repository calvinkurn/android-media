package com.tokopedia.deals.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.listener.*
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.deals.common.ui.fragment.DealsBaseFragment
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.home.di.DealsHomeComponent
import com.tokopedia.deals.home.listener.DealsBannerActionListener
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.activity.DealsHomeActivity
import com.tokopedia.deals.home.ui.adapter.DealsHomeAdapter
import com.tokopedia.deals.home.ui.dataview.*
import com.tokopedia.deals.home.ui.viewmodel.DealsHomeViewModel
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.ui.activity.DealsSearchActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 16/06/20
 */

class DealsHomeFragment : DealsBaseFragment(),
        OnBaseLocationActionListener, SearchBarActionListener,
        DealsVoucherPlaceCardListener, DealsCategoryListener,
        CuratedProductCategoryListener, DealsBannerActionListener,
        DealsBrandActionListener, DealsFavouriteCategoriesListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeViewModel: DealsHomeViewModel
    private lateinit var baseViewModel: DealsBaseViewModel

    private lateinit var localCacheHandler: LocalCacheHandler

    @Inject
    lateinit var analytics: DealsAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLayout()
    }

    private fun observeLayout() {
        homeViewModel.observableEventHomeLayout.observe(this, Observer {
            isLoadingInitialData = true
            when (it) {
                is Success -> {
                    renderList(it.data, false)
                    checkCoachMark(it.data)
                }
                is Fail -> renderList(listOf(getErrorNetworkModel()), false)
            }
        })

        baseViewModel.observableCurrentLocation.observe(this, Observer {
            onBaseLocationChanged(it)
        })
    }

    private fun getErrorNetworkModel(): ErrorNetworkModel {
        val errorModel = ErrorNetworkModel()
        errorModel.onRetryListener = ErrorNetworkModel.OnRetryListener { loadData(0) }
        return errorModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as DealsBaseActivity).searchBarActionListener = this
        analytics.eventSeeHomePage()
    }

    override fun getScreenName(): String = ""

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory)
        homeViewModel = viewModelProvider.get(DealsHomeViewModel::class.java)
        baseViewModel = viewModelProvider.get(DealsBaseViewModel::class.java)
    }

    private fun checkCoachMark(homeLayout: List<DealsBaseItemDataView>) {
        initTrackingSection(homeLayout)
        val shouldShowCoachMark = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, true)
        if (shouldShowCoachMark && homeLayout.isNotEmpty() && homeLayout.first().isLoaded) {
            recyclerView.smoothScrollToPosition(adapter.lastIndex)
            Handler().postDelayed(
                    { showCoachMark(getCoachMarkPosition(homeLayout)) },
                    COACH_MARK_START_DELAY
            )
        }
    }

    private fun initTrackingSection(homeLayout: List<DealsBaseItemDataView>){
        homeLayout.forEach { dealsBaseItemDataView ->
            when (dealsBaseItemDataView) {
                is CuratedProductCategoryDataView -> {
                    if (dealsBaseItemDataView.title.contentEquals("Voucher populer")) {
                        dealsBaseItemDataView.productCards.forEachIndexed { index, productCardDataView ->
                            analytics.eventSeePopularLandmarkView(productCardDataView, index)
                        }
                    }
                }

                is FavouritePlacesDataView -> {
                    if (dealsBaseItemDataView.places.size > 0) {
                        dealsBaseItemDataView.places.forEachIndexed { index, place ->
                            analytics.eventSeeCuratedSection(place, index)
                        }
                    }
                }

                is BannersDataView -> {
                    if (!dealsBaseItemDataView.list.isNullOrEmpty()) {
                        dealsBaseItemDataView.list.forEachIndexed { index, bannerDataView ->
                            analytics.eventSeeHomePageBanner(bannerId = bannerDataView.bannerId, promotions = bannerDataView , bannerPosition = index)
                        }
                    }
                }
            }
        }
    }

    private fun getCoachMarkPosition(homeLayout: List<DealsBaseItemDataView>): CoachMarkPositionDataView {
        var popularPlaceIdx: Int? = null
        var favouriteCategoryIdx: Int? = null
        homeLayout.forEachIndexed { index, dealsBaseItemDataView ->
            when (dealsBaseItemDataView) {
                is VoucherPlacePopularDataView -> popularPlaceIdx = index
                is FavouritePlacesDataView -> favouriteCategoryIdx = index
            }
        }
        return CoachMarkPositionDataView(popularPlaceIdx, favouriteCategoryIdx)
    }

    private fun showCoachMark(coachMarkPosition: CoachMarkPositionDataView) {
        val coachMark = CoachMarkBuilder().build().apply {
            enableSkip = true
            onFinishListener = {
                recyclerView.smoothScrollToPosition(0)
            }
        }
        coachMark.show(
                activity,
                DealsHomeFragment::class.java.simpleName,
                getCoachMarkItems(coachMarkPosition)
        )
        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, false)
            applyEditor()
        }
    }

    private fun getCoachMarkItems(coachMarkPosition: CoachMarkPositionDataView): ArrayList<CoachMarkItem> {
        val orderListCoachMark = CoachMarkItem(
                activity?.findViewById<AppCompatImageView>(R.id.imgDealsOrderListMenu),
                getString(R.string.deals_menu_coach_mark_title),
                getString(R.string.deals_menu_coach_mark_description)
        )

        val popularPlacesCoachMark = coachMarkPosition.popularPlacesPosition?.let {
            CoachMarkItem(
                    recyclerView.findViewHolderForAdapterPosition(it)?.itemView?.findViewById(R.id.lst_voucher_popular_place_card),
                    getString(R.string.deals_popular_place_coach_mark_title),
                    getString(R.string.deals_popular_places_coach_mark_description)
            )
        }

        val favoriteCategoriesCoachMark = coachMarkPosition.favouriteCategoriesPosition?.let {
            CoachMarkItem(
                    recyclerView.findViewHolderForAdapterPosition(it)?.itemView?.findViewById(R.id.lst_voucher_popular_place_card),
                    getString(R.string.deals_favorite_categories_coach_mark_title),
                    getString(R.string.deals_favorite_categories_coach_mark_description)
            )
        }

        val coachMarks: ArrayList<CoachMarkItem> = arrayListOf(orderListCoachMark)
        popularPlacesCoachMark?.let { coachMarks.add(it) }
        favoriteCategoriesCoachMark?.let { coachMarks.add(it) }
        return coachMarks
    }

    override fun initInjector() {
        getComponent(DealsHomeComponent::class.java).inject(this)
    }

    override fun createAdapterInstance(): BaseCommonAdapter {
        return DealsHomeAdapter(this, this, this,
                this, this, this)
    }

    override fun loadData(page: Int) {
        isLoadingInitialData = true
        homeViewModel.getLayout(getCurrentLocation())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DEALS_SEARCH_REQUEST_CODE, DEALS_CATEGORY_REQUEST_CODE, DEALS_BRAND_REQUEST_CODE -> {
                changeLocationAndLoadData()
            }
        }
    }

    private fun changeLocationAndLoadData() {
        if ((activity as DealsBaseActivity).changeLocationBasedOnCache()) loadData(0)
    }

    /* BASE DEALS ACTIVITY ACTION */

    override fun onBaseLocationChanged(location: Location) {
        setCurrentLocation(location)
        loadData(0)
    }

    override fun onClickSearchBar() {
        startActivityForResult(Intent(activity, DealsSearchActivity::class.java), DEALS_SEARCH_REQUEST_CODE)
        analytics.eventClickSearchHomePage()
    }

    override fun afterSearchBarTextChanged(text: String) {/* do nothing */ }

    /* BANNER SECTION ACTION */
    override fun onBannerClicked(banner: List<BannersDataView.BannerDataView>, position: Int) {
        RouteManager.route(context, banner[position].bannerUrl)
        analytics.eventClickHomePageBanner(bannerId = banner[position].bannerId, bannerPosition = position, promotions = banner[position])
    }

    override fun onBannerSeeAllClick(bannerSeeAllUrl: String) {
        RouteManager.route(context, bannerSeeAllUrl)
        analytics.eventClickAllBanner()
    }

    /* CATEGORY SECTION ACTION */
    override fun onDealsCategoryClicked(dealsCategory: DealsCategoryDataView, position: Int) {
        val intent = RouteManager.getIntent(requireContext(), dealsCategory.appUrl)
        startActivityForResult(intent, DEALS_CATEGORY_REQUEST_CODE)
        analytics.eventClickCategoryIcon(dealsCategory.title, position)
    }

    override fun onDealsCategorySeeAllClicked(categories: List<DealsCategoryDataView>) {
        val categoriesBottomSheet = DealsCategoryBottomSheet(this)
        categoriesBottomSheet.showDealsCategories(categories)
        categoriesBottomSheet.show(requireFragmentManager(), "")
        analytics.eventClickCategorySectionOne()
    }

    /* BRAND SECTION ACTION */
    override fun onClickBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventClickBrandPopular(brand, position, false)
        RouteManager.route(context, brand.brandUrl)
    }

    override fun onClickSeeAllBrand(seeAllUrl: String) {
        analytics.eventSeeAllBrandPopular()
        startActivityForResult(DealsBrandActivity.getCallingIntent(requireContext(), ""), DEALS_BRAND_REQUEST_CODE)
    }

    /* PRODUCT SECTION ACTION */
    override fun onProductClicked(productCardDataView: ProductCardDataView, productItemPosition: Int) {
        analytics.curatedProductClick(productCardDataView,productItemPosition)
        RouteManager.route(context, productCardDataView.appUrl)
        trackLandmarkPopular(productCardDataView, productItemPosition)
    }

    private fun trackLandmarkPopular(productCardDataView: ProductCardDataView, position: Int) {
        if (productCardDataView.productCategory?.name.equals("Populer")) {
            analytics.eventClickLandmarkPopular(productCardDataView, position)
        }
    }

    override fun onSeeAllProductClicked(curatedProductCategoryDataView: CuratedProductCategoryDataView, position: Int) {
        val intent = RouteManager.getIntent(context, curatedProductCategoryDataView.seeAllUrl)
        startActivityForResult(intent, DEALS_CATEGORY_REQUEST_CODE)
        analytics.clickAllCuratedProduct()
    }

    /* NEAREST PLACE SECTION ACTION */
    override fun onVoucherPlaceCardClicked(voucherPlaceCard: VoucherPlaceCardDataView, position: Int) {
        (activity as DealsHomeActivity).setCurrentLocation(voucherPlaceCard.location)
    }

    /* FAVOURITE CATEGORY SECTION ACTION */
    override fun onClickFavouriteCategory(url: String, favoritePlacesDataView: FavouritePlacesDataView.Place, position: Int) {
        val intent = RouteManager.getIntent(context, url)
        startActivityForResult(intent, DEALS_CATEGORY_REQUEST_CODE)
        analytics.eventClickCuratedSection(favoritePlacesDataView, position)
    }

    private fun getCurrentLocation() = (activity as DealsBaseActivity).currentLoc
    private fun setCurrentLocation(location: Location) {
        val previousLocation = (activity as DealsBaseActivity).currentLoc
        (activity as DealsBaseActivity).currentLoc = location
        analytics.eventChangeLocationHomePage(prevLocation = previousLocation.name, newLocation = location.name)
    }

    override fun hasInitialLoadingModel(): Boolean = false
    override fun callInitialLoadAutomatically(): Boolean = false
    override fun hasInitialSwipeRefresh() = true

    override fun onImpressionBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventScrollToBrandPopular(brand,position)
    }

    override fun onImpressionCuratedProduct(curatedProductCategoryDataView: CuratedProductCategoryDataView, position: Int) {
        analytics.impressionCuratedProduct(curatedProductCategoryDataView,position)
    }

    companion object {
        const val DEALS_SEARCH_REQUEST_CODE = 27
        const val DEALS_CATEGORY_REQUEST_CODE = 33
        const val DEALS_BRAND_REQUEST_CODE = 39

        fun getInstance(): DealsHomeFragment = DealsHomeFragment()
        private const val PREFERENCES_NAME = "deals_home_preferences"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key"
        private const val COACH_MARK_START_DELAY = 1000L
    }
}