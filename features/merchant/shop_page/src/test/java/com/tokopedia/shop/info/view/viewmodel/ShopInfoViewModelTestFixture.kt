package com.tokopedia.shop.info.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopInfoViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getShopNotesUseCase: GetShopNotesByShopIdUseCase
    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase
    @RelaxedMockK
    lateinit var getShopStatisticsUseCase: GetShopStatisticUseCase
    @RelaxedMockK
    lateinit var getShopReputationUseCase: GetShopReputationUseCase
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    protected lateinit var viewModel: ShopInfoViewModel

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }
    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ShopInfoViewModel(
                userSessionInterface,
                getShopNotesUseCase,
                getShopInfoUseCase,
                getShopStatisticsUseCase,
                getShopReputationUseCase,
                testCoroutineDispatcherProvider
        )
    }
}