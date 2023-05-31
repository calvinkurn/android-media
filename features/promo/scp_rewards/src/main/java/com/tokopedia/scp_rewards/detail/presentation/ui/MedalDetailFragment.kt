package com.tokopedia.scp_rewards.detail.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.databinding.MedalDetailFragmentLayoutBinding
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards.widget.medalDetail.MedalDetail
import com.tokopedia.scp_rewards.widget.medalHeader.MedalHeader
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

    private val medalDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalDetailViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(MedalDetailComponent::class.java).inject(this)
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

        initToolbar()
        setupViewModelObservers()
        medalDetailViewModel.getMedalDetail()

        loadData()
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
        renderCouponWidget()
    }

    private fun setupViewModelObservers() {
        medalDetailViewModel.badgeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    // TODO
                }

                is Error -> {
                    // TODO
                }

                is Loading -> {
                    // TODO
                }
            }
        }
    }

    private fun renderCouponWidget() {
    }

    override fun getScreenName() = ""
}
