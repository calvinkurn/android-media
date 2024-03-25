package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class VariantFilterBottomSheet : BottomSheetUnify(), SelectVariantUiEvent {

    companion object {

        private const val ARG_VARIANTS = "arg_variants"
        fun instance(
            listener: ReadReviewFilterBottomSheetListener,
            variants: List<SelectVariantUiModel.Variant>
        ) = VariantFilterBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_VARIANTS, ArrayList(variants))
            }
            this.listener = listener
        }
    }

    private var listener: ReadReviewFilterBottomSheetListener? = null
    private var variants: List<SelectVariantUiModel.Variant>? = null
    private var uiModel: SelectVariantUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        variants = arguments?.getParcelableArrayList<SelectVariantUiModel.Variant>(ARG_VARIANTS)?.toList()

        if(variants == null) dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val variants = variants ?: return null

        setTitle("Filter Varian")
        setAction("Reset") {
            val options = variants?.flatMap { it.options } ?: emptyList()
            options.forEach { it.isSelected = false }
        }

        val uiModel = SelectVariantUiModel(variants)
        this.uiModel = uiModel
        val view = ComposeView(requireContext()).apply {
            setContent {
                NestTheme {
                    SelectVariantFilter(
                        uiModel = uiModel,
                        uiEvent = this@VariantFilterBottomSheet
                    )
                }
            }
        }

        setChild(view)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onApplyClicked(filter: String) {
        val options = variants?.flatMap { it.options } ?: emptyList()
        val selected = options.filter { it.isSelected }
        listener?.onFilterVariant(
            count = selected.size,
            variantFilter = uiModel?.generateFilter() ?: ""
        )
        dismiss()
    }

}
