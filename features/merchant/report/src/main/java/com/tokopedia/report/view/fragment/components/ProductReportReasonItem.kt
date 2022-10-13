package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.common_compose.principles.nest_text.NestText
import com.tokopedia.common_compose.principles.nest_text.NestTextType
import com.tokopedia.common_compose.principles.nest_text.NestTextWeight
import com.tokopedia.design.R
import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonItem(
    reason: ProductReportReason,
    subtitleVisible: Boolean,
    onClick: (ProductReportReason) -> Unit
) {

    Column {
        ConstraintLayout(
            modifier = Modifier
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

            NestText(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        linkTo(
                            start = contentGuideLineStart,
                            end = iconRight.start,
                            endMargin = 4.dp,
                            bias = 0f
                        )

                        top.linkTo(contentGuideLineTop)

                        if (!subtitleVisible) {
                            bottom.linkTo(contentGuideLineBottom)
                        } else {
                            bottom.linkTo(subtitle.top)
                        }
                    },
                text = reason.value,
                type = NestTextType.Body2,
                weight = NestTextWeight.Bold
            )

            if (subtitleVisible) {
                NestText(
                    modifier = Modifier
                        .constrainAs(subtitle) {
                            top.linkTo(title.bottom, margin = 2.dp)
                            start.linkTo(contentGuideLineStart)
                            end.linkTo(iconRight.start, margin = 4.dp)
                            bottom.linkTo(contentGuideLineBottom)
                        },
                    text = reason.detail,
                    type = NestTextType.Body3,
                    weight = NestTextWeight.Bold,
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
                tint = Color(R.color.divider_color),
                contentDescription = "ic_button_${reason.strLabel}"
            )

            Divider(
                modifier = Modifier
                    .constrainAs(divider) {
                        linkTo(
                            top = contentBarrier,
                            bottom = parent.bottom,
                            bias = 1f
                        )
                    },
                color = colorResource(id = R.color.dividerunify_background)
            )
        }

        Column(
            modifier = Modifier
                .clickable {
                    onClick.invoke(reason)
                },
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    NestText(
                        text = reason.value,
                        type = NestTextType.Body2,
                        weight = NestTextWeight.Bold
                    )

                    if (subtitleVisible) {
                        NestText(
                            text = reason.detail,
                            type = NestTextType.Body3,
                            weight = NestTextWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier
                        .width(16.dp)
                        .wrapContentHeight(),
                    painter = painterResource(id = com.tokopedia.design.R.drawable.ic_arrow_right_grey),
                    tint = Color(com.tokopedia.design.R.color.divider_color),
                    contentDescription = "ic_button_${reason.strLabel}"
                )
            }

            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                color = colorResource(id = R.color.dividerunify_background)
            )
        }
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