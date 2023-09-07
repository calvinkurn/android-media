package com.tokopedia.stories.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import io.mockk.coEvery
import org.junit.Rule
import org.junit.Test

class StoriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockViewModel: StoriesViewModel
    private lateinit var mockRepository: StoriesRepository

    @Test
    fun `when open stories from entry point and success fetch group and detail data`() {
        val bundle = Bundle().apply {
            putString(SHOP_ID, "123")
        }
        mockViewModel.submitAction(StoriesUiAction.SetInitialData(bundle))

        coEvery { mockRepository.getStoriesInitialData(any()) } returns StoriesUiModel()
    }

    @Test
    fun `when open stories from entry point and fail fetch group and detail data`() {

    }

}
