package com.tokopedia.common_compose.card2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R.drawable
import com.tokopedia.common_compose.card2.Card2Border.Border
import com.tokopedia.common_compose.card2.Card2Border.BorderActive
import com.tokopedia.common_compose.card2.Card2Border.BorderDisabled
import com.tokopedia.common_compose.card2.Card2Border.ShadowActive
import com.tokopedia.common_compose.card2.Card2Border.ShadowDisabled


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
            Image(
                painter = painterResource(id = drawable.png_sample),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(150.dp)
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
private fun CardContentTextExample(showImage: Boolean = true) {
    Text("Hai\nHai\nHai\nHai\nHai", modifier = Modifier.padding(4.dp))
}

@Composable
@Preview
private fun Card2Preview1() {
    Surface {
        val type by remember { mutableStateOf(Card2Border.default()) }
        Card2Unify(enableBounceAnimation = true,
            type = type,
            onClick = {

            }, onLongPress = {

            }) {
            CardContentComplexExample()
        }
    }
}

@Composable
@Preview
private fun Card2Preview2() {
    Surface {
        val type by remember { mutableStateOf(Card2Border.default()) }
        Card2Unify(enableBounceAnimation = true,
            type = type,
            onClick = {

            }, onLongPress = {

            }) {
            CardContentComplexExample(false)
        }
    }
}

@Composable
@Preview
private fun Card2Preview3() {
    Surface {
        val type by remember { mutableStateOf(Card2Border.default()) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
            enableBounceAnimation = false,
            type = type,
            onClick = {

            },
            onLongPress = {

            }) {
            CardContentTextExample()
        }
    }
}

@Composable
@Preview
private fun Card2Preview4() {
    Surface {
        var type by remember { mutableStateOf(Card2Border.default()) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
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
}

@Composable
@Preview
private fun Card2Preview5() {
    Surface {
        val type by remember { mutableStateOf(BorderActive) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
            enableBounceAnimation = true,
            enableTransitionAnimation = true,
            type = type,
            onClick = {
            },
            onLongPress = {

            }) {
            Text("Card with Border active", Modifier.padding(8.dp))
        }
    }
}

@Composable
@Preview
private fun Card2Preview6() {
    Surface {
        val type by remember { mutableStateOf(ShadowActive) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
            enableBounceAnimation = true,
            enableTransitionAnimation = true,
            type = type,
            onClick = {
            },
            onLongPress = {

            }) {
            Text("Card with Shadow active", Modifier.padding(8.dp))
        }
    }
}

@Composable
@Preview
private fun Card2Preview7() {
    Surface {
        val type by remember { mutableStateOf(BorderDisabled) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
            enableBounceAnimation = true,
            enableTransitionAnimation = true,
            type = type,
            onClick = {
            },
            onLongPress = {

            }) {
            Text("Card with border disabled", Modifier.padding(8.dp))
        }
    }
}

@Composable
@Preview
private fun Card2Preview8() {
    Surface {
        val type by remember { mutableStateOf(ShadowDisabled) }
        Card2Unify(modifier = Modifier.fillMaxWidth(),
            enableBounceAnimation = true,
            enableTransitionAnimation = true,
            type = type,
            onClick = {
            },
            onLongPress = {

            }) {
            Text("Card with shadow disabled", Modifier.padding(8.dp))
        }
    }
}