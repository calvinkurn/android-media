package com.tokopedia.privacycenter.sharingwishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistSharedViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SharingWishlistSharedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var viewModel: SharingWishlistSharedViewModel? = null

    @Before
    fun setup() {
        viewModel = SharingWishlistSharedViewModel(dispatcherProviderTest)
    }

    @Test
    fun `notify pager`() {
        var accessOptionValue = 0
        runBlocking {
            val collectorJob = launch {
                viewModel?.wishlistCollectionState?.collectLatest {
                    accessOptionValue = it
                }
            }

            viewModel?.notifyPager(0)
            collectorJob.cancel()

            val result = viewModel?.wishlistCollectionState?.first()
            assertEquals(accessOptionValue, result)
        }
    }
}
