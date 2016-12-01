package com.tokopedia.core.rescenter.create.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.core.rescenter.create.fragment.ChooseTroubleFragment;
import com.tokopedia.core.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.core.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 6/16/16.
 */
public class CreateResCenterImpl implements CreateResCenterPresenter {

    private final CreateResCenterListener listener;

    public CreateResCenterImpl(CreateResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    public void initFragment(@NonNull Context context, Uri uriData, Bundle bundleData) {
        listener.inflateFragment(
                ChooseTroubleFragment.newInstance(generatePassData(bundleData, uriData)),
                ChooseTroubleFragment.class.getSimpleName()
        );
    }

    private ActionParameterPassData generatePassData(Bundle bundleData, Uri uriData) {
        ActionParameterPassData passData = new ActionParameterPassData();
        if (bundleData != null) {
            passData.setOrderID(bundleData.getString(CreateResCenterActivity.KEY_PARAM_ORDER_ID));
            passData.setFlagReceived(bundleData.getInt(CreateResCenterActivity.KEY_PARAM_FLAG_RECEIVED));
            if (bundleData.getInt(CreateResCenterActivity.KEY_PARAM_FLAG_RECEIVED) == 0) {
                passData.setTroubleID(bundleData.getInt(CreateResCenterActivity.KEY_PARAM_TROUBLE_ID));
                passData.setSolutionID(bundleData.getInt(CreateResCenterActivity.KEY_PARAM_SOLUTION_ID));
            }
        }
        return passData;
    }
}
