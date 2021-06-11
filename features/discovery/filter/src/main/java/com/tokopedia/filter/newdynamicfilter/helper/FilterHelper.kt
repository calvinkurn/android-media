package com.tokopedia.filter.newdynamicfilter.helper

import android.text.TextUtils

import com.tokopedia.filter.common.data.CategoryFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Option.Companion.METRIC_INTERNATIONAL

object FilterHelper {

    @JvmStatic
    fun getSelectedCategoryDetailsFromFilterList(filterList: List<Filter>, categoryId: String): CategoryFilterModel? {
        return getSelectedCategoryDetails(getCategoryFilterFromList(filterList), categoryId)
    }

    private fun getCategoryFilterFromList(filterList: List<Filter>): Filter? {
        for (filter in filterList) {
            if (filter.isCategoryFilter) return filter
        }

        return null
    }

    @JvmStatic
    fun getSelectedCategoryDetails(categoryFilter: Filter?, categoryId: String): CategoryFilterModel? {
        return if (categoryFilter == null || TextUtils.isEmpty(categoryId)) {
            null
        } else findInRootCategoryList(categoryFilter, categoryId)

    }

    private fun findInRootCategoryList(categoryFilter: Filter, categoryId: String): CategoryFilterModel? {
        val rootCategoryList = categoryFilter.options

        for (rootCategory in rootCategoryList) {
            if (categoryId.equals(rootCategory.value)) {
                return CategoryFilterModel(categoryId, rootCategory.value, rootCategory.name)
            }

            val category = findInLevelTwoCategoryList(rootCategory, categoryId)
            if (category != null) return category
        }

        return null
    }

    private fun findInLevelTwoCategoryList(rootCategory: Option, categoryId: String): CategoryFilterModel? {
        val levelTwoCategoryList = rootCategory.levelTwoCategoryList

        for (levelTwoCategory in levelTwoCategoryList) {
            if (categoryId.equals(levelTwoCategory.value)) {
                return CategoryFilterModel(categoryId, rootCategory.value, levelTwoCategory.name)
            }

            val category = findInLevelThreeCategoryList(rootCategory, levelTwoCategory, categoryId)
            if (category != null) return category
        }

        return null
    }

    private fun findInLevelThreeCategoryList(rootCategory: Option, levelTwoCategory: LevelTwoCategory, categoryId: String): CategoryFilterModel? {
        val levelThreeCategoryList = levelTwoCategory.levelThreeCategoryList

        for (levelThreeCategory in levelThreeCategoryList) {
            if (categoryId.equals(levelThreeCategory.value)) {
                return CategoryFilterModel(categoryId, rootCategory.value, levelThreeCategory.name)
            }
        }

        return null
    }

    @JvmStatic
    fun initializeFilterList(filterList: List<Filter>?): List<Filter> {
        val list = filterList?.toMutableList() ?: mutableListOf()

        removeFiltersWithEmptyOption(list)
        mergeSizeFilterOptionsWithSameValue(list)
        removeBrandFilterOptionsWithSameValue(list)
        removeValueFromOptionWithInputTypeTextBox(list)

        return list
    }

    private fun removeFiltersWithEmptyOption(filterList: List<Filter>) {
        val iterator = filterList.iterator() as MutableIterator
        while (iterator.hasNext()) {
            val filter = iterator.next()
            if (filter.options.isEmpty() && !filter.isSeparator) {
                iterator.remove()
            }
        }
    }

    private fun mergeSizeFilterOptionsWithSameValue(filterList: List<Filter>) {
        val sizeFilter = getSizeFilter(filterList) ?: return

        val sizeFilterOptions = sizeFilter.options
        val iterator = sizeFilterOptions.iterator() as MutableIterator
        val optionMap = hashMapOf<String, Option>()

        while (iterator.hasNext()) {
            val option = iterator.next()
            val existingOption = optionMap.get(option.value)
            if (existingOption != null) {
                existingOption.name = existingOption.name + " / " + getFormattedSizeName(option)
                iterator.remove()
            } else {
                option.name = getFormattedSizeName(option)
                option.metric = ""
                optionMap.put(option.value, option)
            }
        }
    }

    private fun getSizeFilter(filterList: List<Filter>): Filter? {
        for (filter in filterList) {
            if (filter.isSizeFilter) return filter
        }
        return null
    }

    private fun getFormattedSizeName(option: Option): String {
        return if (METRIC_INTERNATIONAL.equals(option.metric)) {
            option.name
        } else {
            option.name + " " + option.metric
        }
    }

    private fun removeBrandFilterOptionsWithSameValue(filterList: List<Filter>) {
        val brandFilter = getBrandFilter(filterList) ?: return

        val brandFilterOptions = brandFilter.options
        val iterator = brandFilterOptions.iterator() as MutableIterator
        val optionMap = hashMapOf<String, Option>()

        while (iterator.hasNext()) {
            val option = iterator.next()
            val existingOption = optionMap.get(option.value)
            if (existingOption != null) {
                iterator.remove()
            } else {
                optionMap.put(option.value, option)
            }
        }
    }

    private fun getBrandFilter(filterList: List<Filter>): Filter? {
        for (filter in filterList) {
            if (filter.isBrandFilter) return filter
        }
        return null
    }

    private fun removeValueFromOptionWithInputTypeTextBox(filterList: List<Filter>) {
        for (filter in filterList) {
            for (option in filter.options) {
                if (option.inputType.equals(Option.INPUT_TYPE_TEXTBOX)) {
                    option.value = ""
                }
            }
        }
    }

    fun createParamsWithoutExcludes(mapParameter: Map<String, String>): Map<String, String> {
        val returnValue = mutableMapOf<String, String>()
        val paramWithExcludeKey = mutableMapOf<String, String>()

        mapParameter.forEach { (key, value) ->
            if (key.startsWith(OptionHelper.EXCLUDE_PREFIX))
                paramWithExcludeKey[key] = value
            else
                returnValue[key] = value
        }

        paramWithExcludeKey.forEach { (key, value) ->
            val actualKey = key.removePrefix(OptionHelper.EXCLUDE_PREFIX)
            if (!returnValue.containsKey(actualKey))
                returnValue[actualKey] = value
        }

        return returnValue
    }

    fun copyFilterWithOptionAsExclude(filters: List<Filter>): List<Filter> {
        return filters.map { filter ->
            Filter(
                    title = filter.title,
                    templateName = filter.templateName,
                    search = filter.search,
                    options = filter.options.map { option ->
                        OptionHelper.copyOptionAsExclude(option)
                    },
            )
        }
    }
}
