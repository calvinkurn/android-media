package com.tokopedia.home_account.ui.fundsAndInvestment.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FundsAndInvestmentSection(
    userId: String,
    title: String,
    list: List<WalletUiModel>,
    textStyle: TextStyle = NestTheme.typography.heading2.copy(
        fontWeight = FontWeight.Bold
    ),
    onItemClicked: (WalletUiModel) -> Unit
) {
    if (list.isNotEmpty()) {
        Column {
            NestTypography(
                text = title,
                textStyle = textStyle,
                modifier = Modifier.padding(horizontal = dp_16)
            )

            LazyColumn {
                items(list, key = { it.id }) {item ->
                    if (item.isLoading) {
                        ItemShimmer()
                    } else {
                        ItemList(
                            modifier = Modifier.animateItemPlacement(),
                            userId = userId,
                            item = item,
                            onItemClicked = {onItemClicked(it)}
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun SectionPreview() {
    NestTheme {
        FundsAndInvestmentSection(
            userId = "12345",
            title = "Saldo & Points",
            list = listOf(
                WalletUiModel(
                    id = "1",
                    title = "Test Title 1",
                    subtitle = "Test Subtitle 1",
                    isFailed = false,
                    isActive = true,
                    isVertical = true,
                    hideTitle = false
                ),
                WalletUiModel(
                    id = "2",
                    title = "Test Title 2",
                    subtitle = "Test Subtitle 2",
                    isFailed = false,
                    isActive = true,
                    isVertical = true,
                    hideTitle = false,
                    statusName = "",
                    isLoading = true
                ),
                WalletUiModel(
                    id = "3",
                    title = "Test Title 3",
                    subtitle = "Test Subtitle 3",
                    isFailed = false,
                    isActive = true,
                    isVertical = true,
                    hideTitle = false,
                )
            )
        ) {}
    }
}

