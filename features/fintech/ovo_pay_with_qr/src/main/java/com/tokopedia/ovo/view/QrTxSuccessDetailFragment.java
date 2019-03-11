package com.tokopedia.ovo.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.GoalQRThanks;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailContract;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailPresenter;

public class QrTxSuccessDetailFragment extends BaseDaggerFragment implements QrOvoPayTxDetailContract.View {

    QrOvoPayTxDetailPresenter presenter;
    private TextView date;
    private TextView amount;
    private TextView merchantName;
    private TextView merchantDescription;
    private TextView transactionCode;
    private ImageView pasteToClipboard;
    private TextView backToMain;
    private int transferId;

    public static Fragment createInstance(int transferId, int transactionId) {
        Fragment fragment = new QrTxSuccessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("transfer_id",transferId);
        bundle.putInt("transaction_id",transactionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new QrOvoPayTxDetailPresenter();
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_pay_tx_detail_success, container, false);
        date = view.findViewById(R.id.tx_date);
        amount = view.findViewById(R.id.transaction_amount);
        merchantName = view.findViewById(R.id.merchant_name);
        merchantDescription = view.findViewById(R.id.merchant_description);
        transactionCode = view.findViewById(R.id.transaction_code);
        pasteToClipboard = view.findViewById(R.id.paste_top_clipboard);
        backToMain = view.findViewById(R.id.back_to_main);
        transferId = getArguments().getInt("transfer_id");
        backToMain.setOnClickListener(view1 -> getActivity().finish());
        pasteToClipboard.setOnClickListener(view1 -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.copied_to_clipboard), transactionCode.getText().toString());
            clipboard.setPrimaryClip(clip);
        });
        presenter.requestForThankYouPage(getActivity(),transferId);
        return view;
    }

    @Override
    public void setSuccessThankYouData(GoalQRThanks data) {
        date.setText(data.getTransactionDate());
        amount.setText(String.valueOf(data.getAmount()));
        merchantName.setText(data.getMerchant().getName());
        merchantDescription.setText(data.getMerchant().getDescription());
        transactionCode.setText(getArguments().getInt("transaction_id"));

    }

    @Override
    public void setFailThankYouData(GoalQRThanks data) {
        ((QrOvoPayTxDetailActivity)getActivity()).goToFailFragment();
    }
}
