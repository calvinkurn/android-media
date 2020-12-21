package com.tokopedia.pms.clickbca.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.pms.R;
import com.tokopedia.pms.clickbca.di.ChangeClickBcaModule;
import com.tokopedia.pms.clickbca.di.DaggerChangeClickBcaComponent;
import com.tokopedia.pms.common.Constant;

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
    private ProgressDialog progressDialog;

    private String transactionId;
    private String merchantCode;
    private String userIdKlikBca;

    public static Fragment createInstance(String transactionId, String merchantCode, String userIdKlikBca){
        ChangeClickBcaFragment changeClickBcaFragment = new ChangeClickBcaFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TRANSACTION_ID, transactionId);
        bundle.putString(Constant.MERCHANT_CODE, merchantCode);
        bundle.putString(Constant.USER_ID_KLIK_BCA, userIdKlikBca);
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
    public void onDestroy() {
        changeClickBcaPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionId = getArguments().getString(Constant.TRANSACTION_ID, "");
        merchantCode = getArguments().getString(Constant.MERCHANT_CODE, "");
        userIdKlikBca = getArguments().getString(Constant.USER_ID_KLIK_BCA, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_click_bca,container, false);
        inputLayoutClickBcaUserId = view.findViewById(R.id.input_layout_click_bca_user_id);
        inputClickBcaUserId = view.findViewById(R.id.input_click_bca_user_id);
        buttonUse = view.findViewById(R.id.button_use);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));

        inputClickBcaUserId.setText(userIdKlikBca);
        inputLayoutClickBcaUserId.setHelper(getString(R.string.payment_label_helper_change_click_bca, userIdKlikBca));
        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(inputClickBcaUserId.getText())){
                    NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.payment_label_error_empty_user_id));
                    return;
                }
                changeClickBcaPresenter.changeClickBcaUserId(getResources(), transactionId, merchantCode, inputClickBcaUserId.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onErrorChangeClickBcaUserID(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onResultChangeClickBcaUserId(boolean isSuccess, String message) {
        if(isSuccess){
            NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), message);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }else{
            NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.hide();
    }
}
