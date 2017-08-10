package com.tokopedia.digital.product.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Validation;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by ashwanityagi on 31/07/17.
 */

public class OperatorVerificationDialog extends DialogFragment {

    private AutoCompleteTextView autoCompleteTextView;
    private ImageView btnClear;
    private TextView btnCancel;
    private TextView btnOk;
    private TextView tvErrorNumber;
    private TextView tvUssdDesc;
    private String selectedOperatorName;
    private ImageView imgOperator;
    private List<Operator> operatorList;
    private List<Validation> validationList;
    private static final String ARG_PARAM_EXTRA_OPERATOR_LIST_DATA =
            "ARG_PARAM_EXTRA_OPERATOR_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_VALIDATION_LIST_DATA =
            "ARG_PARAM_EXTRA_VALIDATION_LIST_DATA";
    public static final String ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY =
            "ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY";
    public static final int REQUEST_CODE = 222;

    public static OperatorVerificationDialog newInstance(List<Operator> operatorListData, List<Validation> validationListData) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorListData);
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationListData);
        OperatorVerificationDialog fragment = new OperatorVerificationDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorList);
        state.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationList);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operatorList = getArguments().getParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA);
            validationList = getArguments().getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.dialog_fragment_usd_operator_verification, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.ac_number);
        btnClear = (ImageView) view.findViewById(R.id.btn_clear_number);
        imgOperator = (ImageView) view.findViewById(R.id.iv_pic_operator);
        btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        btnOk = (TextView) view.findViewById(R.id.btn_ok);
        tvErrorNumber = (TextView) view.findViewById(R.id.tv_error_number);
        tvUssdDesc = (TextView) view.findViewById(R.id.tv_ussd_desc);
        String operatorName = DeviceUtil.getOperatorName(getActivity());
        Resources res = getResources();
        // String text = String.format(res.getString(R.string.msg_ussd_sim_number),operatorName);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvUssdDesc.setText(Html.fromHtml(res.getString(R.string.msg_ussd_sim_number, operatorName), Html.FROM_HTML_MODE_LEGACY));

        } else {
            tvUssdDesc.setText(Html.fromHtml(res.getString(R.string.msg_ussd_sim_number, operatorName)));

        }
        final TextWatcher textWatcher = getTextWatcherInput();
        autoCompleteTextView.removeTextChangedListener(textWatcher);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        this.btnClear.setOnClickListener(getButtonClearClickListener());
        this.btnCancel.setOnClickListener(getButtonCancelClickListener());
        this.btnOk.setOnClickListener(getButtonOkClickListener());
        prefilledClientMobileNumber();

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @NonNull
    private TextWatcher getTextWatcherInput() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                selectedOperatorName = null;
                String tempInput = charSequence.toString();
                btnClear.setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                if (tempInput.length() < 4) {
                    imgOperator.setVisibility(GONE);
                }
                String tempInputTrim = tempInput;
                if (tempInput.startsWith("+62")) {
                    tempInputTrim = tempInput.replace("+62", "0");
                } else if (tempInput.startsWith("62")) {
                    tempInputTrim = tempInput.replace("62", "0");
                }
                if (tempInput.isEmpty()) {

                    tvErrorNumber.setText("");
                    tvErrorNumber.setVisibility(GONE);
                } else {
                    String errorString = null;
                    for (Validation validation : validationList) {
                        if (!Pattern.matches(validation.getRegex(), tempInput)) {
                            errorString = validation.getError();
                            break;
                        } else {
                            errorString = null;
                        }
                    }

                    if (errorString == null) {
                        tvErrorNumber.setText("");
                        tvErrorNumber.setVisibility(GONE);
                        matchOperator(tempInputTrim);
                        setOkButtonEnable(true);
                    } else {
                        if (tempInput.isEmpty()) {
                            tvErrorNumber.setText("");
                            tvErrorNumber.setVisibility(GONE);

                        } else {
                            tvErrorNumber.setText(errorString);
                            tvErrorNumber.setVisibility(VISIBLE);
                        }
                        setOkButtonEnable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void matchOperator(String tempInputTrim) {
        outerLoop:
        for (Operator operator : operatorList) {
            for (String prefix : operator.getPrefixList()) {
                if (tempInputTrim.startsWith(prefix)) {
                    enableImageOperator(operator.getImage());
                    selectedOperatorName = operator.getName();
                    break outerLoop;
                }
            }
        }
    }

    private void prefilledClientMobileNumber() {
        if (SessionHandler.isV4Login(getActivity())) {
            if (SessionHandler.getPhoneNumber() != null && !"".equalsIgnoreCase(SessionHandler.getPhoneNumber())) {
                autoCompleteTextView.setText(SessionHandler.getPhoneNumber());
            } else {
                autoCompleteTextView.setText(SessionHandler.getTempPhoneNumber(getActivity()));
            }
        }
    }

    public void resetInputTyped() {
        autoCompleteTextView.setText("");
        imgOperator.setVisibility(View.GONE);
        btnClear.setVisibility(View.GONE);
    }

    public void enableImageOperator(String imageUrl) {
        imgOperator.setVisibility(VISIBLE);
        Glide.with(getActivity()).load(imageUrl).dontAnimate().into(this.imgOperator);
    }

    @NonNull
    private View.OnClickListener getButtonClearClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInputTyped();
            }
        };
    }

    @NonNull
    private View.OnClickListener getButtonCancelClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @NonNull
    private View.OnClickListener getButtonOkClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyOperator()) {
                    sendResult(autoCompleteTextView.getText().toString());
                } else {
                    sendResult(null);
                }

                dismiss();
            }
        };
    }

    private void setOkButtonEnable(boolean enable) {
        btnOk.setEnabled(enable);
        if (enable) {
            btnOk.setTextColor(ContextCompat.getColor(getActivity(), R.color.green_btn));
        } else {
            btnOk.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        }
    }


    private boolean verifyOperator() {
        String operatorName = DeviceUtil.getOperatorName(getActivity());
        if (operatorName != null && !"".equalsIgnoreCase(operatorName.trim())) {
            operatorName = operatorName.split(" ")[0];
        } else {
            return false;
        }
        if (selectedOperatorName != null && !"".equalsIgnoreCase(selectedOperatorName.trim())) {
            selectedOperatorName = selectedOperatorName.split(" ")[0];
        } else {
            return false;
        }

        if ("Tri".equalsIgnoreCase(selectedOperatorName) && "3".equalsIgnoreCase(operatorName)) {
            return true;
        }

        if (operatorName.equalsIgnoreCase(selectedOperatorName)) {
            return true;
        } else {
            return false;
        }

    }

    private void sendResult(String result) {
        Intent intent = new Intent();
        intent.putExtra(ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY, result);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
