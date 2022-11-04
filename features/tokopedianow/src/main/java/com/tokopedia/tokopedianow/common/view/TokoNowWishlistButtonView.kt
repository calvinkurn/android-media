package com.tokopedia.tokopedianow.common.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
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
        private const val LEFT_POSITION_ROTATION = 11.63f
        private const val RIGHT_POSITION_ROTATION = -11.63f
        private const val ANIMATION_DURATION = 500
    }

    private var productID: String = ""
    private var binding: LayoutTokopedianowWishlistButtonViewBinding
    private var hasBeenSelected: Boolean = false

    @Inject
    lateinit var viewModel: TokoNowWishlistViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    init {
        initInjector()
        binding = LayoutTokopedianowWishlistButtonViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            val ringingAnimation = ObjectAnimator.ofFloat(
                icon,
                View.ROTATION,
                START_POSITION,
                RIGHT_POSITION_ROTATION,
                LEFT_POSITION_ROTATION,
                START_POSITION
            ).setDuration(ANIMATION_DURATION.toLong())

            parentWishlist.setOnClickListener {
                if(!hasBeenSelected){
                    changeStateToRemoveWishlist()
                    viewModel.addToWishlist(productID)
                }
                else{
                    changeStateToAddWishlist()
                    viewModel.removeFromWishlist(productID)
                }
            }

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) {
                        ringingAnimation.start()
                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//                        if(!hasBeenSelected){
//                            viewModel.addToWishlist(productID)
//                        }
//                        else{
//                            viewModel.removeFromWishlist(productID)
//                        }
                    }

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
                        Toaster.build(rootView, "Added to wishlist").show()
                    }
                    else{
                        changeStateToAddWishlist()
                        it.data.wishlistAdd?.message?.let { it1 ->
                            Toaster.build(rootView,
                                it1, Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
                is Fail ->{
                    changeStateToAddWishlist()
                    it.throwable.message
                }
            }
        })
        viewModel.removeFromWishlistLiveData.observe(context as AppCompatActivity, {
            when(it){
                is Success ->{
                    if(it.data.wishlistRemove?.success == true){
                        Toaster.build(rootView, "Removed from wishlist").show()
                    }
                    else{
                        changeStateToRemoveWishlist()
                        it.data.wishlistRemove?.message?.let { it1 ->
                            Toaster.build(rootView,
                                it1, Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
                is Fail ->{
                    changeStateToRemoveWishlist()
                    it.throwable.message
                }
            }
        })
    }

    private fun changeStateToRemoveWishlist() {
        binding.root.setTransition(R.id.start, R.id.end)
        binding.root.transitionToEnd()
    }

    private fun changeStateToAddWishlist() {
        binding.root.setTransition(R.id.end, R.id.start)
        binding.root.transitionToEnd()
    }

    fun setProductId(productID: String){
        this.productID = productID
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
