package com.tokopedia.entertainment.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.Indicator
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.EntertainmentHomeAdapter
import com.tokopedia.entertainment.adapter.HomeItem
import com.tokopedia.entertainment.adapter.factory.HomeTypeFactoryImpl
import com.tokopedia.entertainment.adapter.viewmodel.CategoryViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel
import kotlinx.android.synthetic.main.ent_home_fragment.*
import java.util.*

/**
 * Author errysuprayogi on 27,January,2020
 */

class HomeEntertainmentFragment : BaseDaggerFragment(), BannerView.OnPromoClickListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener {

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

        var listPromo = List<String>(5) { index: Int ->
            "https://ecs7.tokopedia.net/img/attachment/2020/1/1/42484317/42484317_ddeaa295-aef8-4705-9d4e-1a2adc91581c.jpg"
        }

        banner_home_ent?.setPromoList(listPromo)
        banner_home_ent?.setOnPromoClickListener(this)
        banner_home_ent?.setOnPromoAllClickListener(this)
        banner_home_ent?.onPromoScrolledListener = this
        banner_home_ent?.setOnPromoDragListener(this)
        banner_home_ent?.customWidth = resources.getDimensionPixelSize(R.dimen.banner_item_width)
        banner_home_ent?.setBannerSeeAllTextColor(ContextCompat.getColor(context!!, R.color.ent_green))
        banner_home_ent?.setBannerIndicator(Indicator.GREEN)
        banner_home_ent?.buildView()

        var listHomeItems = Arrays.asList(
                CategoryViewModel(),
                EventCarouselViewModel(),
                EventGridViewModel()
        )
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = EntertainmentHomeAdapter(HomeTypeFactoryImpl(), listHomeItems)
    }


    override fun onPromoScrolled(p0: Int) {

    }

    override fun onPromoDragEnd() {
    }

    override fun onPromoDragStart() {
    }

    override fun onPromoLoaded() {
    }

    override fun onPromoClick(pos: Int) {
    }

    override fun onPromoAllClick() {

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