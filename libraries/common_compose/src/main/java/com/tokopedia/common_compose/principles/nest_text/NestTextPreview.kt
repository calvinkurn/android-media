package com.tokopedia.common_compose.principles.nest_text

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
internal fun NestTextRegularPreview() {

    NestTextFontConfig.isFontTypeOpenSauceOne = false

    LazyColumn(modifier = Modifier
        .background(colorResource(unifyR.color.Unify_Background))
    ) {
        item {
            NestText(
                text = "Regular",
                type = NestTextType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }
        items(NestTextType.values()) {
            Column {
                NestText(
                    text = it.name,
                    type = it
                )

                NestText(
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
internal fun NestTextBoldPreview() {
    NestTextFontConfig.isFontTypeOpenSauceOne = false

    LazyColumn(modifier = Modifier
        .background(colorResource(unifyR.color.Unify_Background))
    ) {
        item {
            NestText(
                text = "Bold",
                type = NestTextType.Heading1,
                textStyle = TextStyle(color = colorResource(id = unifyR.color.Unify_G500))
            )
        }
        items(NestTextType.values()) {
            Column {
                NestText(
                    text = it.name,
                    type = it,
                    weight = NestTextWeight.Bold
                )

                NestText(
                    text = it.name,
                    type = it,
                    weight = NestTextWeight.Bold,
                    isFontTypeOpenSauceOne = true
                )
            }

            Divider()
        }
    }
}