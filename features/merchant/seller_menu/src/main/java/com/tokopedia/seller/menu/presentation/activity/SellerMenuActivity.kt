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
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SellerMenuActivity: BaseSellerMenuActivity() {

    @Inject
    lateinit var viewModel: SellerMenuViewModel

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
                    viewModel.uiState.collectLatest { state ->
                        when (state) {
                            is SellerMenuUIState.OnSuccessGetMenuList -> {
                                loadMenuList(state.visitableList)
                            }
                            else -> {

                            }
                        }
                    }
                })

                val state = viewModel.uiState.collectAsState()

                SellerMenuContent(uiState = state.value)
            }
        }
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun loadMenuList(menuList: List<SellerMenuComposeItem>) {

    }

}
