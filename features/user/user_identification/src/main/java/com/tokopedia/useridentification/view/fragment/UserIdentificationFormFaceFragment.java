package com.tokopedia.useridentification.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;

/**
 * @author by alvinatin on 09/11/18.
 */

public class UserIdentificationFormFaceFragment extends TkpdBaseV4Fragment{

    public static Fragment createInstance(){
        Fragment fragment = new UserIdentificationFormFaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
