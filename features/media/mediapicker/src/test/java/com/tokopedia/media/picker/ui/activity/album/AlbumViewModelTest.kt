package com.tokopedia.media.picker.ui.activity.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.repository.BucketAlbumRepository
import com.tokopedia.media.util.test
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class AlbumViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val albumRepository = mockk<BucketAlbumRepository>(relaxed = true)

    private val viewModel = AlbumViewModel(
        albumRepository,
        dispatcher
    )

    @Test
    fun `it should be get album list correctly`() = runBlocking {
        // given
        every { albumRepository() } returns flow {
            emit(mockAlbumList)
        }

        // when
        viewModel.getAlbums()

        // then
        viewModel.getAlbums().test {
            assert(mockAlbumList.size == awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `it should be get empty album list correctly`() = runBlocking {
        // given
        every { albumRepository() } returns flow {
            emit(emptyList<Album>())
        }

        // when
        viewModel.getAlbums()

        // then
        viewModel.getAlbums().test {
            assert(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loading state should be invoke when fetch album data`() = runBlocking {
        // given
        every { albumRepository() } returns flow {
            emit(mockAlbumList)
        }

        // when
        viewModel.getAlbums()

        // then
        viewModel.getAlbums().test {
            assert(viewModel.isLoading.value != null)
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private val mockAlbumList = listOf(
            Album(0, "album 1", count = 0),
            Album(1, "album 2", count = 1),
        )
    }
}
