package com.tokopedia.tokopedianow.recipebookmark.presentation.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.item.RecipeBookmarkShimmeringItem

@Preview
@Composable
fun RecipeBookmarkShimmeringLayout() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        RecipeBookmarkShimmeringItem()
        Spacer(modifier = Modifier.height(16.dp))
        RecipeBookmarkShimmeringItem()
    }
}
