package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.abstraction.R as abstractionR

private const val VIEW_ID_CONSTANT = 100

abstract class BaseProductHighlightItem(
    private val productHighlightData: DataItem,
    private val constraintLayout: ConstraintLayout,
    private val constraintSet: ConstraintSet,
    private val index: Int,
    private val previousProductHighlightItem: BaseProductHighlightItem?,
    open val context: Context,
    private val islastItem: Boolean,
    open val compType: String? = null
) {

    abstract var productHighlightView: View

    abstract fun setDataInCard()

    protected fun addItemConstrains() {
        setDataInCard()

        productHighlightView.id = VIEW_ID_CONSTANT + index
        constraintLayout.addView(productHighlightView)
        constraintSet.clear(productHighlightView.id)
        constraintSet.constrainWidth(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.WRAP_CONTENT)

        if (productHighlightData.typeProductHighlightComponentCard == Constant.ProductHighlight.DOUBLESINGLEEMPTY || productHighlightData.typeProductHighlightComponentCard == Constant.ProductHighlight.TRIPLESINGLEEMPTY) {
            constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
        }

        if (productHighlightData.typeProductHighlightComponentCard == Constant.ProductHighlight.TRIPLEDOUBLEEMPTY) {
            constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.setHorizontalWeight(productHighlightView.id, 2.0f)
        } else {
            constraintSet.setHorizontalWeight(productHighlightView.id, 1.0f)
        }

        if (previousProductHighlightItem == null) {
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                context.resources.getDimensionPixelSize(R.dimen.dp_16)
            )
        } else {
            constraintSet.connect(
                previousProductHighlightItem.productHighlightView.id,
                ConstraintSet.END,
                productHighlightView.id,
                ConstraintSet.START,
                context.resources.getDimensionPixelSize(abstractionR.dimen.dp_4)
            )
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.START,
                previousProductHighlightItem.productHighlightView.id,
                ConstraintSet.END,
                context.resources.getDimensionPixelSize(abstractionR.dimen.dp_4)
            )
        }

        if (islastItem) {
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                context.resources.getDimensionPixelSize(R.dimen.dp_16)
            )
        }
        constraintSet.connect(productHighlightView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP, context.resources.getDimensionPixelSize(R.dimen.dp_16))
        constraintSet.connect(productHighlightView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM, context.resources.getDimensionPixelSize(abstractionR.dimen.dp_4))
        constraintSet.applyTo(constraintLayout)
    }
}
