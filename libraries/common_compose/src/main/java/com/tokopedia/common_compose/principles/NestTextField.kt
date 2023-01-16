package com.tokopedia.common_compose.principles

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTextField(
    value: String,
    onValueChanged: (String) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false
//    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950),
        modifier = modifier,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        label = label,
        enabled = enabled,
        isError = isError
//        supportingText = supportingText
    )
}
