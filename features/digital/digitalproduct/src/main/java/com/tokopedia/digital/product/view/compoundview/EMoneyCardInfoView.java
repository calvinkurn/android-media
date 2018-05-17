package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.CardInfo;

/**
 * Created by Rizky on 15/05/18.
 */
public class EMoneyCardInfoView extends FrameLayout {

    private Context context;

    private TextView textRemainingBalance;
    private TextView textCardNumber;
    private ProgressBar progressBar;
    private LinearLayout viewRemainingBalance;

    public EMoneyCardInfoView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EMoneyCardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public EMoneyCardInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_emoney_card_info, this, true);

        textRemainingBalance = view.findViewById(R.id.text_remaining_balance);
        textCardNumber = view.findViewById(R.id.text_card_number);
        progressBar = view.findViewById(R.id.progress_bar);
        viewRemainingBalance = view.findViewById(R.id.view_remaining_balance);
    }

    public void showCardInfo(CardInfo cardInfo) {
        setVisibility(VISIBLE);
        String formattedCardNumber = formatCardNumber(cardInfo.getCardInfo());
        String lastBalanceInHex = flipLastBalance(cardInfo.getLastBalance());
        int lastBalanceInDecimal = Integer.parseInt(lastBalanceInHex,16);
        viewRemainingBalance.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        textRemainingBalance.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(lastBalanceInDecimal, true));
        textCardNumber.setText(formattedCardNumber);
    }

    private String flipLastBalance(String lastBalance) {
        return lastBalance.substring(6,8) +
                lastBalance.substring(4,6) +
                lastBalance.substring(2,4) +
                lastBalance.substring(0,2);
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.substring(0, 4) + " - " + cardNumber.substring(4, 8) + " - " +
                cardNumber.substring(8, 12) + " - " + cardNumber.substring(12, 16);
    }

    public void showLoading() {
        viewRemainingBalance.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    public void stopLoading() {
        viewRemainingBalance.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }
}
