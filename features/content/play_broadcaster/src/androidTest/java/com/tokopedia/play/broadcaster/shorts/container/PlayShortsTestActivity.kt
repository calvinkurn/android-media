package com.tokopedia.play.broadcaster.shorts.container

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsInjector
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.view.activity.BasePlayShortsActivity
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
class PlayShortsTestActivity : BasePlayShortsActivity() {

    override fun inject() {
        PlayShortsInjector.get()?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTestObserver()
    }

    private fun setupTestObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                val prev = it.prevValue
                val curr = it.value

                if (prev?.config?.shortsId.isNullOrEmpty() && curr.config.shortsId.isNotEmpty() && curr.media.mediaUri.isEmpty()) {
                    viewModel.submitAction(PlayShortsAction.SetMedia("123"))
                }
            }
        }
    }
}
