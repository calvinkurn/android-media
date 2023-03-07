package com.tokopedia.privacycenter.sharingwishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.*
import com.tokopedia.privacycenter.domain.GetWishlistCollectionByIdUseCase
import com.tokopedia.privacycenter.domain.GetWishlistCollectionUseCase
import com.tokopedia.privacycenter.domain.UpdateWishlistCollectionUseCase
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistStateResult
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharingWishlistViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var viewModel: SharingWishlistViewModel? = null

    private var getWishlistCollectionUseCase = mockk<GetWishlistCollectionUseCase>(relaxed = true)
    private var getWishlistCollectionByIdUseCase = mockk<GetWishlistCollectionByIdUseCase>(relaxed = true)
    private var updateWishlistCollectionUseCase = mockk<UpdateWishlistCollectionUseCase>(relaxed = true)

    private var wishlistCollectionObserver = mockk<Observer<SharingWishlistStateResult<WishlistDataModel>>>(relaxed = true)
    private var wishlistCollectionByIdObserver = mockk<Observer<PrivacyCenterStateResult<WishlistBydIdDataModel>>>(relaxed = true)
    private var updateWishlistCollectionObserver = mockk<Observer<PrivacyCenterStateResult<UpdateWishlistDataModel>>>(relaxed = true)

    private var mockResponseWishlistCollection = WishlistDataModel(
        emptyWishlistImageUrl = "",
        isEmptyState = false,
        totalCollection = 2,
        collections = listOf(
            WishlistCollectionsDataModel(
                id = 1,
                access = 1,
                name = "Koleksi Sepatu",
                totalItem = 4,
                itemText = "Barang"
            ),
            WishlistCollectionsDataModel(
                id = 1,
                access = 1,
                name = "Koleksi Baju",
                totalItem = 4,
                itemText = "Barang"
            )
        )
    )

    @Before
    fun setup() {
        viewModel = SharingWishlistViewModel(
            getWishlistCollectionUseCase,
            getWishlistCollectionByIdUseCase,
            updateWishlistCollectionUseCase,
            dispatcherProviderTest
        )

        viewModel?.wishlistCollection?.observeForever(wishlistCollectionObserver)
        viewModel?.wishlistCollectionById?.observeForever(wishlistCollectionByIdObserver)
        viewModel?.updateWishlist?.observeForever(updateWishlistCollectionObserver)
    }

    @After
    fun tearDown() {
        viewModel?.wishlistCollection?.removeObserver(wishlistCollectionObserver)
        viewModel?.wishlistCollectionById?.removeObserver(wishlistCollectionByIdObserver)
        viewModel?.updateWishlist?.removeObserver(updateWishlistCollectionObserver)
    }

    @Test
    fun `get wishlist collection - render collection`() {
        coEvery {
            getWishlistCollectionUseCase(any())
        } returns SharingWishlistStateResult.RenderCollection(mockResponseWishlistCollection)

        viewModel?.getWishlistCollections(1)

        val result = viewModel?.wishlistCollection?.getOrAwaitValue()
        assertTrue(result is SharingWishlistStateResult.RenderCollection)
        result.apply {
            assertTrue(this.data.collections.isNotEmpty())
            assertTrue(!this.data.isEmptyState)
        }
    }

    @Test
    fun `get wishlist collection - empty collection`() {
        mockResponseWishlistCollection = WishlistDataModel(
            isEmptyState = true,
            collections = listOf()
        )

        coEvery {
            getWishlistCollectionUseCase(any())
        } returns SharingWishlistStateResult.CollectionEmpty("")

        viewModel?.getWishlistCollections(1)

        val result = viewModel?.wishlistCollection?.getOrAwaitValue()
        assertTrue(result is SharingWishlistStateResult.CollectionEmpty)
    }

    @Test
    fun `get wishlist collection - fail`() {
        coEvery {
            getWishlistCollectionUseCase(any())
        } throws Throwable("Opss!")

        viewModel?.getWishlistCollections(1)

        val result = viewModel?.wishlistCollection?.getOrAwaitValue()
        assertTrue(result is SharingWishlistStateResult.Fail)
    }

    @Test
    fun `get wishlist collection by id - success`() {
        val collectionId = 1
        val mockResponseWishlistCollectionById = WishlistBydIdDataModel(
            ticker = WishlistByIdTickerDataModel(
                title = "Test",
                descriptions = listOf(
                    "Description 1",
                    "Description 2",
                    "Description 3"
                )
            ),
            collection = WishlistCollectionByIdDataModel(
                id = 1,
                name = "Koleksi Sepatu",
                access = 1
            )
        )

        coEvery {
            getWishlistCollectionByIdUseCase(any())
        } returns PrivacyCenterStateResult.Success(mockResponseWishlistCollectionById)

        viewModel?.getCollectionById(collectionId)

        val result = viewModel?.wishlistCollectionById?.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        result.apply {
            assertEquals(this.data.collection.id, collectionId)
        }
    }

    @Test
    fun `get wishlist collection by id - fail`() {
        val collectionId = 1

        coEvery {
            getWishlistCollectionByIdUseCase(any())
        } throws Throwable("Opss!")

        viewModel?.getCollectionById(collectionId)

        val result = viewModel?.wishlistCollectionById?.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
    }

    @Test
    fun `update wishlist collection - success`() {
        val mockResponse = UpdateWishlistDataModel(
            success = true,
            message = "Success"
        )

        coEvery {
            updateWishlistCollectionUseCase(any())
        } returns PrivacyCenterStateResult.Success(mockResponse)

        viewModel?.updateWishlist(WishlistCollectionByIdDataModel())

        val result = viewModel?.updateWishlist?.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        result.apply {
            assertTrue(this.data.success)
        }
    }

    @Test
    fun `update wishlist collection - failed`() {
        coEvery {
            updateWishlistCollectionUseCase(any())
        } throws Throwable("Opps!")

        viewModel?.updateWishlist(WishlistCollectionByIdDataModel())

        val result = viewModel?.updateWishlist?.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
    }
}
