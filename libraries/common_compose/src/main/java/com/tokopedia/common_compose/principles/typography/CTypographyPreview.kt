package com.tokopedia.common_compose.principles.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by yovi.putra on 10/10/22"
 * Project name: android-tokopedia-core
 **/



@Preview
@Composable
fun CTypographyRegularPreview() {

    TypographyFontConfig.isFontTypeOpenSauceOne = false

    LazyColumn(modifier = Modifier
        .background(colorResource(unifyR.color.Unify_Background))
    ) {
        item {
            CTypography(
                text = "Regular",
                type = CTypographyType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }
        items(CTypographyType.values()) {
            Column {
                CTypography(
                    text = it.name,
                    type = it
                )

                CTypography(
                    text = it.name,
                    type = it,
                    isFontTypeOpenSauceOne = true
                )
            }

            Divider()
        }
    }
}

@Preview
@Composable
fun CTypographyBoldPreview() {
    TypographyFontConfig.isFontTypeOpenSauceOne = false

    LazyColumn(modifier = Modifier
        .background(colorResource(unifyR.color.Unify_Background))
    ) {
        item {
            CTypography(
                text = "Bold",
                type = CTypographyType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }
        items(CTypographyType.values()) {
            Column {
                CTypography(
                    text = it.name,
                    type = it,
                    weight = CTypographyWeight.Bold
                )

                CTypography(
                    text = it.name,
                    type = it,
                    weight = CTypographyWeight.Bold,
                    isFontTypeOpenSauceOne = true
                )
            }

            Divider()
        }
    }
}