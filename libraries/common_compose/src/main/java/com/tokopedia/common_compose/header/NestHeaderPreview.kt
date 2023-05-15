package com.tokopedia.common_compose.header

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 26/04/23"
 * Project name: android-tokopedia-core
 **/

/**
 * SINGLE LINE
 */
// region single line preview
@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSingleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Default"
            )
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSingleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Transparent",
                showBackButton = !isSystemInDarkTheme()
            )
        )
    }
}

// endregion

/**
 * DOUBLE LINE
 */
// region double line preview
@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderDoubleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Default"
            )
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderDoubleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Transparent",
                showBackButton = !isSystemInDarkTheme()
            )
        )
    }
}
// endregion

/**
 * LOCATION
 */
// region location preview
@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderLocationDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Location().copy(
                title = "Header Location Default",
                subTitle = "Pilih Lokasi"
            )
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderLocationTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Location().copy(
                title = "Header Location Transparent",
                subTitle = "Pilih Lokasi",
                showBackButton = !isSystemInDarkTheme()
            )
        )
    }
}
// endregion

/**
 * PROFILE
 */
// region profile preview
@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderProfileDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Default",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = "")
            )
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderProfileTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Profile().copy(
                title = "Header Profile Transparent",
                subTitle = "Pilih Akun",
                imageSource = HeaderImageSource.Remote(source = ""),
                showBackButton = !isSystemInDarkTheme()
            )
        )
    }
}
// endregion

/**
 * SEARCH
 */
// region search preview
@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSearchDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia"
            )
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestHeaderSearchTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                showBackButton = !isSystemInDarkTheme()
            )
        )
    }
}
// endregion
