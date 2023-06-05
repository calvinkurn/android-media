package com.tokopedia.scp_rewards.detail.presentation.ui

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
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
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

const val IMG_DETAIL_BASE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg_base.png"
const val IMG_DETAIL_BG = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg.png"
const val FRAME_MASK = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/26/medali_frame_mask.png"
const val SHIMMER = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/26/shimmer_alt.png"
const val FRAME = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_frame.png"
const val MASKING_SHAPE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_mask.png"
const val MEDAL_ICON = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/01/medali_brand.png"
const val SHUTTER = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_shutter.png"
const val CONTENT = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_inner_icon.png"
const val LOTTIE_BADGE = "https://gist.githubusercontent.com/abhishekjain-gojek/99be6bad982dea1518f03ff1dffbf40d/raw/6cedac0248cbf1d5fc55825385034d1cdb3a5f6c/medali_detail.json"
const val LOTTIE_SPARKS = "https://assets.tokopedia.net/asts/HThbdi/scp/2023/05/08/medali_outer_blinking.json"

class MedalDetailFragment : BaseDaggerFragment() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null
    private lateinit var binding: MedalDetailFragmentLayoutBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private var medaliSlug = ""

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
        medalDetailViewModel.getMedalDetail(medaliSlug)
    }

    private fun setupViewModelObservers() {
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
                        // TODO
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
                        style = it.unifiedStyle
                    )
                }
            ) { appLink, webLink ->
                if (TextUtils.isEmpty(appLink)) {
                    RouteManager.route(requireContext(), String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, Uri.encode(webLink)))
                } else {
                    RouteManager.route(requireContext(), appLink)
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
                medalIconUrl = medaliDetailPage?.iconImageURL ?: MEDAL_ICON,
                lottieUrl = medaliDetailPage?.shimmerShutterLottieURL ?: LOTTIE_BADGE,
                lottieSparklesUrl = medaliDetailPage?.outerBlinkingLottieURL ?: LOTTIE_SPARKS,
                podiumUrl = medaliDetailPage?.baseImageURL ?: IMG_DETAIL_BASE,
                background = medaliDetailPage?.backgroundImageURL ?: IMG_DETAIL_BG,
                backgroundColor = medaliDetailPage?.backgroundImageColor,
                medalUrl = medaliDetailPage?.innerIconImageURL ?: CONTENT,
                frameUrl = medaliDetailPage?.frameImageURL ?: FRAME,
                shimmerUrl = medaliDetailPage?.shimmerImageURL ?: SHIMMER,
                frameMaskUrl = medaliDetailPage?.maskingImageURL ?: FRAME_MASK,
                maskingShapeUrl = medaliDetailPage?.frameMaskingImageURL ?: MASKING_SHAPE,
                shutterUrl = medaliDetailPage?.shutterImageURL ?: SHUTTER,
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
                return
            }
            val taskProgress = TaskProgress(
                title = safeMission.title,
                progress = safeMission.progress,
                tasks = safeMission.task?.map {
                    Task(
                        title = it.title,
                        isCompleted = it.isCompleted
                    )
                }
            )
            binding.viewTasksProgress.apply {
                bindData(taskProgress)
                visible()
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
                    this, com.tokopedia.unifyprinciples.R.color.Unify_N0
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
            binding.mainFlipper.updateLayoutParams<ViewGroup.MarginLayoutParams> {
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
        medalDetailViewModel.getMedalDetail()
        setToolbarBackButtonTint(R.color.Unify_NN0)
    }

    private fun setToolbarBackButtonTint(color: Int) {
        context?.let {
            binding.toolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    private fun loadCouponWidget(benefits:List<Benefit>?) {
        benefits?.let {
            val couponList = mutableListOf<MedalRewardsModel>()
            it.forEach { it1 ->
                couponList.add(MedalRewardsModel(
                    imageUrl = it1.imageUrl ?: "",
                    status = it1.status ?: "",
                    statusDescription = it1.statusDescription ?: "",
                    isActive = it1.isActive
                ))
            }
            binding.couponView.renderCoupons(couponList)
        }
    }

    override fun getScreenName() = ""
}
