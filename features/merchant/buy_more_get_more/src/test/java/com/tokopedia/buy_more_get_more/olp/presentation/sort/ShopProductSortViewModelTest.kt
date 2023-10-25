package com.tokopedia.buy_more_get_more.olp.presentation.sort

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.buy_more_get_more.sort.domain.GetProductSortUseCase
import com.tokopedia.buy_more_get_more.sort.mapper.ShopProductSortMapper
import com.tokopedia.buy_more_get_more.sort.model.ShopStickySortFilter
import com.tokopedia.buy_more_get_more.sort.viewmodel.ShopProductSortViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopProductSortViewModelTest {

    private lateinit var viewModel: ShopProductSortViewModel

    @RelaxedMockK
    lateinit var gqlGetShopSortUseCase: GetProductSortUseCase

    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper

    @RelaxedMockK
    lateinit var shopSortListDataObserver: Observer<in Result<ShopStickySortFilter>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopProductSortViewModel(
            gqlGetShopSortUseCase,
            shopProductSortMapper,
            CoroutineTestDispatchersProvider
        )
        with(viewModel) {
            shopSortListData.observeForever(shopSortListDataObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            shopSortListData.removeObserver(shopSortListDataObserver)
        }
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
