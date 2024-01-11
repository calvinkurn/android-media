package com.tokopedia.mvcwidget.views.benefit

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.mvcwidget.databinding.PromoBenefitBottomsheetBinding
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import com.tokopedia.unifycomponents.R as unifycomponentsR

class PromoBenefitBottomSheet : BottomSheetDialogFragment() {

    private var binding by autoClearedNullable<PromoBenefitBottomsheetBinding>()
    private val usablePromoAdapter = UsablePromoAdapter()
    private val infoAdapter = AdditionalInfoAdapter()

    private var infoStateIsShown = true

    @Inject
    lateinit var vmFactory: ViewModelFactory

    private val viewModel: PromoBenefitViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(
//            DialogFragment.STYLE_NORMAL,
//            unifycomponentsR.style.UnifyBottomSheetOverlapStyle
//        )
        val baseAppComponent = (activity?.application as BaseMainApplication).baseAppComponent
        DaggerMvcComponent.builder()
            .baseAppComponent(baseAppComponent)
            .build()
            .inject(this)
        val id = arguments?.getString(ARG_BOTTOM_SHEET) ?: "-1"
        viewModel.setId(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromoBenefitBottomsheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("DeprecatedMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            val frameDialogView = container.parent as FrameLayout
            frameDialogView.setBackgroundColor(Color.TRANSPARENT)
            frameDialogView.bringToFront()

            val drawable = ContextCompat.getDrawable(
                requireContext(),
                unifycomponentsR.drawable.bottomsheet_background
            )

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.state.collect { model ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            drawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor(model.headerColor),
                                BlendMode.SRC_ATOP
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

                        usablePromoAdapter.submitList(model.usablePromo)
                        infoAdapter.submitList(model.promoInfo)
                    }
                }
            }

            rvInfo.run {
                adapter = infoAdapter
            }
            icClose.setOnClickListener {
                dismiss()
            }
            layoutBenefit.rvUsablePromo.run {
                adapter = usablePromoAdapter
            }
            toggleInfo.setOnClickListener {
                val start = if (infoStateIsShown) 0f else 180f
                val end = if (infoStateIsShown) 180f else 0f
                val animator = ObjectAnimator.ofFloat(it, "rotation", start, end)
                animator.duration = 400
                animator.start()

                infoStateIsShown = !infoStateIsShown
                rvInfo.isVisible = infoStateIsShown
            }
        }
    }

    companion object {
        const val ARG_BOTTOM_SHEET = "ARG_BOTTOM_SHEET"
        fun newInstance(variantId: String) = PromoBenefitBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_BOTTOM_SHEET, variantId)
            }
        }
    }
}

@Parcelize
data class UiModel(
    val headerColor: String = "#FFF5F6",
    val estimatePrice: String = "Rp0",
    val basePrice: String = "Rp0",
    val usablePromo: List<UsablePromoModel> = listOf(),
    val promoInfo: List<String> = listOf()
) : Parcelable
