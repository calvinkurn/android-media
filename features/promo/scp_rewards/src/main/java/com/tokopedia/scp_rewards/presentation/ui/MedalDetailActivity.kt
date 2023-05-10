package com.tokopedia.scp_rewards.presentation.ui

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.FontAssetDelegate
import com.airbnb.lottie.LottieCompositionFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.scp_rewards.databinding.LayoutActivityMedalDetailBinding

const val IMG_DETAIL_BASE = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg_base.png"
const val IMG_DETAIL_BG = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/04/medalidetail_bg.png"
const val LOTTIE_BADGE = "https://assets.tokopedia.net/asts/HThbdi/scp/2023/05/08/medali_inner_icon.json"
class MedalDetailActivity: AppCompatActivity() {

    private lateinit var binding: LayoutActivityMedalDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutActivityMedalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadBaseImage(IMG_DETAIL_BASE)
        loadBackgroundImage(IMG_DETAIL_BG)

        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(this, LOTTIE_BADGE)
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
