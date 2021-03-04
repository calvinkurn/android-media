package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.utils.NFCUtils;
import com.tokopedia.unifycomponents.UnifyButton;

/**
 * Created by Rizky on 15/05/18.
 */
public class CheckETollBalanceView extends LinearLayout {

    private TextView textLabelNote;
    private UnifyButton buttonFeature;

    private LinearLayout viewRemainingBalance;
    private TextView textCardNumber;
    private TextView textRemainingBalance;
    private TextView textDate;

    private Context context;

    private OnCheckBalanceClickListener listener;

    public interface OnCheckBalanceClickListener {
        void onClick();
    }

    public CheckETollBalanceView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CheckETollBalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CheckETollBalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckETollBalanceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    public void setListener(OnCheckBalanceClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_holder_digital_etoll_check_balance, this, true);

        textLabelNote = view.findViewById(R.id.text_label_note);
        buttonFeature = view.findViewById(R.id.button_feature);

        viewRemainingBalance = view.findViewById(R.id.view_remaining_balance);
        textCardNumber = view.findViewById(R.id.text_card_number);
        textRemainingBalance = view.findViewById(R.id.text_remaining_balance);
        textDate = view.findViewById(R.id.text_date);

        buttonFeature.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }

    public void showCheckBalance(String note, String buttonText) {
        viewRemainingBalance.setVisibility(GONE);
        textLabelNote.setVisibility(VISIBLE);
        textLabelNote.setText(note);
        buttonFeature.setText(buttonText);
    }

    public void showRemainingBalance(String cardNumber, String remainingBalance, String date) {
        textLabelNote.setVisibility(GONE);
        viewRemainingBalance.setVisibility(VISIBLE);
        textCardNumber.setText("No. Kartu " + NFCUtils.formatCardUIDWithSpace(cardNumber));
        textRemainingBalance.setText(remainingBalance);
        textDate.setText(date);
    }

}
