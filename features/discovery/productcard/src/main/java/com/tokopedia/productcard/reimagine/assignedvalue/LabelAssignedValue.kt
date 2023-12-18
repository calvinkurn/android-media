package com.tokopedia.productcard.reimagine.assignedvalue

import android.graphics.Bitmap
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ImageSpan
import android.widget.TextView
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

private const val LABEL_ASSIGNED_VALUE_HEIGHT_DP = 18

internal fun Typography.renderProductNameWithAssignedValue(
    productCardModel: ProductCardModel,
    productName: String,
) {
    val labelAssignedValue = productCardModel.labelAssignedValue()
    val imageURL = labelAssignedValue?.imageUrl ?: ""
    val width = labelAssignedValue?.width() ?: 0

    val hasLabelAssignedValue = labelAssignedValue != null && imageURL.isNotEmpty() && width > 0

    if (hasLabelAssignedValue)
        imageURL.getBitmapImageUrl(
            context = context,
            properties = { labelAssignedValueProperties(width) },
            target = target(this, productName),
        )
    else
        setText(SpannedString(productName), TextView.BufferType.SPANNABLE)
}

private fun Properties.labelAssignedValueProperties(width: Int) {
    overrideSize(Resize(width.toPx(), LABEL_ASSIGNED_VALUE_HEIGHT_DP.toPx()))
}

private fun target(
    typography: Typography,
    productName: String,
): MediaBitmapEmptyTarget<Bitmap> =
    MediaBitmapEmptyTarget(
        onReady = { bitmap ->
            val imageSpan = ImageSpan(typography.context, bitmap)
            val spannableStringBuilder = SpannableStringBuilder(" $productName")
            spannableStringBuilder.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            typography.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)
        },
        onFailed = { _ ->
            typography.setText(SpannedString(productName), TextView.BufferType.SPANNABLE)
        }
    )
