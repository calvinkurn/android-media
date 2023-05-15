package com.tokopedia.scp_rewards.detail.presentation.ui

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.FontAssetDelegate
import com.airbnb.lottie.LottieCompositionFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.databinding.LayoutActivityMedalDetailBinding
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import javax.inject.Inject

const val IMG_DETAIL_BASE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg_base.png"
const val IMG_DETAIL_BG = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg.png"
const val LOTTIE_BADGE = "https://assets.tokopedia.net/asts/HThbdi/scp/2023/05/08/medali_inner_icon.json"
class MedalDetailFragment: BaseDaggerFragment() {

    private lateinit var binding: LayoutActivityMedalDetailBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private val medalDetailViewModel by lazy {
        ViewModelProvider(this,viewModelFactory!!).get(MedalDetailViewModel::class.java)
    }


    override fun initInjector() {
        getComponent(MedalDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutActivityMedalDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObservers()
        medalDetailViewModel.getMedalDetail()

        loadBaseImage(IMG_DETAIL_BASE)
        loadBackgroundImage(IMG_DETAIL_BG)

        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(context, LOTTIE_BADGE)
        lottieCompositionLottieTask.addListener { result ->
            binding.lottieBadge.apply {
                show()
                imageAssetsFolder = "images/"
                setFontAssetDelegate(object : FontAssetDelegate() {
                    override fun fetchFont(fontFamily: String): Typeface {
                        return Typeface.createFromAsset(
                            context.assets,
                            "fonts/Open Sauce One.ttf"
                        )
                    }
                })
                setComposition(result)
                loop(true)
                playAnimation()
            }
        }

        binding.lottieBadge.addAnimatorUpdateListener {
            it.currentPlayTime
        }
    }

    private fun setupViewModelObservers() {

        medalDetailViewModel.badgeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Success<*> -> {
                    //TODO
                }
                is Error -> {
                    //TODO
                }
                is Loading -> {
                    //TODO
                }
            }
        }
    }

    override fun getScreenName() = ""

    private fun loadBackgroundImage(url: String) {
        Glide.with(binding.frameBackground.context)
            .asDrawable()
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.frameBackground.background = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun loadBaseImage(url: String) {
        Glide.with(this)
            .asDrawable()
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.ivBadgeBase.setImageDrawable(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {

                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
