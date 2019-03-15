package com.tokopedia.ovo.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.GoalQRThanks;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailContract;
import com.tokopedia.ovo.presenter.QrOvoPayTxDetailPresenter;

import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSACTION_ID;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSFER_ID;

public class QrTxSuccessDetailFragment extends BaseDaggerFragment implements QrOvoPayTxDetailContract.View {

    private static final String FORMAT_DD_MMM_YYYY_HH_MM = "dd MMM yyyy, HH.mm";
    private static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    QrOvoPayTxDetailPresenter presenter;
    private TextView date;
    private TextView amount;
    private TextView merchantName;
    private TextView merchantDescription;
    private TextView transactionCode;
    private ImageView pasteToClipboard;
    private TextView backToMain;
    private int transferId;
    TransactionResultListener listener;

    public static Fragment createInstance(int transferId, int transactionId) {
        Fragment fragment = new QrTxSuccessDetailFragment();
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
        transferId = getArguments().getInt(TRANSFER_ID);
        backToMain.setOnClickListener(view1 -> {
            listener.setResult(Activity.RESULT_OK);
            listener.finish();
        });
        pasteToClipboard.setOnClickListener(view1 -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.copied_to_clipboard), transactionCode.getText().toString());
            clipboard.setPrimaryClip(clip);
            Snackbar.make(getView(), getString(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT).show();
        });
        presenter.requestForThankYouPage(getActivity(), transferId);
        return view;
    }

    @Override
    public void setSuccessThankYouData(GoalQRThanks data) {
        String formattedDate = DateFormatUtils.formatDate(FORMAT_YYYY_MM_DD_HH_MM
                , FORMAT_DD_MMM_YYYY_HH_MM, data.getTransactionDate()
                        .substring(0, data.getTransactionDate().lastIndexOf(":")).trim());
        date.setText(formattedDate);
        amount.setText(Utils.convertToCurrencyStringWithoutRp(data.getAmount().longValue()));
        merchantName.setText(data.getMerchant().getName());
        merchantDescription.setText(data.getMerchant().getDescription());
        transactionCode.setText(String.valueOf(getArguments().getInt(TRANSACTION_ID)));

    }

    @Override
    public void setFailThankYouData(GoalQRThanks data) {
        listener.goToFailFragment();
    }

    @Override
    public void setError(String message) {
        Snackbar.make(getView(), getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public String getErrorMessage() {
        return getString(R.string.error_message);
    }
}
