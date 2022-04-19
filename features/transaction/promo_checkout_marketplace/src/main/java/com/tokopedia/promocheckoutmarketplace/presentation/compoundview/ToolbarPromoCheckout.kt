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

    fun disableResetButton() {
        viewBinding?.buttonResetPromo?.isEnabled = false
        viewBinding?.buttonResetPromo?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }

    fun enableResetButton() {
        viewBinding?.buttonResetPromo?.isEnabled = true
        viewBinding?.buttonResetPromo?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun hideResetButton() {
        viewBinding?.buttonResetPromo?.gone()
    }

    fun showResetButton() {
        viewBinding?.buttonResetPromo?.show()
    }

    private fun init() {
        viewBinding = PromoCheckoutMarketplaceModuleToolbarPromoCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

        viewBinding?.buttonBack?.setOnClickListener {
            listener?.onBackPressed()
        }

        viewBinding?.buttonResetPromo?.setOnClickListener {
            if (viewBinding?.buttonResetPromo?.isEnabled == true) {
                listener?.onClickResetPromo()
            }
        }
    }

}
