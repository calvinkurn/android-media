package com.tokopedia.tokofood.purchase.purchasepage.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.ToolbarPurchaseBinding

class TokoFoodPurchaseToolbar : Toolbar {

    var listener: TokoFoodPurchaseToolbarListener? = null
    private var viewBinding: ToolbarPurchaseBinding? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun showLoading() {
        viewBinding?.layoutToolbarPurchaseLoading?.show()
        viewBinding?.layoutToolbarPurchase?.gone()
    }

    fun hideLoading() {
        viewBinding?.layoutToolbarPurchaseLoading?.gone()
        viewBinding?.layoutToolbarPurchase?.show()
    }

    fun setToolbarData(shopName: String, shopDistance: String) {
        viewBinding?.textShopName?.text = shopName
        viewBinding?.textShopDistance?.text = shopDistance
    }

    private fun init() {
        viewBinding = ToolbarPurchaseBinding.inflate(LayoutInflater.from(context), this, true)
        viewBinding?.iconBackArrow?.setOnClickListener {
            listener?.onBackPressed()
        }
    }

}
