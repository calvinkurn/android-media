package com.tokopedia.feedplus.presentation.uiview

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.feedplus.R
import com.tokopedia.nest.principles.utils.noRippleClickable

/**
 * Created by Jonathan Darwin on 30 April 2024
 */

class FeedSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var onClick by mutableStateOf({})

    fun setOnSearchBarClicked(onClick: () -> Unit) {
        this.onClick = onClick
    }

    @Composable
    override fun Content() {
        FeedSearchBar(
            onClick = onClick
        )
    }
}

@Composable
private fun FeedSearchBar(
    onClick: () -> Unit,
) {
    NestTheme(
        isOverrideStatusBarColor = false,
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .noRippleClickable(onClick)
                .padding(6.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NestIcon(
                modifier = Modifier.size(16.dp),
                iconId = IconUnify.SEARCH,
                colorLightEnable = Color.White,
                colorLightDisable = Color.White,
                colorNightDisable = Color.White,
                colorNightEnable = Color.White,
            )

            Spacer(modifier = Modifier.width(4.dp))

            NestTypography(
                text = stringResource(id = R.string.feed_local_search_page_text_placeholder_fallback),
                textStyle = NestTheme.typography.display2.copy(
                    color = Color.White,
                )
            )
        }
    }

}

@Preview
@Composable
private fun FeedSearchBarPreview() {
    FeedSearchBar(
        onClick = {}
    )
}
