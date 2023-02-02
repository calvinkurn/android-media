package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.design.R
import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonItem(
    modifier: Modifier = Modifier,
    reason: ProductReportReason,
    subtitleVisible: Boolean,
    onClick: (ProductReportReason) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                onClick.invoke(reason)
            }
            .fillMaxWidth()
    ) {
        val contentGuideLineTop = createGuidelineFromTop(12.dp)
        val contentGuideLineBottom = createGuidelineFromBottom(12.dp)
        val contentGuideLineStart = createGuidelineFromStart(16.dp)
        val contentGuideLineEnd = createGuidelineFromEnd(16.dp)
        val (title, subtitle, iconRight, divider) = createRefs()
        val contentBarrier = createBottomBarrier(title, subtitle, iconRight)

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    linkTo(
                        start = contentGuideLineStart,
                        top = contentGuideLineTop,
                        bottom = if (!subtitleVisible) contentGuideLineBottom else subtitle.top,
                        end = iconRight.start,
                        endMargin = 4.dp,
                        horizontalBias = 0f
                    )
                },
            text = reason.value,
            textStyle = NestTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )

        if (subtitleVisible) {
            NestTypography(
                modifier = Modifier
                    .constrainAs(subtitle) {
                        width = Dimension.fillToConstraints
                        linkTo(
                            start = contentGuideLineStart,
                            top = title.bottom,
                            topMargin = 2.dp,
                            end = iconRight.start,
                            endMargin = 4.dp,
                            bottom = contentGuideLineBottom,
                            horizontalBias = 0f
                        )
                    },
                text = reason.detail,
                textStyle = NestTheme.typography.body3,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            modifier = Modifier
                .width(16.dp)
                .wrapContentHeight()
                .constrainAs(iconRight) {
                    top.linkTo(contentGuideLineTop)
                    end.linkTo(contentGuideLineEnd)
                    bottom.linkTo(contentGuideLineBottom)
                },
            painter = painterResource(id = R.drawable.ic_arrow_right_grey),
            tint = NestTheme.colors.NN._600,
            contentDescription = "ic_button_${reason.strLabel}"
        )

        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    linkTo(
                        top = contentBarrier,
                        bottom = parent.bottom,
                        start = contentGuideLineStart,
                        end = contentGuideLineEnd,
                        verticalBias = 1f
                    )
                }
                .padding(start = 16.dp),
            color = NestTheme.colors.NN._50
        )
    }
}

@Preview
@Composable
fun ProductReportReasonItem() {
    ProductReportReasonItem(
        reason = ProductReportReason(
            "1",
            children = emptyList(),
            additionalInfo = emptyList(),
            detail = "adsf",
            value = "vakue ajajajjaaaa"
        ),
        subtitleVisible = true
    ) {
    }
}
