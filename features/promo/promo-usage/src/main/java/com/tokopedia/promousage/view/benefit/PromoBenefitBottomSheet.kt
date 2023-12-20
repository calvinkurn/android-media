package com.tokopedia.promousage.view.benefit

import android.animation.ObjectAnimator
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.LayoutItemInfoBinding
import com.tokopedia.promousage.databinding.PromoBenefitBottomsheetBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.parcelize.Parcelize
import com.tokopedia.unifycomponents.R as unifycomponentsR

class PromoBenefitBottomSheet : BottomSheetDialogFragment() {


    private var binding by autoClearedNullable<PromoBenefitBottomsheetBinding>()
    private lateinit var model: Param

    private var infoStateIsShown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(
//            DialogFragment.STYLE_NORMAL,
//            unifycomponentsR.style.UnifyBottomSheetOverlapStyle
//        )
        model = arguments?.getParcelable(ARG_BOTTOM_SHEET) ?: Param()
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
                unifycomponentsR.drawable.bottomsheet_background
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(model.headerColor), BlendMode.SRC_ATOP
                )
            } else {
                drawable?.setColorFilter(
                    Color.parseColor(model.headerColor),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            topSection.background = drawable
            layoutBenefit.tvEstimate.text = model.estimatePrice
            layoutBenefit.tvBasePrice.text = model.basePrice
            model.promoInfo.forEach {
                val v = LayoutInflater.from(requireContext()).inflate(
                    R.layout.layout_item_info, llInfo, false
                )
                val tv = LayoutItemInfoBinding.bind(v)
                tv.text.text = buildString {
                    append("• ")
                    append(it)
                }
                llInfo.addView(tv.root)
            }
            icClose.setOnClickListener {
                dismiss()
            }
            toggleInfo.setOnClickListener {
                val start = if (infoStateIsShown) 0f else 180f
                val end = if (infoStateIsShown) 180f else 0f
                val animator = ObjectAnimator.ofFloat(it, "rotation", start, end)
                animator.duration = 400
                animator.start()

                infoStateIsShown = !infoStateIsShown
                llInfo.isVisible = infoStateIsShown
            }
        }
    }

    companion object {
        const val ARG_BOTTOM_SHEET = "ARG_BOTTOM_SHEET"
        fun newInstance(param: Param) = PromoBenefitBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_BOTTOM_SHEET, param)
            }
        }
    }

    @Parcelize
    data class Param(
        val estimatePrice: String = "Rp0",
        val basePrice: String = "Rp0",
        val headerColor: String = "#FFF5F6",
        val promoInfo: List<String> = listOf()
    ) : Parcelable

}
