package com.tokopedia.remoteconfig;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GraphqlHelper {
    public static String QUERY = "query";
    public static String VARIABLES = "variables";
    public static String OPERATION_NAME = "operationName";

    public static String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    public static String streamToString(InputStream in) {
        String temp;
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}