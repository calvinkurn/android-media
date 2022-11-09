package com.tokopedia.tokopedianow.common.view.productcard

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
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

    private var productId: String = ""
    private var binding: LayoutTokopedianowWishlistButtonViewBinding
    private var hasBeenSelected: Boolean = false

    @Inject
    lateinit var viewModel: TokoNowWishlistViewModel

    init {
        initInjector()
        binding = LayoutTokopedianowWishlistButtonViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            val ringingAnimation = ObjectAnimator.ofFloat(
                icon,
                View.ROTATION,
                START_POSITION,
                LEFT_POSITION_ROTATION,
                RIGHT_POSITION_ROTATION,
                START_POSITION
            ).setDuration(ANIMATION_DURATION.toLong())

            parentWishlist.setOnClickListener {
                if(!hasBeenSelected){
                    changeStateToRemoveWishlist()
                    viewModel.addToWishlist(productId)
                }
                else{
                    changeStateToAddWishlist()
                    viewModel.removeFromWishlist(productId)
                }
            }

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) = onTransitionStarted(ringingAnimation)

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int){ /* nothing to do*/ }

                    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
        }

        observeLiveData()
    }

    private fun initInjector() {
        DaggerCommonComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeLiveData() {
        viewModel.addToWishlistLiveData.observe(context as AppCompatActivity, {
            when(it){
                is Success ->{
                    if(it.data.wishlistAdd?.success == true){
                        Toaster.build(rootView, "Barang berhasil disimpan di Wishist. Kamu akan dapat notifikasi saat stok kembali.", actionText = "Oke").show()
                    }
                    else{
                        changeStateToAddWishlist()
                        Toaster.build(rootView, "Oops, barang gagal disimpan di Wishlist.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                        "Coba Lagi",
                        clickListener = {
                            changeStateToRemoveWishlist()
                            viewModel.addToWishlist(productId)
                        }).show()
                    }
                }
                is Fail ->{
                    changeStateToAddWishlist()
                    Toaster.build(rootView, "Oops, barang gagal disimpan di Wishlist.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                        "Coba Lagi",
                        clickListener = {
                            changeStateToRemoveWishlist()
                            viewModel.addToWishlist(productId)
                        }).show()
                }
            }
        })
        viewModel.removeFromWishlistLiveData.observe(context as AppCompatActivity, {
            when(it){
                is Success ->{
                    if(it.data.wishlistRemove?.success == true){
                        Toaster.build(rootView, "Barang sudah dihapus dari Wishlist.", actionText = "Oke").show()
                    }
                    else{
                        changeStateToRemoveWishlist()
                        Toaster.build(rootView, "Oops, barang gagal dihapus dari Wishlist.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                            "Coba Lagi",
                            clickListener = {
                                changeStateToAddWishlist()
                                viewModel.removeFromWishlist(productId)
                            }).show()
                    }
                }
                is Fail ->{
                    changeStateToRemoveWishlist()
                    Toaster.build(rootView, "Oops, barang gagal dihapus dari Wishlist.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                        "Coba Lagi",
                        clickListener = {
                            changeStateToAddWishlist()
                            viewModel.removeFromWishlist(productId)
                        }).show()
                }
            }
        })
    }

    private fun changeStateToRemoveWishlist() {
        hasBeenSelected = !hasBeenSelected
        binding.root.setTransition(R.id.start, R.id.end)
        binding.root.transitionToEnd()
    }

    private fun changeStateToAddWishlist() {
        hasBeenSelected = !hasBeenSelected
        binding.root.setTransition(R.id.end, R.id.start)
        binding.root.transitionToEnd()
    }

    private fun onTransitionStarted(ringingAnimation: ObjectAnimator) = if (hasBeenSelected) ringingAnimation.start() else ringingAnimation.reverse()

    fun setProductId(productId: String){
        this.productId = productId
    }

    fun setValue(isSelected: Boolean) {
        hasBeenSelected = isSelected
        if (hasBeenSelected) {
            binding.root.setTransition(R.id.end, R.id.start)
        } else {
            binding.root.setTransition(R.id.start, R.id.end)
        }
    }

    fun getValue() = hasBeenSelected
}
