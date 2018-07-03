package com.tokopedia.paymentmanagementsystem.changeclickbca.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.changeclickbca.di.ChangeClickBcaModule;
import com.tokopedia.paymentmanagementsystem.changeclickbca.di.DaggerChangeClickBcaComponent;
import com.tokopedia.paymentmanagementsystem.common.Constant;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeClickBcaFragment extends BaseDaggerFragment implements ChangeClickBcaContract.View {

    @Inject
    ChangeClickBcaPresenter changeClickBcaPresenter;

    private TkpdHintTextInputLayout inputLayoutClickBcaUserId;
    private EditText inputClickBcaUserId;
    private Button buttonUse;

    String transactionId;
    String merchantCode;

    public static Fragment createInstance(String transactionId, String merchantCode){
        ChangeClickBcaFragment changeClickBcaFragment = new ChangeClickBcaFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TRANSACTION_ID, transactionId);
        bundle.putString(Constant.MERCHANT_CODE, merchantCode);
        changeClickBcaFragment.setArguments(bundle);
        return changeClickBcaFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerChangeClickBcaComponent.builder()
                .changeClickBcaModule(new ChangeClickBcaModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        changeClickBcaPresenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionId = getArguments().getString(Constant.TRANSACTION_ID, "");
        merchantCode = getArguments().getString(Constant.MERCHANT_CODE, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_click_bca,container, false);
        inputLayoutClickBcaUserId = view.findViewById(R.id.input_layout_click_bca_user_id);
        inputClickBcaUserId = view.findViewById(R.id.input_click_bca_user_id);
        buttonUse = view.findViewById(R.id.button_use);

        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeClickBcaPresenter.changeClickBcaUserId(getResources(), transactionId, merchantCode, inputClickBcaUserId.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onErrorChangeClickBcaUserID(Throwable e) {

    }

    @Override
    public void onResultChangeClickBcaUserId(boolean isSuccess) {

    }
}
