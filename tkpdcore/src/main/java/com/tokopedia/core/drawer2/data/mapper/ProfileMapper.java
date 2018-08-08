package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileMapper implements Func1<Response<TkpdResponse>, ProfileModel> {
    @Override
    public ProfileModel call(Response<TkpdResponse> response) {
        ProfileModel model = new ProfileModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ProfileData data = response.body().convertDataObj(ProfileData.class);
                model.setSuccess(true);
                model.setProfileData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
