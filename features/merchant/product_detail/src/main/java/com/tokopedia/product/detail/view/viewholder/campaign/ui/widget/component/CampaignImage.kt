package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.utils.ImageSource

/**
 * Created by yovi.putra on 20/12/23"
 * Project name: android-unify
 **/

@Composable
fun CampaignImage(
    url: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.CenterStart,
    contentScale: ContentScale = ContentScale.FillHeight
) {
    NestImage(
        modifier = modifier,
        type = NestImageType.Rect(0.dp),
        source = ImageSource.Remote(
            source = url,
            loaderType = ImageSource.Remote.LoaderType.NONE,
            customUIError = {}
        ),
        alignment = alignment,
        contentScale = contentScale
    )
}
