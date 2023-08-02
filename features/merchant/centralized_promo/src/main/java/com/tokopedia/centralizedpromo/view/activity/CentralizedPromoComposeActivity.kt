package com.tokopedia.centralizedpromo.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.centralizedpromo.compose.CentralizedPromoScreen
import com.tokopedia.centralizedpromo.di.component.DaggerCentralizedPromoComponent
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.nest.principles.ui.NestTheme
import javax.inject.Inject

class CentralizedPromoComposeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[CentralizedPromoComposeViewModel::class.java]
    }

    private fun injectComponent() {
        DaggerCentralizedPromoComponent.builder()
            .baseAppComponent((this.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)

        setContent {
            NestTheme {
                val uiState = viewModel.layoutList.collectAsState().value
                CentralizedPromoScreen(
                    uiState = uiState,
                    onEvent = viewModel::sendEvent,
                    checkRbac = {
                        viewModel.getKeyRBAC(it)
                    },
                    onBackPressed = {
                        finish()
                    }
                )
            }
        }
    }

}