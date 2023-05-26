package com.tokopedia.common_compose.components.card

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.components.card.NestCardType.Border
import com.tokopedia.common_compose.components.card.NestCardType.BorderActive
import com.tokopedia.common_compose.components.card.NestCardType.BorderDisabled
import com.tokopedia.common_compose.components.card.NestCardType.ShadowActive
import com.tokopedia.common_compose.components.card.NestCardType.ShadowDisabled
import com.tokopedia.common_compose.ui.NestTheme


private const val LOREM = """
    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
        labore et dolore magna aliqua. Faucibus a pellentesque sit amet porttitor eget dolor morbi. Sit amet nisl suscipit
        adipiscing bibendum est.
"""

@Composable
internal fun CardContentComplexExample(showImage: Boolean = true) {
    var textTest by remember {
        mutableStateOf("Click Me")
    }

    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth()
    ) {

        if (showImage) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(colorResource(id = R.color.Unify_BN200))
            )
        }

        Text(
            modifier = Modifier.padding(16.dp), text = "Open the reward"
        )
        Button(modifier = Modifier.wrapContentSize().align(alignment = Alignment.CenterHorizontally)
            .padding(bottom = 16.dp), onClick = {
            textTest = "Button Clicked"
        }) {
            Text(
                modifier = Modifier.padding(8.dp), text = textTest
            )
        }
    }
}

@Composable
private fun NestCardTextContent() {
    Text("Hai\nHai\nHai\nHai\nHai", modifier = Modifier.padding(4.dp))
}

@Composable
@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(name = "Full Preview", showSystemUi = true)
private fun NestCardPreview() {
    NestTheme {
        Surface(color = NestTheme.colors.NN._0 ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                NestCardWithImageAndButton(true)

                NestCardWithActionClick()

                NestCardPreviewSelectionAnimation()

                NestCardPreviewBorderActive()

                NestCardPreviewShadowActive()

                NestCardPreviewBorderDisabled()

                NestCardPreviewShadowDisabled()
            }
        }
    }
}

@Composable
private fun NestCardWithImageAndButton(showImage: Boolean) {
    NestCard(
        enableBounceAnimation = true,
        type = NestCardType.default(),
        onClick = {

        }, onLongPress = {

        }) {
        CardContentComplexExample(showImage)
    }
}

@Composable
private fun NestCardWithActionClick() {
    val context = LocalContext.current

    NestCard(
        enableBounceAnimation = true,
        type = NestCardType.default(),
        onClick = {
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
        }, onLongPress = {
            Toast.makeText(context, "long click", Toast.LENGTH_SHORT).show()
        }) {
        CardContentComplexExample(false)
    }
}

@Composable
private fun NestCardPreviewSelectionAnimation() {
    var type by remember { mutableStateOf(NestCardType.default()) }
    NestCard(modifier = Modifier.fillMaxWidth(),
        enableBounceAnimation = true,
        enableTransitionAnimation = true,
        type = type,
        onClick = {
            type = if (type == BorderActive) {
                Border
            } else {
                BorderActive
            }
        },
        onLongPress = {

        }) {
        Text(LOREM, Modifier.padding(4.dp))
    }
}

@Composable
private fun NestCardPreviewBorderActive() {
    NestCard(modifier = Modifier.fillMaxWidth(),
        enableBounceAnimation = true,
        enableTransitionAnimation = true,
        type = BorderActive,
        onClick = {
        },
        onLongPress = {

        }) {
        Text("Card with Border active", Modifier.padding(8.dp))
    }
}

@Composable
private fun NestCardPreviewShadowActive() {
    NestCard(modifier = Modifier.fillMaxWidth(),
        enableBounceAnimation = true,
        enableTransitionAnimation = true,
        type = ShadowActive,
        onClick = {
        },
        onLongPress = {

        }) {
        Text("Card with Shadow active", Modifier.padding(8.dp))
    }
}

@Composable
private fun NestCardPreviewBorderDisabled() {
    NestCard(modifier = Modifier.fillMaxWidth(),
        enableBounceAnimation = true,
        enableTransitionAnimation = true,
        type = BorderDisabled,
        onClick = {
        },
        onLongPress = {

        }) {
        Text("Card with border disabled", Modifier.padding(8.dp))
    }
}

@Composable
private fun NestCardPreviewShadowDisabled() {
    NestCard(modifier = Modifier.fillMaxWidth(),
        enableBounceAnimation = true,
        enableTransitionAnimation = true,
        type = ShadowDisabled,
        onClick = {
        },
        onLongPress = {

        }) {
        Text("Card with shadow disabled", Modifier.padding(8.dp))
    }
}