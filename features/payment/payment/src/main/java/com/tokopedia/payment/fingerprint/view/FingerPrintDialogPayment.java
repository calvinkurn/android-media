package com.tokopedia.payment.fingerprint.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.payment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class FingerPrintDialogPayment extends FingerPrintDialog implements FingerPrintDialog.Callback {

    private static final int MAX_ERROR = 3;
    public static final String USER_ID = "USER_ID";
    public static final String URL_OTP = "URL_OTP";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String PARTNER = "PARTNER";
    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";

    private ListenerPayment listenerPayment;
    private View containerOtp;
    private Button buttonUseOtp;

    private String userId;
    private String date;
    private String urlOtp;
    private String transactionId;
    private int counterError = 0;

    public static FingerPrintDialogPayment createInstance(String userId, String urlOtp, String transactionId) {
        FingerPrintDialogPayment fingerPrintDialogPayment = new FingerPrintDialogPayment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        bundle.putString(URL_OTP, urlOtp);
        bundle.putString(TRANSACTION_ID, transactionId);
        fingerPrintDialogPayment.setArguments(bundle);
        return fingerPrintDialogPayment;
    }

    @Override
    public String getTextToEncrypt() {
        date = generateDate();
        return userId + date;
    }

    public void setListenerPayment(ListenerPayment listenerPayment) {
        this.listenerPayment = listenerPayment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_bottom_sheet_fingerprint_view_payment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlOtp = getArguments().getString(URL_OTP, "");
        userId = getArguments().getString(USER_ID, "");
        transactionId = getArguments().getString(TRANSACTION_ID, "");
    }

    @Override
    public Callback getCallback() {
        return this;
    }

    private String generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void initView(View view) {
        containerOtp = view.findViewById(R.id.container_otp);
        buttonUseOtp = view.findViewById(R.id.button_use_otp);
        buttonUseOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerPayment.onGoToOtpPage(transactionId, urlOtp);
            }
        });
    }

    public void setVisibilityContainer(boolean isVisible) {
        if (isVisible) {
            containerOtp.setVisibility(View.VISIBLE);
        } else {
            containerOtp.setVisibility(View.GONE);
        }
        updateHeight();
    }

    private boolean updateCounterError() {
        if (isResumed()) {
            counterError++;
            updateTitle(getString(R.string.fingerprint_label_failed_scan));
            setVisibilityContainer(true);
            if (counterError > MAX_ERROR) {
                dismiss();
                listenerPayment.onGoToOtpPage(transactionId, urlOtp);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onCloseButtonClick() {
        listenerPayment.onGoToOtpPage(transactionId, urlOtp);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        updateCounterError();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        updateCounterError();
    }

    @Override
    public void onAuthenticationSucceeded(String publicKey, String signature) {
        listenerPayment.onPaymentFingerPrint(transactionId, publicKey, date, signature, userId);
    }

    @Override
    public void onAuthenticationFailed() {
        updateCounterError();
    }

    public void onErrorNetworkPaymentFingerPrint() {
        if (updateCounterError()) {
            startListening();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public interface ListenerPayment {

        void onGoToOtpPage(String transactionId, String urlOtp);

        void onPaymentFingerPrint(String transactionId, String publicKey, String date, String signature, String userId);
    }
}
