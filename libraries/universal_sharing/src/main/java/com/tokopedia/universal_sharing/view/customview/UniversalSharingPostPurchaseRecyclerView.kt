package com.tokopedia.universal_sharing.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class UniversalSharingPostPurchaseRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var maxHeight: Int = 0

    init {
        val displayMetrics = context.resources.displayMetrics
        maxHeight = (displayMetrics.heightPixels * 0.75).roundToInt() // assume 5% is header
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST))
    }
}
