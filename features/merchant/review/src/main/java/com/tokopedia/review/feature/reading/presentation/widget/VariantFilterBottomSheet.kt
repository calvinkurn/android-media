package com.tokopedia.review.feature.reading.presentation.widget

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
            variants: List<SelectVariantUiModel.Variant>
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
            this.listener = listener
        }
    }

    private var listener: ReadReviewFilterBottomSheetListener? = null
    private var variants: List<SelectVariantUiModel.Variant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (variants == null) dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val variants = variants ?: return null

        setTitle("Filter Varian")
        setAction("Reset") {
            val options = variants.flatMap { it.options }
            options.forEach { it.isSelected = false }
        }

        val view = ComposeView(requireContext()).apply {
            setContent {
                NestTheme {
                    SelectVariantFilter(
                        uiModel = SelectVariantUiModel(variants),
                        uiEvent = this@VariantFilterBottomSheet
                    )
                }
            }
        }

        setChild(view)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onApplyClicked(filter: String, variants: List<SelectVariantUiModel.Variant>) {
        val options = variants.flatMap { it.options }
        val selected = options.filter { it.isSelected }
        listener?.onFilterVariant(
            count = selected.size,
            variantFilter = filter,
            variants
        )
        dismiss()
    }

}
