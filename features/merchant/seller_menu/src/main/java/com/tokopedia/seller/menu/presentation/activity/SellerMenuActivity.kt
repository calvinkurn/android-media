package com.tokopedia.seller.menu.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.component.SellerMenuContent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SellerMenuActivity: BaseSellerMenuActivity() {

    @Inject
    lateinit var viewModel: SellerMenuViewModel

    private var shopAge: Long = 0L
    private var isNewSeller: Boolean = false

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)

        setContent {
            NestTheme {
                LaunchedEffect(key1 = false, block = {
                    viewModel.uiEvent.collectLatest { state ->
                        when (state) {
                            is SellerMenuUIEvent.OnSuccessGetShopInfoUse -> {
                                shopAge = state.shopAge
                                isNewSeller = state.isNewSeller
                                viewModel.getAllSettingShopInfo(false)
                            }
                            else -> {

                            }
                        }
                    }
                })

                val state = viewModel.uiState.collectAsState()

                SellerMenuContent(
                    uiState = state.value,
                    onSuccessLoadInitialState = ::onSuccessLoadInitialState
                )
            }
        }
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun onSuccessLoadInitialState() {
        viewModel.getShopAccountInfo()
    }

}
