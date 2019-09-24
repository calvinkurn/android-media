package com.tokopedia.filter.newdynamicfilter.helper;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.filter.common.data.Category;
import com.tokopedia.filter.common.data.LevelThreeCategory;
import com.tokopedia.filter.common.data.LevelTwoCategory;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by henrypriyono on 8/22/17.
 */

public class OptionHelper {

    public static final String VALUE_SEPARATOR = ",";

    public static void saveOptionShownInMainState(Option option,
                                                  HashMap<String, Boolean> shownInMainState) {

        if (!TextUtils.isEmpty(option.getInputState()) && Boolean.parseBoolean(option.getInputState())) {
            shownInMainState.put(option.getUniqueId(), true);
        } else {
            shownInMainState.remove(option.getUniqueId());
        }
    }

    public static void saveOptionInputState(Option option,
                                            HashMap<String, Boolean> checkedState,
                                            HashMap<String, String> savedTextInput) {

        if (Option.INPUT_TYPE_CHECKBOX.equals(option.getInputType())) {
            saveCheckboxOptionInputState(option, checkedState);
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.getInputType())) {
            saveTextboxOptionInputState(option, savedTextInput);
        }
    }

    public static void saveOptionInputState(Option option, HashMap<String, Boolean> flagFilterHelper) {
        saveCheckboxOptionInputState(option, flagFilterHelper);
    }

    private static void saveCheckboxOptionInputState(Option option,
                                                     HashMap<String, Boolean> checkedState) {

        if (!TextUtils.isEmpty(option.getInputState()) && Boolean.parseBoolean(option.getInputState())) {
            checkedState.put(option.getUniqueId(), true);
        } else {
            checkedState.remove(option.getUniqueId());
        }
    }

    private static void saveTextboxOptionInputState(Option option,
                                                    HashMap<String, String> savedTextInput) {

        savedTextInput.put(option.getKey(), option.getInputState());
    }

    public static String loadOptionInputState(Option option,
                                            HashMap<String, Boolean> checkedState,
                                            HashMap<String, String> savedTextInput) {

        if (Option.INPUT_TYPE_CHECKBOX.equals(option.getInputType())) {
            return loadCheckboxOptionInputState(option, checkedState);
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.getInputType())) {
            return loadTextboxOptionInputState(option, savedTextInput);
        } else {
            return "";
        }
    }

    private static String loadCheckboxOptionInputState(Option option,
                                                       HashMap<String, Boolean> checkedState) {

        Boolean isChecked = checkedState.get(option.getUniqueId());
        if (Boolean.TRUE.equals(isChecked)) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    private static String loadTextboxOptionInputState(Option option,
                                                      HashMap<String, String> savedTextInput) {

        String result = savedTextInput.get(option.getKey());
        return TextUtils.isEmpty(result) ? "" : result;
    }

    public static String parseKeyFromUniqueId(String uniqueId) {
        int firstSeparatorPos = uniqueId.indexOf(Option.UID_FIRST_SEPARATOR_SYMBOL);
        return uniqueId.substring(0, firstSeparatorPos);
    }

    public static String parseValueFromUniqueId(String uniqueId) {
        int firstSeparatorPos = uniqueId.indexOf(Option.UID_FIRST_SEPARATOR_SYMBOL);
        int secondSeparatorPos = uniqueId.indexOf(Option.UID_SECOND_SEPARATOR_SYMBOL);
        return uniqueId.substring(firstSeparatorPos + Option.UID_FIRST_SEPARATOR_SYMBOL.length(), secondSeparatorPos);
    }

    public static String parseNameFromUniqueId(String uniqueId) {
        int secondSeparatorPos = uniqueId.indexOf(Option.UID_SECOND_SEPARATOR_SYMBOL);
        return uniqueId.substring(secondSeparatorPos + Option.UID_SECOND_SEPARATOR_SYMBOL.length(), uniqueId.length());
    }

    public static void bindOptionWithCheckbox(final Option option,
                                              CheckBox checkBox,
                                              final DynamicFilterDetailView filterDetailView) {

        checkBox.setOnCheckedChangeListener(null);

        if (!TextUtils.isEmpty(option.getInputState())) {
            checkBox.setChecked(Boolean.parseBoolean(option.getInputState()));
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterDetailView.onItemCheckedChanged(option, isChecked);
            }
        });
    }

    public static List<Category> convertToCategoryList(List<Option> optionList) {
        List<Category> categoryList = new ArrayList<>();

        for (Option option : optionList) {
            categoryList.add(convertToRootCategory(option));
        }

        return categoryList;
    }

    private static Category convertToRootCategory(Option option) {
        Category category = new Category();
        category.setName(option.getName());
        category.setId(option.getValue());
        category.setIconImageUrl(option.getIconUrl());
        category.setIndentation(1);
        category.setKey(option.getKey());

        List<Category> levelTwoCategoryList
                = convertToLevelTwoCategoryList(option.getLevelTwoCategoryList());

        if (!levelTwoCategoryList.isEmpty()) {
            category.setHasChild(true);
            category.setChildren(levelTwoCategoryList);
        } else {
            category.setHasChild(false);
        }

        return category;
    }

    private static List<Category> convertToLevelTwoCategoryList(List<LevelTwoCategory> levelTwoCategories) {
        List<Category> categoryList = new ArrayList<>();
        if (levelTwoCategories == null) {
            return categoryList;
        }

        for (LevelTwoCategory levelTwoCategory : levelTwoCategories) {
            categoryList.add(convertToLevelTwoCategory(levelTwoCategory));
        }

        return categoryList;
    }

    private static Category convertToLevelTwoCategory(LevelTwoCategory levelTwoCategory) {
        Category category = new Category();
        category.setName(levelTwoCategory.getName());
        category.setId(levelTwoCategory.getValue());
        category.setIndentation(2);
        category.setKey(levelTwoCategory.getKey());

        List<Category> levelThreeCategoryList
                = convertToLevelThreeCategoryList(levelTwoCategory.getLevelThreeCategoryList());

        if (!levelThreeCategoryList.isEmpty()) {
            category.setHasChild(true);
            category.setChildren(levelThreeCategoryList);
        } else {
            category.setHasChild(false);
        }

        return category;
    }

    private static List<Category> convertToLevelThreeCategoryList(List<LevelThreeCategory> levelThreeCategoryList) {
        List<Category> categoryList = new ArrayList<>();
        if (levelThreeCategoryList == null) {
            return categoryList;
        }

        for (LevelThreeCategory levelThreeCategory : levelThreeCategoryList) {
            categoryList.add(convertToLevelThreeCategory(levelThreeCategory));
        }

        return categoryList;
    }

    private static Category convertToLevelThreeCategory(LevelThreeCategory levelThreeCategory) {
        Category category = new Category();
        category.setName(levelThreeCategory.getName());
        category.setId(levelThreeCategory.getValue());
        category.setIndentation(3);
        category.setHasChild(false);
        category.setKey(levelThreeCategory.getKey());
        return category;
    }

    public static Option generateOptionFromCategory(String categoryId, String categoryName) {
        Option option = new Option();
        option.setName(categoryName);
        option.setKey(Option.KEY_CATEGORY);
        option.setValue(categoryId);
        return option;
    }

    public static Option generateOptionFromUniqueId(String uniqueId) {
        String key = parseKeyFromUniqueId(uniqueId);
        String value = parseValueFromUniqueId(uniqueId);
        String name = parseNameFromUniqueId(uniqueId);

        Option option = new Option();
        option.setKey(key);
        option.setValue(value);
        option.setName(name);

        return option;
    }

    public static String constructUniqueId(String key, String value, String name) {
        return key + Option.UID_FIRST_SEPARATOR_SYMBOL + value + Option.UID_SECOND_SEPARATOR_SYMBOL + name;
    }

    public static List<Option> combinePriceFilterIfExists(List<Option> activeFilterOptionList, String combinedPriceFilterName) {
        List<Option> returnedActiveFilterOptionList = new ArrayList<>(activeFilterOptionList);

        boolean hasActivePriceFilter = false;

        Iterator<Option> iterator = returnedActiveFilterOptionList.iterator();
        while(iterator.hasNext()) {
            Option option = iterator.next();

            if(isPriceOption(option)) {
                hasActivePriceFilter = true;
                iterator.remove();
            }
        }

        addGenericFilterOptionIfPriceFilterActive(hasActivePriceFilter, returnedActiveFilterOptionList, combinedPriceFilterName);

        return returnedActiveFilterOptionList;
    }

    private static boolean isPriceOption(Option option) {
        return option.getKey().equals(SearchApiConst.PMIN) || option.getKey().equals(SearchApiConst.PMAX);
    }

    private static void addGenericFilterOptionIfPriceFilterActive(boolean hasActivePriceFilter, List<Option> activeFilterOptionList, String combinedPriceFilterName) {
        if(hasActivePriceFilter) {
            activeFilterOptionList.add(
                    generateOptionFromUniqueId(
                            constructUniqueId(Option.KEY_PRICE_MIN, "", combinedPriceFilterName)
                    )
            );
        }
    }
}
