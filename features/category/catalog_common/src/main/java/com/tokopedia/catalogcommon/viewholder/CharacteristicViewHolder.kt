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
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding


class CharacteristicViewHolder(itemView: View) :
    AbstractViewHolder<CharacteristicUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_characteristic
    }

    private val binding by viewBinding<WidgetItemCharacteristicBinding>()
    private val displayMetrics = itemView.resources.displayMetrics
    private var onceCreateView = false

    override fun bind(element: CharacteristicUiModel) {
        if (!onceCreateView) {
            element.items.forEachIndexed { index, item ->
                createItem(item)
                if (index < element.items.size - 1) {
                    binding?.lnRootUi?.addView(Divider(marginRight = 6, marginLeft = 6))
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

    private fun createLinearLayout(
        width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    ): LinearLayout {
        val linearLayout = LinearLayout(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(width, height)
        layoutParam.weight = 1f
        linearLayout.gravity = Gravity.CENTER
        linearLayout.layoutParams = layoutParam
        linearLayout.orientation = LinearLayout.HORIZONTAL
        return linearLayout
    }

    private fun Divider(width: Int = 2, marginRight: Int = 0, marginLeft: Int = 0): View {
        val view = View(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(
            width.dpToPx(displayMetrics),
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParam.setMargins(
            marginLeft.dpToPx(displayMetrics),
            0,
            marginRight.dpToPx(displayMetrics),
            0
        )
        view.layoutParams = layoutParam
        view.setBackgroundColor("#AAB4C8".stringHexColorParseToInt(20))
        return view
    }
}
