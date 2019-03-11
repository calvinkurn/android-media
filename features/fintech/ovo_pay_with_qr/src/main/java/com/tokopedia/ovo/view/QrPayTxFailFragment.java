package com.tokopedia.ovo.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.GoalQRThanks;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailContract;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailPresenter;

public class QrPayTxFailFragment extends BaseDaggerFragment implements QrOvoPayTxDetailContract.View {
    private TextView failDescription;
    private LinearLayout callSection;
    private TextView backToMain;
    private TextView tryAgain;
    QrOvoPayTxDetailPresenter presenter;

    public static Fragment createInstance(int transferId, int transactionId) {
        Fragment fragment = new QrPayTxFailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("transfer_id", transferId);
        bundle.putInt("transaction_id", transactionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

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
        View view = inflater.inflate(R.layout.qr_pay_tx_detail_fail, container, false);
        failDescription = view.findViewById(R.id.fail_description);
        callSection = view.findViewById(R.id.call_section);
        backToMain = view.findViewById(R.id.back_to_main);
        tryAgain = view.findViewById(R.id.try_again);
        presenter.requestForThankYouPage(getActivity(), getArguments().getInt("transfer_id"));
        return view;
    }

    @Override
    public void setSuccessThankYouData(GoalQRThanks data) {
        ((QrOvoPayTxDetailActivity) getActivity()).goToSuccessFragment();
    }

    @Override
    public void setFailThankYouData(GoalQRThanks data) {
        failDescription.setText(data.getMessage());
        callSection.setOnClickListener(view -> {

        });
        backToMain.setOnClickListener(view -> getActivity().finish());
        tryAgain.setOnClickListener(view -> presenter.requestForThankYouPage(getActivity(), getArguments().getInt("transfer_id")));
    }
}
