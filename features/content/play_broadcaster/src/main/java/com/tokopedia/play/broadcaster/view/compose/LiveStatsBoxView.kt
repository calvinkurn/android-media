package com.tokopedia.play.broadcaster.view.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
@Composable
fun LiveStatsBoxView(
    liveStats: LiveStatsUiModel,
    onClick : (() -> Unit)?,
) {
    NestCard(
        type = NestCardType.NoBorder,
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(12.dp), clip = false)
            .border(BorderStroke(1.dp, MaterialTheme.colors.surface), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .noRippleClickable { onClick?.invoke() }
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestIcon(
                    modifier = Modifier.size(16.dp),
                    iconId = liveStats.icon,
                    colorNightEnable = NestTheme.colors.NN._600,
                    colorNightDisable = NestTheme.colors.NN._600,
                    colorLightDisable = NestTheme.colors.NN._600,
                    colorLightEnable = NestTheme.colors.NN._600,
                )

                Spacer(modifier = Modifier.width(8.dp))

                NestTypography(
                    modifier = Modifier.weight(1f),
                    text = stringResource(liveStats.label),
                    textStyle = NestTheme.typography.body3.copy(
                        color = NestTheme.colors.NN._600,
                    ),
                    maxLines = 1,
                )

                if (onClick != null) {
                    Spacer(modifier = Modifier.width(8.dp))

                    NestIcon(
                        modifier = Modifier.size(16.dp),
                        iconId = IconUnify.CHEVRON_RIGHT,
                        colorNightEnable = NestTheme.colors.NN._600,
                        colorNightDisable = NestTheme.colors.NN._600,
                        colorLightDisable = NestTheme.colors.NN._600,
                        colorLightEnable = NestTheme.colors.NN._600,
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            NestTypography(
                text = liveStats.text,
                textStyle = NestTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
            )
        }
    }
}
