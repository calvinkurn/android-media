package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.ViewFlipper
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetSellerOnboarding
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateCardActivity
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashIntroViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_intro.*
import javax.inject.Inject

class TokomemberDashIntroFragment : BaseDaggerFragment() {

    private var rootView: RelativeLayout? = null
    private var viewFlipperIntro : ViewFlipper?=null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberIntroViewmodel: TokomemberDashIntroViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashIntroViewModel::class.java)
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
        tokomemberIntroViewmodel.getSellerInfo()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberIntroViewmodel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    tokomemberIntroViewmodel.getIntroInfo(
                        it.data.userShopInfo?.info?.shopId?.toInt()?:0
                    )
                }
                is Fail -> {
                }
            }
        })

        tokomemberIntroViewmodel.tokomemberOnboardingResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    viewFlipperIntro?.displayedChild = 1
                    populateUI(it.data.membershipGetSellerOnboarding )
                }
                is Fail -> {
                    viewFlipperIntro?.displayedChild = 0
                }
            }
        })
    }

    private fun populateUI(membershipGetSellerOnboarding: MembershipGetSellerOnboarding?) {
        tvTitle.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.title?.get(0)
                ?: ""
        tvSubtitle.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.subTitle?.get(0)
                ?: ""
        tvSectionOne.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit?.get(
                0
            )?.benefit
                ?: ""
        tvSectionTwo.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit?.get(
                1
            )?.benefit
                ?: ""
        tvSectionThree.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit?.get(
                2
            )?.benefit
                ?: ""
        tvSectionFour.text =
            membershipGetSellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit?.get(
                3
            )?.benefit
                ?: ""

        if (!layoutVideo.isPlaying) {
            layoutVideo.setVideoURI(Uri.parse("https://vod.tokopedia.com/view/adaptive.m3u8?id=bb4a5fb8790f4d8a95e75ca9c5bcf53"))
            layoutVideo.setOnPreparedListener { mp ->
               //  mp.isLooping =  true
            }
            layoutVideo.start()
        }

        btnContinue.setOnClickListener {
            startActivity(Intent(requireActivity() , TokomemberDashCreateCardActivity::class.java))
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
        fun newInstance(): TokomemberDashIntroFragment {
            return TokomemberDashIntroFragment()
        }
    }
}