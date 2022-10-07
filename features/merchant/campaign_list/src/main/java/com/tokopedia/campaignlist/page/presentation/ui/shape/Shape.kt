package com.tokopedia.campaignlist.page.presentation.ui.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val RoundedShapes = Shapes(
    small = RoundedCornerShape(percent = 50), //Bottomsheet on collapsed state
    medium = RoundedCornerShape(4.dp), //Card
    large = RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 16.dp),
)