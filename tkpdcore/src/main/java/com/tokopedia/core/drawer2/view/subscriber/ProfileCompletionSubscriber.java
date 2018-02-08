package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileCompletionSubscriber extends Subscriber<GetUserInfoDomainModel> {

    private final DrawerDataListener viewListener;

    public ProfileCompletionSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetProfileCompletion(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(GetUserInfoDomainModel getUserInfoDomainModel) {
        viewListener.onSuccessGetProfileCompletion(getUserInfoDomainModel.getGetUserInfoDomainData().getCompletion());
    }
}
