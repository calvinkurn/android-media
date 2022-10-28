package com.tokopedia.media.picker.ui.activity.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AlbumViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val albumRepository = mockk<AlbumRepository>(relaxed = true)

    private val viewModel = AlbumViewModel(
        albumRepository,
        dispatcher
    )

    @Test
    fun `it should be get album list correctly`() {
        // given
        coEvery {
            albumRepository(Unit)
        } returns mockAlbumList

        // when
        viewModel.fetch()

        // then
        val result = viewModel.albums.value
        assert(mockAlbumList.size == result?.size)
    }

    @Test
    fun `loading state should be invoke when fetch album data`() {
        // given
        coEvery {
            albumRepository(Unit)
        } returns mockAlbumList

        // when
        viewModel.fetch()

        // then
        assert(viewModel.isLoading.value != null)
    }

    companion object {
        private val mockAlbumList = listOf(
            Album(0, "album 1", count = 0),
            Album(1, "album 2", count = 1),
        )
    }
}
