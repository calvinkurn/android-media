package com.tokopedia.shop.sort.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.shop.common.graphql.domain.usecase.shopsort.GqlGetShopSortUseCase
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopProductSortViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var gqlGetShopSortUseCase: GqlGetShopSortUseCase

    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private var viewModel: ShopProductSortViewModel? = null

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopProductSortViewModel(
            gqlGetShopSortUseCase,
            shopProductSortMapper,
            testCoroutineDispatcherProvider,
        )
    }

    @Test
    fun `when getShopSortListData is success, then shopSortListData should emit success value`() {
        coEvery {
            gqlGetShopSortUseCase.executeOnBackground()
        } returns listOf()
        viewModel?.getShopSortListData()
        val liveDataValue = viewModel?.shopSortListData?.value
        assert(liveDataValue is Success)
    }

    @Test
    fun `when getShopSortListData is error, then shopSortListData should emit fail value`() {
        coEvery {
            gqlGetShopSortUseCase.executeOnBackground()
        } throws Exception()
        viewModel?.getShopSortListData()
        val liveDataValue = viewModel?.shopSortListData?.value
        assert(liveDataValue is Fail)
    }

    @Test
    fun `when error while processing response after getting data from getShopSortListData, then shopSortListData should emit fail value`() {
        val observer = mockk<Observer<Result<ShopStickySortFilter>>>()
        viewModel?.shopSortListData?.observeForever(observer)
        coEvery {
            gqlGetShopSortUseCase.executeOnBackground()
        } returns listOf()
        coEvery { observer.onChanged(any<Success<ShopStickySortFilter>>()) } throws Exception()
        viewModel?.getShopSortListData()
        val liveDataValue = viewModel?.shopSortListData?.value
        assert(liveDataValue is Fail)
    }

}
