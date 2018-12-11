package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core2.R;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.MethodChecker;

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
            viewListener.onGetProfile(
                    convertToViewModel(
                            profileModel.getProfileData()));
        else
            viewListener.onErrorGetProfile(
                    viewListener.getString(R.string.default_request_error_unknown));
    }

    private DrawerProfile convertToViewModel(ProfileData profileData) {
        DrawerProfile drawerProfile = new DrawerProfile();
        if (profileData.getShopInfo() != null) {
            if (profileData.getShopInfo().getShopAvatar() != null)
                drawerProfile.setShopAvatar(profileData.getShopInfo().getShopAvatar());
            if (profileData.getShopInfo().getShopCover() != null)
                drawerProfile.setShopCover(profileData.getShopInfo().getShopCover());
            if (profileData.getShopInfo().getShopName() != null)
                drawerProfile.setShopName(String.valueOf(MethodChecker.fromHtml(
                        profileData.getShopInfo().getShopName())));
        }
        if (profileData.getUserInfo() != null) {
            if (profileData.getUserInfo().getUserImage() != null)
                drawerProfile.setUserAvatar(profileData.getUserInfo().getUserImage());
            if (profileData.getUserInfo().getUserName() != null)
                drawerProfile.setUserName(String.valueOf(MethodChecker.fromHtml(
                        profileData.getUserInfo().getUserName())));
        }
        return drawerProfile;
    }
}
