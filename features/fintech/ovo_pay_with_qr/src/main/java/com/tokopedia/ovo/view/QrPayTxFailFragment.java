package com.tokopedia.ovo.view;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.ovo.analytics.OvoPayByQrTrackerUtil;
import com.tokopedia.ovo.model.GoalQRThanks;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailContract;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailPresenter;

import static com.tokopedia.ovo.Constants.HELP_URL;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSACTION_ID;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSFER_ID;

public class QrPayTxFailFragment extends BaseDaggerFragment implements QrOvoPayTxDetailContract.View {
    private TextView failDescription;
    private LinearLayout callSection;
    private TextView backToMain;
    private TextView tryAgain;
    QrOvoPayTxDetailPresenter presenter;
    TransactionResultListener listener;

    public static Fragment createInstance(int transferId, int transactionId) {
        Fragment fragment = new QrPayTxFailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TRANSFER_ID, transferId);
        bundle.putInt(TRANSACTION_ID, transactionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (TransactionResultListener) activity;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new QrOvoPayTxDetailPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.ovo.R.layout.oqr_qr_pay_tx_detail_fail, container, false);
        failDescription = view.findViewById(com.tokopedia.ovo.R.id.fail_description);
        callSection = view.findViewById(com.tokopedia.ovo.R.id.call_section);
        backToMain = view.findViewById(com.tokopedia.ovo.R.id.back_to_main);
        tryAgain = view.findViewById(com.tokopedia.ovo.R.id.try_again);
        callSection.setOnClickListener(view1 -> {
            OvoPayByQrTrackerUtil.sendEvent(
                    OvoPayByQrTrackerUtil.EVENT.clickOvoPayEvent,
                    OvoPayByQrTrackerUtil.CATEGORY.ovoPayByQr,
                    OvoPayByQrTrackerUtil.ACTION.clickButuhBantuan,
                    OvoPayByQrTrackerUtil.LABEL.defaultLabel);
            startActivity(OvoWebViewActivity.getWebViewIntent(getActivity(), HELP_URL, getString(com.tokopedia.ovo.R.string.oqr_contact_us)));
        });
        backToMain.setOnClickListener(view1 -> {
            OvoPayByQrTrackerUtil.sendEvent(
                    OvoPayByQrTrackerUtil.EVENT.clickOvoPayEvent,
                    OvoPayByQrTrackerUtil.CATEGORY.ovoPayByQr,
                    OvoPayByQrTrackerUtil.ACTION.clickKembaliTidakBerhasil,
                    OvoPayByQrTrackerUtil.LABEL.defaultLabel);
            startActivity(RouteManager.getIntent(getActivity(), ApplinkConst.HOME));
        });
        tryAgain.setOnClickListener(view1 -> {
            OvoPayByQrTrackerUtil.sendEvent(
                    OvoPayByQrTrackerUtil.EVENT.clickOvoPayEvent,
                    OvoPayByQrTrackerUtil.CATEGORY.ovoPayByQr,
                    OvoPayByQrTrackerUtil.ACTION.cobaLagi,
                    OvoPayByQrTrackerUtil.LABEL.defaultLabel);
            presenter.requestForThankYouPage(getActivity(), getArguments().getInt(TRANSFER_ID));
        });

        presenter.requestForThankYouPage(getActivity(), getArguments().getInt(TRANSFER_ID));
        return view;
    }

    @Override
    public void setSuccessThankYouData(GoalQRThanks data) {
        listener.goToSuccessFragment();
    }

    @Override
    public void setFailThankYouData(GoalQRThanks data) {
        failDescription.setText(data.getMessage());
        OvoPayByQrTrackerUtil.sendEvent(
                OvoPayByQrTrackerUtil.EVENT.viewOvoPayEvent,
                OvoPayByQrTrackerUtil.CATEGORY.ovoPayByQr,
                OvoPayByQrTrackerUtil.ACTION.viewPageTransaksi,
                OvoPayByQrTrackerUtil.LABEL.defaultLabel);
    }

    @Override
    public void setError(String message) {
        Snackbar.make(getView(), getErrorMessage(), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public String getErrorMessage() {
        return getString(com.tokopedia.ovo.R.string.oqr_error_message);
    }
}
