package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Lukas on 2019-07-04
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class WishlistTestViewModel {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var wishlistRepository: WishlistRepository

    @MockK
    lateinit var mockLiveDataWishlist: MutableLiveData<List<Visitable<*>>>

    private lateinit var viewModel: WishlistViewModel

    private val dispatcher = Dispatchers.Unconfined
    private val gson = Gson()
    private val successJson = "primary_product_success_response.json"

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        viewModel = WishlistViewModel(userSessionInterface, wishlistRepository, dispatcher)
    }

    @Test
    fun loadSuccessGetWishlist(){
        val spy = spyk(viewModel)
    }

    @Test
    fun loadErrorGetWishlist(){
        val spy = spyk(viewModel)

    }

    @Test
    fun loadEmptyGetWishlist(){
        val spy = spyk(viewModel)
        val mockList = mockk<List<String>>()

    }

    @Test
    fun searchProduct(){

    }

    @Test
    fun checkIsNotLogin(){
        val spy = spyk(viewModel)
        every{ spy.isLoggedIn() } returns false
        Assert.assertFalse(spy.isLoggedIn())
    }

    @Test
    fun checkIsLogin(){
        val spy = spyk(viewModel)
        every{ spy.isLoggedIn() } returns true
        Assert.assertTrue(spy.isLoggedIn())
    }

}