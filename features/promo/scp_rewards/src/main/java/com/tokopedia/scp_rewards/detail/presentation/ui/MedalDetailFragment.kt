package com.tokopedia.scp_rewards.detail.presentation.ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.common.constants.NON_WHITELISTED_USER_ERROR_CODE
import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.MEDALI_DETAIL_PAGE
import com.tokopedia.scp_rewards.common.utils.launchLink
import com.tokopedia.scp_rewards.common.utils.launchWeblink
import com.tokopedia.scp_rewards.databinding.MedalDetailFragmentLayoutBinding
import com.tokopedia.scp_rewards.detail.analytics.MedalDetailAnalyticsImpl
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.domain.model.BenefitButton
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliDetailPage
import com.tokopedia.scp_rewards.detail.domain.model.Mission
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards.widget.medalDetail.MedalDetail
import com.tokopedia.scp_rewards.widget.medalHeader.MedalHeaderData
import com.tokopedia.scp_rewards_widgets.medal_footer.FooterData
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel
import com.tokopedia.scp_rewards_widgets.task_progress.Task
import com.tokopedia.scp_rewards_widgets.task_progress.TaskProgress
import com.tokopedia.unifycomponents.Toaster
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MedalDetailFragment : BaseDaggerFragment() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null
    private lateinit var binding: MedalDetailFragmentLayoutBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private var medaliSlug = ""
    private var sourceName = ""

    private val medalDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalDetailViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(MedalDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.data?.pathSegments?.last() ?: ""
            sourceName = it.extras?.getString(
                ApplinkConstInternalPromo.SOURCE_PARAM,
                TrackerConstants.General.SOURCE_OTHER_PAGE
            ) ?: TrackerConstants.General.SOURCE_OTHER_PAGE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MedalDetailFragmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar)
        setTopBottomMargin()
        setupScrollListener()
        setupViewModelObservers()
        getMedaliDetail()
    }

    private fun getMedaliDetail() {
        medalDetailViewModel.getMedalDetail(
            medaliSlug = medaliSlug,
            sourceName = sourceName,
            pageName = MEDALI_DETAIL_PAGE
        )
    }

    private fun setupViewModelObservers() {
        observeBadgeData()
        observeCouponAutoApply()
    }

    private fun observeCouponAutoApply() {
        medalDetailViewModel.autoApplyCoupon.observe(viewLifecycleOwner) {
            it?.let { safeResult ->
                when (safeResult) {
                    is MedalDetailViewModel.AutoApplyState.Error -> {
                        binding.viewMedalFooter.showLoading(safeResult.footerData.id, false)
                        showToastAndNavigateToLink(
                            safeResult.footerData.id,
                            safeResult.throwable.localizedMessage,
                            safeResult.footerData.appLink,
                            safeResult.footerData.url
                        )
                    }

                    is MedalDetailViewModel.AutoApplyState.Loading -> {
                        binding.viewMedalFooter.showLoading(safeResult.footerData.id, true)
                    }

                    is MedalDetailViewModel.AutoApplyState.SuccessCouponApplied -> {
                        showToastAndNavigateToLink(
                            safeResult.footerData.id,
                            safeResult.data?.couponAutoApply?.infoMessage?.title,
                            safeResult.footerData.appLink,
                            safeResult.footerData.url
                        )
                        MedalDetailAnalyticsImpl.sendImpressionAutoApplyToaster(
                            badgeId = medaliSlug,
                            promoCode = medalDetailViewModel.couponCode,
                            couponStatus = medalDetailViewModel.couponStatus,
                            couponNotes = medalDetailViewModel.couponNotes,
                            isAutoApplySuccess = true
                        )
                    }

                    is MedalDetailViewModel.AutoApplyState.SuccessCouponFailed -> {
                        showToastAndNavigateToLink(
                            safeResult.footerData.id,
                            safeResult.data?.couponAutoApply?.infoMessage?.title,
                            safeResult.footerData.appLink,
                            safeResult.footerData.url
                        )
                        MedalDetailAnalyticsImpl.sendImpressionAutoApplyToaster(
                            badgeId = medaliSlug,
                            promoCode = medalDetailViewModel.couponCode,
                            couponStatus = medalDetailViewModel.couponStatus,
                            couponNotes = medalDetailViewModel.couponNotes,
                            isAutoApplySuccess = false
                        )
                    }
                }
            }
        }
    }

    private fun showToastAndNavigateToLink(
        id: Int?,
        message: String?,
        appLink: String?,
        url: String?
    ) {
        binding.viewMedalFooter.showLoading(id, false)
        Toaster.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                toasterCustomBottomHeight = getNavigationBarHeight()
            }
        }
            .build(activity?.window?.decorView!!, message.orEmpty())
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    requireContext().launchLink(appLink, url)
                }
            })
            .show()
    }

    private fun getNavigationBarHeight(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        val resources = context?.resources
        val resourceId: Int =
            resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
        return if (resourceId > 0) {
            resources?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    private fun observeBadgeData() {
        medalDetailViewModel.badgeLiveData.observe(viewLifecycleOwner) {
            it?.let { safeResult ->
                when (safeResult) {
                    is Success<*> -> {
                        binding.mainFlipper.displayedChild = 1
                        val data = safeResult.data as MedalDetailResponseModel
                        setTransparentStatusBar()
                        loadHeader(data.detail?.medaliDetailPage)
                        loadMedalDetails(data.detail?.medaliDetailPage)
                        loadTaskProgress(data.detail?.medaliDetailPage)
                        loadCouponWidget(data.detail?.medaliDetailPage)
                        loadFooter(data.detail?.medaliDetailPage)
                        MedalDetailAnalyticsImpl.sendImpressionMDP(medaliSlug)
                    }

                    is Error -> {
                        setWhiteStatusBar()
                        handleError(safeResult)
                    }

                    is Loading -> {
                        binding.loadContainer.loaderFlipper.visible()
                        MedalDetailAnalyticsImpl.sendImpressionPageShimmer(medaliSlug)
                    }
                }
            }
        }
    }

    private fun loadFooter(medaliDetailPage: MedaliDetailPage?) {
        val listOfButtons = medaliDetailPage?.benefitButtons
        if (listOfButtons.isNullOrEmpty()) {
            binding.viewMedalFooter.gone()
        } else {
            binding.viewMedalFooter.bindData(
                listOfButtons.map {
                    FooterData(
                        text = it.text,
                        url = it.url,
                        appLink = it.appLink,
                        style = it.unifiedStyle,
                        autoApply = it.isAutoApply,
                        couponCode = it.couponCode
                    )
                }
            ) { data ->
                if (data.autoApply) {
                    medalDetailViewModel.applyCoupon(
                        footerData = data,
                        shopId = null,
                        couponCode = data.couponCode.orEmpty()
                    )
                } else {
                    requireContext().launchLink(data.appLink, data.url)
                }
                sendClickCtaAnalytics(data)
            }
            sendViewCtaAnalytics(listOfButtons)
        }
    }

    private fun sendViewCtaAnalytics(listOfButtons: List<BenefitButton>) {
        listOfButtons.find { it.unifiedStyle == "primary" }?.let { applyCouponCta ->
            MedalDetailAnalyticsImpl.sendImpressionShopCta(
                medaliSlug,
                promoCode = medalDetailViewModel.couponCode,
                couponStatus = medalDetailViewModel.couponStatus,
                couponNotes = medalDetailViewModel.couponNotes,
                ctaText = applyCouponCta.text.orEmpty()
            )
        }

        listOfButtons.find { it.unifiedStyle == "secondary" }?.let { showCabinetCta ->
            MedalDetailAnalyticsImpl.sendImpressionCabinetCta(
                medaliSlug,
                promoCode = medalDetailViewModel.couponCode,
                couponStatus = medalDetailViewModel.couponStatus,
                couponNotes = medalDetailViewModel.couponNotes
            )
        }
    }

    private fun sendClickCtaAnalytics(data: FooterData) {
        if (data.style == "primary") {
            MedalDetailAnalyticsImpl.sendClickShopCta(
                medaliSlug,
                promoCode = medalDetailViewModel.couponCode,
                couponStatus = medalDetailViewModel.couponStatus,
                couponNotes = medalDetailViewModel.couponNotes,
                ctaText = data.text.orEmpty()
            )
        } else if (data.style == "secondary") {
            MedalDetailAnalyticsImpl.sendClickCabinetCta(
                medaliSlug,
                promoCode = medalDetailViewModel.couponCode,
                couponStatus = medalDetailViewModel.couponStatus,
                couponNotes = medalDetailViewModel.couponNotes
            )
        }
    }

    private fun loadMedalDetails(medaliDetailPage: MedaliDetailPage?) {
        binding.viewMedalDetail.bindData(
            MedalDetail(
                sponsorText = medaliDetailPage?.sourceText,
                sponsorTextColor = medaliDetailPage?.sourceFontColor,
                sponsorBackgroundColor = medaliDetailPage?.sourceBackgroundColor,
                medalTitle = medaliDetailPage?.name,
                medalDescription = medaliDetailPage?.description
            )
        )
    }

    private fun loadHeader(medaliDetailPage: MedaliDetailPage?) {
        binding.viewMedalHeader.bindData(
            MedalHeaderData(
                isGrayScale = medaliDetailPage?.isMedaliGrayScale ?: false,
                medalIconUrl = medaliDetailPage?.iconImageURL,
                lottieUrl = medaliDetailPage?.shimmerShutterLottieURL,
                lottieSparklesUrl = medaliDetailPage?.outerBlinkingLottieURL,
                baseImageURL = medaliDetailPage?.baseImageURL,
                background = medaliDetailPage?.backgroundImageURL,
                backgroundColor = medaliDetailPage?.backgroundImageColor,
                medalUrl = medaliDetailPage?.innerIconImageURL,
                frameImageUrl = medaliDetailPage?.frameImageURL,
                shimmerImageUrl = medaliDetailPage?.shimmerImageURL,
                frameMaskingImageUrl = medaliDetailPage?.frameMaskingImageURL,
                shutterMaskingImageUrl = medaliDetailPage?.shutterMaskingImageURL,
                shutterUrl = medaliDetailPage?.shutterImageURL,
                shutterText = medaliDetailPage?.shutterText.orEmpty(),
                coachMarkInformation = medaliDetailPage?.coachMark?.text
            )
        ) {
            MedalDetailAnalyticsImpl.sendClickMedali(
                medaliSlug,
                medaliDetailPage?.isMedaliGrayScale ?: false,
                medaliDetailPage?.sourceText.orEmpty(),
                medaliDetailPage?.name.orEmpty(),
                medaliDetailPage?.description.orEmpty()
            )
        }

        MedalDetailAnalyticsImpl.sendImpressionMedali(
            medaliSlug,
            medaliDetailPage?.isMedaliGrayScale ?: false,
            medaliDetailPage?.sourceText.orEmpty(),
            medaliDetailPage?.name.orEmpty(),
            medaliDetailPage?.description.orEmpty()
        )
        medaliDetailPage?.tncButton?.apply {
            binding.tvTermsConditions.text = text
            binding.tvTermsConditions.setOnClickListener {
                MedalDetailAnalyticsImpl.sendClickTncCta(medaliSlug)
                launchWeblink(requireContext(), url.orEmpty())
            }
        }
    }

    private fun loadTaskProgress(medaliDetailPage: MedaliDetailPage?) {
        medaliDetailPage?.mission?.let { safeMission ->
            if (safeMission.task.isNullOrEmpty()) {
                binding.viewTasksProgress.gone()
            } else {
                val taskProgress = TaskProgress(
                    title = safeMission.title,
                    progress = safeMission.progress,
                    tasks = safeMission.task.map {
                        Task(
                            title = it.title,
                            isCompleted = it.isCompleted,
                            progressInfo = it.progressInfo
                        )
                    }
                )
                binding.viewTasksProgress.apply {
                    bindData(taskProgress)
                }
                sendTaskProgressAnalytics(safeMission, medaliDetailPage)
            }
        } ?: run { binding.viewTasksProgress.gone() }
    }

    private fun sendTaskProgressAnalytics(mission: Mission, medaliDetailPage: MedaliDetailPage) {
        val totalTasks = mission.task?.size.orZero()
        val completedTasks = mission.task?.count { it.isCompleted }.orZero()
        MedalDetailAnalyticsImpl.sendImpressionProgressSection(
            medaliSlug,
            isLocked = medaliDetailPage.isMedaliGrayScale ?: false,
            taskProgressPercent = "${mission.progress.orZero()} %",
            noOfTasksCompleted = "$completedTasks/$totalTasks"
        )
    }

    private fun setupToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        (activity as? AppCompatActivity)?.apply {
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
        (activity as? AppCompatActivity)?.apply {
            window?.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN900)

            windowInsetsController?.isAppearanceLightStatusBars = true
            binding.tvTermsConditions.setTextColor(
                ContextCompat.getColor(
                    this,
                    unifyprinciplesR.color.Unify_GN500
                )
            )
        }
    }

    private fun setTransparentStatusBar() {
        (activity as? AppCompatActivity)?.apply {
            activity?.window?.statusBarColor = Color.TRANSPARENT
            binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
            setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN0)

            windowInsetsController?.isAppearanceLightStatusBars = false
            binding.tvTermsConditions.setTextColor(
                ContextCompat.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
        }
    }

    private fun setupScrollListener() {
        binding.scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY == 0) {
                    setTransparentStatusBar()
                } else {
                    setWhiteStatusBar()
                }
            }
        )
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

    private fun handleError(scpError: Error) {
        binding.loadContainer.loaderFlipper.displayedChild = 1
        setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        val error = scpError.error

        when {
            error is UnknownHostException || error is SocketTimeoutException -> {
                binding.loadContainer.mdpError.setType(GlobalError.NO_CONNECTION)
                binding.loadContainer.mdpError.setActionClickListener {
                    resetPage()
                }
            }

            scpError.errorCode == NON_WHITELISTED_USER_ERROR_CODE -> {
                binding.loadContainer.mdpError.apply {
                    setType(GlobalError.PAGE_NOT_FOUND)
                    errorTitle.text = context.getText(com.tokopedia.scp_rewards.R.string.error_non_whitelisted_user_title)
                    errorDescription.text = context.getText(com.tokopedia.scp_rewards.R.string.error_non_whitelisted_user_description)
                    errorAction.text = context.getText(com.tokopedia.scp_rewards.R.string.error_non_whitelisted_user_action)
                    setActionClickListener {
                        MedalDetailAnalyticsImpl.sendNonWhitelistedUserCtaClick()
                        RouteManager.route(context, ApplinkConst.HOME)
                        activity?.finish()
                    }
                }
                MedalDetailAnalyticsImpl.sendImpressionNonWhitelistedError()
            }

            else -> {
                binding.loadContainer.mdpError.apply {
                    setType(GlobalError.SERVER_ERROR)
                    setActionClickListener {
                        resetPage()
                    }
                }
            }
        }
    }

    private fun resetPage() {
        binding.mainFlipper.displayedChild = 0
        binding.loadContainer.loaderFlipper.displayedChild = 0
        getMedaliDetail()
        setToolbarBackButtonTint(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    }

    private fun setToolbarBackButtonTint(color: Int) {
        context?.let {
            binding.toolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    private fun loadCouponWidget(medaliDetailPage: MedaliDetailPage?) {
        val benefits = medaliDetailPage?.benefits
        if (benefits.isNullOrEmpty()) {
            binding.couponView.gone()
        } else {
            val couponList = mutableListOf<MedalRewardsModel>()
            benefits.forEach { benefit ->
                couponList.add(
                    MedalRewardsModel(
                        imageUrl = benefit.imageUrl ?: "",
                        status = benefit.status ?: "",
                        statusDescription = benefit.statusDescription ?: "",
                        isActive = benefit.isActive
                    )
                )
            }

            binding.couponView.renderCoupons("", couponList) {
                MedalDetailAnalyticsImpl.sendImpressionCouponError(
                    badgeId = medaliSlug,
                    promoCode = medalDetailViewModel.couponCode
                )
            }

            MedalDetailAnalyticsImpl.sendImpressionBonusCoupon(
                badgeId = medaliSlug,
                promoCode = medalDetailViewModel.couponCode,
                couponStatus = medalDetailViewModel.couponStatus,
                couponNotes = medalDetailViewModel.couponNotes
            )
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        val state = medalDetailViewModel.badgeLiveData.value
        if (state is Error && state.errorCode == NON_WHITELISTED_USER_ERROR_CODE) {
            MedalDetailAnalyticsImpl.sendNonWhitelistedBackClick()
        } else {
            MedalDetailAnalyticsImpl.sendClickBackButton(medaliSlug)
        }
        return super.onFragmentBackPressed()
    }

    override fun getScreenName() = ""
}
