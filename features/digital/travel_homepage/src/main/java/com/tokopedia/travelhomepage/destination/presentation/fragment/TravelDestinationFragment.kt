package com.tokopedia.travelhomepage.destination.presentation.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.travelhomepage.destination.di.TravelDestinationComponent
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity.Companion.EXTRA_DESTINATION_WEB_URL
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelDestinationTypeFactory
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_travel_homepage_destination.*
import javax.inject.Inject
import com.tokopedia.travelhomepage.R
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.travelhomepage.destination.widget.DestinationImageViewPager
import kotlinx.android.synthetic.main.layout_image_slider.view.*
import android.widget.LinearLayout
import android.graphics.Point
import android.os.Build
import android.widget.ImageView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingUtil
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity.Companion.PARAM_CITY_ID
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_DEALS_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_EVENT_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_RECOMMENDATION_ORDER
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.android.synthetic.main.layout_travel_destination_shimmering.*
import kotlinx.android.synthetic.main.layout_travel_destination_summary.*
import android.view.Window.ID_ANDROID_CONTENT
import android.graphics.Rect
import android.view.Window
import kotlinx.android.synthetic.main.travel_homepage_order_section_list_without_subtitle_item.*


/**
 * @author by jessica on 2019-12-20
 */

class TravelDestinationFragment : BaseListFragment<TravelDestinationItemModel, TravelDestinationTypeFactory>(), ActionListener,
OnViewHolderBindListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: TravelDestinationViewModel

    @Inject
    lateinit var travelDestinationTrackingUtil: TravelDestinationTrackingUtil

    private var cityId: String = ""
    var cityName: String = ""
    var webUrl: String = ""

    private var indicatorItems: ArrayList<ImageView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(TravelDestinationViewModel::class.java)
        }

        val args: Bundle? = savedInstanceState ?: arguments
        args?.let {
            webUrl = it.getString(EXTRA_DESTINATION_WEB_URL, "")
            cityId = it.getString(PARAM_CITY_ID, "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_DESTINATION_WEB_URL, webUrl)
        outState.putString(SAVED_CITY_ID, cityId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_travel_homepage_destination, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        destinationViewModel.travelDestinationCityModel.observe(this, Observer {
            when (it) {
                is Success -> {
                    cityId = it.data.cityId
                    cityName = it.data.cityName
                    destinationViewModel.getInitialList()
                }

                is Fail -> showNetworkErrorLayout()
            }
        })

        destinationViewModel.travelDestinationItemList.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })

        destinationViewModel.isAllError.observe(this, Observer {
            it?.let { isAllError ->
                if (isAllError) showNetworkErrorLayout()
            }
        })
    }

    private fun buildViewPagerIndicator(imageCount: Int) {
        indicator_banner_container.visibility = View.VISIBLE

        indicatorItems.clear()
        indicator_banner_container.removeAllViews()
        for (count in 0 until imageCount) {
            val pointView = ImageView(context)
            pointView.setPadding(5,60,5,60)
            if (count == 0) pointView.setImageResource(getIndicatorFocus())
            else pointView.setImageResource(getIndicator())

            indicatorItems.add(pointView)
            indicator_banner_container.addView(pointView)
        }
    }

    private fun showNetworkErrorLayout() {
        NetworkErrorHelper.showEmptyState(context, view?.rootView) { destinationViewModel.getDestinationCityData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_data), webUrl ) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()

        if (savedInstanceState != null) {
            webUrl = savedInstanceState.getString(SAVED_DESTINATION_WEB_URL, "")
        }

        if (cityId.isEmpty()) {
            destinationViewModel.getDestinationCityData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_data), webUrl)
        } else {
            destinationViewModel.getInitialList()
        }
    }

    override fun onUpdatePeekSize(height: Int) {
        setUpContentPeekSize(height)
    }

    private fun setUpContentPeekSize(peekSize: Int) {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    display.getRealSize(size)
                } else display.getSize(size)
            } catch (err: NoSuchMethodError) {
                display.getSize(size)
            }
            val height = size.y

            //get height of status bar
            val rectgle = Rect()
            val window = it.window
            window.getDecorView().getWindowVisibleDisplayFrame(rectgle)
            val statusBarHeight = rectgle.top
            val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).getTop()
            val titleBarHeight = contentViewTop - statusBarHeight

            val lp = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    height - peekSize - indicator_banner_container.height - Math.abs(titleBarHeight) -
                            resources.getDimensionPixelSize(R.dimen.destination_padding_info))
            lp.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            lp.parallaxMultiplier = 0.6f
            toolbar_container.layoutParams = lp
            toolbar_container.requestLayout()
        }
    }

    private fun renderLayout() {
        (activity as TravelDestinationActivity).setSupportActionBar(travel_homepage_destination_toolbar)
        (activity as TravelDestinationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = travel_homepage_destination_toolbar.navigationIcon
        navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
        (activity as TravelDestinationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        collapsing_toolbar.title = ""

        shimmering_back_icon.setOnClickListener { activity?.onBackPressed() }
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var isShowMoveUpLayout = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = cityName
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.black), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
                    isShow = false
                }

                if (verticalOffset < -50) {
                    arrow_up?.hide()
                    tv_move_up?.hide()
                    isShowMoveUpLayout = false
                } else if (!isShowMoveUpLayout) {
                    arrow_up?.show()
                    tv_move_up?.show()
                    isShowMoveUpLayout = true
                }

                (activity as TravelDestinationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)
            }
        })
    }

    override fun getAdapterTypeFactory(): TravelDestinationTypeFactory = TravelDestinationAdapterTypeFactory(this, this)


    override fun onItemClicked(t: TravelDestinationItemModel?) { /* do nothing */ }

    override fun getScreenName(): String = ""

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun initInjector() {
        getComponent(TravelDestinationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) { /* do nothing */ }

    override fun clickAndRedirect(appUrl: String, webUrl: String) {
        RouteManager.route(context, appUrl)
    }

    override fun onCitySummaryVHBind() {
        destinationViewModel.getDestinationSummaryData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_summary), cityId)
    }

    override fun onCitySummaryLoaded(imgUrls: List<String>, peekSize: Int, cityName: String) {
        if (this.cityName.isEmpty()) this.cityName = cityName
        renderLayout()
        if (imgUrls.isNotEmpty()) {
            travel_homepage_destination_view_pager.setImages(imgUrls)
        }
        buildViewPagerIndicator(imgUrls.size)
        travel_homepage_destination_view_pager.imageViewPagerListener = object : DestinationImageViewPager.ImageViewPagerListener {
            override fun onImageScrolled(position: Int) {
                scrollImageViewPagerIndicator(position)
            }

            override fun onImageClicked(position: Int) {
                ImagePreviewSlider.instance.start(context, cityName, imgUrls, imgUrls, position, travel_homepage_destination_view_pager.image_banner)
            }
        }
        travel_homepage_destination_view_pager.buildView()
        layout_travel_destination_shimmering.hide()
    }

    private fun scrollImageViewPagerIndicator(currentPosition: Int) {
        for (i in 0 until indicatorItems.size) {
            if (currentPosition != i) indicatorItems[i].setImageResource(getIndicator())
            else indicatorItems[i].setImageResource(getIndicatorFocus())
        }
    }

    private fun getIndicatorFocus(): Int = R.drawable.widget_image_view_indicator_focus
    private fun getIndicator(): Int = R.drawable.widget_image_view_indicator

    override fun onCityRecommendationVHBind() {
        destinationViewModel.getCityRecommendationData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation),
                cityId, "ALL", CITY_RECOMMENDATION_ORDER)
    }

    override fun onCityDealsVHBind() {
        destinationViewModel.getCityRecommendationData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation),
                cityId, "DEALS", CITY_DEALS_ORDER)
    }

    override fun onCityArticleVHBind() {
        destinationViewModel.getCityArticles(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_article), cityId)
    }

    override fun onOrderListVHBind() {
        destinationViewModel.getOrderList(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_order_list), cityId)
    }

    override fun onCityEventVHBind() {
        destinationViewModel.getCityRecommendationData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation),
                cityId, "EVENTS", CITY_EVENT_ORDER)
    }

    override fun onTrackOrderListImpression(list: List<TravelDestinationSectionViewModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.orderListImpression(list, firstVisiblePosition)
    }

    override fun onTrackOrderClick(item: TravelDestinationSectionViewModel.Item, position: Int) {
        travelDestinationTrackingUtil.orderListClicked(item, position)
    }

    override fun onTrackRecommendationsImpression(list: List<TravelDestinationSectionViewModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityRecommendationImpression(list, firstVisiblePosition)
    }

    override fun onTrackRecommendationItemClick(item: TravelDestinationSectionViewModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityRecommendationItemClicked(item, position)
    }

    override fun onTrackEventsImpression(list: List<TravelDestinationSectionViewModel.Item>, firstVisiblePosition: Int) {
       travelDestinationTrackingUtil.cityEventsImpression(list, firstVisiblePosition)
    }

    override fun onTrackEventItemClick(item: TravelDestinationSectionViewModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityEventsClick(item, position)
    }

    override fun onTrackEventClickSeeAll() {
        travelDestinationTrackingUtil.cityEventsSeeAllClicked()
    }

    override fun onTrackDealsImpression(list: List<TravelDestinationSectionViewModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityDealsImpression(list, firstVisiblePosition)
    }

    override fun onTrackDealsItemClick(item: TravelDestinationSectionViewModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityDealsClick(item, position)
    }

    override fun onTrackDealsClickSeeAll() {
        travelDestinationTrackingUtil.cityDealsSeeAllClicked()
    }

    override fun onTrackArticleImpression(list: List<TravelArticleModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityArticleImpression(list, firstVisiblePosition)
    }

    override fun onTrackArticleItemClick(item: TravelArticleModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityArticleClicked(item, position)
    }

    override fun onTrackArticleClickSeeAll() {
        travelDestinationTrackingUtil.cityArticleClickSeeAll()
    }

    companion object {

        const val SAVED_DESTINATION_WEB_URL = "webUrl"
        const val SAVED_CITY_ID = "cityId"

        fun getInstance(webUrl: String, cityId: String): TravelDestinationFragment = TravelDestinationFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_DESTINATION_WEB_URL, webUrl)
                putString(PARAM_CITY_ID, cityId)
            }
        }
    }
}