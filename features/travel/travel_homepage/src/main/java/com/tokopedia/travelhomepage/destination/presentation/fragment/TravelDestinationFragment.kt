package com.tokopedia.travelhomepage.destination.presentation.fragment

import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.common.util.TravelHomepageGqlQuery
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingUtil
import com.tokopedia.travelhomepage.destination.di.TravelDestinationComponent
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity.Companion.EXTRA_DESTINATION_WEB_URL
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity.Companion.PARAM_CITY_ID
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel
import com.tokopedia.travelhomepage.destination.widget.DestinationImageViewPager
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelDestinationTypeFactory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_travel_homepage_destination.*
import kotlinx.android.synthetic.main.layout_image_slider.view.*
import kotlinx.android.synthetic.main.layout_travel_destination_shimmering.*
import kotlinx.android.synthetic.main.layout_travel_destination_summary.*
import javax.inject.Inject


/**
 * @author by jessica on 2019-12-20
 */

class TravelDestinationFragment : BaseListFragment<TravelDestinationItemModel, TravelDestinationTypeFactory>(), ActionListener,
        OnViewHolderBindListener {

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

        destinationViewModel.travelDestinationCityModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    cityId = it.data.cityId
                    cityName = it.data.cityName
                    destinationViewModel.getInitialList()
                    destinationViewModel.getAllContent(TravelHomepageGqlQuery.DESTINATION_CITY_SUMMARY,
                            TravelHomepageGqlQuery.RECOMMENDATION,
                            TravelHomepageGqlQuery.ORDER_LIST,
                            TravelHomepageGqlQuery.DESTINATION_CITY_ARTICLE,
                            cityId)
                }

                is Fail -> showNetworkErrorLayout()
            }
        })

        destinationViewModel.travelDestinationItemList.observe(viewLifecycleOwner, Observer {
            clearAllData()
            it?.run {
                if (it.isEmpty()) showNetworkErrorLayout()
                else renderList(this) }
        })

        destinationViewModel.isAllError.observe(viewLifecycleOwner, Observer {
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
            pointView.setPadding(5, 60, 5, 60)
            if (count == 0) pointView.setImageResource(getIndicatorFocus())
            else pointView.setImageResource(getIndicator())

            indicatorItems.add(pointView)
            indicator_banner_container.addView(pointView)
        }
    }

    private fun showNetworkErrorLayout() {
        NetworkErrorHelper.showEmptyState(context, view?.rootView) {
            loadData(0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (getRecyclerView(view) as? VerticalRecyclerView)?.clearItemDecoration()

        if (savedInstanceState != null) {
            webUrl = savedInstanceState.getString(SAVED_DESTINATION_WEB_URL, "")
        }
    }

    private fun setUpContentPeekSize(peekSize: Int) {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (err: NoSuchMethodError) {
                display.getSize(size)
            }
            val height = size.y
            val titleBarHeight = getStatusBarHeight(it)

            val heightTitle = peekSize + resources.getDimensionPixelSize(R.dimen.destination_padding_info)
            val heightIndicator = indicator_banner_container.height + resources.getDimensionPixelSize(R.dimen.destination_margin_top_indicator)
            val diffHeight = heightIndicator + heightTitle - Math.abs(resources.getDimensionPixelSize(R.dimen.destination_margin_top_info))

            val tmpHeight = height - diffHeight - Math.abs(titleBarHeight) - getSoftButtonsBarHeight()

            val lp = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    tmpHeight)
            lp.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            lp.parallaxMultiplier = 0.6f
            toolbar_container.layoutParams = lp
            toolbar_container.requestLayout()
        }
    }

    private fun getStatusBarHeight(it: FragmentActivity): Int {
        //get height of status bar
        val rectgle = Rect()
        val window = it.window
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle)
        val statusBarHeight = rectgle.top
        val contentViewTop = window.findViewById<View>(ID_ANDROID_CONTENT).getTop()
        val titleBarHeight = contentViewTop - statusBarHeight
        return titleBarHeight
    }

    private fun getSoftButtonsBarHeight(): Int {
            val metrics = DisplayMetrics();
            activity?.let {
                it.windowManager.defaultDisplay.getMetrics(metrics);
                val usableHeight = metrics.heightPixels;
                it.windowManager.defaultDisplay.getRealMetrics(metrics);
                val realHeight = metrics.heightPixels;
                if (realHeight > usableHeight)
                    return realHeight - usableHeight;
                else
                    return 0;
            }
        return 0;
    }

    private fun renderLayout() {
        (activity as TravelDestinationActivity).setSupportActionBar(travel_homepage_destination_toolbar)
        (activity as TravelDestinationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = travel_homepage_destination_toolbar.navigationIcon
        navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
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
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
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


    override fun onItemClicked(t: TravelDestinationItemModel?) { /* do nothing */
    }

    override fun getScreenName(): String = ""

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun initInjector() {
        getComponent(TravelDestinationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) { /* do nothing */
        if (cityId.isEmpty()) {
            if (webUrl.last() != '/') webUrl += "/"
            destinationViewModel.getDestinationCityData(TravelHomepageGqlQuery.DESTINATION_CITY_DATA, webUrl)
        } else {
            destinationViewModel.getInitialList()
            destinationViewModel.getAllContent(TravelHomepageGqlQuery.DESTINATION_CITY_SUMMARY,
                    TravelHomepageGqlQuery.RECOMMENDATION,
                    TravelHomepageGqlQuery.ORDER_LIST,
                    TravelHomepageGqlQuery.DESTINATION_CITY_ARTICLE,
                    cityId)
        }
    }

    override fun clickAndRedirect(appUrl: String, webUrl: String) {
        RouteManager.route(context, appUrl)
    }

    override fun onCitySummaryLoaded(imgUrls: List<String>, peekSize: Int, cityName: String) {
        if (this.cityName.isEmpty()) this.cityName = cityName
        renderLayout()
        setUpContentPeekSize(peekSize)
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
        layout_travel_destination_shimmering.alpha = 0f
    }

    private fun scrollImageViewPagerIndicator(currentPosition: Int) {
        for (i in 0 until indicatorItems.size) {
            if (currentPosition != i) indicatorItems[i].setImageResource(getIndicator())
            else indicatorItems[i].setImageResource(getIndicatorFocus())
        }
    }

    private fun getIndicatorFocus(): Int = R.drawable.widget_image_view_indicator_focus
    private fun getIndicator(): Int = R.drawable.widget_image_view_indicator

    override fun onTrackOrderListImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.orderListImpression(list, firstVisiblePosition)
    }

    override fun onTrackOrderClick(item: TravelDestinationSectionModel.Item, position: Int) {
        travelDestinationTrackingUtil.orderListClicked(item, position)
    }

    override fun onTrackRecommendationsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityRecommendationImpression(list, firstVisiblePosition)
    }

    override fun onTrackRecommendationItemClick(item: TravelDestinationSectionModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityRecommendationItemClicked(item, position)
    }

    override fun onTrackEventsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityEventsImpression(list, firstVisiblePosition)
    }

    override fun onTrackEventItemClick(item: TravelDestinationSectionModel.Item, position: Int) {
        travelDestinationTrackingUtil.cityEventsClick(item, position)
    }

    override fun onTrackEventClickSeeAll() {
        travelDestinationTrackingUtil.cityEventsSeeAllClicked()
    }

    override fun onTrackDealsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int) {
        travelDestinationTrackingUtil.cityDealsImpression(list, firstVisiblePosition)
    }

    override fun onTrackDealsItemClick(item: TravelDestinationSectionModel.Item, position: Int) {
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