package com.tokopedia.tokocash.historytokocash.presentation;

import com.tokopedia.core.app.MainApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public class Util {

    public static String loadJSONFromAsset(String nameJson) {
        String json;

        try {
            InputStream is = MainApplication.getAppContext().getAssets().open("json/" + nameJson);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
