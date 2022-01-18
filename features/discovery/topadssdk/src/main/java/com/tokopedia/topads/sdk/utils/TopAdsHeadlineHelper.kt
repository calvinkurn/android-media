package com.tokopedia.topads.sdk.utils

import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_2
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_6
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import javax.inject.Inject

class TopAdsHeadlineHelper  @Inject constructor() {

    var seenAds: Int = 0

    fun processHeadlineAds(
            cpmModel: CpmModel,
            pageNumber: Int = 2,
            process: (Int, ArrayList<CpmData>, Boolean) -> Unit,
    ) {

        val listLayoutFive = arrayListOf<CpmData>()
        val listLayoutSix = arrayListOf<CpmData>()
        run breaker@{
            cpmModel.data?.forEachIndexed { index, cpmData ->
                if (pageNumber == 2 && index >= 1) return
                if (!shouldShowCpmShop(cpmData)) return@forEachIndexed

                when (cpmData.cpm.layout) {
                    LAYOUT_6 -> {
                        listLayoutSix.add(cpmData)
                        if (cpmModel.data.size - 1 != index) return@forEachIndexed
                        process(index, listLayoutSix, false)
                    }
                    LAYOUT_5 -> {
                        listLayoutFive.add(cpmData)
                        if (cpmModel.data.size - 1 != index) return@forEachIndexed
                        process(index, listLayoutFive, false)
                    }
                    LAYOUT_2 -> {
                        val list = arrayListOf<CpmData>()
                        list.add(cpmData)
                        process(index, list, true)
                        return@breaker
                    }
                    else -> {
                        val list = arrayListOf<CpmData>()
                        list.add(cpmData)
                        process(index, list, true)
                    }
                }
            }
        }
    }

    private fun shouldShowCpmShop(cpmData: CpmData?): Boolean {
        cpmData ?: return false
        val cpm = cpmData.cpm ?: return false

        return if (isViewWillRenderCpmShop(cpm)) true
        else isViewWillRenderCpmDigital(cpm)
    }

    private fun isViewWillRenderCpmShop(cpm: Cpm): Boolean {
        return cpm.cpmShop != null
                && cpm.cta.isNotEmpty()
                && cpm.promotedText.isNotEmpty()
    }

    var CPM_TEMPLATE_ID = 4

    private fun isViewWillRenderCpmDigital(cpm: Cpm) = cpm.templateId == CPM_TEMPLATE_ID


}