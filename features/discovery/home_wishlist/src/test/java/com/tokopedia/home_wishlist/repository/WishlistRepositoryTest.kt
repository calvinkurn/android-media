package com.tokopedia.home_wishlist.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class RepoRepositoryTest {
    private lateinit var repository: WishlistRepository

    @MockK
    private lateinit var graphqlRepository: GraphqlRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repository = WishlistRepository(graphqlRepository, any())
    }

    @Test
    fun loadRepoFromNetwork() = runBlocking{
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(), null, false)
        val observer = mockk<Observer<List<Visitable<*>>>>()

        coVerify { graphqlRepository wasNot Called }
        com.nhaarman.mockitokotlin2.verify(observer).onChanged(listOf(any()))

        com.nhaarman.mockitokotlin2.verify(observer).onChanged(listOf(any()))
    }

    @Test
    fun searchNextPage_null() {
//        `when`(dao.findSearchResult("foo")).thenReturn(null)
//        val observer = mock<Observer<Resource<Boolean>>>()
//        repository.searchNextPage("foo").observeForever(observer)
//        verify(observer).onChanged(null)
    }

    @Test
    fun search_fromServer() {
//        val ids = arrayListOf(1, 2)
//        val repo1 = TestUtil.createRepo(1, "owner", "repo 1", "desc 1")
//        val repo2 = TestUtil.createRepo(2, "owner", "repo 2", "desc 2")
//
//        val observer = mock<Observer<Resource<List<Repo>>>>()
//        val dbSearchResult = MutableLiveData<RepoSearchResult>()
//        val repositories = MutableLiveData<List<Repo>>()
//
//        val repoList = arrayListOf(repo1, repo2)
//        val apiResponse = RepoSearchResponse(2, repoList)
//
//        val callLiveData = MutableLiveData<ApiResponse<RepoSearchResponse>>()
//        `when`(service.searchRepos("foo")).thenReturn(callLiveData)
//
//        `when`(dao.getWishlistData("foo")).thenReturn(dbSearchResult)
//
//        repository.getWishlistData("foo").observeForever(observer)
//
//        verify(observer).onChanged(Resource.loading(null))
//        verifyNoMoreInteractions(service)
//        reset(observer)
//
//        `when`(dao.loadOrdered(ids)).thenReturn(repositories)
//        dbSearchResult.postValue(null)
//        verify(dao, never()).loadOrdered(anyList())
//
//        verify(service).searchRepos("foo")
//        val updatedResult = MutableLiveData<RepoSearchResult>()
//        `when`(dao.getWishlistData("foo")).thenReturn(updatedResult)
//        updatedResult.postValue(RepoSearchResult("foo", ids, 2, null))
//
//        callLiveData.postValue(ApiResponse.create(Response.success(apiResponse)))
//        verify(dao).insertRepos(repoList)
//        repositories.postValue(repoList)
//        verify(observer).onChanged(Resource.success(repoList))
//        verifyNoMoreInteractions(service)
    }

    @Test
    fun search_fromServer_error() {
//        `when`(dao.getWishlistData("foo")).thenReturn(AbsentLiveData.create())
//        val apiResponse = MutableLiveData<ApiResponse<RepoSearchResponse>>()
//        `when`(service.searchRepos("foo")).thenReturn(apiResponse)
//
//        val observer = mock<Observer<Resource<List<Repo>>>>()
//        repository.getWishlistData("foo").observeForever(observer)
//        verify(observer).onChanged(Resource.loading(null))
//
//        apiResponse.postValue(ApiResponse.create(Exception("idk")))
//        verify(observer).onChanged(Resource.error("idk", null))
    }
}
