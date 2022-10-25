package com.tokopedia.tokomember_seller_dashboard.view.fragment


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.shape.CornerFamily
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.EditCardCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.TickerItem
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_IS_SHOW_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.LOADED
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH_HOME
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_CTA_PRIMARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TM_PREVIEW_BS_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUCCESS_HAPPY
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_home_fragment.*
import javax.inject.Inject


class TokomemberDashHomeFragment : BaseDaggerFragment() {

    private var shopAvatar = ""
    private var prefManager: TmPrefManager? = null
    private var tmTracker: TmTracker? = null
    private var shopId = 0
    private var isShowBs = false
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashHomeViewmodel: TokomemberDashHomeViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(TokomemberDashHomeViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShowBs = arguments?.getBoolean(BUNDLE_IS_SHOW_BS, false)?:false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.tm_dash_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_home.errorTitle.hide()
        iv_home.errorDescription.hide()
        observeDataFromApi()
        arguments?.getInt(BUNDLE_SHOP_ID)?.let{
            shopId = it
        }
        prefManager = context?.let { TmPrefManager(it) }
        if(shopId == null || shopId.isZero()){
            prefManager?.shopId?.let{
                shopId = it
            }
        }
        if (shopId != null) {
            tokomemberDashHomeViewmodel.getHomePageData(shopId)
        }

        tmTracker = TmTracker()
        tmTracker?.viewHomeTabsSection(shopId.toString())

        if(isShowBs){
            tmTracker?.viewBottomSheetHome(shopId.toString())
            val bundle = Bundle()
            val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
                TM_PREVIEW_BS_TITLE, TM_PREVIEW_BS_DESC,
                TM_SUCCESS_HAPPY, TM_PREVIEW_BS_CTA_PRIMARY, errorCount = 0
            )
            bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
            val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
            bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
                override fun onButtonClick(errorCount: Int) {
                    tmTracker?.clickDismissBottomSheetHome(shopId.toString())
                    bottomsheet.dismiss()
                }
            })
            bottomsheet.show(childFragmentManager,"")
            isShowBs = false
        }
        context?.let {
            btn_edit_card.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            btn_edit_card.background = ContextCompat.getDrawable(it, R.drawable.tm_dash_edit_card)
            btn_edit_card.setOnClickListener {
                if (shopId != null) {
                    tmTracker?.clickHomeUbahKartu(shopId.toString())
                    prefManager?.cardId?.let { it1 ->
                        TmDashCreateActivity.setCardEditCallback(object : EditCardCallback{
                            override fun cardEdit() {
                                tokomemberDashHomeViewmodel.getHomePageData(shopId)
                            }
                        })
                        TmDashCreateActivity.openActivity(
                            shopId,
                            activity,
                            CreateScreenType.CARD,
                            ProgramActionType.EDIT,
                            REQUEST_CODE_REFRESH_HOME,
                            0,
                            cardId = it1,
                            shopAvatar
                        )
                    }
                }
            }
        }
    }

    private fun observeDataFromApi() {
        tokomemberDashHomeViewmodel.tokomemberHomeResultLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.SUCCESS->{
                    tokomemberDashHomeViewmodel.refreshHomeData(LOADED)
                    Glide.with(flShop)
                        .asDrawable()
                        .load(it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.homeCardTemplate?.backgroundImgUrl)
                        .into(object : CustomTarget<Drawable>(){
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                val bitmap = cropImage(resource)
                                flShopbg.setImageBitmap(bitmap)
                                flShopbg.shapeAppearanceModel=flShopbg.shapeAppearanceModel.toBuilder().setTopRightCorner(
                                    CornerFamily.ROUNDED,30f)
                                    .setTopLeftCorner(CornerFamily.ROUNDED,30f)
                                    .build()
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                    shopAvatar = it.data?.membershipGetSellerAnalyticsTopSection?.shopProfile?.shop?.avatar.toString()
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
                    tokomemberDashHomeViewmodel.refreshHomeData(LOADED)
                }
            }
        })

        tokomemberDashHomeViewmodel.tokomemberHomeRefreshLiveData.observe(viewLifecycleOwner, {
            when (it) {
                REFRESH ->{
                    tokomemberDashHomeViewmodel.getHomePageData(shopId)
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

    private fun cropImage(resource:Drawable) : Bitmap{
        val bm = resource.toBitmap()
        return Bitmap.createBitmap(bm,0,0,bm.width-15,bm.height-15)
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
