package com.tokopedia.common_compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestSearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String,
    onTextChanged: (String) -> Unit = { _ -> },
    onSearchBarCleared: () -> Unit = {},
    onKeyboardSearchAction: (String) -> Unit
) {
    val borderColor = NestTheme.colors.NN._200
    val searchIconColor = NestTheme.colors.NN._500

    var text by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier
            .height(36.dp)
            .border(0.5.dp, borderColor, RoundedCornerShape(8.dp)),
        value = text,
        onValueChange = { newText ->
            onTextChanged(newText)
            text = newText
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onKeyboardSearchAction(text) }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Search Icon",
                    tint = searchIconColor
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        NestTypography(
                            text = placeholderText,
                            textStyle = NestTheme.typography.display2.copy(color = NestTheme.colors.NN._600)
                        )
                    }

                    innerTextField()
                }

                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                text = ""
                                onSearchBarCleared()
                            },
                        contentDescription = "Close Icon",
                        tint = searchIconColor
                    )
                }
            }
        }
    )
}

@Preview(name = "Searchbar")
@Composable
fun NestSearchBarPreview() {
    NestSearchBar(
        Modifier,
        "Cari sesuatu..",
        onTextChanged = {},
        onSearchBarCleared = {},
        onKeyboardSearchAction = {}
    )
}
