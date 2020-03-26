package com.tokopedia.shop.setting.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopPageSettingViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var getWhitelistUseCase: GetWhitelistUseCase

    @RelaxedMockK
    lateinit var getShopReputationUseCase: GetShopReputationUseCase

    protected lateinit var viewModel: ShopPageSettingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(GQLGetShopInfoUseCase)

        viewModel = ShopPageSettingViewModel(
                gqlRepository,
                userSessionInterface,
                getShopInfoUseCase,
                getWhitelistUseCase,
                getShopReputationUseCase,
                Dispatchers.Unconfined
        )
    }
}