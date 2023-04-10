package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    icon1: @Composable (() -> Unit)? = null,
    icon2: @Composable (() -> Unit)? = null,
    suffix: String? = null,
    prefix: String? = null,
    error: String? = null,
    onValueChanged: (String) -> Unit = {},
    counter: Int? = null,
    helper: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (counter != null) {
                    if (it.length <= counter) onValueChanged(it)
                } else {
                    onValueChanged(it)
                }
            },
            textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950),
            modifier = modifier.defaultMinSize(minHeight = 48.dp).fillMaxWidth(),
            placeholder = placeholder?.let { { NestTypography(text = it) } },
            leadingIcon = generatePrefix(prefix),
            trailingIcon = generateLeadingIcon(icon1, icon2, suffix),
            label = label?.let {
                {
                    NestTypography(text = it)
                }
            },
            enabled = enabled,
            singleLine = true,
            isError = !error.isNullOrEmpty(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = NestTheme.colors.NN._300,
                focusedBorderColor = NestTheme.colors.GN._500,
                errorBorderColor = NestTheme.colors.RN._500
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            helper?.takeIf { error.isNullOrEmpty() }?.let {
                NestTypography(
                    text = it,
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 16.dp),
                    textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._600)
                )
            }
            error?.let {
                NestTypography(
                    text = it,
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 16.dp),
                    textStyle = NestTheme.typography.small.copy(color = NestTheme.colors.RN._500)
                )
            }
            counter?.let {
                val currentCount = value.length
                NestTypography(
                    text = "$currentCount/$it",
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 16.dp),
                    textStyle = NestTheme.typography.paragraph3.copy(
                        color = if (currentCount >= it) NestTheme.colors.RN._500 else NestTheme.colors.NN._600,
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}

@Composable
private fun generatePrefix(prefix: String? = null): @Composable (() -> Unit)? {
    if (prefix != null) {
        return {
            NestTypography(
                text = prefix,
                textStyle = NestTheme.typography.display2.copy(
                    color = NestTheme.colors.NN._600,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    } else {
        return null
    }
}

@Composable
private fun generateLeadingIcon(
    icon1: @Composable (() -> Unit)? = null,
    icon2: @Composable (() -> Unit)? = null,
    suffix: String? = null
): @Composable (() -> Unit)? {
    if (icon1 != null || icon2 != null || !suffix.isNullOrEmpty()) {
        return {
            Row {
                suffix?.let {
                    NestTypography(
                        text = it,
                        textStyle = NestTheme.typography.display2.copy(
                            color = NestTheme.colors.NN._600,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                ShowAndAddPadding(component = icon1, addPadding = suffix != null)
                ShowAndAddPadding(component = icon2, addPadding = icon1 != null)
            }
        }
    } else {
        return null
    }
}

@Composable
private fun ShowAndAddPadding(component: @Composable (() -> Unit)?, addPadding: Boolean) {
    component?.let {
        if (addPadding) {
            Box(modifier = Modifier.padding(start = 12.dp)) {
                it()
            }
        } else {
            it()
        }
    }
}

@Preview(name = "Text Field")
@Composable
fun NestTextFieldPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        placeholder = "placeholder",
        helper = "helper"
    )
}

@Preview(name = "Text Field (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestTextFieldDarkPreview() {
    NestTheme {
        NestTextField(
            value = "value",
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            label = "label",
            placeholder = "placeholder",
            helper = "helper"
        )
    }
}

@Preview(name = "Text Field (Counter)")
@Composable
fun NestTextFieldWithCounterPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        placeholder = "placeholder",
        counter = 30
    )
}

@Preview(name = "Text Field (Error)")
@Composable
fun NestTextFieldErrorPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        placeholder = "placeholder",
        error = "Error"
    )
}

@Preview(name = "Text Field (Disabled)")
@Composable
fun NestTextFieldDisabledPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        enabled = false,
        placeholder = "placeholder"
    )
}

@Preview(name = "Text Field (Helper)")
@Composable
fun NestTextFieldWithHelperPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        helper = "Helper text",
        placeholder = "placeholder"
    )
}

@Preview(name = "Text Field (Icon 1)")
@Composable
fun NestTextFieldWithIcon1Preview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        icon1 = {
            NestTypography(text = "aaa")
        },
        placeholder = "placeholder"
    )
}

@Preview(name = "Text Field (Icon 2)")
@Composable
fun NestTextFieldWithIcon2Preview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        icon1 = {
            NestTypography(text = "aaa")
        },
        icon2 = {
            NestTypography(text = "bbb")
        },
        placeholder = "placeholder"
    )
}

@Preview(name = "Text Field (suffix)")
@Composable
fun NestTextFieldWithSuffixPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        icon1 = {
            NestTypography(text = "aaa")
        },
        icon2 = {
            NestTypography(text = "bbb")
        },
        suffix = "$",
        placeholder = "placeholder"
    )
}

@Preview(name = "Text Field (prefix)")
@Composable
fun NestTextFieldWithPrefixPreview() {
    NestTextField(
        value = "value",
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = "label",
        icon1 = {
            NestTypography(text = "aaa")
        },
        icon2 = {
            NestTypography(text = "bbb")
        },
        suffix = "$",
        prefix = "IDR",
        placeholder = "placeholder"
    )
}
