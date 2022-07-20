package com.tokopedia.tokopedianow.educationalinfo.presentation.listener

import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics
import com.tokopedia.tokopedianow.educationalinfo.presentation.bottomsheet.TokoNowEducationalInfoBottomSheet

class EducationalInfoBottomSheetCallback(
    private val analytics: EducationalInfoAnalytics,
    private val channelId: String?,
    private val state: String?
): TokoNowEducationalInfoBottomSheet.EducationalInfoBottomSheetListener {
    override fun impressUspBottomSheet() {
        analytics.impressUspBottomSheet(channelId, state)
    }

    override fun clickVisitInformationPage(keyRes: String) {
        if (keyRes == TokoNowServiceTypeUtil.EDU_BOTTOMSHEET_SK_RESOURCE_ID) {
            analytics.clickTermsAndConditions(channelId, state)
        } else {
            analytics.clickOperationalHour(channelId, state)
        }
    }

    override fun clickVisitNowBottomSheet() {
        analytics.clickVisitNowBottomSheet(channelId, state)
    }
}