package com.tokopedia.cart.view.compoundview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.cart.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyImageButton

class CartToolbarWithBackView : Toolbar, CartToolbar {

    lateinit var btnBack: ImageView
    lateinit var textView: TextView
    lateinit var btnWishlist: UnifyImageButton
    lateinit var btnWishlistLottie: LottieAnimationView
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
        View.inflate(context, R.layout.cart_toolbar_with_back_view, this)?.let {
            btnBack = it.findViewById(R.id.btn_back)
            textView = it.findViewById(R.id.textview_title)
            btnWishlist = it.findViewById(R.id.btn_wishlist)
            btnWishlistLottie = it.findViewById(R.id.btn_wishlist_lottie)

            btnBack.setOnClickListener { listener.onBackPressed() }
            btnWishlistLottie.setOnClickListener { listener.onWishlistClicked() }
            btnWishlist.setOnClickListener { listener.onWishlistClicked() }
        }
    }

    override fun getWishlistIconPosition(): Pair<Int, Int> {
        val location = IntArray(2)
        btnWishlist.getLocationOnScreen(location)
        val xCoordinate = location[0]
        val yCoordinate = location[1]

        return Pair(xCoordinate, yCoordinate)
    }

    override fun animateWishlistIcon() {
        btnWishlist.gone()
        btnWishlistLottie.show()
        btnWishlistLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator?) {}

            override fun onAnimationEnd(animator: Animator?) {
                btnWishlistLottie.gone()
                btnWishlist.show()
            }

            override fun onAnimationCancel(animator: Animator?) {
                btnWishlistLottie.gone()
                btnWishlist.show()
            }

            override fun onAnimationStart(animator: Animator?) {}
        })
        if (!btnWishlistLottie.isAnimating) {
            btnWishlistLottie.playAnimation()
        }
    }
}
