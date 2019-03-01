package com.tokopedia.core.util;

import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.user.session.UserSession;

public class DataMapper {

    public static LinkerShareData getLinkerShareData(LinkerData linkerData){
        UserSession userSession = new UserSession(LinkerManager.getInstance().getContext());

        UserData userData = new UserData();
        userData.setName(userSession.getName());
        userData.setPhoneNumber(userSession.getPhoneNumber());
        userData.setUserId(userSession.getUserId());
        userData.setEmail(userSession.getEmail());
        userData.setFirstTimeUser(userSession.isFirstTimeUser());
        userData.setLoggedin(userSession.isLoggedIn());

        LinkerShareData linkerShareData = new LinkerShareData();
        linkerShareData.setLinkerData(linkerData);
        linkerShareData.setUserData(userData);

        return linkerShareData;
    }

}
