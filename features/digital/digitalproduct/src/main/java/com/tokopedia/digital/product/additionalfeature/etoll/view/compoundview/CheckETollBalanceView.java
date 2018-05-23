package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;

/**
 * Created by Rizky on 15/05/18.
 */
public class CheckETollBalanceView extends LinearLayout {

    private TextView textLabelNote;
    private TextView textRemainingBalance;
    private TextView textLabelDate;
    private LinearLayout viewRemainingBalance;
    private LinearLayout viewCheckBalance;
    private TextView buttonFeature;

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
                .inflate(R.layout.view_holder_check_etoll_balance_view, this, true);

        textLabelNote = view.findViewById(R.id.text_label_note);
        textLabelDate = view.findViewById(R.id.text_label_date);
        textRemainingBalance = view.findViewById(R.id.text_remaining_balance);
        viewRemainingBalance = view.findViewById(R.id.view_remaining_balance);
        viewCheckBalance = view.findViewById(R.id.view_check_balance);
        buttonFeature = view.findViewById(R.id.button_feature);

        viewCheckBalance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }

    public void showCheckBalance(String note, String buttonText) {
        textLabelNote.setText(note);
        textLabelNote.setVisibility(VISIBLE);
        buttonFeature.setText(buttonText);
        buttonFeature.setVisibility(VISIBLE);
        viewRemainingBalance.setVisibility(GONE);
    }

    public void showRemainingBalance(String remainingBalance, String cardNumber) {
        textRemainingBalance.setVisibility(VISIBLE);
        textRemainingBalance.setText(remainingBalance);
        textLabelDate.setVisibility(VISIBLE);
        textLabelDate.setText(cardNumber);
        viewRemainingBalance.setVisibility(VISIBLE);
        textLabelNote.setVisibility(GONE);
    }

}
