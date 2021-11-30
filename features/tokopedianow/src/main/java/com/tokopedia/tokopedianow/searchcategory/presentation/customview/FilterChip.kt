package com.tokopedia.tokopedianow.searchcategory.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ChipTokopedianowCategoryFilterBinding
import com.tokopedia.unifycomponents.BaseCustomView

class FilterChip: BaseCustomView, View.OnClickListener {

    companion object {
        private const val IMAGE_ROUNDED = 4.0f
    }

    private var binding: ChipTokopedianowCategoryFilterBinding? = null

    var listener: Listener? = null
    private var state = false
    private val mStates = intArrayOf(
            R.attr.category_filter_chip_on,
            R.attr.category_filter_chip_off,
    )

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
        binding = ChipTokopedianowCategoryFilterBinding.inflate(LayoutInflater.from(context),this, true)
        background = ContextCompat.getDrawable(context, R.drawable.tokopedianow_category_filter_chip_selector)

        setOnClickListener(this)
    }

    fun setContent(model: Model) {
        binding?.tokoNowCategoryFilterImage?.loadImageRounded(model.imageUrl, IMAGE_ROUNDED)
        binding?.tokoNowCategoryFilterTitle?.text = model.title
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