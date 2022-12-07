package com.tokopedia.promocheckoutmarketplace.presentation.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleToolbarPromoCheckoutBinding

class ToolbarPromoCheckout : Toolbar {

    var listener: ToolbarPromoCheckoutListener? = null
    private var viewBinding: PromoCheckoutMarketplaceModuleToolbarPromoCheckoutBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun disableInputPromoButton() {
        viewBinding?.buttonInputPromo?.isEnabled = false
        viewBinding?.buttonInputPromo?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }

    fun enableInputPromoButton() {
        viewBinding?.buttonInputPromo?.isEnabled = true
        viewBinding?.buttonInputPromo?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun hideInputPromoButton() {
        viewBinding?.buttonInputPromo?.gone()
    }

    fun showInputPromoButton() {
        viewBinding?.buttonInputPromo?.show()
    }

    private fun init() {
        viewBinding = PromoCheckoutMarketplaceModuleToolbarPromoCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

        viewBinding?.buttonBack?.setOnClickListener {
            listener?.onBackPressed()
        }

        viewBinding?.buttonInputPromo?.setOnClickListener {
            listener?.onClickInputCode()
        }
    }

}
