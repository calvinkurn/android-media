package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestTextField(
    value: String,
    onValueChanged: (String) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
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
        supportingText = supportingText,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = NestTheme.colors.GN._500,
            errorBorderColor = NestTheme.colors.RN._500,
            errorSupportingTextColor = NestTheme.colors.RN._500
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
