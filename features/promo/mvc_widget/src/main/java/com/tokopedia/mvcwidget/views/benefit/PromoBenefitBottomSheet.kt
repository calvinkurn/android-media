package com.tokopedia.mvcwidget.views.benefit

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvcwidget.databinding.PromoBenefitBottomsheetBinding
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.mvcwidget.trackers.PromoBenefitAnalytics
import com.tokopedia.mvcwidget.utils.getUnifyColorFromHex
import com.tokopedia.mvcwidget.utils.setAttribute
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.unifycomponents.R as unifycomponentsR

class PromoBenefitBottomSheet : BottomSheetDialogFragment() {

    private var binding by autoClearedNullable<PromoBenefitBottomsheetBinding>()
    private val usablePromoAdapter = UsablePromoAdapter()
    private val infoAdapter = AdditionalInfoAdapter()

    private var infoStateIsShown = true
    private var productId: String = ""
    private var shopId: String = ""

    @Inject
    lateinit var vmFactory: ViewModelFactory

    @Inject
    lateinit var analytics: PromoBenefitAnalytics

    private val viewModel: PromoBenefitViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            unifycomponentsR.style.UnifyBottomSheetOverlapStyle
        )
        val baseAppComponent = (activity?.application as BaseMainApplication).baseAppComponent
        DaggerMvcComponent.builder()
            .baseAppComponent(baseAppComponent)
            .build()
            .inject(this)
        val meta = arguments?.getString(ARG_BOTTOM_SHEET) ?: "-1"
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
        shopId = arguments?.getString(ARG_SHOP_ID) ?: ""
        viewModel.setId(meta)
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
        analytics.sendImpressionPromoDetailBottomSheetEvent(productId, shopId, emptyList())
        binding?.run {
            val frameDialogView = container.parent as FrameLayout
            frameDialogView.setBackgroundColor(Color.TRANSPARENT)
            frameDialogView.bringToFront()

            val drawable = ContextCompat.getDrawable(
                requireContext(),
                unifycomponentsR.drawable.bottomsheet_background
            )

            lifecycleScope.launchWhenCreated {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        viewModel.state.collect { model ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                drawable?.colorFilter = BlendModeColorFilter(
                                    requireContext().getUnifyColorFromHex(model.bgColor),
                                    BlendMode.SRC_ATOP
                                )
                            } else {
                                drawable?.setColorFilter(
                                    requireContext().getUnifyColorFromHex(model.bgColor),
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                            topSection.background = drawable
                            benefitBackground.loadImage(model.bgImgUrl)
                            model.estimatePrice.run {
                                model.estimatePrice.run {
                                    layoutBenefit.tvEstimate.setAttribute(
                                        text,
                                        requireContext().getUnifyColorFromHex(textColor),
                                        textFormat
                                    )
                                    layoutBenefit.tvEstimateTitle.setAttribute(
                                        title,
                                        requireContext().getUnifyColorFromHex(titleColor),
                                        titleFormat
                                    )
                                }
                            }
                            model.basePrice.run {
                                layoutBenefit.tvBasePrice.setAttribute(
                                    text,
                                    requireContext().getUnifyColorFromHex(textColor),
                                    textFormat
                                )
                                layoutBenefit.tvBasePriceTitle.setAttribute(
                                    title,
                                    requireContext().getUnifyColorFromHex(titleColor),
                                    titleFormat
                                )
                            }

                            usablePromoAdapter.submitList(model.usablePromo)
                            infoAdapter.submitList(model.tnc.tncTexts, model.tnc.color)
                        }
                    }
                    launch {
                        viewModel.error.collect {
                            globalError.isVisible = it
                            layoutBenefit.root.isVisible = !it
                            containerTnc.isVisible = !it
                        }
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
            containerTnc.setOnClickListener {
            containerTnc.setOnClickListener {
            containerTnc.setOnClickListener {
                val start = if (infoStateIsShown) 0f else 180f
                val end = if (infoStateIsShown) 180f else 0f
                val animator = ObjectAnimator.ofFloat(toggleInfo, "rotation", start, end)
                animator.duration = 400
                animator.start()

                infoStateIsShown = !infoStateIsShown
                rvInfo.isVisible = infoStateIsShown
            }
            globalError.errorAction.gone()
        }
    }

    companion object {
        const val ARG_BOTTOM_SHEET = "ARG_BOTTOM_SHEET"
        const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        const val ARG_SHOP_ID = "ARG_SHOP_ID"
        fun newInstance(metaDataJson: String, productId: String, shopId: String) =
            PromoBenefitBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_BOTTOM_SHEET, metaDataJson)
                    putString(ARG_PRODUCT_ID, productId)
                    putString(ARG_SHOP_ID, shopId)
                }
            }
    }
}
