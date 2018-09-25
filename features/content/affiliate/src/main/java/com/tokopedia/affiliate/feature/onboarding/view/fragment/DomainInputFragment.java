package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.R;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.text.TkpdHintTextInputLayout;

/**
 * @author by milhamj on 9/24/18.
 */
public class DomainInputFragment extends BaseDaggerFragment {

    private ImageView avatar;
    private TkpdHintTextInputLayout usernameWrapper;
    private TextInputEditText usernameInput;
    private TextView termsAndCondition;
    private ButtonCompat saveBtn;

    public static DomainInputFragment newInstance() {
        DomainInputFragment fragment = new DomainInputFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_domain_input, container, false);

        return view;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }


}
