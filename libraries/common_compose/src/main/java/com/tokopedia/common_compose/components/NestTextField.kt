package com.tokopedia.common_compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChanged: (String) -> Unit = {},
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950),
            modifier = modifier.defaultMinSize(minHeight = 48.dp),
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            label = label,
            enabled = enabled,
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = NestTheme.colors.NN._300,
                focusedBorderColor = NestTheme.colors.GN._500,
                errorBorderColor = NestTheme.colors.RN._500
            ),
            shape = RoundedCornerShape(8.dp)
        )
        if (supportingText != null) {
            supportingText()
        }
    }
}

@Preview
@Composable
fun NestTextFieldPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = {
            NestTypography(
                text = "label"
            )
        },
        enabled = false,
        placeholder = {
            NestTypography(text = "placeholder")
        },
    )
}
