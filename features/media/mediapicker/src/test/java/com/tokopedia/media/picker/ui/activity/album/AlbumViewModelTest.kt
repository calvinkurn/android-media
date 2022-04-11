package com.tokopedia.media.picker.ui.activity.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers
    private val albumRepository = mockk<AlbumRepository>()
    private val viewModel: AlbumViewModel = AlbumViewModel(
        albumRepository,
        testDispatcher
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(CoroutineTestDispatchers.main)
    }

    @ExperimentalCoroutinesApi
    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get device album list`() {
        // Given

        // When
        coEvery { albumRepository.invoke(Unit) } returns albumCollection

        viewModel.fetch()
        val result = viewModel.albums.value

        // Then
        assertEquals(albumCollection[3].name, result?.get(3)?.name)
        assertEquals(albumCollection.size, result?.size)
    }

    @Test
    fun `check loading state flow`() {
        // Given

        // When
        coEvery { albumRepository.invoke(Unit) } returns albumCollection
        viewModel.fetch()

        // Then
        assert(viewModel.isLoading.value != null)
    }

    companion object{
        val albumCollection = listOf(
            Album(12, "album name 1", count = 2),
            Album(13, "album name 2", count = 3),
            Album(14, "album name 3", count = 4),
            Album(15, "album name 4", count = 5),
            Album(16, "album name 5", count = 6)
        )
    }
}