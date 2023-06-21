package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategorySharingModel
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.getParentPrivateField
import org.junit.Assert
import org.junit.Test

class CategoryShareTest : CategoryTestFixtures() {

    private val categoryModel = "oldcategory/first-page-8-products-share.json".jsonToObject<CategoryModel>()

    private val privateQueryParamMutable by lazy {
        tokoNowCategoryViewModel.getParentPrivateField<MutableMap<String, String>>("queryParamMutable")
    }

    @Test
    fun `when view created, category filter level 2 and level 3 are not selected`() {
        val selectedFilterOption = OptionHelper.copyOptionAsExclude(categoryModel.categoryFilter.filter[0].options[1])
        val categoryIdLvl2 = ""
        val categoryIdLvl3 = ""

        `Given category view model`(defaultCategoryL1, selectedFilterOption.value, defaultExternalServiceType, mapOf())
        `Given get category first page use case will be successful`(categoryModel)

        privateQueryParamMutable["${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}"] = categoryIdLvl2
        privateQueryParamMutable[SearchApiConst.SC] = categoryIdLvl3

        `When view created`()

        val constructedLink = getConstructedLink(
            categoryUrl = categoryModel.categoryDetail.data.url,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val utmCampaignList = getUtmCampaignList(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val title = getTitleCategory(
            categoryIdLvl2 = categoryIdLvl2,
            categoryModel = categoryModel
        )

        val sharingModel = CategorySharingModel(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3,
            title = title,
            deeplinkParam = constructedLink.first,
            url = constructedLink.second,
            utmCampaignList = utmCampaignList
        )

        `Then assert share live data`(sharingModel)
    }

    @Test
    fun `when view created, category filter level 3 is not selected`() {
        val selectedFilterOption = OptionHelper.copyOptionAsExclude(categoryModel.categoryFilter.filter[0].options[1])
        val categoryIdLvl2 = "12344"
        val categoryIdLvl3 = ""

        `Given category view model`(defaultCategoryL1, selectedFilterOption.value, defaultExternalServiceType, mapOf())
        `Given get category first page use case will be successful`(categoryModel)

        privateQueryParamMutable["${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}"] = categoryIdLvl2
        privateQueryParamMutable[SearchApiConst.SC] = categoryIdLvl3

        `When view created`()

        val constructedLink = getConstructedLink(
            categoryUrl = categoryModel.categoryDetail.data.url,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val utmCampaignList = getUtmCampaignList(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val title = getTitleCategory(
            categoryIdLvl2 = categoryIdLvl2,
            categoryModel = categoryModel
        )

        val sharingModel = CategorySharingModel(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3,
            title = title,
            deeplinkParam = constructedLink.first,
            url = constructedLink.second,
            utmCampaignList = utmCampaignList
        )

        `Then assert share live data`(sharingModel)
    }

    @Test
    fun `when view created, select all filter`() {
        val selectedFilterOption = OptionHelper.copyOptionAsExclude(categoryModel.categoryFilter.filter[0].options[1])
        val categoryIdLvl2 = "12344"
        val categoryIdLvl3 = "14423"
        val externalServiceType = ""

        `Given category view model`(defaultCategoryL1, selectedFilterOption.value, externalServiceType, mapOf())
        `Given get category first page use case will be successful`(categoryModel)

        privateQueryParamMutable["${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}"] = categoryIdLvl2
        privateQueryParamMutable[SearchApiConst.SC] = categoryIdLvl3

        `When view created`()

        val constructedLink = getConstructedLink(
            categoryUrl = categoryModel.categoryDetail.data.url,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val utmCampaignList = getUtmCampaignList(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )

        val title = getTitleCategory(
            categoryIdLvl2 = categoryIdLvl2,
            categoryModel = categoryModel
        )

        val sharingModel = CategorySharingModel(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3,
            title = title,
            deeplinkParam = constructedLink.first,
            url = constructedLink.second,
            utmCampaignList = utmCampaignList
        )

        `Then assert share live data`(sharingModel)
    }

    private fun getConstructedLink(categoryUrl: String, categoryIdLvl2: String, categoryIdLvl3: String): Pair<String, String> {
        var deeplinkParam = "${TokoNowCategoryFragment.DEFAULT_DEEPLINK_PARAM}/${tokoNowCategoryViewModel.categoryL1}"
        var url = categoryUrl
        if (categoryIdLvl2.isNotBlank() && categoryIdLvl2 != TokoNowCategoryFragment.DEFAULT_CATEGORY_ID) {
            deeplinkParam += "/$categoryIdLvl2"
            url += String.format(TokoNowCategoryFragment.URL_PARAM_LVL_2, categoryIdLvl2)

            if (categoryIdLvl3.isNotBlank() && categoryIdLvl3 != TokoNowCategoryFragment.DEFAULT_CATEGORY_ID) {
                deeplinkParam += String.format(TokoNowCategoryFragment.DEEPLINK_PARAM_LVL_3, categoryIdLvl3)
                url += String.format(TokoNowCategoryFragment.URL_PARAM_LVL_3, categoryIdLvl3)
            }
        }
        return Pair(deeplinkParam, url)
    }

    private fun getUtmCampaignList(categoryIdLvl2: String, categoryIdLvl3: String): List<String> {
        val categoryId: String
        val categoryLvl: Int
        when {
            categoryIdLvl3.isNotBlank() && categoryIdLvl3 != TokoNowCategoryFragment.DEFAULT_CATEGORY_ID -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_3
                categoryId = categoryIdLvl3
            }
            categoryIdLvl2.isNotBlank() && categoryIdLvl2 != TokoNowCategoryFragment.DEFAULT_CATEGORY_ID -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_2
                categoryId = categoryIdLvl2
            }
            else -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_1
                categoryId = tokoNowCategoryViewModel.categoryL1
            }
        }
        return listOf(String.format(TokoNowCategoryFragment.PAGE_TYPE_CATEGORY, categoryLvl), categoryId)
    }

    private fun getTitleCategory(categoryIdLvl2: String, categoryModel: CategoryModel): String {
        return if (categoryIdLvl2.isNotBlank() && categoryIdLvl2 != TokoNowCategoryFragment.DEFAULT_CATEGORY_ID) {
            categoryModel.quickFilter.filter.first().title
        } else {
            categoryModel.categoryDetail.data.name
        }
    }

    private fun `Then assert share live data`(sharingModel: CategorySharingModel) {
        println(tokoNowCategoryViewModel.shareLiveData.value)
        println(sharingModel)

        Assert.assertTrue(tokoNowCategoryViewModel.shareLiveData.value == sharingModel)
    }
}
