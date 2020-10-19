package com.tokopedia.cart.view.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.cart.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by meta on 19/07/18.
 */
class CartToolbarView : BaseCustomView {

    lateinit var textView: Typography
    lateinit var btnWishlist: ImageView
    lateinit var listener: CartToolbarListener

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
        View.inflate(context, R.layout.cart_toolbar_view, this)?.let {
            textView = it.findViewById(R.id.textview_title)
            btnWishlist = it.findViewById(R.id.btn_wishlist)

            btnWishlist.setOnClickListener { listener.onWishlistClicked() }
        }
    }

    fun setTitle(title: CharSequence) {
        textView.text = title
    }

}
