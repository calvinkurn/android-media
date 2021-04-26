package com.tokopedia.topads.edit.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TipsRowItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val LAYOUT = R.layout.tip_row_item
    lateinit var image: ImageUnify
    lateinit var tvTitle: Typography


    init {
        View.inflate(context, LAYOUT, this)
        orientation = LinearLayout.HORIZONTAL
        image = findViewById(R.id.rowImg)
        tvTitle = findViewById(R.id.rowTxt)
    }

    fun setData(rowItemText: String) {
        tvTitle.text = rowItemText
        image.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
    }

}