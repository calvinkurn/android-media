package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.Cta
import com.tokopedia.tokomember_seller_dashboard.model.TickerItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_home_fragment.*
import javax.inject.Inject

class TokomemberDashHomeFragment : BaseDaggerFragment() {

    private var tmTracker: TmTracker? = null
    private var shopId = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashHomeViewmodel: TokomemberDashHomeViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashHomeViewmodel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tmTracker = TmTracker()
        tmTracker?.viewHomeTabsSection(shopId.toString())

        renderTicker(null)
        ivShopContainer.loadImage("https://ecs7.tokopedia.net/cards/ray2-f.png")
        ivShopIcon.loadImage("https://images.tokopedia.net/img/seller_no_logo_0.png")
        tvShopName.text = "desynila7"

        observeViewModel()
        tokomemberDashHomeViewmodel.getHomePageData(6553698, 3827)
    }

    private fun observeViewModel() {

        tokomemberDashHomeViewmodel.tokomemberHomeResultLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.SUCCESS->{
                    it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.homeCardTemplate?.backgroundColor
                    renderTicker(it.data?.membershipGetSellerAnalyticsTopSection?.ticker)
                }
                TokoLiveDataResult.STATUS.ERROR->{

                }
            }
        })
    }

    private fun renderTicker(ticker: List<TickerItem?>?) {
        val list = ArrayList<TickerItem>()
        list.add(TickerItem(Cta(appLink = "tokopedia://rewards", text = "Pelajari TokoMember"), title = "Mau lebih mengenal TokoMember?", description = "Yuk, cari tahu cara kerja TokoMember dan berbagai keuntungan yang bisa kamu dapatkan di sini!", iconImageUrl = "https://images.tokopedia.net/img/retention/tokomember/seller/homepage/ticker/card/Intro.png"))
        list.add(TickerItem(Cta(appLink = "tokopedia://rewards", text = "Pelajari TokoMember"), title = "Cek cara pakai Statistik TokoMember, yuk!", description = "Lihat cara baca data yang ditampilkan di halaman ini, biar kamu bisa atur strategi penjualan dengan TokoMember!", iconImageUrl = "https://images.tokopedia.net/img/retention/tokomember/seller/homepage/ticker/graph/Graph.png"))
        list.add(TickerItem(Cta(appLink = "tokopedia://rewards", text = "Pelajari TokoMember"), title = "Cek cara pakai Statistik TokoMember, yuk!", description = "Lihat cara baca data yang ditampilkan di halaman ini, biar kamu bisa atur strategi penjualan dengan TokoMember!", iconImageUrl = "https://images.tokopedia.net/img/retention/tokomember/seller/homepage/ticker/graph/Graph.png"))
        val itemParam = { view: View, data: Any ->
            val ivTicker = view.findViewById<ImageUnify>(R.id.ivTicker)
            val tvTickerTitle = view.findViewById<Typography>(R.id.tvTickerTitle)
            val tvTickerDesc = view.findViewById<Typography>(R.id.tvTickerDesc)
            val tvTickerCta = view.findViewById<Typography>(R.id.tvTickerCta)
            tvTickerTitle.text = (data as TickerItem).title
            tvTickerDesc.text = data.description
            tvTickerCta.text = data.cta?.text
            ivTicker.loadImage(data.iconImageUrl)
            tvTickerCta.setOnClickListener {
                try {
                    RouteManager.route(context, data.cta?.appLink)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        carouselTicker.apply {
            slideToScroll = 1
            indicatorPosition = CarouselUnify.INDICATOR_BC
            infinite = true
            addItems(R.layout.tm_dash_home_ticker_item, list as ArrayList<Any>, itemParam)
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberDashHomeFragment {
            return TokomemberDashHomeFragment()
        }
    }
}