package com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 08/05/20
 */

data class TipsTrickCarouselModel(
        val title: String,
        val imageUrl: String,
        val impressHolder: ImpressHolder = ImpressHolder())