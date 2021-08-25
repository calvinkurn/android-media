package com.tokopedia.play.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R

/**
 * Created By : Jonathan Darwin on August 23, 2021
 */
class ProductIconView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private val tvTotal: TextView

    init {
        val view = View.inflate(context, R.layout.view_product_icon, this)
        tvTotal = view.findViewById(R.id.tv_play_total_featured_product)
    }

    fun setTotalProduct(total: Int) {
        if(total != -1) {
            tvTotal.text = total.toString()
            tvTotal.visible()
        }
        else tvTotal.gone()
    }
}