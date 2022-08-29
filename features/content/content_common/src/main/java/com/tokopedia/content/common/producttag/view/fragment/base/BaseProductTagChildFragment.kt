package com.tokopedia.content.common.producttag.view.fragment.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
open class BaseProductTagChildFragment : TkpdBaseV4Fragment() {

    protected lateinit var viewModelProvider: ViewModelProvider

    override fun getScreenName(): String = "BaseProductTagChildFragment"

    fun createViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransition()
    }

    private fun setupTransition() {
        setupEnterTransition()
        setupReturnTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.IN))
            .setDuration(FRAGMENT_TRANSITION_DURATION)

        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(FRAGMENT_TRANSITION_DURATION)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.OUT))
            .setDuration(FRAGMENT_TRANSITION_DURATION)

        sharedElementReturnTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(FRAGMENT_TRANSITION_DURATION)
    }

    companion object {
        private const val FRAGMENT_TRANSITION_DURATION = 300L
    }
}