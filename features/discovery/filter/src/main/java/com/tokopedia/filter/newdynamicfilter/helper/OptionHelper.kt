package com.tokopedia.filter.newdynamicfilter.helper

import android.text.TextUtils
import android.widget.CheckBox
import android.widget.CompoundButton
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Category
import com.tokopedia.filter.common.data.LevelThreeCategory
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView
import java.util.*

object OptionHelper {

    const val VALUE_SEPARATOR = ","
    const val OPTION_SEPARATOR = "#"
    const val EXCLUDE_PREFIX = "exclude_"

    @JvmStatic
    fun saveOptionShownInMainState(option: Option,
                                   shownInMainState: HashMap<String, Boolean>) {

        if (!TextUtils.isEmpty(option.inputState) && option.inputState.toBoolean()) {
            shownInMainState.put(option.uniqueId, true)
        } else {
            shownInMainState.remove(option.uniqueId)
        }
    }

    @JvmStatic
    fun saveOptionInputState(option: Option,
                             checkedState: HashMap<String, Boolean>,
                             savedTextInput: HashMap<String, String>) {

        if (Option.INPUT_TYPE_CHECKBOX.equals(option.inputType)) {
            saveCheckboxOptionInputState(option, checkedState)
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.inputType)) {
            saveTextboxOptionInputState(option, savedTextInput)
        }
    }

    @JvmStatic
    fun saveOptionInputState(option: Option, flagFilterHelper: HashMap<String, Boolean>) {
        saveCheckboxOptionInputState(option, flagFilterHelper)
    }

    private fun saveCheckboxOptionInputState(option: Option,
                                             checkedState: HashMap<String, Boolean>) {

        if (!TextUtils.isEmpty(option.inputState) && option.inputState.toBoolean()) {
            checkedState.put(option.uniqueId, true)
        } else {
            checkedState.remove(option.uniqueId)
        }
    }

    private fun saveTextboxOptionInputState(option: Option,
                                            savedTextInput: HashMap<String, String>) {

        savedTextInput.put(option.key, option.inputState)
    }

    @JvmStatic
    fun loadOptionInputState(option: Option,
                             checkedState: HashMap<String, Boolean>,
                             savedTextInput: HashMap<String, String>): String {

        return if (Option.INPUT_TYPE_CHECKBOX.equals(option.inputType)) {
            loadCheckboxOptionInputState(option, checkedState)
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.inputType)) {
            loadTextboxOptionInputState(option, savedTextInput)
        } else {
            ""
        }
    }

    private fun loadCheckboxOptionInputState(option: Option,
                                             checkedState: HashMap<String, Boolean>): String {

        val isChecked = checkedState.get(option.uniqueId)
        return if (true.equals(isChecked)) {
            true.toString()
        } else {
            false.toString()
        }
    }

    private fun loadTextboxOptionInputState(option: Option,
                                            savedTextInput: HashMap<String, String>): String {

        val result = savedTextInput.get(option.key)
        return if (TextUtils.isEmpty(result)) "" else result.toString()
    }

    @JvmStatic
    fun parseKeyFromUniqueId(uniqueId: String): String {
        val firstSeparatorPos = uniqueId.indexOf(Option.UID_FIRST_SEPARATOR_SYMBOL)
        return uniqueId.substring(0, firstSeparatorPos)
    }

    @JvmStatic
    fun parseValueFromUniqueId(uniqueId: String): String {
        val firstSeparatorPos = uniqueId.indexOf(Option.UID_FIRST_SEPARATOR_SYMBOL)
        val secondSeparatorPos = uniqueId.indexOf(Option.UID_SECOND_SEPARATOR_SYMBOL)
        return uniqueId.substring(firstSeparatorPos + Option.UID_FIRST_SEPARATOR_SYMBOL.length, secondSeparatorPos)
    }

    @JvmStatic
    fun parseNameFromUniqueId(uniqueId: String): String {
        val secondSeparatorPos = uniqueId.indexOf(Option.UID_SECOND_SEPARATOR_SYMBOL)
        return uniqueId.substring(secondSeparatorPos + Option.UID_SECOND_SEPARATOR_SYMBOL.length, uniqueId.length)
    }

    @JvmStatic
    fun bindOptionWithCheckbox(option: Option,
                               checkBox: CheckBox,
                               filterDetailView: DynamicFilterDetailView) {

        checkBox.setOnCheckedChangeListener(null)

        if (!TextUtils.isEmpty(option.inputState)) {
            checkBox.isChecked = option.inputState.toBoolean()
        } else {
            checkBox.setChecked(false)
        }

        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                filterDetailView.onItemCheckedChanged(option, isChecked)
            }
        })
    }

    @JvmStatic
    fun convertToCategoryList(optionList: List<Option>): List<Category> {
        val categoryList = arrayListOf<Category>()

        for (option in optionList) {
            categoryList.add(convertToRootCategory(option))
        }

        return categoryList
    }

    private fun convertToRootCategory(option: Option): Category {
        val category = Category()
        category.name = option.name
        category.id = option.value
        category.iconImageUrl = option.iconUrl
        category.indentation = 1
        category.key = option.key

        val levelTwoCategoryList = convertToLevelTwoCategoryList(option.levelTwoCategoryList)

        if (!levelTwoCategoryList.isEmpty()) {
            category.hasChild = true
            category.children = levelTwoCategoryList
        } else {
            category.hasChild = false
        }

        return category
    }

    private fun convertToLevelTwoCategoryList(levelTwoCategories: List<LevelTwoCategory>?): List<Category> {
        val categoryList = arrayListOf<Category>()
        if (levelTwoCategories == null) {
            return categoryList
        }

        for (levelTwoCategory in levelTwoCategories) {
            categoryList.add(convertToLevelTwoCategory(levelTwoCategory))
        }

        return categoryList
    }

    private fun convertToLevelTwoCategory(levelTwoCategory: LevelTwoCategory): Category {
        val category = Category()
        category.name = levelTwoCategory.name
        category.id = levelTwoCategory.value
        category.indentation = 2
        category.key = levelTwoCategory.key

        val levelThreeCategoryList = convertToLevelThreeCategoryList(levelTwoCategory.levelThreeCategoryList)

        if (!levelThreeCategoryList.isEmpty()) {
            category.hasChild = true
            category.children = levelThreeCategoryList
        } else {
            category.hasChild = false
        }

        return category
    }

    private fun convertToLevelThreeCategoryList(levelThreeCategoryList: List<LevelThreeCategory>?): List<Category> {
        val categoryList = arrayListOf<Category>()
        if (levelThreeCategoryList == null) {
            return categoryList
        }

        for (levelThreeCategory in levelThreeCategoryList) {
            categoryList.add(convertToLevelThreeCategory(levelThreeCategory))
        }

        return categoryList
    }

    private fun convertToLevelThreeCategory(levelThreeCategory: LevelThreeCategory): Category {
        val category = Category()
        category.name = levelThreeCategory.name
        category.id = levelThreeCategory.value
        category.indentation = 3
        category.hasChild = false
        category.key = levelThreeCategory.key
        return category
    }

    @JvmStatic
    fun generateOptionFromCategory(categoryId: String, categoryName: String): Option {
        val option = Option()
        option.name = categoryName
        option.key = Option.KEY_CATEGORY
        option.value = categoryId
        return option
    }

    @JvmStatic
    fun generateOptionFromUniqueId(uniqueId: String): Option {
        val key = parseKeyFromUniqueId(uniqueId)
        val value = parseValueFromUniqueId(uniqueId)
        val name = parseNameFromUniqueId(uniqueId)

        val option = Option()
        option.key = key
        option.value = value
        option.name = name

        return option
    }

    @JvmStatic
    fun constructUniqueId(key: String, value: String, name: String): String {
        return key + Option.UID_FIRST_SEPARATOR_SYMBOL + value + Option.UID_SECOND_SEPARATOR_SYMBOL + name
    }

    @JvmStatic
    fun combinePriceFilterIfExists(activeFilterOptionList: List<Option>, combinedPriceFilterName: String): List<Option> {
        val returnedActiveFilterOptionList = ArrayList(activeFilterOptionList)

        var hasActivePriceFilter = false

        val iterator = returnedActiveFilterOptionList.iterator()
        while (iterator.hasNext()) {
            val option = iterator.next()

            if (isPriceOption(option)) {
                hasActivePriceFilter = true
                iterator.remove()
            }
        }

        addGenericFilterOptionIfPriceFilterActive(hasActivePriceFilter, returnedActiveFilterOptionList, combinedPriceFilterName)

        return returnedActiveFilterOptionList
    }

    private fun isPriceOption(option: Option): Boolean {
        return option.key.equals(SearchApiConst.PMIN) || option.key.equals(SearchApiConst.PMAX)
    }

    private fun addGenericFilterOptionIfPriceFilterActive(hasActivePriceFilter: Boolean, activeFilterOptionList: List<Option>, combinedPriceFilterName: String) {
        if (hasActivePriceFilter) {
            (activeFilterOptionList as MutableList).add(
                    generateOptionFromUniqueId(
                            constructUniqueId(Option.KEY_PRICE_MIN, "", combinedPriceFilterName)
                    )
            )
        }
    }

    fun getKeyRemoveExclude(option: Option) = option.key.removePrefix(EXCLUDE_PREFIX)

    fun copyOptionAsExclude(option: Option): Option {
        return Option(
                name = option.name,
                key = EXCLUDE_PREFIX + option.key,
                value = option.value,
        )
    }
}
