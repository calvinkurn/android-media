package com.tokopedia.shop_settings.presenter.shopsettingsaddress

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.AddShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.DeleteShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.GetShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.UpdateShopLocationUseCase
import com.tokopedia.shop.settings.address.presenter.ShopLocationOldPresenter
import com.tokopedia.shop.settings.address.presenter.ShopSettingAddressAddEditPresenter
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSettingsAddressTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var addShopLocationUseCase: AddShopLocationUseCase

    @RelaxedMockK
    lateinit var updateShopLocationUseCase: UpdateShopLocationUseCase

    @RelaxedMockK
    lateinit var getShopLocationUseCase: GetShopLocationUseCase

    @RelaxedMockK
    lateinit var deleteShopLocationUseCase: DeleteShopLocationUseCase

    protected lateinit var shopLocationPresenter: ShopLocationOldPresenter
    protected lateinit var shopSettingsAddressAddEditPresenter: ShopSettingAddressAddEditPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(TextUtils::class)

        shopLocationPresenter = ShopLocationOldPresenter(getShopLocationUseCase, deleteShopLocationUseCase)
        shopSettingsAddressAddEditPresenter = ShopSettingAddressAddEditPresenter(addShopLocationUseCase, updateShopLocationUseCase)
    }
}