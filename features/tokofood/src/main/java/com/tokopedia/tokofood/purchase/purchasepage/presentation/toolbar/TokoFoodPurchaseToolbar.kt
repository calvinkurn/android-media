package com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.tokopedia.tokofood.databinding.ToolbarPurchaseBinding

class TokoFoodPurchaseToolbar : Toolbar {

    var listener: TokoFoodPurchaseToolbarListener? = null
    private var viewBinding: ToolbarPurchaseBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setToolbarData(shopName: String, shopDistance: String) {
        viewBinding?.let {
            it.textShopName.text = shopName
            it.textShopDistance.text = shopDistance
        }
    }

    private fun init() {
        viewBinding = ToolbarPurchaseBinding.inflate(LayoutInflater.from(context), this, true)

        viewBinding?.iconBackArrow?.setOnClickListener {
            listener?.onBackPressed()
        }
    }

}
