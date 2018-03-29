package com.tokopedia.gamification.cracktoken.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamification.R;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * Created by hendry on 28/03/18.
 */

public class FloatingEggButtonFragment extends BaseDaggerFragment {

    public static FloatingEggButtonFragment newInstance() {
        return new FloatingEggButtonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_floating_egg, container, false);
        view.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEggData();
    }

    // TODO load data to get egg/token data from server or cache here
    private void loadEggData(){
        onSuccessLoadTokopointsToken();
    }

    public void onSuccessLoadTokopointsToken(){
        //TODO will set the variable from the model here
        boolean offFlag = true;
        int sumToken = 99;
        String sumTokenString = "99+";
        String tokenUnit = "buah";
        //String pageUrl = "http://tokopedia.com";
        //String appLink = "tokopedia://";
        long timeRemainingSeconds = 61;
        boolean isShowTime = true;

        String imageUrl = "https://user-images.githubusercontent.com/13778932/38075015-049430aa-335b-11e8-822e-ca662dc45b7f.png";




    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
