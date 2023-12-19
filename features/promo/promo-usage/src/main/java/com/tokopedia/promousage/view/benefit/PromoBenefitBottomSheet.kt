package com.tokopedia.promousage.view.benefit

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.promousage.databinding.PromoBenefitBottomsheetBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PromoBenefitBottomSheet : BottomSheetDialogFragment() {


    private var binding by autoClearedNullable<PromoBenefitBottomsheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(
//            DialogFragment.STYLE_NORMAL,
//            unifycomponentsR.style.UnifyBottomSheetOverlapStyle
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromoBenefitBottomsheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii =
                floatArrayOf(20f, 20f, 20f, 20f, 0f, 0f, 0f, 0f) // Top corners are rounded
            colors = intArrayOf(
                0xFFE53935.toInt(),
                0xFFFFCDD2.toInt(),
            ) // Red to Orange to White
            orientation = GradientDrawable.Orientation.BL_TR // 45-degree angle
        }
        binding?.run {
            val frameDialogView = container.parent as FrameLayout
            frameDialogView.setBackgroundColor(Color.TRANSPARENT)
            frameDialogView.bringToFront()

            topSection.background = gradientDrawable
        }
    }
}
