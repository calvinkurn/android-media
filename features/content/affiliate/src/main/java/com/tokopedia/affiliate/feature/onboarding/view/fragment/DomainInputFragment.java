package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.activity.OnboardingActivity;
import com.tokopedia.affiliate.feature.onboarding.view.widget.PrefixEditText;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.user.session.UserSession;

/**
 * @author by milhamj on 9/24/18.
 */
public class DomainInputFragment extends BaseDaggerFragment {

    private static final Integer USERNAME_MAX_LENGTH = 15;
    private static final Integer USERNAME_MIN_LENGTH = 3;

    private UserSession userSession;
    private ImageView avatar;
    private TkpdHintTextInputLayout usernameWrapper;
    private PrefixEditText usernameInput;
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
        avatar = view.findViewById(R.id.avatar);
        usernameWrapper = view.findViewById(R.id.usernameWrapper);
        usernameInput = view.findViewById(R.id.usernameInput);
        termsAndCondition = view.findViewById(R.id.termsAndCondition);
        saveBtn = view.findViewById(R.id.saveBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initVar() {
        userSession = new UserSession(getContext());
    }

    private void initView() {
        ImageHandler.loadImageCircle2(getContext(), avatar, userSession.getProfilePicture());
        saveBtn.setEnabled(false);
        usernameInput.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String byMeUsername = usernameInput.getTextWithoutPrefix();

                if (byMeUsername.length() > USERNAME_MAX_LENGTH) {
                    usernameInput.removeTextChangedListener(this);
                    usernameInput.setText(byMeUsername.substring(0, USERNAME_MAX_LENGTH));
                    usernameInput.addTextChangedListener(this);
                }

                if (byMeUsername.length() < USERNAME_MIN_LENGTH) {
                    usernameWrapper.setError(String.format(
                            getString(R.string.af_minimal_character),
                            USERNAME_MIN_LENGTH)
                    );
                    disableSaveBtn();
                } else {
                    usernameWrapper.setError(null);
                    enableSaveBtn();
                }
            }
        });
        saveBtn.setOnClickListener(view -> {
            startActivity(
                    OnboardingActivity.createIntent(getContext(), OnboardingActivity.FINISH_TRUE)
            );
        });
    }

    private void enableSaveBtn() {
        saveBtn.setEnabled(true);
    }

    private void disableSaveBtn() {
        saveBtn.setEnabled(false);
    }
}
