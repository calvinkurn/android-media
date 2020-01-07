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
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
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
import com.tokopedia.travelhomepage.destination.widget.ImageViewPager
import kotlinx.android.synthetic.main.layout_image_slider.view.*
import android.widget.LinearLayout
import android.graphics.Point
import android.os.Build
import android.widget.FrameLayout
import com.google.android.material.appbar.CollapsingToolbarLayout


/**
 * @author by jessica on 2019-12-20
 */

class TravelDestinationFragment : BaseListFragment<TravelDestinationItemModel, TravelDestinationTypeFactory>(), OnClickListener,
OnViewHolderBindListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: TravelDestinationViewModel

    var cityId: String = ""
    var webUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(TravelDestinationViewModel::class.java)
        }

        arguments?.let {
            webUrl = it.getString(EXTRA_DESTINATION_WEB_URL, "")
        }
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
                    renderLayout(it.data.cityName)
                    destinationViewModel.getInitialList()
                }
            }
        })

        destinationViewModel.travelDestinationItemList.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()

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

            val lp = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 4 / 5)
            lp.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            lp.parallaxMultiplier = 0.6f
            toolbar_container.layoutParams = lp
            toolbar_container.requestLayout()
        }

        destinationViewModel.getDestinationCityData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_data), webUrl)

    }

    fun renderLayout(title: String) {
        (activity as TravelDestinationActivity).setSupportActionBar(travel_homepage_destination_toolbar)
        (activity as TravelDestinationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = travel_homepage_destination_toolbar.navigationIcon
        navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
        (activity as TravelDestinationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        collapsing_toolbar.title = ""
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = title
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.black), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.design.R.color.white), PorterDuff.Mode.SRC_ATOP)
                    isShow = false
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

    override fun onCitySummaryLoaded(imgUrls: List<String>, peekSize: Int) {
        if (imgUrls.isNotEmpty()) {
            travel_homepage_destination_view_pager.setImages(imgUrls)
        }
        travel_homepage_destination_view_pager.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener {
            override fun onImageClicked(position: Int) {
                ImagePreviewSlider.instance.start(context, "lalalala", imgUrls, imgUrls, position, travel_homepage_destination_view_pager.image_banner)
            }
        }
        travel_homepage_destination_view_pager.buildView()
    }

    override fun onCityRecommendationVHBind() {
        destinationViewModel.getCityRecommendationData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation), cityId)
    }

    override fun onCityDealsVHBind() {
        destinationViewModel.getCityDeals(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation), cityId)
    }

    override fun onCityArticleVHBind() {
        destinationViewModel.getCityArticles(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_article), cityId)
    }

    companion object {
        fun getInstance(webUrl: String): TravelDestinationFragment = TravelDestinationFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_DESTINATION_WEB_URL, webUrl)
            }
        }
    }
}