package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.*
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingEtalaseListPresenter
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingEtalaseListReorderPresenter
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingsEtalaseAddEditPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
    lateinit var deleteShopEtalaseUseCase: DeleteShopEtalaseUseCase

    @RelaxedMockK
    lateinit var reorderShopEtalaseUseCase: ReorderShopEtalaseUseCase

    @RelaxedMockK
    lateinit var addShopEtalaseUseCase: AddShopEtalaseUseCase

    @RelaxedMockK
    lateinit var updateShopEtalaseUseCase: UpdateShopEtalaseUseCase

    protected lateinit var shopSettingsEtalaseListPresenter: ShopSettingEtalaseListPresenter
    protected lateinit var shopSettingsEtalaseListReorderPresenter: ShopSettingEtalaseListReorderPresenter
    protected lateinit var shopSettingsEtalaseAddEditPresenter: ShopSettingsEtalaseAddEditPresenter
    protected lateinit var userSession: UserSessionInterface

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val context = mockk<Context>()
        userSession = UserSession(context)

        shopSettingsEtalaseListPresenter = ShopSettingEtalaseListPresenter(
                getShopEtalaseUseCase,
                deleteShopEtalaseUseCase
        )

        shopSettingsEtalaseListReorderPresenter = ShopSettingEtalaseListReorderPresenter(
                reorderShopEtalaseUseCase
        )

        shopSettingsEtalaseAddEditPresenter = ShopSettingsEtalaseAddEditPresenter(
                addShopEtalaseUseCase,
                updateShopEtalaseUseCase,
                getShopEtalaseUseCase,
                userSession
        )
    }

}