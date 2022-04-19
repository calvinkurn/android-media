package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.text.Spanned
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemOrderExtensionRequestInfoDescriptionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class OrderExtensionRequestInfoDescriptionViewHolder(
    itemView: View?
) : BaseOrderExtensionRequestInfoViewHolder<OrderExtensionRequestInfoUiModel.DescriptionUiModel>(
    itemView
) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_description
    }

    private val binding by viewBinding<ItemOrderExtensionRequestInfoDescriptionBinding>()

    override fun bind(element: OrderExtensionRequestInfoUiModel.DescriptionUiModel?) {
        super.bind(element)
        element?.run {
            setupDescription(description.compose(binding?.root?.context))
            setupAlignment(alignment)
            setupFont(fontColor, typographyType)
        }
    }

    override fun onTap(event: MotionEvent?): Boolean {
        super.onTap(event)
        if (event != null) {
            binding?.root?.run {
                var x = event.x
                var y = event.y.toInt()

                x -= totalPaddingLeft
                y -= totalPaddingTop

                x += scrollX
                y += scrollY

                val layout = layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x)

                text?.takeIf { it is Spanned }?.let {
                    (it as Spanned).getSpans(off, off, URLSpan::class.java).firstOrNull()
                        ?.let { urlSpan ->
                            RouteManager.route(context, urlSpan.url.toString())
                        }
                }
            }
        }
        return true
    }

    private fun setupDescription(description: CharSequence) {
        binding?.root?.text = description
    }

    private fun setupAlignment(alignment: OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment) {
        binding?.root?.textAlignment = when (alignment) {
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_INHERIT -> View.TEXT_ALIGNMENT_INHERIT
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_GRAVITY -> View.TEXT_ALIGNMENT_GRAVITY
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_CENTER -> View.TEXT_ALIGNMENT_CENTER
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_TEXT_START -> View.TEXT_ALIGNMENT_TEXT_START
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_TEXT_END -> View.TEXT_ALIGNMENT_TEXT_END
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_VIEW_START -> View.TEXT_ALIGNMENT_VIEW_START
            OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_VIEW_END -> View.TEXT_ALIGNMENT_VIEW_END
        }
    }

    private fun setupFont(
        fontColor: Int,
        typographyType: OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType
    ) {
        binding?.root?.run {
            setTextColor(MethodChecker.getColor(context, fontColor))
            when (typographyType) {
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_1 -> setType(
                    Typography.HEADING_1
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_2 -> setType(
                    Typography.HEADING_2
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_3 -> setType(
                    Typography.HEADING_3
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_4 -> setType(
                    Typography.HEADING_4
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_5 -> setType(
                    Typography.HEADING_5
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_6 -> setType(
                    Typography.HEADING_6
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.BODY_1 -> setType(
                    Typography.BODY_1
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.BODY_2 -> setType(
                    Typography.BODY_2
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.BODY_3 -> setType(
                    Typography.BODY_3
                )
                OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.SMALL -> setType(
                    Typography.SMALL
                )
            }
        }
    }
}