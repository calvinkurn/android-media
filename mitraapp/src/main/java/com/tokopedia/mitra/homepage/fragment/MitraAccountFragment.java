package com.tokopedia.mitra.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.mitra.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitraAccountFragment extends Fragment {


    public MitraAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mitra_account, container, false);
    }

    public static MitraAccountFragment newInstance() {
        return new MitraAccountFragment();
    }
}
