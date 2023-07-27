package com.tokopedia.stories.common

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@Composable
internal fun StoriesAvatarContent(
    imageUrl: String,
    storiesStatus: StoriesStatus,
    modifier: Modifier = Modifier,
    imageToBorderPadding: Dp = 8.dp
) {
    NestTheme {
        Box(
            modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = storiesStatus.borderDp,
                    brush = storiesStatus.brush,
                    shape = CircleShape
                )
        ) {
            NestImage(
                imageUrl = imageUrl,
                Modifier
                    .matchParentSize()
                    .padding(imageToBorderPadding)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.NoStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HasUnseenStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.HasUnseenStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AllStoriesSeenAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.AllStoriesSeen,
        Modifier.size(200.dp)
    )
}

private val StoriesStatus.borderDp: Dp
    get() = when (this) {
        StoriesStatus.NoStories -> 1.dp
        StoriesStatus.HasUnseenStories -> 2.dp
        StoriesStatus.AllStoriesSeen -> 1.dp
    }

private val StoriesStatus.brush: Brush
    get() = when (this) {
        StoriesStatus.NoStories -> SolidColor(Color(0x00000000))
        StoriesStatus.HasUnseenStories -> {
            Brush.linearGradient(
                0f to Color(0xFF83ECB2),
                1f to Color(0xFF00AA5B)
            )
        }
        StoriesStatus.AllStoriesSeen -> SolidColor(Color(0xFFD6DFEB))
    }
