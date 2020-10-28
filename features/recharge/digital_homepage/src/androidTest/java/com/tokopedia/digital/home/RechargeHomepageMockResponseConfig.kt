package com.tokopedia.digital.home

import android.content.Context
import com.tokopedia.digital.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class RechargeHomepageMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_SKELETON = "rechargeGetDynamicPageSkeleton"
        const val KEY_QUERY_SEARCH = "rechargeCatalogMenu"

        var KEY_CONTAINS_SECTION_BANNER = """"sectionIDs": [
        1
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_FAVORITE = """"sectionIDs": [
        2
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_CATEGORY = """"sectionIDs": [
        3
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_TRUST_MARK = """"sectionIDs": [
        4
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_REMINDER = """"sectionIDs": [
        5
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_DUAL_BANNERS = """"sectionIDs": [
        6
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_LEGO_BANNERS = """"sectionIDs": [
        7
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_PRODUCT_CARDS = """"sectionIDs": [
        8
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_SINGLE_BANNER = """"sectionIDs": [
        9
      ],""".trimIndent()
        var KEY_CONTAINS_SECTION_PRODUCT_BANNER = """"sectionIDs": [
        10
      ],""".trimIndent()
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_SKELETON,
                getRawString(context, R.raw.recharge_homepage_skeleton_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_SEARCH,
                getRawString(context, R.raw.recharge_homepage_search_category_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_BANNER,
                getRawString(context, R.raw.recharge_homepage_banner_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_FAVORITE,
                getRawString(context, R.raw.recharge_homepage_favorite_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_CATEGORY,
                getRawString(context, R.raw.recharge_homepage_category_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_TRUST_MARK,
                getRawString(context, R.raw.recharge_homepage_trustmark_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_REMINDER,
                getRawString(context, R.raw.recharge_homepage_reminder_widget_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_DUAL_BANNERS,
                getRawString(context, R.raw.recharge_homepage_dual_banners_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_LEGO_BANNERS,
                getRawString(context, R.raw.recharge_homepage_lego_banners_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_PRODUCT_CARDS,
                getRawString(context, R.raw.recharge_homepage_product_cards_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_SINGLE_BANNER,
                getRawString(context, R.raw.recharge_homepage_single_banner_mock_response),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_SECTION_PRODUCT_BANNER,
                getRawString(context, R.raw.recharge_homepage_product_banner_mock_response),
                FIND_BY_CONTAINS)
        return this
    }
}