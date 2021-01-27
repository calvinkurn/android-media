package com.tokopedia.shop_settings.presenter.shopsettingsnote

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.*
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListPresenter
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListReorderPresenter
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingsNoteAddEditPresenter
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSettingsNoteTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var addShopNoteUseCase: AddShopNoteUseCase

    @RelaxedMockK
    lateinit var updateShopNoteUseCase: UpdateShopNoteUseCase

    @RelaxedMockK
    lateinit var getShopNoteUseCase: GetShopNotesUseCase

    @RelaxedMockK
    lateinit var deleteShopNoteUseCase: DeleteShopNoteUseCase

    @RelaxedMockK
    lateinit var reorderShopNoteUseCase: ReorderShopNoteUseCase

    protected lateinit var shopSettingsNoteAddEditPresenter: ShopSettingsNoteAddEditPresenter
    protected lateinit var shopSettingsNoteListPresenter: ShopSettingNoteListPresenter
    protected lateinit var shopSettingsNoteListReorderPresenter: ShopSettingNoteListReorderPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(TextUtils::class)

        shopSettingsNoteAddEditPresenter = ShopSettingsNoteAddEditPresenter(addShopNoteUseCase, updateShopNoteUseCase)
        shopSettingsNoteListPresenter = ShopSettingNoteListPresenter(getShopNoteUseCase, deleteShopNoteUseCase)
        shopSettingsNoteListReorderPresenter = ShopSettingNoteListReorderPresenter(reorderShopNoteUseCase)
    }
}