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
    view: StoriesAvatarView,
    listener: Listener
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
                    }
                }

                launch {
                    _shopId.flatMapLatest {
                        viewModel.getStoriesMessage(it)
                    }.collect { message ->
                        if (message == null) return@collect

                        when (message) {
                            is StoriesAvatarMessage.ShowCoachMark -> {
                                listener.onShowCoachMark(this@StoriesAvatarObserver, view)
                            }
                        }

                        viewModel.clearMessage(message.id)
                    }
                }
            }
        }
    }

    fun observe(shopId: String) {
        _shopId.value = shopId
    }

    internal interface Listener {
        fun onShowCoachMark(observer: StoriesAvatarObserver, view: StoriesAvatarView)
    }
}
