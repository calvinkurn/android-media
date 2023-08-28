package com.tokopedia.minicart.bmgm.presentation.viewmodel

import com.tokopedia.minicart.bmgm.domain.usecase.GetBmgmMiniCartDataUseCase
import com.tokopedia.minicart.bmgm.domain.usecase.LocalCacheUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

/**
 * Created by @ilhamsuaib on 28/08/23.
 */
internal class BmgmMiniCartViewModelTest : BaseCartCheckboxViewModelTest() {

    @RelaxedMockK
    lateinit var getBmgmMiniCartDataUseCase: GetBmgmMiniCartDataUseCase

    @RelaxedMockK
    lateinit var localCacheUseCase: LocalCacheUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var viewModel: BmgmMiniCartViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initVariables() {
        super.initVariables()

        viewModel = BmgmMiniCartViewModel(
            { setCartListCheckboxStateUseCase },
            { getBmgmMiniCartDataUseCase },
            { localCacheUseCase },
            { userSession },
            { coroutineTestRule.dispatchers },
        )
    }

    @Test
    fun `get mini cart data with loading state then return success result`() {

    }

    @Test
    fun `get mini cart data without loading state then return success result`() {

    }

    @Test
    fun `get mini cart data with loading state then return failed result`() {

    }

    @Test
    fun `get mini cart data without loading state then return failed result`() {

    }

    @Test
    fun clearCartDataLocalCache() {
    }

    fun storeCartDataToLocalCache() {
    }
}