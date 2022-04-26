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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.ViewFlipper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPEN_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberIntroAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroItem
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroButtonVh
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashIntroViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_intro.*
import javax.inject.Inject

class TokomemberDashIntroFragment : BaseDaggerFragment(),
    TokomemberIntroButtonVh.TokomemberIntroButtonListener {

    private var rootView: RelativeLayout? = null
    private var viewFlipperIntro : ViewFlipper?=null
    private var openBS: Boolean = false
    private var videoUrl:String? =null
    private var cardID:Int?=null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberIntroViewmodel: TokomemberDashIntroViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashIntroViewModel::class.java)
    }
    val adapter: TokomemberIntroAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TokomemberIntroAdapter(arrayListOf(), TokomemberIntroFactory(this))
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
        return inflater.inflate(R.layout.tm_dash_intro_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFlipperIntro = view.findViewById(R.id.viewFlipperIntro)
        rootView = view.findViewById(R.id.rootView)
        hideStatusBar()
        observeViewModel()
        renderHeader()
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let { tokomemberIntroViewmodel.getIntroInfo(it) }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {

        tokomemberIntroViewmodel.tokomemberOnboardingResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    viewFlipperIntro?.displayedChild = 1
                    cardID = it.data.membershipGetSellerOnboarding?.cardID.toIntOrZero()
                    populateUI(it.data )
                }
                is Fail -> {
                    viewFlipperIntro?.displayedChild = 0
                }
            }
        })

        tokomemberIntroViewmodel.tokomemberIntroSectionResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> renderSectionBenefit(it.data)
                is Fail -> {}
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateUI(membershipData: MembershipData) {

        if(openBS){
            val bundle = Bundle()
            val tmIntroBottomsheetModel = TmIntroBottomsheetModel("Title", "Desc", "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png")
            bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
            TokomemberBottomsheet.show(bundle, childFragmentManager)
        }

        val animation: Animation =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_benefit_left)
         cardTokmemberParent.startAnimation(animation)

        val animation1: Animation =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_push_up)
        buttonContainer.startAnimation(animation1)

        val headerData = membershipData.membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText
        headerData?.let {
            tvTitle.text = it.title?.getOrNull(0)
            tvSubtitle.text = it.subTitle?.getOrNull(0)
        }
        videoUrl = membershipData.membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeInfo?.infoURL?:""
         setVideoView(
            videoUrl?:"")

        val animation2: Animation =
            AnimationUtils.loadAnimation(this.context, R.anim.tm_dash_intro_push_up)
        frame_video.startAnimation(animation2)

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
        startActivity(Intent(this.context, TokomemberDashCreateActivity::class.java).putExtra(
            "cardID",cardID?:0
        ))
    }

    private fun renderHeader(){
        header?.apply {
            isShowBackButton = true
            headerSubTitle =""
            headerTitle =""
        }
    }

    override fun onResume() {
        super.onResume()
        if(frame_video.isPlaying() == false){
            frame_video?.setVideoPlayer(videoUrl?:"");
        }
    }

    override fun onStop() {
        super.onStop()
        frame_video.stopVideoPlayer()
        frame_video.releaseVideoPlayer()
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
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            }
        }
        if (Build.VERSION.SDK_INT in 19..20) {
            activity?.let {
                setWindowFlag(
                    it,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    true
                )
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
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

    companion object {
        fun newInstance(bundle: Bundle?) = TokomemberDashIntroFragment().apply {
            arguments = bundle
        }
    }
}