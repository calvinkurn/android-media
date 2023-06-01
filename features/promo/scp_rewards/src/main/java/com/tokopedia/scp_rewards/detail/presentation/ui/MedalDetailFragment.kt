package com.tokopedia.scp_rewards.detail.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.tokopedia.scp_rewards.databinding.MedalDetailFragmentLayoutBinding
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.domain.model.BenefitButton
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliDetailPage
import com.tokopedia.scp_rewards.detail.domain.model.Mission
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards.widget.medalDetail.MedalDetail
import com.tokopedia.scp_rewards.widget.medalHeader.MedalHeader
import com.tokopedia.scp_rewards_widgets.medal_footer.FooterData
import com.tokopedia.scp_rewards_widgets.task_progress.Task
import com.tokopedia.scp_rewards_widgets.task_progress.TaskProgress
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

const val IMG_DETAIL_BASE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg_base.png"
const val IMG_DETAIL_BG = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg.png"
const val CONTENT = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_inner_icon.png"
const val FRAME = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_frame.png"
const val SHIMMER = "https://user-images.githubusercontent.com/121924518/240866873-6121041d-b841-43fe-8c54-76815f095f39.png"
const val MASK = "https://user-images.githubusercontent.com/121924518/240869225-f2a42fca-a6ad-4a27-ba1b-a04051864e34.png"
const val MASKING_SHAPE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_mask.png"
const val SHUTTER = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/08/medali_shutter.png"
const val LOTTIE_BADGE = "https://gist.githubusercontent.com/rooparshgojek/8502ff5cb6f84b918141a213498f007a/raw/9e5bf0fae736334521b47a07a5d0d76346c9fc57/medali-detail.json"
const val LOTTIE_SPARKS = "https://assets.tokopedia.net/asts/HThbdi/scp/2023/05/08/medali_outer_blinking.json"

class MedalDetailFragment : BaseDaggerFragment() {

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

    private fun initToolbar() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                elevation = 0f
                // setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.Unify_Background)))

                // it.setTransparentSystemBar()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupToolbar(binding.toolbar)
        setupToolbar(binding.loadContainer.loadToolbar)
        setupViewModelObservers()
        medalDetailViewModel.getMedalDetail(medaliSlug)

        loadData()
    }

    private fun setupToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                elevation = 0f
                // setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.Unify_Background)))

                // it.setTransparentSystemBar()
            }
        }
    }

    private fun loadData() {
        binding.viewMedalHeader.bindData(
            MedalHeader(
                lottieUrl = LOTTIE_BADGE,
                lottieSparklesUrl = LOTTIE_SPARKS,
                podiumUrl = IMG_DETAIL_BASE,
                background = IMG_DETAIL_BG,
                medalUrl = CONTENT,
                frameUrl = FRAME,
                shimmerUrl = SHIMMER,
                maskUrl = MASK,
                maskingShapeUrl = MASKING_SHAPE,
                shutterUrl = SHUTTER
            )
        )

        binding.layoutDetailContent.viewMedalDetail.bindData(
            MedalDetail()
        )

        binding.ivBadgeBase.setImageUrl(IMG_DETAIL_BASE)
    }

    private fun setupViewModelObservers() {
        medalDetailViewModel.badgeLiveData.observe(viewLifecycleOwner) {
            it?.let { safeResult ->
                when (safeResult) {
                    is Success<*> -> {
                        setupToolbar(binding.toolbar)
                        binding.mainFlipper.displayedChild = 1
                        val data = safeResult.data as MedalDetailResponseModel
                        loadHeader(data.detail?.medaliDetailPage)
                        loadMedalDetails(data.detail?.medaliDetailPage)
                        loadTaskProgress(data.detail?.medaliDetailPage?.mission)
                        loadFooter(data.detail?.medaliDetailPage?.benefitButton)
                    }

                    is Error -> {
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
            binding.layoutDetailContent.viewMedalFooter.bindData(
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
        binding.layoutDetailContent.viewMedalDetail.bindData(
            MedalDetail(
                sponsorInformation = medaliDetailPage?.sourceText,
                medalTitle = medaliDetailPage?.name,
                medalDescription = medaliDetailPage?.description
            )
        )
    }

    private fun loadHeader(medaliDetailPage: MedaliDetailPage?) {
        binding.viewMedalHeader.bindData(
            MedalHeader(
                lottieUrl = medaliDetailPage?.shimmerShutterLottieURL ?: LOTTIE_BADGE,
                lottieSparklesUrl = medaliDetailPage?.outerBlinkingLottieURL ?: LOTTIE_SPARKS,
                background = medaliDetailPage?.backgroundImageURL ?: IMG_DETAIL_BG,
                backgroundColor = medaliDetailPage?.backgroundImageColor,
                medalUrl = medaliDetailPage?.innerIconImageURL ?: CONTENT,
                frameUrl = medaliDetailPage?.frameImageURL ?: FRAME,
                shimmerUrl = medaliDetailPage?.shimmerImageURL ?: SHIMMER,
                maskUrl = medaliDetailPage?.maskingImageURL ?: MASK,
                maskingShapeUrl = medaliDetailPage?.frameMaskingImageURL ?: MASKING_SHAPE,
                shutterUrl = medaliDetailPage?.shutterImageURL ?: SHUTTER,
                coachMarkInformation = medaliDetailPage?.coachMark?.text
            )
        )

        binding.ivBadgeBase.setImageUrl(medaliDetailPage?.baseImageURL ?: IMG_DETAIL_BASE)
    }

    private fun loadTaskProgress(mission: Mission?) {
        mission?.let { safeMission ->
            if (safeMission.task?.isEmpty() == true) {
                binding.layoutDetailContent.viewTasksProgress.gone()
                return
            }
            val taskProgress = TaskProgress(
                title = safeMission.title,
                progress = safeMission.progress,
                tasks = safeMission.task?.map { Task(title = it.title, isCompleted = it.isCompleted) }
            )
            binding.layoutDetailContent.viewTasksProgress.apply {
                bindData(taskProgress)
                visible()
            }
        } ?: run { binding.layoutDetailContent.viewTasksProgress.gone() }
    }

    private fun handleError(error: Throwable) {
        binding.loadContainer.loaderFlipper.displayedChild = 1
        setLoadToolBarBackButtonTint(R.color.Unify_NN900)
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
        setupToolbar(binding.loadContainer.loadToolbar)
        setLoadToolBarBackButtonTint(R.color.Unify_NN0)
    }

    private fun setLoadToolBarBackButtonTint(color: Int) {
        context?.let {
            binding.loadContainer.loadToolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    override fun getScreenName() = ""
}
