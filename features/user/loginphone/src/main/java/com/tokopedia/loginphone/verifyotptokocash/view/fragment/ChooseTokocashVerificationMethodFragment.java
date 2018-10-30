package com.tokopedia.loginphone.verifyotptokocash.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseTokocashVerificationMethodFragment extends ChooseVerificationMethodFragment {

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseTokocashVerificationMethodFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
