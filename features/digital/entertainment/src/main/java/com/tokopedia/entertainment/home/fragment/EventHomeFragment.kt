package com.tokopedia.entertainment.home.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventAdapter
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactoryImpl
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.di.EventHomeComponent
import com.tokopedia.entertainment.home.viewmodel.FragmentView
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModel
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModelFactory
import kotlinx.android.synthetic.main.ent_home_fragment.*
import java.util.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 27,January,2020
 */

class EventHomeFragment : BaseDaggerFragment(), FragmentView {

    companion object {
        fun getInstance(): EventHomeFragment = EventHomeFragment()
        val TAG = EventHomeFragment::class.java.simpleName
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
        viewModel.getHomeData(this, this::onSuccessGetData, this::onErrorGetData)

        val itemsEvent1 : List<EventCarouselEventViewHolder.EventItemModel> = Arrays.asList(
                EventCarouselEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                )
        )

        val itemsEvent2 : List<EventGridEventViewHolder.EventItemModel> = Arrays.asList(
                EventGridEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Legoland Malaysia",
                        "Jakarta, Bandung",
                        "Rp 183.000",
                        "Rp 83.000"
                ),
                EventGridEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Legoland Malaysia",
                        "Jakarta, Bandung",
                        "Rp 183.000",
                        "Rp 83.000"
                ),
                EventGridEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Legoland Malaysia",
                        "Jakarta, Bandung",
                        "Rp 183.000",
                        "Rp 83.000"
                ),
                EventGridEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Legoland Malaysia",
                        "Jakarta, Bandung",
                        "Rp 183.000",
                        "Rp 83.000"
                )
        )

        val itemsEvent3 : List<EventLocationEventViewHolder.EventItemModel> = Arrays.asList(
                EventLocationEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Singapore",
                        "Tagline goes here"
                ),
                EventLocationEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Singapore",
                        "Tagline goes here"
                ),
                EventLocationEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Singapore",
                        "Tagline goes here"
                ),
                EventLocationEventViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "Singapore",
                        "Tagline goes here"
                )
        )

//        val eventItems: List<HomeEventItem<*>> = Arrays.asList(
//                BannerViewModel(),
//                CategoryViewModel(),
//                EventCarouselViewModel(itemsEvent1),
//                EventGridViewModel("Wahana keren yang wajib dicoba", itemsEvent2),
//                EventLocationViewModel("Berencana Liburan ke Luar Negeri", itemsEvent3)
//        )


        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            homeAdapter = HomeEventAdapter(HomeTypeFactoryImpl())
            adapter = homeAdapter
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
        Log.d(TAG, "actionMenuMore")
    }

    private fun actionMenuFavorite() {
        Log.d(TAG, "actionMenuFavorite")
    }

    override fun getRes(): Resources = resources
}