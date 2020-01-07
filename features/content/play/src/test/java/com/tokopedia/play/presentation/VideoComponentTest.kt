package com.tokopedia.play.presentation

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.ui.video.VideoComponent
import com.tokopedia.play.ui.video.VideoView
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.Test


/**
 * Created by mzennis on 2020-01-07.
 */
class VideoComponentTest {

    private lateinit var component: TestVideoComponent

    private val owner = mockk<LifecycleOwner>()
    private val coroutineScope = mockk<CoroutineScope>()

    @Before
    fun setup() {
        component = TestVideoComponent(mockk(), EventBusFactory.get(owner), coroutineScope)
    }

    @Test
    fun testSetVideo() {
    }
}

class TestVideoComponent(container: ViewGroup,
                         bus: EventBusFactory,
                         coroutineScope: CoroutineScope) : VideoComponent(container, bus, coroutineScope) {

    override fun initUiView(container: ViewGroup): VideoView {
        return mockk()
    }
}
