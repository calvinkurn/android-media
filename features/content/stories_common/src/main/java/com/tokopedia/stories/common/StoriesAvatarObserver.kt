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
    view: StoriesAvatarView
) {

    private val _shopId = MutableStateFlow("")
    val shopId: String get() = _shopId.value

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _shopId.flatMapLatest {
                        viewModel.getStoriesState(it)
                    }.collectLatest { newState ->
                        view.setState { newState ?: it }
                    }
                }
            }
        }
    }

    fun observe(shopId: String) {
        _shopId.value = shopId
    }
}
