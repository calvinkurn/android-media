package com.tokopedia.scp_rewards.cabinet.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.postDelayed
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalyticsImpl
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.domain.model.MedaliCabinetData
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.MedalCabinetViewModel
import com.tokopedia.scp_rewards.common.constants.NON_WHITELISTED_USER_ERROR_CODE
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.launchLink
import com.tokopedia.scp_rewards.databinding.FragmentMedalCabinetLayoutBinding
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.medal.BannerData
import com.tokopedia.scp_rewards_widgets.medal.MedalCallbackListener
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MedalCabinetFragment : BaseDaggerFragment() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null
    private lateinit var binding: FragmentMedalCabinetLayoutBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private var medaliSlug = ""

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalCabinetViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(MedalCabinetComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.extras?.getString(
                ApplinkConstInternalPromo.SOURCE_PARAM,
                ""
            ) ?: ""
        }
        activity?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedalCabinetLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHomePage(medaliSlug)
        setupToolbar(binding.toolbar)
        setTopBottomMargin()
        setupScrollListener()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.cabinetLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    showData(it.data as MedaliCabinetData)
                    MedalCabinetAnalyticsImpl.sendViewMedalCabinetPageEvent()
                }

                is Loading -> {
                    binding.mainFlipper.displayedChild = 0
                    MedalCabinetAnalyticsImpl.sendViewMedalCabinetSkeletonPageEvent()
                }

                is Error -> {
                    handleError(it)
                }

                else -> {}
            }
        }
    }

    private fun showData(medalCabinetData: MedaliCabinetData) {
        binding.viewCabinet.postDelayed(1500) {
            binding.mainFlipper.displayedChild = 1
            binding.viewCabinet.bindData(
                cabinetHeader = medalCabinetData.header ?: CabinetHeader(),
                medalList = listOf(
                    medalCabinetData.earnedMedaliData ?: MedalData(),
                    medalCabinetData.progressMedaliData ?: MedalData()
                )
            )
            if (medalCabinetData.earnedMedaliData?.bannerData != null) {
                MedalCabinetAnalyticsImpl.sendViewBannerEvent(medalCabinetData.earnedMedaliData.bannerData?.creativeName.orEmpty(), medalCabinetData.earnedMedaliData.id.toString())
            } else if (medalCabinetData.progressMedaliData?.bannerData != null) {
                MedalCabinetAnalyticsImpl.sendViewBannerEvent(medalCabinetData.progressMedaliData.bannerData?.creativeName.orEmpty(), medalCabinetData.progressMedaliData.id.toString())
            }

            binding.viewCabinet.attachMedalClickListener(object : MedalCallbackListener {
                override fun onMedalClick(medalItem: MedalItem) {
                    if (medalItem.isEarned()) {
                        MedalCabinetAnalyticsImpl.sendClickUnlockedMedalEvent(
                            medalItem.id.toString(),
                            medalItem.isDisabled ?: false,
                            medalItem.name.orEmpty(),
                            medalItem.extraInfo.orEmpty()
                        )
                    } else {
                        MedalCabinetAnalyticsImpl.sendClickLockedMedalEvent(
                            medalItem.id.toString(),
                            medalItem.isDisabled ?: false,
                            medalItem.name.orEmpty(),
                            medalItem.extraInfo.orEmpty(),
                            medalItem.progression.toString()
                        )
                    }
                    requireContext().launchLink(
                        appLink = medalItem.cta?.appLink,
                        webLink = medalItem.cta?.deepLink
                    )
                }

                override fun onSeeMoreClick(medalData: MedalData) {
                    if (medalData.isEarned()) {
                        medalData.medalList?.firstOrNull()?.let { medalItem ->
                            MedalCabinetAnalyticsImpl.sendClickUnlockedMedalSectionCtaEvent(medalItem.cta?.text.orEmpty())
                        }
                    } else {
                        medalData.medalList?.firstOrNull()?.let { medalItem ->
                            MedalCabinetAnalyticsImpl.sendClickLockedMedalSectionCtaEvent(medalItem.cta?.text.orEmpty())
                        }
                    }
                    requireContext().launchLink(
                        appLink = medalData.cta?.appLink,
                        webLink = medalData.cta?.deepLink
                    )
                }

                override fun onMedalLoad(medalItem: MedalItem) {
                    if (medalItem.isEarned()) {
                        MedalCabinetAnalyticsImpl.sendViewUnlockedMedalEvent(
                            medalItem.id.toString(),
                            medalItem.isDisabled ?: false,
                            medalItem.name.orEmpty(),
                            medalItem.extraInfo.orEmpty()
                        )
                    } else {
                        MedalCabinetAnalyticsImpl.sendViewLockedMedalEvent(
                            medalItem.id.toString(),
                            medalItem.isDisabled ?: false,
                            medalItem.name.orEmpty(),
                            medalItem.extraInfo.orEmpty(),
                            medalItem.progression.toString()
                        )
                    }
                }

                override fun onMedalFailed(medalItem: MedalItem) {
                    MedalCabinetAnalyticsImpl.sendViewUnlockedMedalGenericEvent(medalItem.id.toString())
                }

                override fun onSeeMoreLoad(medalData: MedalData) {
                    if (medalData.isEarned()) {
                        medalData.medalList?.firstOrNull()?.let { medalItem ->
                            MedalCabinetAnalyticsImpl.sendViewUnlockedMedalSectionCtaEvent(medalItem.cta?.text.orEmpty())
                        }
                    } else {
                        medalData.medalList?.firstOrNull()?.let { medalItem ->
                            MedalCabinetAnalyticsImpl.sendViewLockedMedalSectionCtaEvent(medalItem.cta?.text.orEmpty())
                        }
                    }
                }

                override fun onBannerClick(bannerData: BannerData?, position: Int?) {
                    MedalCabinetAnalyticsImpl.sendClickBannerEvent(bannerData?.creativeName.orEmpty(), position.toString())
                    requireContext().launchLink(
                        appLink = bannerData?.appLink,
                        webLink = bannerData?.webLink
                    )
                }
            })
        }
    }

    private fun setupToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                elevation = 0f
            }
            windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            setTransparentStatusBar()
        }
    }

    private fun setWhiteStatusBar() {
        (activity as AppCompatActivity?)?.apply {
            window?.statusBarColor = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN900)

            windowInsetsController?.isAppearanceLightStatusBars = true
        }
    }

    private fun setTransparentStatusBar() {
        (activity as AppCompatActivity?)?.apply {
            activity?.window?.statusBarColor = Color.TRANSPARENT
            binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
            setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN0)

            windowInsetsController?.isAppearanceLightStatusBars = false
        }
    }

    private fun setupScrollListener() {
        binding.viewCabinet.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (binding.viewCabinet.computeVerticalScrollOffset() == 0) {
                    setTransparentStatusBar()
                } else {
                    setWhiteStatusBar()
                }
            }
        })
    }

    private fun setTopBottomMargin() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setToolbarBackButtonTint(color: Int) {
        context?.let {
            binding.toolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    private fun handleError(scpError: Error) {
        setWhiteStatusBar()
        setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN900)
        binding.loadContainer.loaderFlipper.displayedChild = 1

        val error = scpError.error
        when {
            error is UnknownHostException || error is SocketTimeoutException -> {
                binding.loadContainer.medalCabinetError.apply {
                    setType(GlobalError.NO_CONNECTION)
                    setActionClickListener {
                        resetPage()
                        MedalCabinetAnalyticsImpl.sendClickCobaLagiMedalCabinetPageInternetErrorEvent()
                    }
                }
                MedalCabinetAnalyticsImpl.sendViewMedalCabinetPageInternetErrorEvent()
            }

            scpError.errorCode == NON_WHITELISTED_USER_ERROR_CODE -> {
                binding.loadContainer.medalCabinetError.apply {
                    setType(GlobalError.PAGE_NOT_FOUND)
                    errorTitle.text = context.getText(R.string.error_non_whitelisted_user_title)
                    errorDescription.text =
                        context.getText(R.string.error_non_whitelisted_user_description)
                    val ctaText = context.getString(R.string.error_non_whitelisted_user_action)
                    errorAction.text = ctaText
                    setActionClickListener {
                        MedalCabinetAnalyticsImpl.sendClickCtaMedalCabinetPageNonWhitelistedErrorEvent(
                            ctaText
                        )
                        RouteManager.route(context, ApplinkConst.HOME)
                        activity?.finish()
                    }
                }
                MedalCabinetAnalyticsImpl.sendViewMedalCabinetPageNonWhitelistedErrorEvent()
            }

            else -> {
                MedalCabinetAnalyticsImpl.sendViewMedalCabinetPageApiErrorEvent()
                binding.loadContainer.medalCabinetError.apply {
                    setType(GlobalError.SERVER_ERROR)
                    setActionClickListener {
                        resetPage()
                        MedalCabinetAnalyticsImpl.sendClickCobaLagiMedalCabinetPageApiErrorEvent()
                    }
                }
            }
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        val state = viewModel.cabinetLiveData.value
        if (state is Error) {
            when {
                state.error is UnknownHostException || state.error is SocketTimeoutException -> {
                    MedalCabinetAnalyticsImpl.sendClickBackMedalCabinetPageInternetErrorEvent()
                }

                state.errorCode == NON_WHITELISTED_USER_ERROR_CODE -> {
                    MedalCabinetAnalyticsImpl.sendClickBackMedalCabinetPageNonWhitelistedErrorEvent()
                }

                else -> {
                    MedalCabinetAnalyticsImpl.sendClickBackMedalCabinetPageApiErrorEvent()
                }
            }
        } else {
            MedalCabinetAnalyticsImpl.sendClickBackMedalCabinetPageEvent()
        }
        return super.onFragmentBackPressed()
    }

    private fun resetPage() {
        binding.mainFlipper.displayedChild = 0
        binding.loadContainer.loaderFlipper.displayedChild = 0
        setTransparentStatusBar()
        viewModel.getHomePage(medaliSlug)
    }

    override fun getScreenName() = ""
}
