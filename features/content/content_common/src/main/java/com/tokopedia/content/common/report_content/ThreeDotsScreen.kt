package com.tokopedia.content.common.report_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 31/07/23
 */

@Composable
fun ThreeDotsPage(
    menuList: List<ContentMenuItem>,
    onMenuClicked: (ContentMenuItem) -> Unit = {}
) {
    NestTheme(darkTheme = true) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val menuView = createRef()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(menuView) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            ) {
                items(menuList) {
                    ItemMenu(
                        menu = it,
                        onMenuClicked = onMenuClicked
                    )
                }
            }
        }
    }
}

@Composable
fun ItemMenu(menu: ContentMenuItem, onMenuClicked: (ContentMenuItem) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onMenuClicked(menu)
            }
    ) {
        val (iconView, nameView, dividerView) = createRefs()

        val styleColor = when (menu.type) {
            ContentMenuIdentifier.Report, ContentMenuIdentifier.Delete -> {
                colorResource(unifyprinciplesR.color.Unify_RN500)
            }

            else -> {
                colorResource(unifyprinciplesR.color.Unify_NN900)
            }
        }

        NestIcon(
            iconId = menu.iconUnify,
            colorLightEnable = styleColor,
            colorNightEnable = styleColor,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(iconView) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top)
                }
        )

        NestTypography(
            text = stringResource(menu.name),
            maxLines = 2,
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = styleColor
            ),
            modifier = Modifier.constrainAs(nameView) {
                width = Dimension.wrapContent
                height = Dimension.wrapContent
                bottom.linkTo(iconView.bottom)
                start.linkTo(iconView.end, margin = 8.dp)
                top.linkTo(iconView.top)
            }
        )

        Box(modifier = Modifier
            .background(NestTheme.colors.NN._100)
            .requiredHeight(1.dp)
                .constrainAs(dividerView) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(nameView.bottom, margin = 16.dp)
                }
                .fillMaxWidth()
        )
    }
}
