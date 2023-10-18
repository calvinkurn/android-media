package com.tokopedia.search.result.mps.loading

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.search.databinding.SearchMpsLoadingLayoutBinding
import com.tokopedia.search.R
import com.tokopedia.unifycomponents.ProgressBarUnify.Companion.SIZE_LARGE

class MPSLoadingView: ConstraintLayout {

    private var binding: SearchMpsLoadingLayoutBinding? = null
    private var loadingAnimation: AnimatedVectorDrawableCompat? = null
    private var viewModel: MPSLoadingViewModel? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {

        init(context)
    }

    private fun init(context: Context) {
        binding = SearchMpsLoadingLayoutBinding.inflate(LayoutInflater.from(context), this)

        initLoadingAnimationResource(context)
        initViews(context)
        initViewModel(context)

        viewModel?.start()
    }

    private fun initLoadingAnimationResource(context: Context) {
        loadingAnimation =
            AnimatedVectorDrawableCompat.create(context, R.drawable.search_mps_loading_state)

        loadingAnimation?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                binding?.searchMpsLoadingAnimationView?.post {
                    loadingAnimation?.start()
                }
            }
        })
    }

    private fun initViews(context: Context) {
        val (title, subtitle) = MPSLoadingCopy.get()

        binding?.apply {
            searchMpsLoadingAnimationView.setImageDrawable(loadingAnimation)
            searchMpsLoadingProgressBar.progressBarHeight = SIZE_LARGE
            searchMpsLoadingTitle.text = context.getString(title)
            searchMpsLoadingSubtitle.text = context.getString(subtitle)
        }

        loadingAnimation?.start()
    }

    private fun initViewModel(context: Context) {
        if (context !is LifecycleOwner) return

        viewModel = MPSLoadingViewModel(
            MPSLoadingState(),
            context.lifecycleScope,
            MPSLoadingConfig.default(),
        ).apply {
            onState(context.lifecycleScope) { state ->
                runOnMainThread(context, state)
            }
        }
    }

    private fun runOnMainThread(context: LifecycleOwner, state: MPSLoadingState) {
        Handler(Looper.getMainLooper()).post {
            if (context.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                refresh(state)
        }
    }

    private fun refresh(state: MPSLoadingState) {
        binding?.searchMpsLoadingProgressBar?.setValue(state.loadingValue)
    }

    fun finish() {
        viewModel?.finish()
    }

    override fun onDetachedFromWindow() {
        loadingAnimation?.stop()
        loadingAnimation?.clearAnimationCallbacks()
        loadingAnimation = null

        super.onDetachedFromWindow()
    }
}
