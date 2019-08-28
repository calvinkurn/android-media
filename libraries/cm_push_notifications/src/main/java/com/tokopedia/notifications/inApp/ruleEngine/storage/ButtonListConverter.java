package com.tokopedia.notifications.inApp.ruleEngine.storage;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ButtonListConverter {
    @TypeConverter
    public ArrayList<CMButton> toButton(String value) {
        if (value == null)
            return null;
        Type listType = new TypeToken<ArrayList<CMButton>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String toJson(ArrayList<CMButton> list) {
        if (list == null)
            return null;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}