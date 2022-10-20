package com.tokopedia.common_compose.principles.nest_typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by yovi.putra on 10/10/22"
 * Project name: android-tokopedia-core
 **/

@Preview
@Composable
internal fun NestTypographyPreview() {
    LazyColumn(modifier = Modifier
        .background(colorResource(unifyR.color.Unify_Background))
    ) {
        item {
            NestTypography(
                text = "Regular",
                type = NestTypographyType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }

        items(NestTypographyType.values()) {
            Column {
                NestTypography(
                    text = it.name,
                    type = it
                )

                NestTypography(
                    text = it.name,
                    type = it,
                    isFontTypeOpenSauceOne = false
                )
            }

            Divider()
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            NestTypography(
                text = "Bold",
                type = NestTypographyType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }

        items(NestTypographyType.values()) {
            Column {
                NestTypography(
                    text = it.name,
                    type = it,
                    weight = NestTypographyWeight.Bold
                )

                NestTypography(
                    text = it.name,
                    type = it,
                    weight = NestTypographyWeight.Bold,
                    isFontTypeOpenSauceOne = false
                )
            }

            Divider()
        }
    }
}