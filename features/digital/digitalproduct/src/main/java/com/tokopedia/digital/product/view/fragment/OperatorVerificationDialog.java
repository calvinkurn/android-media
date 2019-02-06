package com.tokopedia.digital.product.view.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.R;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

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
    private Operator selectedOperator;
    private List<Validation> validationList;
    private List<Operator> selectedOperatorList;
    private boolean isEdit;
    private int selectedSimIndex = 0;

    public static final String ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY =
            "ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY";
    public static final String EXTRA_CALLBACK_OPERATOR_DATA =
            "EXTRA_CALLBACK_OPERATOR_DATA";
    private static final String ARG_PARAM_EXTRA_OPERATOR_DATA = "ARG_PARAM_EXTRA_OPERATOR_DATA";
    private static final String ARG_PARAM_EXTRA_VALIDATION_LIST_DATA =
            "ARG_PARAM_EXTRA_VALIDATION_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_SIM_INDEX_DATA =
            "ARG_PARAM_EXTRA_SIM_INDEX_DATA";

    private static final String ARG_PARAM_EXTRA_IS_EDIT_DATA =
            "ARG_PARAM_EXTRA_IS_EDIT_DATA";
    private static final String EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA = "EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA";

    public static final int REQUEST_CODE_DIGITAL_USSD_OPERATOR_MATCH = 223;

    public static OperatorVerificationDialog newInstance(Operator operatorData,
                                                         List<Validation> validationListData, int simPos, boolean isEdit, List<Operator> selectedOperatorList
    ) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_OPERATOR_DATA, operatorData);
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationListData);
        bundle.putInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, simPos);
        bundle.putBoolean(ARG_PARAM_EXTRA_IS_EDIT_DATA, isEdit);
        bundle.putParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) selectedOperatorList);
        OperatorVerificationDialog fragment = new OperatorVerificationDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putParcelable(ARG_PARAM_EXTRA_OPERATOR_DATA, selectedOperator);
        state.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationList);
        state.putInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, selectedSimIndex);
        state.putBoolean(ARG_PARAM_EXTRA_IS_EDIT_DATA, isEdit);
        state.putParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) selectedOperatorList);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedOperator = getArguments().getParcelable(ARG_PARAM_EXTRA_OPERATOR_DATA);
            validationList = getArguments().getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);
            selectedSimIndex = getArguments().getInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, 0);
            selectedOperatorList = getArguments().getParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA);
            isEdit = getArguments().getBoolean(ARG_PARAM_EXTRA_IS_EDIT_DATA);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvUssdDesc.setText(Html.fromHtml(getMessageToShow(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUssdDesc.setText(Html.fromHtml(getMessageToShow()));

        }
        final TextWatcher textWatcher = getTextWatcherInput();
        autoCompleteTextView.removeTextChangedListener(textWatcher);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        this.btnClear.setOnClickListener(getButtonClearClickListener());
        this.btnCancel.setOnClickListener(getButtonCancelClickListener());
        this.btnOk.setOnClickListener(getButtonOkClickListener());
        setOkButtonEnable(false);
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
                tempInput = DeviceUtil.formatPrefixClientNumber(tempInput);
                if (tempInput.isEmpty()) {
                    tvErrorNumber.setText("");
                    tvErrorNumber.setVisibility(GONE);
                } else {
                    String errorString = DeviceUtil.validateNumber(validationList, tempInput);
                    if (errorString == null) {
                        tvErrorNumber.setText("");
                        tvErrorNumber.setVisibility(GONE);
                        if (matchOperator(tempInput)) {
                            setOkButtonEnable(true);
                        } else {
                            tvErrorNumber.setText(getActivity().getString(R.string.error_message_ussd_operator_not_matched));
                            tvErrorNumber.setVisibility(VISIBLE);
                            setOkButtonEnable(false);
                        }
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

    private boolean matchOperator(String tempInputTrim) {
        for (Operator operator : selectedOperatorList) {
            if (DeviceUtil.matchOperatorAndNumber(operator, tempInputTrim)) {
                selectedOperator = operator;
                enableImageOperator(selectedOperator.getImage());
                selectedOperatorName = selectedOperator.getName();
                return true;
            }
        }
        return false;
    }

    private void resetInputTyped() {
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
        String operatorName = DeviceUtil.getOperatorName(getActivity(), selectedSimIndex);
        return DeviceUtil.verifyUssdOperator(operatorName, selectedOperatorName);
    }

    private void sendResult(String result) {
        Intent intent = new Intent();
        intent.putExtra(ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY, result);
        intent.putExtra(EXTRA_CALLBACK_OPERATOR_DATA, selectedOperator);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE_DIGITAL_USSD_OPERATOR_MATCH, intent);
    }

    private String getMessageToShow() {
        String message;
        if (isEdit) {
            message = getActivity().getString(R.string.msg_ussd_sim_number_change, DeviceUtil.getOperatorFirstName(selectedOperator.getName()));
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            message = getActivity().getString(R.string.msg_ussd_sim_number, DeviceUtil.getOperatorFirstName(selectedOperator.getName()));
            btnCancel.setVisibility(View.GONE);
        }
        return message;
    }

}
