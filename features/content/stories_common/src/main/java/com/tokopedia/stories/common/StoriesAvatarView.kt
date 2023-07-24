package com.tokopedia.stories.common

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(
    context,
    attrs,
    defStyleAttr
) {

    private var mImageUrl by mutableStateOf("")

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    @Composable
    override fun Content() {
        StoriesAvatarContent(mImageUrl)
    }
}

@Composable
private fun StoriesAvatarContent(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    NestTheme {
        Box(
            modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        0f to Color(0xFF69F2E2),
                        1f to Color(0xFF00AA5B),
                    )
                )
        ) {
            Box(
                Modifier
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(NestTheme.colors.NN._0)
                    .fillMaxSize()
            ) {
                NestImage(
                    imageUrl = imageUrl,
                    Modifier
                        .matchParentSize()
                        .padding(2.dp)
                        .clip(CircleShape),
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        Modifier.size(200.dp)
    )
}
