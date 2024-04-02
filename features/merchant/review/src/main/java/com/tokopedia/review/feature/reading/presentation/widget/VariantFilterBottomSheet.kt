package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class VariantFilterBottomSheet : BottomSheetUnify(), SelectVariantUiEvent {

    companion object {

        fun instance(
            listener: ReadReviewFilterBottomSheetListener,
            variants: List<SelectVariantUiModel.Variant>,
            pairedOptions: List<List<String>>
        ) = VariantFilterBottomSheet().apply {
            val copyVariants = variants.map { variant ->
                variant.copy(
                    options = variant.options.map {
                        it.copy().apply {
                            isSelected = it.isSelected
                        }
                    }
                )
            }
            this.variants = copyVariants
            this.pairedOptions = pairedOptions
            this.listener = listener
        }
    }

    private var listener: ReadReviewFilterBottomSheetListener? = null
    private var variants: List<SelectVariantUiModel.Variant> = emptyList()
    private var pairedOptions: List<List<String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (variants.isEmpty()) dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitle("Filter Varian")
        setAction("Reset") {
            val options = variants.flatMap { it.options }
            options.forEach { it.isSelected = false }
        }

        val view = ComposeView(requireContext()).apply {
            setContent {
                NestTheme {
                    SelectVariantFilter(
                        uiModel = SelectVariantUiModel(variants, pairedOptions),
                        uiEvent = this@VariantFilterBottomSheet
                    )
                }
            }
        }

        setChild(view)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onApplyClicked(uiModel: SelectVariantUiModel) {
        updateBadgeStatus()
        uiModel.calculate()
        listener?.onFilterVariant(uiModel)
        dismiss()
    }

    private fun updateBadgeStatus(){
        val sharedPref = context?.getSharedPreferences("READ_REVIEW", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putBoolean("VARIANT_FILTER_BADGE", false)
            apply()
        }
    }

}
