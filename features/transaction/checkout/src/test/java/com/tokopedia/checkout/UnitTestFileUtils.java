package com.tokopedia.checkout;

import java.io.IOException;
import java.io.InputStream;

public class UnitTestFileUtils {

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

