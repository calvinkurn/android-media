package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.raw.articlesJson
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsEducationViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopAdsEducationViewModel

    @Before
    fun setUp() {
        viewModel = TopAdsEducationViewModel()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchArticles success test`() = runBlockingTest {

        val expectedObj = ListArticle()

        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(articlesJson, ListArticle::class.java)
        } returns expectedObj

        viewModel.fetchArticles()
        assertEquals((viewModel.articlesLiveData.value as Success).data, expectedObj)
    }

    @Test
    fun `fetchArticles onError block test`() = runBlockingTest {

        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(articlesJson, ListArticle::class.java)
        } answers {
            throw Exception()
        }

        viewModel.fetchArticles()
        assert(viewModel.articlesLiveData.value is Fail)
    }
}