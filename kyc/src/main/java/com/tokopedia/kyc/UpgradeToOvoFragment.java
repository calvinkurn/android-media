package com.tokopedia.kyc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

public class UpgradeToOvoFragment extends BaseDaggerFragment implements UpgradeToOvoContract.View{
    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showSnackbarErrorMessage(String message) {

    }

    @Override
    public String getErrorMessage(Throwable e) {
        return null;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upgrade_ovo, container, false);
        activationOvoBtn = view.findViewById(R.id.activation_ovo_btn);
        learnMoreOvoBtn = view.findViewById(R.id.learn_more_ovo_btn);
        titleOvo = view.findViewById(R.id.title_intro);
        imgIntroOvo = view.findViewById(R.id.image_ovo);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }
}
