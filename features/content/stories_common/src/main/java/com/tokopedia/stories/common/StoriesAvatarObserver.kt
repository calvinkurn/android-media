package com.tokopedia.stories.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 28/07/23
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StoriesAvatarObserver(
    viewModel: StoriesAvatarViewModel,
    lifecycleOwner: LifecycleOwner,
    view: StoriesBorderLayout,
    animationStrategy: AnimationStrategy,
) {

    private val _shopId = MutableStateFlow("")

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _shopId.flatMapLatest {
                        viewModel.getStoriesState(it)
                    }.collectLatest { newState ->
                        view.setState { newState ?: it }

                        if (newState == null) return@collectLatest
                        if (newState.status != StoriesStatus.HasUnseenStories) return@collectLatest
                        if (animationStrategy.shouldPlayAnimation(newState.shopId)) {
                            view.startAnimation()
                            animationStrategy.onAnimate(newState.shopId)
                        }
                    }
                }
            }
        }
    }

    fun observe(shopId: String) {
        _shopId.value = shopId
    }
}
