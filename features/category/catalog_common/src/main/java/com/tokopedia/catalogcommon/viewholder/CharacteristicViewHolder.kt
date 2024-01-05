package com.tokopedia.catalogcommon.viewholder

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemLayoutCharacteristicBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemCharacteristicBinding
import com.tokopedia.catalogcommon.uimodel.CharacteristicUiModel
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding


class CharacteristicViewHolder(itemView: View) :
    AbstractViewHolder<CharacteristicUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_characteristic
        const val DIVIDER_COLOR = "#AAB4C8"
        const val ALPHA_20_PERCENTAGE = 20
        const val HORIZONTAL_MARGIN = 6
    }

    private val binding by viewBinding<WidgetItemCharacteristicBinding>()
    private val displayMetrics = itemView.resources.displayMetrics
    private var onceCreateView = false

    override fun bind(element: CharacteristicUiModel) {
        if (!onceCreateView) {
            element.items.forEachIndexed { index, item ->
                createItem(item)
                if (index < element.items.size - Int.ONE) {
                    binding?.lnRootUi?.addView(Divider(marginRight = HORIZONTAL_MARGIN, marginLeft = HORIZONTAL_MARGIN))
                }
            }
        }

        onceCreateView = true
    }

    private fun createItem(item: CharacteristicUiModel.ItemCharacteristicUiModel) {
        val layout = ItemLayoutCharacteristicBinding.inflate(
            LayoutInflater.from(itemView.context),
            binding?.root,
            false
        )
        layout.ivIcon.showWithCondition(item.icon.isNotEmpty())
        layout.ivIcon.loadImage(item.icon)
        layout.tvTitle.text = item.title
        layout.tvTitle.setTextColor(item.textColorTitle)
        binding?.lnRootUi?.addView(layout.root)
    }

    private fun Divider(width: Int = Int.ONE, marginRight: Int = Int.ZERO, marginLeft: Int = Int.ZERO): View {
        val view = View(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(
            width.dpToPx(displayMetrics),
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParam.setMargins(
            marginLeft.dpToPx(displayMetrics),
            Int.ZERO,
            marginRight.dpToPx(displayMetrics),
            Int.ZERO
        )
        view.layoutParams = layoutParam
        view.setBackgroundColor(DIVIDER_COLOR.stringHexColorParseToInt(ALPHA_20_PERCENTAGE))
        return view
    }
}
