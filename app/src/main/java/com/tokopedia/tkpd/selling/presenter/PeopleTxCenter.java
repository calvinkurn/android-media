package com.tokopedia.tkpd.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.session.baseFragment.BaseImpl;

/**
 * Created by Toped10 on 7/15/2016.
 */
public abstract class PeopleTxCenter extends BaseImpl<PeopleTxCenterView> {

    public PeopleTxCenter(PeopleTxCenterView view) {
        super(view);
    }

    public abstract void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context);

    public abstract void setLocalyticFlow(Activity context);
}
