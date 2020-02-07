package com.tokopedia.entertainment.home.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventAdapter
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactoryImpl
import com.tokopedia.entertainment.home.di.EventHomeComponent
import com.tokopedia.entertainment.home.viewmodel.FragmentView
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModel
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModelFactory
import com.tokopedia.entertainment.home.widget.MenuSheet
import com.tokopedia.graphql.data.model.CacheType
import kotlinx.android.synthetic.main.ent_home_fragment.*
import okhttp3.Route
import javax.inject.Inject

/**
 * Author errysuprayogi on 27,January,2020
 */

class EventHomeFragment : BaseDaggerFragment(), FragmentView, MenuSheet.ItemClickListener {

    companion object {
        fun getInstance(): EventHomeFragment = EventHomeFragment()
        val TAG = EventHomeFragment::class.java.simpleName
        const val PROMOURL = "https://www.tokopedia.com/promo/tiket/events/"
        const val FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/"
    }

    @Inject
    lateinit var factory : HomeEventViewModelFactory
    lateinit var viewModel : HomeEventViewModel
    lateinit var homeAdapter:  HomeEventAdapter


    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
        getComponent(EventHomeComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.run {
            viewModel = ViewModelProviders.of(this, factory).get(HomeEventViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getHomeData(this, this::onSuccessGetData, this::onErrorGetData, CacheType.CACHE_FIRST)

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            homeAdapter = HomeEventAdapter(HomeTypeFactoryImpl())
            adapter = homeAdapter
        }

        swipe_refresh_layout.setOnRefreshListener {
            viewModel.getHomeData(this, this::onSuccessGetData, this::onErrorGetData, CacheType.CLOUD_THEN_CACHE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ent_home_fragment, container, false)
        return view
    }

    private fun onErrorGetData(throwable: Throwable){
        Log.e(TAG, throwable.localizedMessage)
    }

    private fun onSuccessGetData(data: List<HomeEventItem<*>>) {
        homeAdapter.setItems(data)
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.entertainment_menu_homepage, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> actionMenuFavorite()
            R.id.action_more -> actionMenuMore()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionMenuMore() {
        context?.let { MenuSheet.newInstance(it, this).show() }
    }

    private fun actionMenuFavorite() {
        Log.d(TAG, "actionMenuFavorite")
    }

    override fun onMenuPromoClick() {
        RouteManager.route(context, PROMOURL)
    }

    override fun onMenuHelpClick() {
        RouteManager.route(context, FAQURL)
    }

    override fun onMenuTransactionListClick() {

    }

    override fun getRes(): Resources = resources
}