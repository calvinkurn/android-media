package com.tokopedia.tokomart.searchcategory.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.tokomart.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class FilterChip: BaseCustomView, View.OnClickListener {

    var listener: Listener? = null
    private var state = false
    private val mStates = intArrayOf(
            R.attr.category_filter_chip_on,
            R.attr.category_filter_chip_off,
    )
    private var chipImage: ImageUnify? = null
    private var chipTitle: Typography? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.chip_tokomart_category_filter, this)

        background = ContextCompat.getDrawable(context, R.drawable.tokomart_category_filter_chip_selector)
        chipImage = findViewById(R.id.tokomartCategoryFilterImage)
        chipTitle = findViewById(R.id.tokomartCategoryFilterTitle)

        setOnClickListener(this)
    }

    fun setContent(model: Model) {
        chipImage?.loadImageRounded(model.imageUrl, 4.0f)
        chipTitle?.text = model.title
        state = model.state

        refreshDrawableState()
    }

    override fun onClick(v: View?) {
        state = !state
        listener?.onClick(state)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        val chipState = intArrayOf(mStates[if (state) 0 else 1])

        mergeDrawableStates(drawableState, chipState)

        return drawableState
    }

    interface Listener {
        fun onClick(state: Boolean)
    }

    data class Model(
            val imageUrl: String = "",
            val title: String = "",
            val state: Boolean = false
    )
}