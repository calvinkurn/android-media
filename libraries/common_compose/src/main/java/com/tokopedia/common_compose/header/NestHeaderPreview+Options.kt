package com.tokopedia.common_compose.header

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

/**
 * Created by yovi.putra on 26/04/23"
 * Project name: android-tokopedia-core
 **/

/**
 * SINGLE LINE
 */
// region single line preview
@Preview
@Composable
private fun NestHeaderSingleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Default",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSingleLineDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Default",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action", color = NestTheme.colors.RN._500),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview
@Composable
private fun NestHeaderSingleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Transparent",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSingleLineTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Transparent",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

// endregion

/**
 * DOUBLE LINE
 */
// region double line preview
@Preview
@Composable
private fun NestHeaderDoubleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Default",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action", color = NestTheme.colors.RN._500),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderDoubleLineDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Default",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview
@Composable
private fun NestHeaderDoubleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Transparent",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderDoubleLineTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Transparent",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}
// endregion

/**
 * LOCATION
 */
// region location preview
@Preview
@Composable
private fun NestHeaderLocationDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Location().copy(
                title = "Header Location Default",
                subTitle = "Pilih Lokasi",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderLocationDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Location().copy(
                title = "Header Location Default",
                subTitle = "Pilih Lokasi",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview
@Composable
private fun NestHeaderLocationTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Location().copy(
                title = "Header Location Transparent",
                subTitle = "Pilih Lokasi",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action", color = NestTheme.colors.RN._500),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderLocationTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Location().copy(
                title = "Header Location Transparent",
                subTitle = "Pilih Lokasi",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}
// endregion

/**
 * PROFILE
 */
// region profile preview
@Preview
@Composable
private fun NestHeaderProfileDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Default",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = ""),
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderProfileDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Default",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = ""),
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview
@Composable
private fun NestHeaderProfileTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Transparent",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = ""),
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderProfileTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Transparent",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = ""),
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}
// endregion

/**
 * SEARCH
 */
// region search preview
@Preview
@Composable
private fun NestHeaderSearchDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSearchDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview
@Composable
private fun NestHeaderSearchTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                showBackButton = true,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSearchTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action", color = NestTheme.colors.RN._500),
                    HeaderTextButton(text = "Add")
                )
            )
        )
    }
}
// endregion
