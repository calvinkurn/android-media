package com.tokopedia.updateinactivephone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.updateinactivephone.R;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.OLD_PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;

public class UpdateNewPhoneEmailFragment extends TkpdBaseV4Fragment {

    private String userId;
    private String oldPhoneNumber;
    private EditText newPhoneEditText;
    private EditText newEmailEditText;
    private Button submissionButton;

    private TextView phoneErrorTextView;
    private TextView emailErrorTextView;

    private UpdateNewPhoneEmailInteractor updateNewPhoneEmailInteractor;

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        try {
            updateNewPhoneEmailInteractor = (UpdateNewPhoneEmailInteractor) context;
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_new_phone_email_fragment, parent, false);
        prepareView(view);
        return view;
    }

    private void prepareView(View view) {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            userId = bundle.getString(USER_ID);
            oldPhoneNumber = bundle.getString(OLD_PHONE);
        }

        View dashLineView = view.findViewById(R.id.dash_line);
        dashLineView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TextView oldPhoneTextView = view.findViewById(R.id.old_phone_text_view);
        emailErrorTextView = view.findViewById(R.id.email_error);
        phoneErrorTextView = view.findViewById(R.id.phone_error);

        newEmailEditText = view.findViewById(R.id.new_email_edit_text);
        newPhoneEditText = view.findViewById(R.id.new_phone_edit_text);
        submissionButton = view.findViewById(R.id.submission_button);

        oldPhoneTextView.setText(oldPhoneNumber);


        newEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSubmissionButtonState();
                emailErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSubmissionButtonState();
                phoneErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submissionButton.setOnClickListener(view1 -> updateNewPhoneEmailInteractor.onSubmissionButtonClicked(
                newEmailEditText.getText().toString(),
                newPhoneEditText.getText().toString(),
                userId));

    }

    private void setSubmissionButtonState() {
        if (!TextUtils.isEmpty(newEmailEditText.getText().toString()) &&
                !TextUtils.isEmpty(newPhoneEditText.getText().toString())) {
            submissionButton.setEnabled(true);
            submissionButton.setClickable(true);
            submissionButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            submissionButton.setEnabled(false);
            submissionButton.setClickable(false);
            submissionButton.setTextColor(getResources().getColor(R.color.black_26));
        }
    }

    public static UpdateNewPhoneEmailFragment getInstance(Bundle bundle) {
        UpdateNewPhoneEmailFragment updateNewPhoneEmailFragment = new UpdateNewPhoneEmailFragment();
        updateNewPhoneEmailFragment.setArguments(bundle);
        return updateNewPhoneEmailFragment;
    }

    public void showErrorPhone(int phoneErrorId) {
        phoneErrorTextView.setText(getString(phoneErrorId));
        phoneErrorTextView.setVisibility(View.VISIBLE);
    }

    public void showErrorEmail(int error_invalid_email) {
        emailErrorTextView.setText(getString(error_invalid_email));
        emailErrorTextView.setVisibility(View.VISIBLE);
    }

    public interface UpdateNewPhoneEmailInteractor {
        void onSubmissionButtonClicked(String email, String phone, String userId);
    }
}
