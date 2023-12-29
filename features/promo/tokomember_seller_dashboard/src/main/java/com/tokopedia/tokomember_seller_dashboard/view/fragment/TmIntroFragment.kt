package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPEN_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.TM_INTRO_BG
import com.tokopedia.tokomember_seller_dashboard.util.TM_NOT_ELIGIBLE_CTA
import com.tokopedia.tokomember_seller_dashboard.util.TM_NOT_ELIGIBLE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TM_NOT_ELIGIBLE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_SELLER_INTRO_EDU
import com.tokopedia.tokomember_seller_dashboard.util.TM_SELLER_INTRO_OS
import com.tokopedia.tokomember_seller_dashboard.util.TM_SELLER_NO_OS
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmIntroAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroItem
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmIntroButtonVh
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashIntroViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_intro_new.*
import javax.inject.Inject

private const val SDK_19 = 19
private const val SDK_20 = 20
private const val SDK_21 = 21

class TmIntroFragment : BaseDaggerFragment(),
    TmIntroButtonVh.TokomemberIntroButtonListener {

    private var rootView: CoordinatorLayout? = null
    private var viewFlipperIntro : ViewFlipper?=null
    private var openBS: Boolean = false
    private var videoUrl:String? =null
    private var cardID:Int?=null
    private var tmTracker: TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmIntroViewModel: TmDashIntroViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashIntroViewModel::class.java)
    }
    val adapter: TmIntroAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TmIntroAdapter(arrayListOf(), TokomemberIntroFactory(this))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getBoolean(BUNDLE_OPEN_BS)?.let {
            openBS = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_intro_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFlipperIntro = view.findViewById(R.id.viewFlipperIntro)
        rootView = view.findViewById(R.id.rootView)
        ivBg.loadImage(TM_INTRO_BG)
        hideStatusBar()
        observeViewModel()
        tmTracker = TmTracker()
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            tmTracker?.viewIntroPage(it.toString())
            introLogic(it)
            btnContinue.setOnClickListener { _ ->
                proceedIntroLogic()
                tmTracker?.clickIntroLanjut(it.toString())
            }
        }

        toolbar_tokomember.setActivity(activity)

        scrollContainer?.viewTreeObserver?.addOnScrollChangedListener {
            val y = scrollContainer.scrollY
            val diff = dpToPx(30)

            if (y > diff) {
                status_bar_bg?.alpha = 1F
                toolbar_tokomember.switchToDarkMode()
                toolbar_tokomember.applyAlphaToToolbarBackground(1F)
                activity?.window?.statusBarColor = Color.WHITE
            } else {
                status_bar_bg?.alpha = 0F
                toolbar_tokomember.switchToTransparentMode()
                toolbar_tokomember.applyAlphaToToolbarBackground(0F)
                activity?.window?.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun introLogic(it: Int) {
        if (com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck.isConnectedToInternet(context)) {
            tmIntroViewModel.getIntroInfo(it)
        }
        else {
            noInternetUi {
                introLogic(it)
            }
        }
    }

    private fun proceedIntroLogic() {
        if (com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck.isConnectedToInternet(context)) {
            if (openBS) {
                redirectSellerOs()
            } else {
                openCreationActivity()
            }
        }
        else {
            noInternetUi {
                proceedIntroLogic()
            }
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun noInternetUi(action: () -> Unit) {
        //show no internet bottomsheet

        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            ERROR_CREATING_TITLE_NO_INTERNET,
            ERROR_CREATING_DESC_NO_INTERNET,
            "",
            RETRY,
            errorCount = 0,
            showSecondaryCta = true
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                action()
            }})
        bottomsheet.show(childFragmentManager,"")
    }
    private fun observeViewModel() {

        tmIntroViewModel.tokomemberOnboardingResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    viewFlipperIntro?.displayedChild = 1
                    buttonContainer.show()
                    cardID = it.data.membershipGetSellerOnboarding?.cardID.toIntOrZero()
                    populateUI(it.data )
                }
                is Fail -> {
                    viewFlipperIntro?.displayedChild = 0
                    buttonContainer.hide()
                }
            }
        })

        tmIntroViewModel.tokomemberIntroSectionResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> renderSectionBenefit(it.data)
                is Fail -> {}
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun populateUI(membershipData: MembershipData) {

        if(openBS){
            redirectSellerOs()
        }
        animateViews()
        val headerData = membershipData.membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText
        headerData?.let {
            var titleText = ""
            it.title?.forEachIndexed { index, title ->
                titleText += if (index!= it.title.size-1){
                    title+"\n"
                }else{
                    title
                }
            }
            tvTitle.text = titleText
            tvSubtitle.text = it.subTitle?.getOrNull(0)
        }
        videoUrl = membershipData.membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeInfo?.infoURL?:""
         setVideoView(
            videoUrl?:"")
    }

    private fun redirectSellerOs(){
        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            TM_NOT_ELIGIBLE_TITLE,
            TM_NOT_ELIGIBLE_DESC,
            TM_SELLER_NO_OS,
            TM_NOT_ELIGIBLE_CTA
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
                    tmTracker?.clickButtonBsNonOs(it.toString())
                }
                RouteManager.route(context,String.format("%s?url=%s", ApplinkConst.WEBVIEW,TM_SELLER_INTRO_OS))
                bottomSheet.dismiss()
            }
        })
        bottomSheet.setOnDismissListener {
            arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
                tmTracker?.clickDismissBsNonOs(it.toString())
            }
            bottomSheet.dismiss()
        }
        bottomSheet.show( childFragmentManager,"")
    }

    private fun animateViews(){
        val animationBenefit =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_benefit_left)
        cardTokmemberParent.startAnimation(animationBenefit)

        val animationIntro =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_push_up)
        buttonContainer.startAnimation(animationIntro)

        val animationVideo =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_push_up)
        frame_video.startAnimation(animationVideo)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderSectionBenefit(data: TokomemberIntroItem) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.addItems(data.tokoVisitable)
    }

    private fun setVideoView(
        url: String) {
        context?.let {
            frame_video?.setVideoPlayer(url)
        }
    }

    override fun onButtonItemClick(position: Int) {
        RouteManager.route(context,String.format("%s?url=%s", ApplinkConst.WEBVIEW,TM_SELLER_INTRO_EDU))
    }

    private fun openCreationActivity() {
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            tmTracker?.clickIntroDaftar(it.toString())
        }
        val intent = Intent(this.context, TmDashCreateActivity::class.java)
        intent.putExtra(BUNDLE_CARD_ID,cardID?:0)
        intent.putExtra(BUNDLE_SHOP_AVATAR, arguments?.getString(BUNDLE_SHOP_AVATAR))
        intent.putExtra(BUNDLE_SHOP_NAME, arguments?.getString(BUNDLE_SHOP_NAME))
        intent.putExtra(BUNDLE_SHOP_ID, arguments?.getInt(BUNDLE_SHOP_ID))
        activity?.finish()
        startActivity(intent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        frame_video?.onDetach()
    }

    override fun onStop() {
        super.onStop()
        if (frame_video.isPlaying() == true) {
            frame_video?.stopVideoPlayer()
        }
    }

    @SuppressLint("ObsoleteSdkInt", "DeprecatedMethod")
    private fun hideStatusBar() {
        rootView?.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            rootView?.requestApplyInsets()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = rootView?.systemUiVisibility
            flags = flags?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            if (flags != null) {
                rootView?.systemUiVisibility = flags
            }
            if (context != null) {
                activity?.window?.statusBarColor = androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            }
        }
        if (Build.VERSION.SDK_INT in SDK_19..SDK_20) {
            activity?.let {
                setWindowFlag(
                    it,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    true
                )
            }
        }
        if (Build.VERSION.SDK_INT >= SDK_19) {
            activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= SDK_21) {
            activity?.let {
                setWindowFlag(
                    it,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    false
                )
            }
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
        val win = activity?.window
        val winParams = win?.attributes
        if (on) {
            winParams?.flags = winParams?.flags?.or(bits)
        } else {
            winParams?.flags = winParams?.flags?.and(bits.inv())
        }
        win?.attributes = winParams
    }

    private fun setStatusBarViewHeight() {
        if (activity != null) status_bar_bg?.layoutParams?.height = getStatusBarHeight(activity)
    }


    fun getStatusBarHeight(context: Context?): Int {
        var height = 0
        val resId = context?.resources?.getIdentifier("status_bar_height", "dimen", "android")
        if (resId != null) {
            if (resId > 0) {
                height = context.resources?.getDimensionPixelSize(resId)?:0
            }
        }
        return height
    }


    companion object {
        fun newInstance(bundle: Bundle?) = TmIntroFragment().apply {
            arguments = bundle
        }
    }
}