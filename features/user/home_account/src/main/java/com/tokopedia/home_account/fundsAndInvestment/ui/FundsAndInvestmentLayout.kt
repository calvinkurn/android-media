package com.tokopedia.home_account.fundsAndInvestment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.home_account.R
import com.tokopedia.home_account.fundsAndInvestment.FundsAndInvestmentResult
import com.tokopedia.home_account.fundsAndInvestment.isRefreshData
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.nest.principles.ui.NestTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FundsAndInvestmentLayout(
    uiState: LiveData<FundsAndInvestmentResult>,
    onItemClicked: (WalletUiModel) -> Unit,
    onBackClicked: () -> Unit,
    onReloadData: (Boolean) -> Unit
) {
    val uiStateObserver = uiState.observeAsState()
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = uiStateObserver.value.let { it.isRefreshData() },
                onRefresh = { onReloadData(true) }
            )

            Scaffold(
                topBar = {
                    NestHeader(
                        type = NestHeaderType.SingleLine(
                            title = "",
                            onBackClicked = { onBackClicked() }
                        )
                    )
                },
                content = { padding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .pullRefresh(pullRefreshState)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            when(val state = uiStateObserver.value) {
                                is FundsAndInvestmentResult.Loading -> {
                                    ShimmerLayout()
                                }
                                is FundsAndInvestmentResult.Recomposition -> {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FundsAndInvestmentSection(
                                        title = LocalContext.current.getString(R.string.funds_and_investment_balance_and_points),
                                        list = state.listVertical
                                    ) {
                                        onItemClicked(it)
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    FundsAndInvestmentSection(
                                        title = LocalContext.current.getString(R.string.funds_and_investment_try_another),
                                        list = state.listHorizontal,
                                        textStyle = NestTheme.typography.heading3.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        onItemClicked(it)
                                    }
                                }
                                is FundsAndInvestmentResult.Failed -> {
                                    FailedLayout {
                                        onReloadData(false)
                                    }
                                }
                                else -> {}
                            }
                        }

                        PullRefreshIndicator(
                            refreshing = uiStateObserver.value.let { it.isRefreshData() },
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter),
                            contentColor = NestTheme.colors.GN._500
                        )
                    }
                },
            )
        }
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun FundsAndInvestmentLayoutLoadingPreview() {
    FundsAndInvestmentLayout(
        uiState = MutableLiveData(FundsAndInvestmentResult.Loading(true)),
        onItemClicked = {},
        onBackClicked = {},
        onReloadData = {}
    )
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun FundsAndInvestmentLayoutSuccessPreview() {
    FundsAndInvestmentLayout(
        uiState = MutableLiveData(
            FundsAndInvestmentResult.Recomposition(
                listVertical = listOf(
                    WalletUiModel(
                        id = "1",
                        title = "Test Title 1",
                        subtitle = "Test Subtitle 1",
                        isFailed = true,
                        isActive = true,
                        isVertical = true,
                        hideTitle = false
                    )
                ),
                listHorizontal = listOf(
                    WalletUiModel(
                        id = "1",
                        title = "Test Title 1",
                        subtitle = "Test Subtitle 1",
                        isFailed = true,
                        isActive = true,
                        isVertical = false,
                        hideTitle = false
                    )
                )
            )
        ),
        onItemClicked = {},
        onBackClicked = {},
        onReloadData = {}
    )
}


@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun FundsAndInvestmentLayoutFailedPreview() {
    FundsAndInvestmentLayout(
        uiState = MutableLiveData(FundsAndInvestmentResult.Failed(Throwable())),
        onItemClicked = {},
        onBackClicked = {},
        onReloadData = {}
    )
}
