package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTips(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: CharSequence? = null,
    descriptionModifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = NestTheme.colors.NN._300),
        backgroundColor = NestTheme.colors.NN._50
    ) {
        Box {
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (8).dp, y = (-8).dp)) {
                Box(
                    modifier = Modifier
                        .width(40.dp).height(40.dp)
                        .background(NestTheme.colors.NN._100, shape = CircleShape)
                )
            }
            Icon(
                painter = painterResource(id = com.tokopedia.iconunify.R.drawable.iconunify_lightbulb),
                contentDescription = "tips icon",
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(end = 2.dp, top = 2.dp)
                    .height(24.dp),
                tint = NestTheme.colors.NN._300
            )
            Column(modifier = Modifier.padding(12.dp)) {
                title?.run {
                    NestTypography(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = this,
                        textStyle = NestTheme.typography.paragraph3.copy(
                            fontWeight = FontWeight.Bold,
                            color = NestTheme.colors.NN._950
                        )
                    )
                }
                description?.run {
                    NestTypography(
                        text = this,
                        modifier = descriptionModifier.padding(top = 4.dp),
                        textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NestTipsPreview() {
    NestTips(
        modifier = Modifier.padding(16.dp),
        title = "Title goes here",
        description = "Be brief, ya! You can add link in the end of a sentence. Test Link"
    )
}

@Preview(name = "Tips Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NestTipsDarkPreview() {
    NestTheme(darkTheme = true) {
        NestTips(title = "tips title", description = "tips description")
    }
}
