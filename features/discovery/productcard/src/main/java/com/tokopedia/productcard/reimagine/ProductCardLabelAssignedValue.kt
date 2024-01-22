package com.tokopedia.productcard.reimagine

import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography

internal class ProductCardLabelAssignedValue(view: View) {

    private val background by view.lazyView<ImageView?>(R.id.productCardLabelAssignedValueBackground)
    private val text by view.lazyView<Typography?>(R.id.productCardLabelAssignedValueText)
    private val icon by view.lazyView<ImageView?>(R.id.productCardLabelAssignedValueIcon)

    fun render(labelAssignedValue: ProductCardModel.LabelGroup?) {
        if (labelAssignedValue == null) hideLabel()
        else renderLabel(labelAssignedValue)
    }

    private fun hideLabel() {
        background?.hide()
        text?.hide()
        icon?.hide()
    }

    private fun renderLabel(labelAssignedValue: ProductCardModel.LabelGroup) {
        background?.show()
        text?.show()

        ProductCardLabel(background?.drawable, text).render(labelAssignedValue)

        renderIcon(labelAssignedValue)
    }

    private fun renderIcon(labelAssignedValue: ProductCardModel.LabelGroup) {
        val iconUrl = labelAssignedValue.imageUrl

        icon?.shouldShowWithAction(iconUrl.isNotEmpty()) {
            it.loadIcon(iconUrl)
        }
    }
}
