package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.tokopedia.tokofood.databinding.ToolbarPromoBinding

class TokoFoodPromoToolbar : Toolbar {

    var listener: TokoFoodPromoToolbarListener? = null
    private var viewBinding: ToolbarPromoBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setTitle(title: String) {
        viewBinding?.textTitlePurchasePromo?.text = title
    }

    private fun init() {
        viewBinding = ToolbarPromoBinding.inflate(LayoutInflater.from(context), this, true)
        viewBinding?.iconBackArrow?.setOnClickListener {
            listener?.onBackPressed()
        }
    }

}
