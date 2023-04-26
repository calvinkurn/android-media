package com.tokopedia.common_compose.header

import android.content.res.Configuration
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
@Preview
@Composable
fun NestHeaderSingleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Default"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestHeaderSingleLineDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Default"
            )
        )
    }
}

@Preview
@Composable
fun NestHeaderSingleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Transparent"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestHeaderSingleLineTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.SingleLine().copy(
                title = "Header Single Line Transparent"
            )
        )
    }
}

// endregion

// region double line preview
@Preview
@Composable
fun NestHeaderDoubleLineDefaultPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Default"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestHeaderDoubleLineDefaultDarkPreview() {
    NestTheme {
        NestHeader(
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Default"
            )
        )
    }
}

@Preview
@Composable
fun NestHeaderDoubleLineTransparentPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Transparent"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestHeaderDoubleLineTransparentDarkPreview() {
    NestTheme {
        NestHeader(
            variant = NestHeaderVariant.Transparent,
            type = NestHeaderType.DoubleLine().copy(
                title = "Header Double Line Transparent"
            )
        )
    }
}
// endregion
