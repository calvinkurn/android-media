package com.gokil;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;

public class TestAja {
    public static void googlingAja(){
        String test = AuthHelper.Companion.getVersionName().invoke(GlobalConfig.VERSION_NAME);
    }
}
