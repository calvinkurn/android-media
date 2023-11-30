package com.tokopedia.home_explore_category.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.globalerror.compose.NestGlobalError
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.R
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun ExploreCategoryListGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
    }
}

@Composable
fun ExploreCategoryItem(
    exploreCategoryUiModel: ExploreCategoryUiModel
) {
    Column {
        var isSelected by remember {
            mutableStateOf(exploreCategoryUiModel.isSelected)
        }

        NestCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(147.dp),
            enableBounceAnimation = true,
            type = NestCardType.Border
        ) {
            Column(
                modifier = Modifier.clickable {
                    isSelected = !isSelected
                }
            ) {
                NestTypography(
                    text = exploreCategoryUiModel.categoryTitle,
                    textStyle = NestTheme.typography.display3,
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
                NestImage(
                    source = ImageSource.Remote(
                        exploreCategoryUiModel.categoryImageUrl
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp),
                    contentDescription = null
                )
            }
        }

        if (isSelected) {
            LazyColumn {
            }
        }
    }
}

@Composable
fun SubExploreCategoryItem(
    subExploreCategoryUiModel:
        ExploreCategoryUiModel.SubExploreCategoryUiModel
) {
}

@Composable
fun ExploreCategoryGlobalError(globalErrorType: NestGlobalErrorType) {

    NestGlobalError(
        type = globalErrorType,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp),
        onClickAction = {

        },
        onClickSecondaryAction = {

        },
        secondaryActionText = if (globalErrorType == NestGlobalErrorType.NoConnection) {
             stringResource(id = R.string.secondary_title_go_to_setting)
        } else {
            null
        }
    )
}

@Preview
@Composable
fun ExploreCategoryScreenPreview() {
    ExploreCategoryGlobalError(NestGlobalErrorType.NoConnection)
}
