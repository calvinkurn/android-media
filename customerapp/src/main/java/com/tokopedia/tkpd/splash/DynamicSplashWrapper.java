package com.tokopedia.tkpd.splash;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DynamicSplashWrapper {

    public static DynamicSplash transform(String dataRemoteConfig) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<DynamicSplash>(){}.getType();
            return gson.fromJson(dataRemoteConfig, type);
        } catch (JsonSyntaxException ignored) {
            return null;
        }
    }

    public static DynamicSplash getDefaultVal() {
        DynamicSplash data = new DynamicSplash();
        data.setMainLogo("https://ecs7.tokopedia.net/img/android/totopolis/splash/main/ic_system_splash_normal.png");
        List<DynamicBackground> backgrounds = new ArrayList<>();
        backgrounds.add(new DynamicBackground("https://ecs7.tokopedia.net/img/android/totopolis/splash/background/bg_system_splash1_normal.png"));
        backgrounds.add(new DynamicBackground("https://ecs7.tokopedia.net/img/android/totopolis/splash/background/bg_system_splash2_normal.png"));
        backgrounds.add(new DynamicBackground("https://ecs7.tokopedia.net/img/android/totopolis/splash/background/bg_system_splash3_normal.png"));
        backgrounds.add(new DynamicBackground("https://ecs7.tokopedia.net/img/android/totopolis/splash/background/bg_system_splash4_normal.png"));
        backgrounds.add(new DynamicBackground("https://ecs7.tokopedia.net/img/android/totopolis/splash/background/bg_system_splash5_normal.png"));
        data.setBackground(backgrounds);
        return data;
    }
}
