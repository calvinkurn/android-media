package com.tokopedia.centralizedpromo.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.centralizedpromo.R.string
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.compose.CentralizedPromoScreen
import com.tokopedia.centralizedpromo.compose.ShowToaster
import com.tokopedia.centralizedpromo.di.component.DaggerCentralizedPromoComponent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.CoachMarkShown
import com.tokopedia.centralizedpromo.view.model.CoachMarkAnchorWithKey
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel.Companion.PAGE_ID_SHOP_COUPON
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.nest.components.CoachMarkItem
import com.tokopedia.nest.components.CoachMarkPosition.TOP
import com.tokopedia.nest.components.NestCoachMark
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CentralizedPromoComposeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

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
                val coachMarkAnchor = remember {
                    mutableStateOf(CoachMarkAnchorWithKey())
                }

                // Don't use mutableStateOf because
                // we don't want to recomposition happen when this value change
                val alreadyShowCoachMark = remember(Unit) {
                    viewModel.getCoachmarkSharedPref(PAGE_ID_SHOP_COUPON)
                }

                CentralizedPromoScreenViewed()
                ShowToaster(viewModel)

                CentralizedPromoScreen(
                    uiState = uiState,
                    onEvent = viewModel::sendEvent,
                    checkRbac = {
                        viewModel.getKeyRBAC(it)
                    },
                    coachMarkAnchor = { anchor, key ->
                        coachMarkAnchor.value = CoachMarkAnchorWithKey(
                            anchor, key
                        )
                    },
                    enableCoachMark = !alreadyShowCoachMark,
                    onBackPressed = {
                        finish()
                    }
                )

                if (!alreadyShowCoachMark) {
                    CentralizePromoCoachMark(coachMarkAnchor)
                }
            }
        }
    }

    @Composable
    private fun CentralizePromoCoachMark(
        coachMarkAnchor: MutableState<CoachMarkAnchorWithKey>
    ) {
        val coachMarkVisible by remember { derivedStateOf { coachMarkAnchor.value.anchor.y > 0 } }

        LaunchedEffect(coachMarkVisible) {
            if (coachMarkVisible) {
                viewModel.sendEvent(CoachMarkShown(coachMarkAnchor.value.key))
            }
        }

        NestCoachMark(
            visible = coachMarkVisible,
            item = CoachMarkItem(
                stringResource(string.centralize_promo_flash_sale_title_coachmark),
                stringResource(string.centralize_promo_flash_sale_desc_coachmark),
                coachMarkAnchor.value.anchor,
                TOP
            ),
            onCloseIconClick = {
            }
        )
    }

    @Composable
    private fun CentralizedPromoScreenViewed() {
        LaunchedEffect(Unit) {
            CentralizedPromoTracking.sendOpenScreenEvent(
                userSession.isLoggedIn,
                userSession.userId
            )
        }
    }

}