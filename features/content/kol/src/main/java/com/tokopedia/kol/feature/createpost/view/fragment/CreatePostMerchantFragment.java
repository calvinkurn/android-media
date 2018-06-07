package com.tokopedia.kol.feature.createpost.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostMerchantFragment extends BaseDaggerFragment {

    public static CreatePostMerchantFragment newInstance(Bundle bundle) {
        CreatePostMerchantFragment fragment = new CreatePostMerchantFragment();
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
