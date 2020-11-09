package com.tokopedia.hotel.destination.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_CURRENT_LOCATION_LANG
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_CURRENT_LOCATION_LAT
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_TYPE
import com.tokopedia.hotel.destination.view.adapter.PopularSearchTypeFactory
import com.tokopedia.hotel.destination.view.adapter.RecentSearchAdapter
import com.tokopedia.hotel.destination.view.adapter.RecentSearchListener
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_recommendation.*
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelRecommendationFragment : BaseListFragment<PopularSearch, PopularSearchTypeFactory>(), RecentSearchListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    lateinit var currentLocationTextView: TextView
    lateinit var currentLocationLayout: View
    lateinit var deleteSearchTextView: TextView
    lateinit var recentSearchLayout: View

    lateinit var recentSearchAdapter: RecentSearchAdapter

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var gpsRetryCounter: Int = 0
    private val GPS_MAX_RETRY = 5

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDestinationComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        }

        permissionCheckerHelper = PermissionCheckerHelper()
        destinationViewModel.setPermissionChecker(permissionCheckerHelper)
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
        val staticDimen8dp = context?.getResources()?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
        val recentSearchRecyclerView = view.findViewById(R.id.recent_search_recycler_view) as RecyclerView
        recentSearchRecyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp
                ?: 0, staticDimen8dp ?: 0))
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

    override fun getSwipeRefreshLayoutResourceId(): Int = 0

    override fun getRecyclerViewResourceId() = com.tokopedia.hotel.R.id.recycler_view

    fun initCurrentLocationTextView(view: View) {

        currentLocationTextView = view.findViewById(R.id.current_location_tv)

        currentLocationLayout = view.findViewById(R.id.current_location_layout)
        currentLocationLayout.setOnClickListener {
            destinationViewModel.getCurrentLocation(activity as HotelBaseActivity, fusedLocationProviderClient)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        destinationViewModel.popularSearch.observe(this, androidx.lifecycle.Observer {
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
            }
        })

        destinationViewModel.recentSearch.observe(this, androidx.lifecycle.Observer {
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
            }
        })

        destinationViewModel.longLat.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onClickCurrentLocation(lang = it.data.first, lat = it.data.second)
                is Fail -> if (!it.throwable.message.isNullOrEmpty() && it.throwable.message.equals(GPS_FAILED_SHOW_ERROR)) {
                    onErrorGetLocation()
                } else {
                    checkGPS()
                }
            }
        })
    }

    private fun onClickCurrentLocation(lang: Double, lat: Double) {
        val intent = Intent()
        intent.putExtra(HOTEL_CURRENT_LOCATION_LANG, lang)
        intent.putExtra(HOTEL_CURRENT_LOCATION_LAT, lat)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
        activity?.overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_anim_stay,
                com.tokopedia.common.travel.R.anim.travel_slide_out_up)
    }

    private fun renderRecentSearch(recentSearches: MutableList<RecentSearch>) {
        recentSearchLayout.visibility = if (recentSearches.isEmpty()) View.GONE else View.VISIBLE
        if (recentSearches.size >= 5) recentSearchAdapter.setData(recentSearches.subList(0, 5))
        else recentSearchAdapter.setData(recentSearches)
    }

    override fun getAdapterTypeFactory(): PopularSearchTypeFactory = PopularSearchTypeFactory()

    override fun onItemClicked(popularSearch: PopularSearch) {
        val intent = Intent()
        intent.putExtra(HOTEL_DESTINATION_NAME, popularSearch.name)
        intent.putExtra(HOTEL_DESTINATION_SEARCH_ID, popularSearch.searchId)
        intent.putExtra(HOTEL_DESTINATION_SEARCH_TYPE, popularSearch.type)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
        activity?.overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_anim_stay,
                com.tokopedia.common.travel.R.anim.travel_slide_out_up)
    }

    override fun loadData(page: Int) {
        showOnlyList(true)
        destinationViewModel.getHotelRecommendation()
    }

    private fun showOnlyList(showListOnly: Boolean) {
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
        intent.putExtra(HOTEL_DESTINATION_SEARCH_ID, recentSearch.property.id)
        intent.putExtra(HOTEL_DESTINATION_SEARCH_TYPE, recentSearch.property.type)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
        activity?.overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_anim_stay,
                com.tokopedia.common.travel.R.anim.travel_slide_out_up)
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

    private fun checkGPS() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showDialogEnableGPS()
        } else {
            if (gpsRetryCounter < GPS_MAX_RETRY) {
                gpsRetryCounter++
                destinationViewModel.getCurrentLocation(activity as HotelBaseActivity, fusedLocationProviderClient)
            } else {
                destinationViewModel.getLocationFromUpdates(fusedLocationProviderClient)
                gpsRetryCounter = 0
            }
        }
    }

    private fun showDialogEnableGPS() {
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.hotel_recommendation_gps_dialog_title))
        dialog.setDescription(getString(R.string.hotel_recommendation_gps_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.hotel_recommendation_gps_dialog_ok))
        dialog.setPrimaryCTAClickListener {
            startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPS)
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(getString(R.string.hotel_recommendation_gps_dialog_cancel))
        dialog.setSecondaryCTAClickListener {
            onErrorGetLocation()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onErrorGetLocation() {
        NetworkErrorHelper.showRedSnackbar(activity, getString(R.string.hotel_destination_error_get_location))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_GPS -> {
                destinationViewModel.getCurrentLocation(activity as HotelBaseActivity, fusedLocationProviderClient)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GPS = 10101
        const val GPS_FAILED_SHOW_ERROR = "GPS_FAILED_SHOW_ERROR"

        fun getInstance(): HotelRecommendationFragment = HotelRecommendationFragment()
    }

}