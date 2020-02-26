package com.tokopedia.purchase_platform.features.promo.presentation.compoundview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifyprinciples.Typography

class ToolbarPromoCheckout : Toolbar {

    lateinit var buttonBack: ImageView
    lateinit var labelTitle: Typography
    lateinit var buttonResetPromo: Typography

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.toolbar_promo_checkout, this)
        buttonBack = view.findViewById(R.id.button_back)
        labelTitle = view.findViewById(R.id.label_title)
        buttonResetPromo = view.findViewById(R.id.button_reset_promo)

        buttonBack.setOnClickListener {
            (context as Activity).finish()
        }
    }

}
