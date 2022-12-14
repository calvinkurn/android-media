package com.tokopedia.tokopedianow.common.view.productcard

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.di.component.DaggerCommonComponent
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowWishlistViewModel
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowWishlistButtonViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowWishlistButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val START_POSITION = 0f
        private const val LEFT_POSITION_ROTATION = 17f
        private const val RIGHT_POSITION_ROTATION = -17f
        private const val ANIMATION_DURATION = 500
    }

    @Inject
    lateinit var viewModel: TokoNowWishlistViewModel

    private var binding: LayoutTokopedianowWishlistButtonViewBinding
    private var mProductId: String = ""
    private var hasBeenSelected: Boolean? = null
    private var listener: TokoNowWishlistButtonListener? = null

    init {
        initInjector()
        binding = LayoutTokopedianowWishlistButtonViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            setupRingingAnimation()
        }

        observeLiveData()
    }

    private fun LayoutTokopedianowWishlistButtonViewBinding.setupRingingAnimation() {
        val ringingAnimation = ObjectAnimator.ofFloat(
            icon,
            View.ROTATION,
            START_POSITION,
            LEFT_POSITION_ROTATION,
            RIGHT_POSITION_ROTATION,
            START_POSITION
        ).setDuration(ANIMATION_DURATION.toLong())

        parentWishlist.setOnClickListener {
            if (hasBeenSelected != true) {
                ringingAnimation.reverse()
                viewModel.addToWishlist(mProductId)
            } else {
                ringingAnimation.start()
                viewModel.removeFromWishlist(mProductId)
            }
        }
    }

    private fun initInjector() {
        DaggerCommonComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeLiveData() {
        viewModel.addToWishlistLiveData.observe(context as AppCompatActivity) {
            when (it) {
                is Success -> {
                    if (it.data.success == true) {
                        listener?.onWishlistButtonClicked(
                            productId = mProductId,
                            isWishlistSelected = true,
                            descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_add_success),
                            ctaToaster = context.getString(R.string.tokopedianow_toaster_ok)
                        )
                    } else {
                        listener?.onWishlistButtonClicked(
                            productId = mProductId,
                            isWishlistSelected = false,
                            descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_add_fail),
                            ctaToaster = context.getString(R.string.tokopedianow_toaster_retry),
                            type = Toaster.TYPE_ERROR,
                            ctaClickListener = {
                                viewModel.addToWishlist(mProductId)
                            }
                        )
                    }
                }
                is Fail -> {
                    listener?.onWishlistButtonClicked(
                        productId = mProductId,
                        isWishlistSelected = false,
                        descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_add_fail),
                        ctaToaster = context.getString(R.string.tokopedianow_toaster_retry),
                        type = Toaster.TYPE_ERROR,
                        ctaClickListener = {
                            viewModel.addToWishlist(mProductId)
                        }
                    )
                }
            }
        }

        viewModel.removeFromWishlistLiveData.observe(context as AppCompatActivity) {
            when (it) {
                is Success -> {
                    if (it.data.success == true) {
                        listener?.onWishlistButtonClicked(
                            productId = mProductId,
                            isWishlistSelected = false,
                            descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_remove_success),
                            ctaToaster = context.getString(R.string.tokopedianow_toaster_ok)
                        )
                    } else {
                        listener?.onWishlistButtonClicked(
                            productId = mProductId,
                            isWishlistSelected = true,
                            descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_remove_fail),
                            ctaToaster = context.getString(R.string.tokopedianow_toaster_retry),
                            type = Toaster.TYPE_ERROR,
                            ctaClickListener = {
                                viewModel.removeFromWishlist(mProductId)
                            }
                        )
                    }
                }
                is Fail -> {
                    listener?.onWishlistButtonClicked(
                        productId = mProductId,
                        isWishlistSelected = true,
                        descriptionToaster = context.getString(R.string.tokopedianow_product_card_wishlist_remove_fail),
                        ctaToaster = context.getString(R.string.tokopedianow_toaster_retry),
                        type = Toaster.TYPE_ERROR,
                        ctaClickListener = {
                            viewModel.removeFromWishlist(mProductId)
                        }
                    )
                }
            }
        }
    }

    fun setListener(wishlistButtonListener: TokoNowWishlistButtonListener) {
        listener = wishlistButtonListener
    }

    fun bind(isSelected: Boolean, productId: String) {
        if (isSelected == hasBeenSelected && productId == mProductId) return

        if (hasBeenSelected == null) {
            if (isSelected) {
                binding.root.setTransition(R.id.end, R.id.start)
            } else {
                binding.root.setTransition(R.id.start, R.id.end)
            }
        } else {
            if (isSelected) {
                binding.root.setTransition(R.id.start, R.id.end)
                binding.root.transitionToEnd()
            } else {
                binding.root.setTransition(R.id.end, R.id.start)
                binding.root.transitionToEnd()
            }
        }

        mProductId = productId
        hasBeenSelected = isSelected
    }

    interface TokoNowWishlistButtonListener {
        fun onWishlistButtonClicked(
            productId: String,
            isWishlistSelected: Boolean,
            descriptionToaster: String,
            ctaToaster: String,
            type: Int = Toaster.TYPE_NORMAL,
            ctaClickListener: (() -> Unit)? = null
        )
    }
}
