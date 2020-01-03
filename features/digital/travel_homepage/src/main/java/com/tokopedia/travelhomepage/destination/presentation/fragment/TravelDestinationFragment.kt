package com.tokopedia.travelhomepage.destination.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.di.TravelDestinationComponent
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.presentation.activity.TravelDestinationActivity.Companion.EXTRA_DESTINATION_WEB_URL
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelDestinationTypeFactory
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-12-20
 */

class TravelDestinationFragment : BaseListFragment<TravelDestinationItemModel, TravelDestinationTypeFactory>(), OnClickListener,
OnViewHolderBindListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: TravelDestinationViewModel

    var cityId: String = ""

    var webUrl: String = "https://m.tokopedia.com/travel-entertainment/bandung/"

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

        destinationViewModel.getDestinationCityData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_data), webUrl)
    }

    override fun getAdapterTypeFactory(): TravelDestinationTypeFactory = TravelDestinationAdapterTypeFactory(this, this)


    override fun onItemClicked(t: TravelDestinationItemModel?) { /* do nothing */ }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TravelDestinationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) { /* do nothing */ }

    override fun clickAndRedirect(appUrl: String, webUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCitySummaryVHBind() {
        destinationViewModel.getDestinationSummaryData(GraphqlHelper.loadRawString(resources, R.raw.query_travel_destination_city_summary), cityId)
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