package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun SubExploreCategoryItem(
    modifier: Modifier = Modifier,
    subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
    onUiEvent: (ExploreCategoryUiEvent) -> Unit,
    actualPosition: Int,
    categoryName: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onUiEvent(
                    ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked(
                        subExploreCategoryUiModel = subExploreCategoryUiModel,
                        position = actualPosition,
                        categoryName = categoryName
                    )
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestImage(
            source = ImageSource.Remote(
                subExploreCategoryUiModel.imageUrl,
                shouldRetried = true
            ),
            type = NestImageType.Rect(12.dp),
            modifier = Modifier.size(42.dp),
            contentDescription = null
        )
        NestTypography(
            text = subExploreCategoryUiModel.name,
            textStyle = NestTheme.typography.display3,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
