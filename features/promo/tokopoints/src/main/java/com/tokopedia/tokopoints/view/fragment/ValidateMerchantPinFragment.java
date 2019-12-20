package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.widget.PinEditText;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.contract.ValidateMerchantPinContract;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate;
import com.tokopedia.tokopoints.view.presenter.ValidateMerchantPinPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import javax.inject.Inject;

public class ValidateMerchantPinFragment extends BaseDaggerFragment implements ValidateMerchantPinContract.View, View.OnClickListener {

    @Inject
    public ValidateMerchantPinPresenter mPresenter;

    private PinEditText mEditPin;
    private TextView mTextError;
    private TextView mTextInfo;
    private ValidatePinCallBack mValidatePinCallBack;


    public static ValidateMerchantPinFragment newInstance(Bundle extras) {
        ValidateMerchantPinFragment fragment = new ValidateMerchantPinFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setmValidatePinCallBack(ValidatePinCallBack mValidatePinCallBack) {
        this.mValidatePinCallBack = mValidatePinCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_validate_merchant_pin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);

        mEditPin = view.findViewById(R.id.et_input_otp);
        mTextError = view.findViewById(R.id.tv_pin_error);
        mTextInfo = view.findViewById(R.id.text_info);

        if (getArguments() != null) {
            mTextInfo.setText(getArguments().getString(CommonConstant.EXTRA_PIN_INFO));
        }

        mEditPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditPin.getText().toString().length() == CommonConstant.PIN_COUNT) {
                    mPresenter.swipeMyCoupon(getArguments().getString(CommonConstant.EXTRA_COUPON_ID), mEditPin.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_send) {
            if (getArguments() == null || getActivity() == null) {
                return;
            }

            KeyboardHandler.hideSoftKeyboard(getActivity());
            mPresenter.swipeMyCoupon(getArguments().getString(CommonConstant.EXTRA_COUPON_ID), mEditPin.getText().toString());
        }
    }

    @Override
    protected void initInjector() {
        DaggerTokoPointComponent.builder().baseAppComponent(((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent()).build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoading() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send).setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(CouponSwipeUpdate couponSwipeUpdate) {
        mValidatePinCallBack.onSuccess(couponSwipeUpdate);
    }

    @Override
    public void onError(String error) {
        mTextError.setVisibility(View.VISIBLE);
        mTextError.setText(error);
    }

    @Override
    public Context getAppContext() {
        return getActivity();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    public interface ValidatePinCallBack {
        void onSuccess(CouponSwipeUpdate couponSwipeUpdate);
    }
}
