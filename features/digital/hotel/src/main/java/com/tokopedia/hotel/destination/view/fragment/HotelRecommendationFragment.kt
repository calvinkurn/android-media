package com.tokopedia.hotel.destination.view.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.adapter.PopularSearchClickListener
import com.tokopedia.hotel.destination.view.adapter.PopularSearchTypeFactory
import com.tokopedia.hotel.destination.view.adapter.RecentSearchAdapter
import com.tokopedia.hotel.destination.view.adapter.RecentSearchClickListener
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_hotel_recommendation.*
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelRecommendationFragment: BaseListFragment<PopularSearch, PopularSearchTypeFactory>(),
        PopularSearchClickListener, RecentSearchClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    lateinit var currentLocationTextView: TextViewCompat
    lateinit var deleteSearchTextView: TextView
    lateinit var recentSearchLayout: View

    lateinit var recentSearchAdapter: RecentSearchAdapter

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

        permissionCheckerHelper = PermissionCheckerHelper()
        destinationViewModel.setPermissionChecker(permissionCheckerHelper)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_recommendation, container, false)
        initView(view)
        return view
    }

    fun initView(view: View) {
        currentLocationTextView = view.findViewById(R.id.current_location_tv)
        initCurrentLocationTextView()
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

        //dummy data
        var list: MutableList<RecentSearch> = arrayListOf()
        list.add(RecentSearch(0,"", "Jakarta", ""))
        list.add(RecentSearch(0,"", "Jalan Sudirman", ""))
        list.add(RecentSearch(0,"", "Central Park Mall", ""))
        list.add(RecentSearch(0,"", "Bandung", ""))
        list.add(RecentSearch(0,"", "Kyoto Japan", ""))
        recentSearchAdapter.setData(list)
    }

    fun initCurrentLocationTextView() {
        currentLocationTextView.setDrawableLeft(R.drawable.ic_system_action_currentlocation_grayscale_24)
        currentLocationTextView.setOnClickListener {
            destinationViewModel.getCurrentLocation(activity as HotelDestinationActivity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): PopularSearchTypeFactory = PopularSearchTypeFactory(this)

    override fun onItemClicked(t: PopularSearch) {

    }

    override fun loadData(page: Int) {
        var list: MutableList<PopularSearch> = arrayListOf()
        list.add(PopularSearch(0, "city","","Jakarta", "Indonesia",2000))
        list.add(PopularSearch(0, "city","","Bandung", "Indonesia",1200))
        list.add(PopularSearch(0, "city","","Yogyakarta", "Indonesia",1300))
        list.add(PopularSearch(0, "city","","Kuta", "Bali, Indonesia",500))
        renderList(list, false)
    }

    override fun popularSearchClicked(popularSearch: PopularSearch) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(activity as HotelDestinationActivity,
                    requestCode, permissions,
                    grantResults)
        }
    }

    override fun onDeleteRecentSearchItem(keyword: String) {
        if (recentSearchAdapter.itemCount == 0) recentSearchLayout.visibility = View.GONE
        //call viewModel delete recentSearch
    }

    override fun onDeleteAllRecentSearch() {
        recentSearchLayout.visibility = View.GONE
        //call viewModel delete all
    }

    override fun onItemClicked(applink: String, webUrl: String, shouldFinishActivity: Boolean) {
        Toast.makeText(context, "Item Clicked $applink", Toast.LENGTH_SHORT).show()
        if (shouldFinishActivity) activity?.finish()
        //pass to homepage activity
    }

    companion object {
        fun getInstance(): HotelRecommendationFragment = HotelRecommendationFragment()
    }

}