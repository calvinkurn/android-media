package com.tokopedia.home.beranda.ext

import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel

fun BusinessUnitItemDataModel.mapToPromotionTracker() = BaseTracking.Promotion(
        id = content.contentId.toString(),
        name = content.contentName,
        creative = "",
        creativeUrl = content.imageUrl,
        position = itemPosition.toString(),
        promoCodes = "",
        promoIds = "")