package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.di.DaggerInboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxModule;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;

import javax.inject.Inject;

public class FragmentProvideRating extends BaseDaggerFragment implements ProvideRatingContract.ProvideRatingView {

    @Inject
    ProvideRatingContract.ProvideRatingPresenter presenter;

    InboxComponent component;
    private TextView mTxtHelpTitle;
    private LinearLayout mSmileLayout;
    private TextView mTxtSmileSelected;
    private TextView mTxtFeedbackQuestion;
    private TextView mTxtFirstOption;
    private TextView mTxtSecondOption;
    private TextView mTxtThirdOption;
    private TextView mTxtFourthOption;
    private TextView mTxtFinished;

    public static FragmentProvideRating newInstance(Bundle bundle) {
        FragmentProvideRating fragment = new FragmentProvideRating();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        component = DaggerInboxComponent.builder()
                .inboxModule(new InboxModule(getContext()))
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_provide, container, false);
        initView(view);
        presenter = component.getProvideRatingPresenter();
        presenter.attachView(this);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void setFirstOption(String option) {
        mTxtFirstOption.setText(option);
    }

    @Override
    public void setSecondOption(String option) {
        mTxtSecondOption.setText(option);
    }

    @Override
    public void setThirdOption(String option) {
        mTxtThirdOption.setText(option);
    }

    @Override
    public void setFourthOption(String option) {
        mTxtFourthOption.setText(option);
    }

    private void initView(View view) {
        mTxtHelpTitle = view.findViewById(R.id.txt_help_title);
        mSmileLayout = view.findViewById(R.id.smile_layout);
        mTxtSmileSelected = view.findViewById(R.id.txt_smile_selected);
        mTxtFeedbackQuestion = view.findViewById(R.id.txt_feedback_question);
        mTxtFirstOption = view.findViewById(R.id.txt_first_option);
        mTxtSecondOption = view.findViewById(R.id.txt_second_option);
        mTxtThirdOption = view.findViewById(R.id.txt_third_option);
        mTxtFourthOption = view.findViewById(R.id.txt_fourth_option);
        mTxtFinished = view.findViewById(R.id.txt_finished);
    }
}
