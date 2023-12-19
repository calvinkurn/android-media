package com.tokopedia.promousage.view.benefit

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
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
//        val gradientDrawable = GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            cornerRadii =
//                floatArrayOf(20f, 20f, 20f, 20f, 0f, 0f, 0f, 0f) // Top corners are rounded
//            colors = intArrayOf(
//                0xFFE53935.toInt(),
//                0xFFFFCDD2.toInt(),
//            ) // Red to Orange to White
//            orientation = GradientDrawable.Orientation.BL_TR // 45-degree angle
//        }
        binding?.run {
            val frameDialogView = container.parent as FrameLayout
            frameDialogView.setBackgroundColor(Color.TRANSPARENT)
            frameDialogView.bringToFront()

            val drawable = ContextCompat.getDrawable(
                requireContext(),
                com.tokopedia.unifycomponents.R.drawable.bottomsheet_background
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor("#FFF5F6"), BlendMode.SRC_ATOP
                )
            } else {
                drawable?.setColorFilter(
                    Color.parseColor("#FFF5F6"),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            topSection.background = drawable
        }
    }
}
