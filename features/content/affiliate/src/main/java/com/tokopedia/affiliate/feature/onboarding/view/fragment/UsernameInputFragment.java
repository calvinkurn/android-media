package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.activity.UsernameInputActivity;
import com.tokopedia.affiliate.feature.onboarding.view.activity.OnboardingActivity;
import com.tokopedia.affiliate.feature.onboarding.view.activity.RecommendProductActivity;
import com.tokopedia.affiliate.feature.onboarding.view.adapter.SuggestionAdapter;
import com.tokopedia.affiliate.feature.onboarding.view.contract.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.widget.PrefixEditText;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 9/24/18.
 */
public class UsernameInputFragment extends BaseDaggerFragment
        implements UsernameInputContract.View {

    private static final Integer USERNAME_MAX_LENGTH = 15;
    private static final Integer USERNAME_MIN_LENGTH = 3;
    private static final Integer SHOW_SUGGESTION_LENGTH = 1;

    private ImageView avatar;
    private TkpdHintTextInputLayout usernameWrapper;
    private PrefixEditText usernameInput;
    private CardView suggestionCard;
    private RecyclerView suggestionRv;
    private TextView termsAndCondition;
    private ButtonCompat saveBtn;
    private View loadingView;

    private SuggestionAdapter adapter;

    @Inject
    private UsernameInputContract.Presenter presenter;

    private UserSession userSession;
    private String productId = "";

    public static UsernameInputFragment newInstance(@NonNull Bundle bundle) {
        UsernameInputFragment fragment = new UsernameInputFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_username_input, container, false);
        avatar = view.findViewById(R.id.avatar);
        usernameWrapper = view.findViewById(R.id.usernameWrapper);
        usernameInput = view.findViewById(R.id.usernameInput);
        suggestionCard = view.findViewById(R.id.suggestionCard);
        suggestionRv = view.findViewById(R.id.suggestionRv);
        termsAndCondition = view.findViewById(R.id.termsAndCondition);
        saveBtn = view.findViewById(R.id.saveBtn);
        loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initVar(savedInstanceState);
        initView();
        presenter.getUsernameSuggestion();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(UsernameInputActivity.PARAM_PRODUCT_ID, productId);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetUsernameSuggestion(List<String> suggestions) {
        adapter.setList(suggestions);
        suggestionCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuggestionClicked(String username) {

    }

    private void initVar(Bundle savedInstanceState) {
        if (userSession == null) {
            userSession = new UserSession(getContext());
        }
        if (adapter == null) {
            adapter = new SuggestionAdapter(this);
        }

        if (savedInstanceState != null) {
            productId = savedInstanceState.getString(UsernameInputActivity.PARAM_PRODUCT_ID, "");
        } else if (getArguments() != null) {
            productId = getArguments().getString(UsernameInputActivity.PARAM_PRODUCT_ID, "");
        }
    }

    private void initView() {
        ImageHandler.loadImageCircle2(getContext(), avatar, userSession.getProfilePicture());
        usernameInput.addTextChangedListener(getUsernameTextWatcher());
        suggestionRv.setAdapter(adapter);
        saveBtn.setOnClickListener(getSaveBtnOnClickListener());
        disableSaveBtn();
    }

    private void enableSaveBtn() {
        saveBtn.setEnabled(true);
    }

    private void disableSaveBtn() {
        saveBtn.setEnabled(false);
    }

    private AfterTextWatcher getUsernameTextWatcher() {
        return new AfterTextWatcher() {
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

                if (byMeUsername.length() < SHOW_SUGGESTION_LENGTH) {
                    suggestionCard.setVisibility(View.VISIBLE);
                } else {
                    suggestionCard.setVisibility(View.GONE);
                }
            }
        };
    }

    private View.OnClickListener getSaveBtnOnClickListener() {
        return view -> {
            Intent intent;
            if (!TextUtils.isEmpty(productId)) {
                intent = RecommendProductActivity.createIntent(
                        getContext(),
                        productId
                );
            } else {
                intent = OnboardingActivity.createIntent(
                        getContext(),
                        OnboardingActivity.FINISH_TRUE
                );
            }
            startActivity(intent);
        };
    }
}
