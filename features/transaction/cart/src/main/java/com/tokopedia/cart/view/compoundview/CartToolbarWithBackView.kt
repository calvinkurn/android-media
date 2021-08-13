package com.tokopedia.cart.view.compoundview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.tokopedia.cart.databinding.CartToolbarWithBackViewBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class CartToolbarWithBackView : Toolbar, CartToolbar {

    lateinit var listener: CartToolbarListener
    lateinit var binding: CartToolbarWithBackViewBinding

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
        binding = CartToolbarWithBackViewBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            btnBack.setOnClickListener { listener.onBackPressed() }
            btnWishlistLottie.setOnClickListener { listener.onWishlistClicked() }
            btnWishlist.setOnClickListener { listener.onWishlistClicked() }
        }
    }

    override fun getWishlistIconPosition(): Pair<Int, Int> {
        val location = IntArray(2)
        binding.btnWishlist.getLocationOnScreen(location)
        val xCoordinate = location[0]
        val yCoordinate = location[1]

        return Pair(xCoordinate, yCoordinate)
    }

    override fun animateWishlistIcon() {
        binding.btnWishlist.gone()
        binding.btnWishlistLottie.show()
        binding.btnWishlistLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator?) {}

            override fun onAnimationEnd(animator: Animator?) {
                binding.btnWishlistLottie.gone()
                binding.btnWishlist.show()
            }

            override fun onAnimationCancel(animator: Animator?) {
                binding.btnWishlistLottie.gone()
                binding.btnWishlist.show()
            }

            override fun onAnimationStart(animator: Animator?) {}
        })
        if (!binding.btnWishlistLottie.isAnimating) {
            binding.btnWishlistLottie.playAnimation()
        }
    }
}
