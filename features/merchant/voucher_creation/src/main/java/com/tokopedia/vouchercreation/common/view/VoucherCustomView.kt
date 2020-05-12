package com.tokopedia.vouchercreation.common.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

abstract class VoucherCustomView @JvmOverloads constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        private val defStyleAttr: Int = 0,
        private val defStyleRes: Int = 0,
        private val layoutResource: Int,
        private val styleableResource: IntArray = intArrayOf()
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        setupLayout()
    }

    protected var view : View? = null

    protected var attributes : TypedArray? = null

    fun setupLayout() {
        view = LayoutInflater.from(context).inflate(layoutResource, this, false)
        attributes = context.theme.obtainStyledAttributes(
                attrs,
                styleableResource,
                defStyleAttr,
                defStyleRes)
        setupAttributes()
        setupView()
        addView(view)
    }

    abstract fun setupAttributes()
    abstract fun setupView()

}