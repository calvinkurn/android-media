package com.tokopedia.stories.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyprinciples.R

/**
 * @author by astidhiyaa on 31/07/23
 */

@Composable
fun ThreeDotsPage(
    menuList: List<ContentMenuItem>,
    onDeleteStoryClicked: (ContentMenuItem) -> Unit,
    onMenuClicked: (ContentMenuItem) -> Unit = {}
) {
    NestTheme {
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
                        onMenuClicked = if (it.type == ContentMenuIdentifier.Delete) onDeleteStoryClicked else onMenuClicked
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
                R.color.Unify_RN500
            }

            else -> {
                R.color.Unify_NN900
            }
        }

        val ctx = LocalContext.current
        val iconDrawable = getIconUnifyDrawable(
            ctx,
            menu.iconUnify,
            MethodChecker.getColor(
                ctx,
                styleColor
            )
        )
        if (iconDrawable != null)
            Image(painter = rememberImagePainter(iconDrawable), contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(iconView) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(parent.top)
                    }
            )

        NestTypography(
            text = ctx.getString(menu.name),
            maxLines = 2,
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold, color = colorResource(
                    id = styleColor
                )
            ),
            modifier = Modifier.constrainAs(nameView) {
                width = Dimension.wrapContent
                height = Dimension.wrapContent
                bottom.linkTo(iconView.bottom)
                start.linkTo(iconView.end, margin = 8.dp)
                top.linkTo(iconView.top)
            })

        Divider(color = NestTheme.colors.NN._100, modifier = Modifier
            .constrainAs(dividerView) {
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                top.linkTo(nameView.bottom, margin = 16.dp)
            }
            .height(1.dp)
            .fillMaxWidth())
    }
}

