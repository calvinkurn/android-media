package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.CardInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rizky on 15/05/18.
 */
public class ETollCardInfoView extends FrameLayout {

    private Context context;

    private CardInfo cardInfo;

    private TextView textRemainingBalance;
    private TextView textDate;
    private TextView textCardNumber;
    private ProgressBar progressBar;
    private LinearLayout viewRemainingBalance;
    private ImageView imageIssuer;

    public ETollCardInfoView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ETollCardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ETollCardInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_etoll_card_info, this, true);

        textRemainingBalance = view.findViewById(R.id.text_remaining_balance);
        textCardNumber = view.findViewById(R.id.text_card_number);
        textDate = view.findViewById(R.id.text_date);
        progressBar = view.findViewById(R.id.progress_bar);
        viewRemainingBalance = view.findViewById(R.id.view_remaining_balance);
        imageIssuer = view.findViewById(R.id.image_issuer);
    }

    public void showCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
        viewRemainingBalance.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        imageIssuer.setVisibility(VISIBLE);
        Glide.with(context)
                .load(cardInfo.getIssuerImage())
                .into(imageIssuer);
        textCardNumber.setText(cardInfo.getFormattedCardNumber());
        textCardNumber.setTextColor(getResources().getColor(R.color.black));
        textCardNumber.setTypeface(textCardNumber.getTypeface(), Typeface.BOLD);
        textRemainingBalance.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(cardInfo.getLastBalance(), true));
        textRemainingBalance.setTextColor(getResources().getColor(R.color.green_400));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm",
                DateFormatUtils.DEFAULT_LOCALE);
        Date date = new Date();
        textDate.setText(simpleDateFormat.format(date));
    }

    public void showLoading() {
        viewRemainingBalance.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    public void stopLoading() {
        viewRemainingBalance.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }

    public String getCardNumber() {
        return cardInfo.getCardNumber();
    }

    public void removeCardInfo() {
        textCardNumber.setText("Belum Tersedia");
        textCardNumber.setTextColor(getResources().getColor(R.color.grey_300));
        textCardNumber.setTypeface(Typeface.DEFAULT);
        textRemainingBalance.setText("Belum Tersedia");
        textRemainingBalance.setTextColor(getResources().getColor(R.color.grey_300));
        textDate.setText("");
        imageIssuer.setImageDrawable(null);
    }

    public String getCardLastBalance() {
        return CurrencyFormatUtil.convertPriceValueToIdrFormat(cardInfo.getLastBalance(), true);
    }

    public String getCardLastUpdatedDate() {
        return textDate.getText().toString();
    }

}
