package com.tokopedia.stories.common

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.AttributeSet
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(
    context,
    attrs,
    defStyleAttr
) {

    private var mImageUrl by mutableStateOf("")
    private var hasUnseenStories by mutableStateOf(false)

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    @Composable
    override fun Content() {
        StoriesAvatarContent(mImageUrl, hasUnseenStories)
    }
}

@Composable
private fun StoriesAvatarContent(
    imageUrl: String,
    hasUnseenStories: Boolean,
    modifier: Modifier = Modifier,
    imageToBorderPadding: Dp = 8.dp
) {
    NestTheme {
        Box(
            modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = if (hasUnseenStories) 2.dp else 1.dp,
                    brush = if (hasUnseenStories) {
                        Brush.linearGradient(
                            0f to Color(0xFF69F2E2),
                            1f to Color(0xFF00AA5B)
                        )
                    } else {
                        SolidColor(Color(0xFFD6DFEB))
                    },
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
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NoUnseenStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        false,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HasUnseenStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        true,
        Modifier.size(200.dp)
    )
}
