package com.tokopedia.tkpd.flight.presentation;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.tkpd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightPhoneVerificationFragment extends Fragment {
    private PhoneVerificationFragment.PhoneVerificationFragmentListener phoneVerificationFragmentListener;

    public FlightPhoneVerificationFragment() {
        // Required empty public constructor
    }

    public static FlightPhoneVerificationFragment newInstance() {
        return new FlightPhoneVerificationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_phone_verification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addFragment(R.id.container, PhoneVerificationFragment.createInstance
                (phoneVerificationFragmentListener, true));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PhoneVerificationFragment.PhoneVerificationFragmentListener) {
            phoneVerificationFragmentListener = (PhoneVerificationFragment.PhoneVerificationFragmentListener) activity;
        } else {
            throw new RuntimeException("Activity must implement PhoneVerificationFragment.PhoneVerificationFragmentListener");
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof PhoneVerificationFragment.PhoneVerificationFragmentListener) {
            phoneVerificationFragmentListener = (PhoneVerificationFragment.PhoneVerificationFragmentListener) activity;
        } else {
            throw new RuntimeException("Activity must implement PhoneVerificationFragment.PhoneVerificationFragmentListener");
        }
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
