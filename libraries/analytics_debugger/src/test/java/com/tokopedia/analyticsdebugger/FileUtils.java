package com.tokopedia.analyticsdebugger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public List<Map<String, Object>> getJsonArrayResources(String path) {
        String j = getJsonFromAsset(path);
        Gson gson = new Gson();
        Type t = new TypeToken<ArrayList>(){}.getType();
        return gson.fromJson(j, t);
    }

    public Map<String, Object> getJsonObjResources(String path) {
        String j = getJsonFromAsset(path);
        Gson gson = new Gson();
        Type t = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(j, t);
    }

    public String getJsonFromAsset(String path) {
        String json = "";
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}

