package com.tokopedia.play.broadcaster.shorts.container

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsInjector
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.view.activity.BasePlayShortsActivity
import kotlinx.coroutines.Dispatchers
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

        lifecycleScope.launch(Dispatchers.Default) {
            delay(3000)
            withContext(Dispatchers.Main) {
                viewModel.submitAction(PlayShortsAction.SetMedia("123213"))
            }
        }
    }
}
