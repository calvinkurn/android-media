package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.TickerItem
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_IS_SHOW_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_CTA_PRIMARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUCCESS_HAPPY
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_home_fragment.*
import javax.inject.Inject

class TokomemberDashHomeFragment : BaseDaggerFragment() {

    private var prefManager: TmPrefManager? = null
    private var tmTracker: TmTracker? = null
    private var shopId = 0
    private var isShowBs = false

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashHomeViewmodel: TokomemberDashHomeViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashHomeViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShowBs = arguments?.getBoolean(BUNDLE_IS_SHOW_BS, false)?:false
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

        iv_home.errorTitle.hide()
        iv_home.errorDescription.hide()
        observeViewModel()
        var shopId = arguments?.getInt(BUNDLE_SHOP_ID)
        if(shopId == null || shopId.isZero()){
            prefManager = context?.let { TmPrefManager(it) }
            shopId = prefManager?.shopId
        }
        if (shopId != null) {
            tokomemberDashHomeViewmodel.getHomePageData(shopId)
        }

        tmTracker = TmTracker()
        tmTracker?.viewHomeTabsSection(shopId.toString())

        if(isShowBs){
            val bundle = Bundle()
            val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
                TM_PREVIEW_BS_TITLE, TM_PREVIEW_BS_DESC,
                TM_SUCCESS_HAPPY, TM_PREVIEW_BS_CTA_PRIMARY, errorCount = 0
            )
            bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
            val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
            bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
                override fun onButtonClick(errorCount: Int) {
                    bottomsheet.dismiss()
                }
            })
            bottomsheet.show(childFragmentManager,"")
            isShowBs = false
        }
    }

    private fun observeViewModel() {

        tokomemberDashHomeViewmodel.tokomemberHomeResultLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.SUCCESS->{
                    Glide.with(flShop)
                        .asDrawable()
                        .load(it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.homeCardTemplate?.backgroundImgUrl)
                        .into(object : CustomTarget<Drawable>(){
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

                                flShop.background = resource
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                    Glide.with(ivShopIcon)
                        .load(it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.shop?.avatar)
                        .circleCrop()
                        .into(ivShopIcon)
                    tvShopName.text = it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.shop?.name
                    renderTicker(it.data?.membershipGetSellerAnalyticsTopSection?.ticker)
                    val prefManager = context?.let { it1 -> TmPrefManager(it1) }
                    prefManager?.shopId = it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.homeCard?.shopID
                    prefManager?.cardId = it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.homeCard?.id
                }
                TokoLiveDataResult.STATUS.ERROR->{

                }
            }
        })
    }

    private fun renderTicker(list: List<TickerItem?>?) {
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
                    if(data.cta?.appLink.isNullOrEmpty() && data.cta?.urlMobile?.isNotEmpty() == true){
                        RouteManager.route(context,String.format("%s?url=%s", ApplinkConst.WEBVIEW, data.cta.urlMobile))
                    }
                    else if(data.cta?.urlMobile.isNullOrEmpty() && data.cta?.appLink?.isNotEmpty() == true){
                        RouteManager.route(context, data.cta.appLink)
                    }
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
        fun newInstance(bundle: Bundle?) = TokomemberDashHomeFragment().apply {
            arguments = bundle
        }
    }
}