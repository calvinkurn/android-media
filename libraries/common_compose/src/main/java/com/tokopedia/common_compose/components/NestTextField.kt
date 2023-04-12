package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

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
    helper: String? = null,
    onClearable: (() -> Unit)? = null
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
            modifier = modifier
                .defaultMinSize(minHeight = 48.dp, minWidth = 75.dp)
                .fillMaxWidth(),
            placeholder = placeholder?.let { { NestTypography(text = it) } },
            leadingIcon = generatePrefix(prefix, enabled),
            trailingIcon = generateLeadingIcon(icon1, icon2, suffix, enabled, onClearable),
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
                errorBorderColor = NestTheme.colors.RN._500,
                disabledBorderColor = NestTheme.colors.NN._200,
                disabledTextColor = NestTheme.colors.NN._400,
                textColor = NestTheme.colors.NN._950
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            helper?.takeIf { error.isNullOrEmpty() }?.let {
                NestTypography(
                    text = it,
                    modifier = Modifier
                        .weight(4F)
                        .padding(start = 28.dp),
                    textStyle = NestTheme.typography.paragraph3.copy(
                        color = generateHelperColor(
                            enabled = enabled,
                            error = !error.isNullOrEmpty()
                        )
                    )
                )
            }
            error?.let {
                NestTypography(
                    text = it,
                    modifier = Modifier
                        .weight(4F)
                        .padding(start = 28.dp),
                    textStyle = NestTheme.typography.small.copy(color = NestTheme.colors.RN._500)
                )
            }
            counter?.let {
                val currentCount = value.length
                NestTypography(
                    text = "$currentCount/$it",
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 28.dp),
                    textStyle = NestTheme.typography.paragraph3.copy(
                        color = generateCounterColor(
                            counter = it,
                            currentCount = currentCount,
                            enabled = enabled
                        ),
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}

@Composable
fun NestTextFieldSkeleton(modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = modifier
                .defaultMinSize(minHeight = 48.dp, minWidth = 75.dp)
                .fillMaxWidth()
                .shimmerBackground(RoundedCornerShape(8.dp))
        )
        Row(modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)) {
            Box(
                modifier = modifier
                    .height(12.dp)
                    .weight(3F)
                    .padding(end = 8.dp)
                    .shimmerBackground(RoundedCornerShape(8.dp))
            )
            Box(
                modifier = modifier
                    .height(12.dp)
                    .weight(1F)
                    .padding(start = 8.dp)
                    .shimmerBackground(RoundedCornerShape(8.dp))
            )
        }
    }
}

fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )
    val shimmerColors = listOf(
        Color(0xFFD6DFEB),
        Color(0xFFE4EBF5),
        Color(0xFFD6DFEB)
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, translateAnimation),
        end = Offset(translateAnimation + 100f, translateAnimation + 30f),
        tileMode = TileMode.Clamp
    )
    return@composed this.then(background(brush, shape))
}

@Composable
private fun generateCounterColor(counter: Int, currentCount: Int, enabled: Boolean): Color {
    return if (!enabled) {
        return NestTheme.colors.NN._400
    } else if (currentCount >= counter) {
        NestTheme.colors.RN._500
    } else {
        NestTheme.colors.NN._600
    }
}

@Composable
private fun generateHelperColor(enabled: Boolean, error: Boolean): Color {
    return if (!enabled) {
        NestTheme.colors.NN._400
    } else if (error) {
        NestTheme.colors.RN._500
    } else {
        NestTheme.colors.NN._600
    }
}

@Composable
private fun generatePrefix(prefix: String? = null, enabled: Boolean): @Composable (() -> Unit)? {
    if (prefix != null) {
        return {
            NestTypography(
                text = prefix,
                textStyle = NestTheme.typography.display2.copy(
                    color = if (enabled) NestTheme.colors.NN._600 else NestTheme.colors.NN._400,
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
    suffix: String? = null,
    enabled: Boolean,
    onClearable: (() -> Unit)? = null
): @Composable (() -> Unit)? {
    if (icon1 != null || icon2 != null || !suffix.isNullOrEmpty() || onClearable != null) {
        return {
            Row {
                onClearable?.let {
                    Icon(
                        painter = painterResource(id = R.drawable.iconunify_clear_small),
                        contentDescription = "tips icon",
                        tint = NestTheme.colors.NN._500,
                        modifier = Modifier.clickable { it() }
                    )
                }
                suffix?.let {
                    NestTypography(
                        text = it,
                        textStyle = NestTheme.typography.display2.copy(
                            color = if (enabled) NestTheme.colors.NN._600 else NestTheme.colors.NN._400,
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

@Preview(name = "Text Field (skeleton)")
@Composable
fun NestTextFieldSkeletonPreview() {
    NestTextFieldSkeleton()
}

@Preview(name = "Text Field (clearable)")
@Composable
fun NestTextFieldClearablePreview() {
    NestTextField(value = "aaa", onClearable = {})
}
