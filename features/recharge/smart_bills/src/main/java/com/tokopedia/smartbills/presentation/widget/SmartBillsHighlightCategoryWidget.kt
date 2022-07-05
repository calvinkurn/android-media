package com.tokopedia.smartbills.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.smartbills.data.uimodel.HighlightCategoryUiModel
import com.tokopedia.smartbills.databinding.ViewSmartBillsHighlightedCategoryBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class SmartBillsHighlightCategoryWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                  defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding = ViewSmartBillsHighlightedCategoryBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderHighlightCategory(uiModel: HighlightCategoryUiModel) {
        with(binding) {
            imgHighligtedCategory.loadImage(uiModel.imageUrl)
            tgHighlightedCategoryTitle.text = uiModel.title
            tgHighlightedCategoryDate.text = uiModel.date
            tgHighlightedCategoryDesc.text = uiModel.desc
        }
    }

    fun showHighlightCategory() {
        binding.root.show()
    }

    fun hideHighlightCategory() {
        binding.root.hide()
    }
}