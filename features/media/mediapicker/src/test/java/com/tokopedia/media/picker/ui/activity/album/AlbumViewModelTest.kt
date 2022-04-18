package com.tokopedia.media.picker.ui.activity.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val albumRepository = mockk<AlbumRepository>()

    private val viewModel = AlbumViewModel(
        albumRepository,
        coroutineScopeRule.dispatchers
    )

    @Test
    fun `get device album list`() {
        // When
        coEvery { albumRepository.invoke(Unit) } returns albumCollection

        viewModel.fetch()
        val result = viewModel.albums.value

        // Then
        assertEquals(albumCollection.first().name, result?.first()?.name)
        assertEquals(albumCollection.size, result?.size)
    }

    @Test
    fun `check loading state flow`() {
        // When
        coEvery { albumRepository.invoke(Unit) } returns albumCollection
        viewModel.fetch()

        // Then
        assert(viewModel.isLoading.value != null)
    }

    companion object {
        val albumCollection = listOf(
            Album(0, "album name 0", count = 0),
            Album(1, "album name 1", count = 1),
            Album(2, "album name 2", count = 2),
            Album(3, "album name 3", count = 3),
            Album(4, "album name 4", count = 4)
        )
    }
}