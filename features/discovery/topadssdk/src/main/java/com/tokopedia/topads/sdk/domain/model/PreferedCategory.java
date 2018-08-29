package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 4/20/17.
 */

public class PreferedCategory {

    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_CATEGORY = "user_categories";
    private static final String KEY_USER_CATEGORY_ID = "user_categories_id";

    private int userType;
    private List<String> userCategories = new ArrayList<>();
    private List<Integer> userCategoriesId = new ArrayList<>();
    private String errorMessage;

    public PreferedCategory() {
    }

    public PreferedCategory(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_USER_TYPE)){
            setUserType(object.getInt(KEY_USER_TYPE));
        }
        if(!object.isNull(KEY_USER_CATEGORY)) {
            JSONArray array = object.getJSONArray(KEY_USER_CATEGORY);
            for (int i = 0; i < array.length(); i++) {
                userCategories.add(array.getString(i));
            }
        }
        if(!object.isNull(KEY_USER_CATEGORY_ID)) {
            JSONArray array = object.getJSONArray(KEY_USER_CATEGORY_ID);
            for (int i = 0; i < array.length(); i++) {
                userCategoriesId.add(array.getInt(i));
            }
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<String> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(List<String> userCategories) {
        this.userCategories = userCategories;
    }

    public List<Integer> getUserCategoriesId() {
        return userCategoriesId;
    }

    public void setUserCategoriesId(List<Integer> userCategoriesId) {
        this.userCategoriesId = userCategoriesId;
    }
}
