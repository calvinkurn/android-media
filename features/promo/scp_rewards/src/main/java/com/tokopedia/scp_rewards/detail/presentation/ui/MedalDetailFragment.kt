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
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.launchLink
import com.tokopedia.scp_rewards.common.utils.launchWeblink
import com.tokopedia.scp_rewards.databinding.MedalDetailFragmentLayoutBinding
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.domain.model.Benefit
import com.tokopedia.scp_rewards.detail.domain.model.BenefitButton
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliDetailPage
import com.tokopedia.scp_rewards.detail.domain.model.Mission
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards.widget.medalDetail.MedalDetail
import com.tokopedia.scp_rewards.widget.medalHeader.MedalHeader
import com.tokopedia.scp_rewards_widgets.medal_footer.FooterData
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel
import com.tokopedia.scp_rewards_widgets.task_progress.Task
import com.tokopedia.scp_rewards_widgets.task_progress.TaskProgress
import com.tokopedia.unifycomponents.Toaster
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

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
        medalDetailViewModel.getMedalDetail(
            medaliSlug = medaliSlug,
            sourceName = sourceName
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
                    }

                    is MedalDetailViewModel.AutoApplyState.SuccessCouponFailed -> {
                        showToastAndNavigateToLink(
                            safeResult.footerData.id,
                            safeResult.data?.couponAutoApply?.infoMessage?.title,
                            safeResult.footerData.appLink,
                            safeResult.footerData.url
                        )
                    }
                }
            }
        }
    }

    private fun showToastAndNavigateToLink(id: Int?, message: String?, appLink: String?, url: String?) {
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
            val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        val resources = context?.resources
        val resourceId: Int = resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
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
                        loadTaskProgress(data.detail?.medaliDetailPage?.mission)
                        loadCouponWidget(data.detail?.medaliDetailPage?.benefit)
                        loadFooter(data.detail?.medaliDetailPage?.benefitButton)
                    }

                    is Error -> {
                        setWhiteStatusBar()
                        handleError(safeResult.error)
                    }

                    is Loading -> {
                        binding.loadContainer.loaderFlipper.visible()
                    }
                }
            }
        }
    }

    private fun loadFooter(listOfButtons: List<BenefitButton>?) {
        listOfButtons?.let { buttons ->
            binding.viewMedalFooter.bindData(
                buttons.map {
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
            }
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
            MedalHeader(
                isGrayScale = medaliDetailPage?.isMedaliGrayScale ?: false,
                medalIconUrl = medaliDetailPage?.iconImageURL,
                lottieUrl = medaliDetailPage?.shimmerShutterLottieURL,
                lottieSparklesUrl = medaliDetailPage?.outerBlinkingLottieURL,
                podiumUrl = medaliDetailPage?.baseImageURL,
                background = medaliDetailPage?.backgroundImageURL,
                backgroundColor = medaliDetailPage?.backgroundImageColor,
                medalUrl = medaliDetailPage?.innerIconImageURL,
                frameUrl = medaliDetailPage?.frameImageURL,
                shimmerUrl = medaliDetailPage?.shimmerImageURL,
                frameMaskUrl = medaliDetailPage?.maskingImageURL,
                maskingShapeUrl = medaliDetailPage?.frameMaskingImageURL,
                shutterUrl = medaliDetailPage?.shutterImageURL,
                shutterText = medaliDetailPage?.shutterText.orEmpty(),
                coachMarkInformation = medaliDetailPage?.coachMark?.text
            )
        )
        medaliDetailPage?.tncButton?.apply {
            binding.tvTermsConditions.text = text
            binding.tvTermsConditions.setOnClickListener {
                launchWeblink(requireContext(), url.orEmpty())
            }
        }
    }

    private fun loadTaskProgress(mission: Mission?) {
        mission?.let { safeMission ->
            if (safeMission.task?.isEmpty() == true) {
                binding.viewTasksProgress.gone()
            } else {
                val taskProgress = TaskProgress(
                    title = safeMission.title,
                    progress = safeMission.progress,
                    tasks = safeMission.task?.map {
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
            }
        } ?: run { binding.viewTasksProgress.gone() }
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
            window?.statusBarColor = Color.WHITE
            binding.toolbar.setBackgroundColor(Color.WHITE)
            setToolbarBackButtonTint(R.color.Unify_NN900)

            windowInsetsController?.isAppearanceLightStatusBars = true
            binding.tvTermsConditions.setTextColor(
                ContextCompat.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
        }
    }

    private fun setTransparentStatusBar() {
        (activity as AppCompatActivity?)?.apply {
            activity?.window?.statusBarColor = Color.TRANSPARENT
            binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
            setToolbarBackButtonTint(R.color.Unify_NN0)

            windowInsetsController?.isAppearanceLightStatusBars = false
            binding.tvTermsConditions.setTextColor(
                ContextCompat.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
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

    private fun handleError(error: Throwable) {
        binding.loadContainer.loaderFlipper.displayedChild = 1
        setToolbarBackButtonTint(R.color.Unify_NN900)
        if (error is UnknownHostException || error is SocketTimeoutException) {
            binding.loadContainer.mdpError.setType(GlobalError.NO_CONNECTION)
        } else {
            binding.loadContainer.mdpError.apply {
                setType(GlobalError.SERVER_ERROR)
                errorSecondaryAction.text = context.getText(R.string.goto_medali_cabinet_text)
                setActionClickListener {
                    resetPage()
                }
            }
        }
    }

    private fun resetPage() {
        binding.mainFlipper.displayedChild = 0
        binding.loadContainer.loaderFlipper.displayedChild = 0
        medalDetailViewModel.getMedalDetail(
            medaliSlug = medaliSlug,
            sourceName = sourceName
        )
        setToolbarBackButtonTint(R.color.Unify_NN0)
    }

    private fun setToolbarBackButtonTint(color: Int) {
        context?.let {
            binding.toolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    private fun loadCouponWidget(benefits: List<Benefit>?) {
        benefits?.let {
            val couponList = mutableListOf<MedalRewardsModel>()
            it.forEach { it1 ->
                couponList.add(
                    MedalRewardsModel(
                        imageUrl = it1.imageUrl ?: "",
                        status = it1.status ?: "",
                        statusDescription = it1.statusDescription ?: "",
                        isActive = it1.isActive
                    )
                )
            }
            binding.couponView.renderCoupons(couponList)
        }
    }

    override fun getScreenName() = ""
}
