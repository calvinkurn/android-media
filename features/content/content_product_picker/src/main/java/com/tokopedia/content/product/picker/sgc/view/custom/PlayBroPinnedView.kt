package com.tokopedia.content.product.picker.sgc.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.product.picker.databinding.ViewPlayBroPinProductBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.content.product.picker.R

/**
 * @author by astidhiyaa on 26/07/22
 */
class PlayBroPinnedView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewPlayBroPinProductBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setupPinned(isPinned: Boolean, isLoading: Boolean) {
        binding.ivLoaderPin.showWithCondition(isLoading)
        binding.ivPin.showWithCondition(!isLoading)

        if (isPinned) {
            binding.ivPin.setImage(
                newIconId = IconUnify.PUSH_PIN,
                newDarkEnable = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_RN400
                ),
                newLightEnable = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_RN400
                )
            )
            binding.tvPin.text =
                context.resources.getString(R.string.play_bro_unpin)
            binding.tvPin.setTextColor(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_RN400
                )
            )
        } else {
            binding.ivPin.setImage(
                newIconId = IconUnify.PUSH_PIN,
                newDarkEnable = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Static_White
                ),
                newLightEnable = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Static_White
                )
            )
            binding.tvPin.text =
                context.resources.getString(R.string.play_bro_pin)
            binding.tvPin.setTextColor(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Static_White
                )
            )
        }
    }
}
