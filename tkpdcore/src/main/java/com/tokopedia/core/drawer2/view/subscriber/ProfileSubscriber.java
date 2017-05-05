package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerDataListener;

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

    }

    @Override
    public void onNext(ProfileModel profileModel) {
        if(profileModel.isSuccess())
            viewListener.onGetProfile(convertToViewModel(profileModel.getProfileData()));
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
