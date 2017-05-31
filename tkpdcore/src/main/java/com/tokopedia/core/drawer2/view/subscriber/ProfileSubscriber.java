package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileSubscriber extends Subscriber<ProfileModel> {

    private final DrawerDataListener viewListener;

    public ProfileSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetProfile(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ProfileModel profileModel) {
        if (profileModel.isSuccess())
            viewListener.onGetProfile(convertToViewModel(profileModel.getProfileData()));
        else
            viewListener.onErrorGetProfile(profileModel.getErrorMessage());
    }

    private DrawerProfile convertToViewModel(ProfileData profileData) {
        DrawerProfile drawerProfile = new DrawerProfile();
        drawerProfile.setShopAvatar(profileData.getShopInfo().getShopAvatar());
        drawerProfile.setShopCover(profileData.getShopInfo().getShopCover());
        drawerProfile.setShopName(profileData.getShopInfo().getShopName());
        drawerProfile.setUserAvatar(profileData.getUserInfo().getUserImage());
        drawerProfile.setUserName(profileData.getUserInfo().getUserName());
        return drawerProfile;
    }
}
