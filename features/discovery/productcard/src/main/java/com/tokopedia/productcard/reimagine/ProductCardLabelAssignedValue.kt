package com.tokopedia.productcard.reimagine

import android.view.View
import android.widget.ImageView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

internal class ProductCardLabelAssignedValue(view: View) {

    private val context = view.context
    private val background by view.lazyView<ImageView?>(R.id.productCardLabelAssignedValueBackground)
    private val text by view.lazyView<Typography?>(R.id.productCardLabelAssignedValueText)
    private val icon by view.lazyView<IconUnify?>(R.id.productCardLabelAssignedValueIcon)

    fun render(labelAssignedValue: ProductCardModel.LabelGroup?) {
        val hasLabelAssignedValue = labelAssignedValue != null
        val title = labelAssignedValue?.title ?: ""

        background?.showWithCondition(hasLabelAssignedValue)

        text?.shouldShowWithAction(hasLabelAssignedValue) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                it,
                title,
                context.getString(R.string.product_card_content_desc_label_assigned_value)
            )
        }

        icon?.showWithCondition(hasLabelAssignedValue)
    }
}
