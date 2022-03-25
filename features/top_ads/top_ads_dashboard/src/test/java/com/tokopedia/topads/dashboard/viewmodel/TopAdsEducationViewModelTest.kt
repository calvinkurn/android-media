package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.raw.articlesJson
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
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

    @Test
    fun `fetchArticles method should parse articlesJson in ListArticle`() = runBlockingTest{

        val expectedObj = Gson().fromJson(articlesJson,ListArticle::class.java)
        viewModel.fetchArticles()

        assertEquals((viewModel.articlesLiveData.value as Success).data , expectedObj)
    }
}