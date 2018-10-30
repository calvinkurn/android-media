package com.tokopedia.loginphone.verifyotptokocash.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;

/**
 * @author by nisie on 11/30/17.
 */

public class TokoCashVerificationFragment extends VerificationFragment {

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new TokoCashVerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
