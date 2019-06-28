package com.tokopedia.hotel.destination.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_CURRENT_LOCATION_LANG
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_CURRENT_LOCATION_LAT
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_TYPE
import com.tokopedia.hotel.destination.view.adapter.PopularSearchTypeFactory
import com.tokopedia.hotel.destination.view.adapter.RecentSearchAdapter
import com.tokopedia.hotel.destination.view.adapter.RecentSearchListener
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_recommendation.*
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelRecommendationFragment: BaseListFragment<PopularSearch, PopularSearchTypeFactory>(), RecentSearchListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    lateinit var currentLocationTextView: TextViewCompat
    lateinit var currentLocationLayout: View
    lateinit var deleteSearchTextView: TextView
    lateinit var recentSearchLayout: View

    lateinit var recentSearchAdapter: RecentSearchAdapter

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDestinationComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_recommendation, container, false)
        initView(view)
        return view
    }

    fun initView(view: View) {
        initCurrentLocationTextView(view)
        initRecentSearch(view)
    }

    fun initRecentSearch(view: View) {
        //init recyclerview
        val layoutManager = ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = context!!.getResources().getDimensionPixelOffset(R.dimen.dp_8)
        val recentSearchRecyclerView = view.findViewById(R.id.recent_search_recycler_view) as RecyclerView
        recentSearchRecyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp, staticDimen8dp))
        recentSearchRecyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recentSearchRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        recentSearchAdapter = RecentSearchAdapter(this)
        recentSearchRecyclerView.adapter = recentSearchAdapter

        //init titleBar
        recentSearchLayout = view.findViewById(R.id.recent_search_layout)
        deleteSearchTextView = view.findViewById(R.id.delete_text_view)
        deleteSearchTextView.setOnClickListener {
            recentSearchAdapter.deleteAllRecentSearch()
        }
    }

    fun initCurrentLocationTextView(view: View) {

        currentLocationTextView = view.findViewById(R.id.current_location_tv)
        currentLocationTextView.setDrawableLeft(R.drawable.ic_system_action_currentlocation_grayscale_24)

        currentLocationLayout = view.findViewById(R.id.current_location_layout)
        currentLocationLayout.setOnClickListener {
            destinationViewModel.getCurrentLocation(activity as HotelBaseActivity, fusedLocationProviderClient)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        destinationViewModel.popularSearch.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    showOnlyList(false)
                    isLoadingInitialData = true
                    renderList(it.data, false)
                }
                is Fail -> {
                    //hide other element
                    showGetListError(it.throwable)
                    showOnlyList(true)
                }
            } })

        destinationViewModel.recentSearch.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    showOnlyList(false)
                    renderRecentSearch(it.data.toMutableList())
                }
                is Fail -> {
                    //hide other element
                    showGetListError(it.throwable)
                    showOnlyList(true)
                }
            } })

        destinationViewModel.longLat.observe(this, android.arch.lifecycle.Observer {when (it) {
            is Success -> onClickCurrentLocation(lang = it.data.first, lat = it.data.second)
            is Fail -> {
                NetworkErrorHelper.showRedSnackbar(activity, getString(R.string.hotel_destination_error_get_location))
            }
        }
        })

        destinationViewModel.deleteSuccess.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                true -> { }
                false -> { }
        }})
    }

    fun onClickCurrentLocation(lang: Double, lat: Double) {
        val intent = Intent()
        intent.putExtra(HOTEL_CURRENT_LOCATION_LANG, lang)
        intent.putExtra(HOTEL_CURRENT_LOCATION_LAT, lat)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    fun renderRecentSearch(recentSearches: MutableList<RecentSearch>) {
        recentSearchLayout.visibility = if (recentSearches.isEmpty()) View.GONE else View.VISIBLE
        if (recentSearches.size >= 5) recentSearchAdapter.setData(recentSearches.subList(0, 5))
        else recentSearchAdapter.setData(recentSearches)
    }

    override fun getAdapterTypeFactory(): PopularSearchTypeFactory = PopularSearchTypeFactory()

    override fun onItemClicked(popularSearch: PopularSearch) {
        val intent = Intent()
        intent.putExtra(HOTEL_DESTINATION_NAME, popularSearch.name)
        intent.putExtra(HOTEL_DESTINATION_ID, popularSearch.destinationId)
        intent.putExtra(HOTEL_DESTINATION_TYPE, popularSearch.type)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun loadData(page: Int) {
        showOnlyList(true)
        destinationViewModel.getHotelRecommendation(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_destination_popular),
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_destination_recent_search))
    }

    fun showOnlyList(showListOnly: Boolean) {
        current_location_layout.visibility = if (showListOnly) View.GONE else View.VISIBLE
        recent_search_layout.visibility = if (showListOnly) View.GONE else View.VISIBLE
        popular_search_title.visibility = if (showListOnly) View.GONE else View.VISIBLE
    }

    override fun onDeleteRecentSearchItem(uuid: String) {
        if (recentSearchAdapter.itemCount == 0) recentSearchLayout.visibility = View.GONE
        destinationViewModel.deleteRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.gql_delete_recent_search_mutation), uuid)
    }

    override fun onDeleteAllRecentSearch() {
        recentSearchLayout.visibility = View.GONE
        destinationViewModel.deleteRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.gql_delete_recent_search_mutation), "")
    }

    override fun onItemClicked(recentSearch: RecentSearch) {
        val intent = Intent()
        intent.putExtra(HOTEL_DESTINATION_NAME, recentSearch.property.value)
        intent.putExtra(HOTEL_DESTINATION_ID, recentSearch.property.id.toInt())
        intent.putExtra(HOTEL_DESTINATION_TYPE, recentSearch.property.type)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun isRecentSearchEmpty() {
        recentSearchLayout.visibility = View.GONE
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandlerHotel.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    companion object {
        fun getInstance(): HotelRecommendationFragment = HotelRecommendationFragment()
    }

}