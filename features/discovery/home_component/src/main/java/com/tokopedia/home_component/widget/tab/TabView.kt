@file:SuppressLint("InflateParams")

package com.tokopedia.home_component.widget.tab

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.home_component.databinding.TabMegaRecommendationBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class TabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding: TabMegaRecommendationBinding

    init {
        binding = TabMegaRecommendationBinding.inflate(
            LayoutInflater.from(context)
        )

        addView(binding.root)
    }

    fun createTabView(item: MegaTabItem) {
        binding.txtTitle.text = item.title

        if (item.imageUrl.isNotEmpty()) {
            onImageLoad(item.imageUrl)
        } else {
            binding.txtTitle.show()
        }
    }

    private fun onImageLoad(url: String) {
        binding.loaderShimmering.show()
        binding.imgIcon.show()

        binding.imgIcon.loadImageWithoutPlaceholder(url) {
            setSignatureKey(ObjectKey(System.currentTimeMillis())) // temporary for debug
            listener(
                onSuccess = { _, _ -> onImageFetchSucceed() },
                onError = { _ -> onImageFetchFailed() }
            )
        }
    }

    private fun onImageFetchSucceed() {
        binding.imgIcon.show()

        binding.loaderShimmering.hide()
        binding.txtTitle.hide()
    }

    private fun onImageFetchFailed() {
        // forcibly shows the title
        binding.txtTitle.show()

        binding.loaderShimmering.hide()
        binding.imgIcon.hide()
    }
}
