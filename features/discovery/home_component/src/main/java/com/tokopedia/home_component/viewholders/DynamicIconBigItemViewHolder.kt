package com.tokopedia.home_component.viewholders

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconItemBigBinding
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class DynamicIconBigItemViewHolder(
    itemView: View,
    private val listener: DynamicIconComponentListener
) : DynamicIconItemViewHolder(itemView, listener) {
    override val scaleMinImage: Float = SCALE_MIN_IMAGE
    override val maxAlphaRipple: Float = MAX_ALPHA_RIPPLE

    private var binding: HomeComponentDynamicIconItemBigBinding? by viewBinding()
    override val dynamicIconTypography: Typography? = binding?.dynamicIconTypography
    override val dynamicIconImageView: ImageView? = binding?.dynamicIconImageView
    override val dynamicIconRippleContainer: View? = binding?.containerRippleDynamicIcons
    override val dynamicIconContainer: ViewGroup? = binding?.dynamicIconContainer

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon_item_big
        private const val SCALE_MIN_IMAGE = 0.9375f
        private const val MAX_ALPHA_RIPPLE = 0.6f
    }

    override fun loadImage(item: DynamicIconComponent.DynamicIcon) {
        (dynamicIconImageView as? ImageUnify)?.setImageUrl(item.imageUrl)
    }
}
