package com.tokopedia.entertainment.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.EntertainmentHomeAdapter
import com.tokopedia.entertainment.adapter.HomeItem
import com.tokopedia.entertainment.adapter.factory.HomeTypeFactoryImpl
import com.tokopedia.entertainment.adapter.viewholder.EventCarouselViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel
import com.tokopedia.entertainment.adapter.viewmodel.CategoryViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel
import kotlinx.android.synthetic.main.ent_home_fragment.*
import java.util.*

/**
 * Author errysuprayogi on 27,January,2020
 */

class HomeEntertainmentFragment : BaseDaggerFragment() {

    companion object {
        fun getInstance(): HomeEntertainmentFragment = HomeEntertainmentFragment()
        val TAG = HomeEntertainmentFragment::class.java.simpleName
    }


    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val itemsEvent1 : List<EventCarouselViewHolder.EventItemModel> = Arrays.asList(
                EventCarouselViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                ),
                EventCarouselViewHolder.EventItemModel(
                        "https://ecs7.tokopedia.net/img/banner/2019/12/23/41831484/41831484_49d7ce00-a29f-4f66-b34f-9914e45d1f1a.jpg",
                        "SEA LIFE Bangkok Ocean World",
                        "Bangkok",
                        "Rp 83.000",
                        "25\nFEB"
                )
        )

        val items: List<HomeItem<*>> = Arrays.asList(
                BannerViewModel(),
                CategoryViewModel(),
                EventCarouselViewModel(itemsEvent1),
                EventGridViewModel("Wahana keren yang wajib dicoba")
        )
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = EntertainmentHomeAdapter(HomeTypeFactoryImpl(), items)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ent_home_fragment, container, false)
        return view
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
}