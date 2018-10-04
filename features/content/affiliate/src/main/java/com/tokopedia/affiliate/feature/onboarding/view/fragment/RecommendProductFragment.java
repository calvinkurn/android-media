package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductFragment extends BaseDaggerFragment {

    public static RecommendProductFragment newInstance(@NonNull Bundle bundle) {
        RecommendProductFragment fragment = new RecommendProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
