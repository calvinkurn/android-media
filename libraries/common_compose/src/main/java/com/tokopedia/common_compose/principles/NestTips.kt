package com.tokopedia.common_compose.principles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTips(
    title: String? = null,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = NestTheme.colors.NN._200),
        backgroundColor = if (isSystemInDarkTheme()) NestTheme.colors.NN._200 else NestTheme.colors.NN._50
    ) {
        Box {
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (6).dp, y = (-8).dp)) {
                Box(
                    modifier = Modifier
                        .width(40.dp).height(40.dp)
                        .background(NestTheme.colors.NN._100, shape = CircleShape)
                )
            }
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = "tips icon",
                modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp, top = 4.dp),
                tint = NestTheme.colors.NN._300
            )
            Column(modifier = Modifier.padding(16.dp)) {
                title?.run {
                    NestTypography(
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
                        textStyle = NestTheme.typography.small.copy(color = NestTheme.colors.NN._950)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NestTipsPreview() {
    NestTips(title = "tips title", description = "tips description")
}
