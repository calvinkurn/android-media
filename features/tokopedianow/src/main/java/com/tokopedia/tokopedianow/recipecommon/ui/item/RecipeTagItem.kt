package com.tokopedia.tokopedianow.recipecommon.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipecommon.ui.model.TagUiModel

@Composable
fun RecipeTagItem(data: TagUiModel) {
    val tag = data.tag
    val shouldFormatTag = remember { data.shouldFormatTag }

    val text = if (shouldFormatTag) {
        stringResource(R.string.tokopedianow_recipe_other_label, tag.toIntSafely())
    } else {
        tag
    }

    NestLabel(
        modifier = Modifier.padding(start = 8.dp),
        labelType = NestLabelType.HIGHLIGHT_LIGHT_GREY,
        labelText = text
    )
}

@Preview
@Composable
private fun RecipeTagItemPreview() {
    RecipeTagItem(data = TagUiModel(tag = "Halal", shouldFormatTag = false))
}
