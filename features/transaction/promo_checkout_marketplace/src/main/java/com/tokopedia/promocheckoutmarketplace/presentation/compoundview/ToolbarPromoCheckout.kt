package com.tokopedia.promocheckoutmarketplace.presentation.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.unifyprinciples.Typography

class ToolbarPromoCheckout : Toolbar {

    lateinit var buttonBack: IconUnify
    lateinit var labelTitle: Typography
    lateinit var buttonResetPromo: Typography
    lateinit var listener: ToolbarPromoCheckoutListener

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun disableResetButton() {
        buttonResetPromo.isEnabled = false
        buttonResetPromo.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }

    fun enableResetButton() {
        buttonResetPromo.isEnabled = true
        buttonResetPromo.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun hideResetButton() {
        buttonResetPromo.gone()
    }

    fun showResetButton() {
        buttonResetPromo.show()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.promo_checkout_marketplace_module_toolbar_promo_checkout, this)
        buttonBack = view.findViewById(R.id.button_back)
        labelTitle = view.findViewById(R.id.label_title)
        buttonResetPromo = view.findViewById(R.id.button_reset_promo)

        buttonBack.setOnClickListener {
            listener.onBackPressed()
        }

        buttonResetPromo.setOnClickListener {
            if (buttonResetPromo.isEnabled) {
                listener.onClickResetPromo()
            }
        }
    }

}
