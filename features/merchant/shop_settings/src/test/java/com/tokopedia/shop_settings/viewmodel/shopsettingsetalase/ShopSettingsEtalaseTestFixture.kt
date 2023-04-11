package com.tokopedia.shop_settings.viewmodel.shopsettingsetalase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.AddShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.UpdateShopEtalaseUseCase
import com.tokopedia.shop.settings.etalase.view.viewmodel.ShopSettingsEtalaseAddEditViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSettingsEtalaseTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var addShopEtalaseUseCase: AddShopEtalaseUseCase

    @RelaxedMockK
    lateinit var updateShopEtalaseUseCase: UpdateShopEtalaseUseCase

    protected lateinit var shopSettingsEtalaseAddEditViewModel: ShopSettingsEtalaseAddEditViewModel
    protected lateinit var userSession: UserSessionInterface

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val context = mockk<Context>(relaxed = true)
        userSession = UserSession(context)

        shopSettingsEtalaseAddEditViewModel = ShopSettingsEtalaseAddEditViewModel(
            addShopEtalaseUseCase,
            updateShopEtalaseUseCase,
            getShopEtalaseUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )
    }
}
