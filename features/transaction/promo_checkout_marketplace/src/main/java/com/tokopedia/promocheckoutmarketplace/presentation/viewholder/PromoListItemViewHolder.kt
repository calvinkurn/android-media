package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.setImageFilterGrayScale
import com.tokopedia.promocheckoutmarketplace.presentation.setImageFilterNormal
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

class PromoListItemViewHolder(private val view: View,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(view) {

    private val containerImagePromoItem by lazy {
        view.findViewById<LinearLayout>(R.id.container_image_promo_item)
    }
    private val labelPromoItemTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_item_title)
    }
    private val labelPromoItemSubTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_item_sub_title)
    }
    private val labelPromoCodeInfo by lazy {
        view.findViewById<Typography>(R.id.label_promo_code_info)
    }
    private val labelPromoCodeValue by lazy {
        view.findViewById<Typography>(R.id.label_promo_code_value)
    }
    private val labelPromoItemErrorMessage by lazy {
        view.findViewById<Typography>(R.id.label_promo_item_error_message)
    }
    private val cardPromoItem by lazy {
        view.findViewById<CardUnify>(R.id.card_promo_item)
    }
    private val imageSelectPromo by lazy {
        view.findViewById<ImageView>(R.id.image_select_promo)
    }

    companion object {
        val LAYOUT = R.layout.item_promo_list_item
    }

    override fun bind(element: PromoListItemUiModel) {
        if (element.uiData.imageResourceUrls.isNotEmpty()) {
            containerImagePromoItem.removeAllViews()
            var hasNonBlankUrl = false
            element.uiData.imageResourceUrls.forEach {
                if (it.isNotBlank()) {
                    hasNonBlankUrl = true
                    val imageView = ImageView(itemView.context)
                    imageView.layoutParams = LinearLayout.LayoutParams(
                            itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30),
                            LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    imageView.setMargin(0, 0, itemView.context.resources.getDimensionPixelSize(R.dimen.dp_4), 0)
                    imageView.scaleType = ImageView.ScaleType.FIT_START
                    ImageHandler.loadImageRounded2(itemView.context, imageView, it)
                    containerImagePromoItem.addView(imageView)
                }
            }
            if (hasNonBlankUrl) {
                containerImagePromoItem.show()
            } else {
                labelPromoItemTitle.setMargin(0, 0, itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
                containerImagePromoItem.gone()
            }
        } else {
            labelPromoItemTitle.setMargin(0, 0, itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
            containerImagePromoItem.gone()
        }

        if (element.uiState.isAttempted) {
            labelPromoCodeValue.text = element.uiData.promoCode
            labelPromoCodeValue.show()
            labelPromoCodeInfo.show()
        } else {
            labelPromoCodeValue.gone()
            labelPromoCodeInfo.gone()
        }

        labelPromoItemTitle.text = element.uiData.title
        formatSubTitle(element)

        if (element.uiState.isParentEnabled && element.uiData.currentClashingPromo.isNullOrEmpty()) {
            if (element.uiState.isDisabled) {
                renderDisablePromoItem(element)
            } else {
                renderEnablePromoItem(element)
            }
        } else {
            renderDisablePromoItem(element)
        }

        cardPromoItem.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION && element.uiData.currentClashingPromo.isNullOrEmpty() && element.uiState.isParentEnabled && !element.uiState.isDisabled) {
                listener.onClickPromoListItem(element)
            }
        }

    }

    private fun renderEnablePromoItem(element: PromoListItemUiModel) {
        labelPromoItemErrorMessage.gone()
        labelPromoItemSubTitle.setMargin(0, itemView.context.resources.getDimension(R.dimen.dp_2).toInt(),
                itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
        if (element.uiState.isSelected) {
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_ACTIVE
            imageSelectPromo.show()
        } else {
            cardPromoItem.cardType = CardUnify.TYPE_BORDER
            imageSelectPromo.gone()
        }

        val childcount = containerImagePromoItem.childCount
        for (index in 0 until childcount) {
            val child = containerImagePromoItem.getChildAt(index)
            if (child is ImageView) {
                setImageFilterNormal(child)
            }
        }

        labelPromoItemTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.clr_f531353b))
        labelPromoCodeInfo.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_70))
        labelPromoCodeValue.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_70))
        labelPromoItemErrorMessage.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_70))
        labelPromoItemSubTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_70))
    }

    private fun renderDisablePromoItem(element: PromoListItemUiModel) {
        cardPromoItem.cardType = CardUnify.TYPE_BORDER_DISABLED
        if (element.uiData.errorMessage.isNotBlank()) {
            labelPromoItemErrorMessage.text = element.uiData.errorMessage
            labelPromoItemErrorMessage.show()
        } else {
            labelPromoItemErrorMessage.gone()
        }
        labelPromoItemSubTitle.setMargin(0, itemView.context.resources.getDimension(R.dimen.dp_8).toInt(),
                itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
        imageSelectPromo.gone()

        val childcount = containerImagePromoItem.childCount
        for (index in 0 until childcount) {
            val child = containerImagePromoItem.getChildAt(index)
            if (child is ImageView) {
                setImageFilterGrayScale(child)
            }
        }

        val disabledColor = ContextCompat.getColor(itemView.context, R.color.n_700_44)
        labelPromoItemTitle.setTextColor(disabledColor)
        labelPromoCodeInfo.setTextColor(disabledColor)
        labelPromoCodeValue.setTextColor(disabledColor)
        labelPromoItemErrorMessage.setTextColor(disabledColor)
        labelPromoItemSubTitle.setTextColor(disabledColor)
    }

    private fun formatSubTitle(element: PromoListItemUiModel) {
        if (!element.uiState.isAttempted) {
            var clickableText = itemView.context.getString(R.string.label_promo_show_detail)
            if (element.uiData.subTitle.isNotBlank() && !element.uiData.subTitle.contains(clickableText)) {
                clickableText = " $clickableText"
            }

            if (!element.uiData.subTitle.contains(clickableText)) element.uiData.subTitle += clickableText

            val startSpan = element.uiData.subTitle.indexOf(clickableText)
            val endSpan = element.uiData.subTitle.indexOf(clickableText) + clickableText.length
            val formattedClickableText = SpannableString(element.uiData.subTitle)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    listener.onClickPromoItemDetail(element)
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = false
                    textPaint.color = ContextCompat.getColor(itemView.context, R.color.Green_G500)
                }
            }
            formattedClickableText.setSpan(clickableSpan, startSpan, endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            labelPromoItemSubTitle.movementMethod = LinkMovementMethod.getInstance()
            labelPromoItemSubTitle.text = formattedClickableText
            labelPromoItemSubTitle.show()
        } else {
            if (element.uiData.subTitle.isNotBlank()) {
                labelPromoItemSubTitle.text = element.uiData.subTitle
                labelPromoItemSubTitle.show()
            } else {
                labelPromoItemSubTitle.gone()
            }
        }
    }


}