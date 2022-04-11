package com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before

open class ReviewImagePlayerViewModelTestFixture {
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ReviewImagePlayerViewModel

    @Before
    fun setUp() {
        viewModel = ReviewImagePlayerViewModel(coroutineDispatchers)
    }
}