package com.tokopedia.mitra.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.presentation.activity.MitraDigitalCategoryActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitraHelpFragment extends Fragment {

    private Button buttonPulsa, buttonBPJS, buttonPDAM, buttonVoucherGame;

    public MitraHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mitra_help, container, false);

        buttonPulsa = rootview.findViewById(R.id.button_pulsa);
        buttonBPJS = rootview.findViewById(R.id.button_bpjs);
        buttonPDAM = rootview.findViewById(R.id.button_pdam);
        buttonVoucherGame = rootview.findViewById(R.id.button_voucher_game);

        buttonPulsa.setOnClickListener(view -> {
            Intent intent = MitraDigitalCategoryActivity.newInstance(getActivity(), 1);
            startActivity(intent);
        });

        buttonBPJS.setOnClickListener(view -> {
            Intent intent = MitraDigitalCategoryActivity.newInstance(getActivity(), 4);
            startActivity(intent);
        });

        buttonPDAM.setOnClickListener(view -> {
            Intent intent = MitraDigitalCategoryActivity.newInstance(getActivity(), 5);
            startActivity(intent);
        });

        buttonVoucherGame.setOnClickListener(view -> {
            Intent intent = MitraDigitalCategoryActivity.newInstance(getActivity(), 6);
            startActivity(intent);
        });

        return rootview;
    }

    public static MitraHelpFragment newInstance() {
        return new MitraHelpFragment();
    }
}
