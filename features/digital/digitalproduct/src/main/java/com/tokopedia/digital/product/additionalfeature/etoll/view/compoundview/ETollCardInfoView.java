package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    private TextView textLabelBalance;
    private TextView textRemainingBalance;
    private TextView textDate;
    private TextView textLabelCardNumber;
    private TextView textCardNumber;
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

        textLabelBalance = view.findViewById(R.id.text_label_balance);
        textRemainingBalance = view.findViewById(R.id.text_remaining_balance);
        textLabelCardNumber = view.findViewById(R.id.text_label_card_number);
        textCardNumber = view.findViewById(R.id.text_card_number);
        textDate = view.findViewById(R.id.text_date);
        imageIssuer = view.findViewById(R.id.image_issuer);
    }

    public void showCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;

        imageIssuer.setVisibility(VISIBLE);
        Glide.with(context)
                .load(cardInfo.getIssuerImage())
                .into(imageIssuer);

        textLabelCardNumber.setText(getResources().getString(R.string.emoney_card_info_label_card_number));
        textCardNumber.setText(cardInfo.getFormattedCardNumber());
        textCardNumber.setTextColor(getResources().getColor(R.color.black));
        textCardNumber.setTypeface(textCardNumber.getTypeface(), Typeface.BOLD);

        textLabelBalance.setText(getResources().getString(R.string.emoney_card_info_label_card_balance));
        textRemainingBalance.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(cardInfo.getLastBalance(), true));
        textRemainingBalance.setTextColor(getResources().getColor(R.color.green_400));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm",
                DateFormatUtils.DEFAULT_LOCALE);
        Date date = new Date();
        textDate.setText(simpleDateFormat.format(date));
    }

    public void showLoading() {
        getResources().getString(R.string.emoney_card_info_label_card_balance);
        textLabelBalance.measure(0, 0);
        ViewGroup.LayoutParams paramsTextLabelBalance = textLabelBalance.getLayoutParams();
        paramsTextLabelBalance.width = textLabelBalance.getMeasuredWidth();
        textLabelBalance.setLayoutParams(paramsTextLabelBalance);

        textRemainingBalance.setText("Rp. 1.000.000");
        textRemainingBalance.measure(0, 0);
        ViewGroup.LayoutParams paramsTextRemainingBalance = textRemainingBalance.getLayoutParams();
        paramsTextRemainingBalance.width = textRemainingBalance.getMeasuredWidth();
        textRemainingBalance.setLayoutParams(paramsTextRemainingBalance);

        textDate.setText("26 Jun 2018, 13:47");
        textDate.measure(0, 0);
        ViewGroup.LayoutParams paramsTextDate = textDate.getLayoutParams();
        paramsTextDate.width = textDate.getMeasuredWidth();
        textDate.setLayoutParams(paramsTextDate);

        ((LoaderTextView) findViewById(R.id.text_label_balance)).resetLoader();
        ((LoaderTextView) findViewById(R.id.text_remaining_balance)).resetLoader();
        ((LoaderTextView) findViewById(R.id.text_label_card_number)).resetLoader();
        ((LoaderTextView) findViewById(R.id.text_card_number)).resetLoader();
        ((LoaderTextView) findViewById(R.id.text_date)).resetLoader();
    }

    public String getCardNumber() {
        return cardInfo.getCardNumber();
    }

    public void removeCardInfo() {
        textLabelCardNumber.setText(getResources().getString(R.string.emoney_card_info_label_card_number));
        textCardNumber.setText(getResources().getString(R.string.card_info_is_not_available_yet));
        textCardNumber.setTextColor(getResources().getColor(R.color.grey_300));
        textCardNumber.setTypeface(Typeface.DEFAULT);
        textLabelBalance.setText(getResources().getString(R.string.emoney_card_info_label_card_balance));
        textRemainingBalance.setText(getResources().getString(R.string.card_info_is_not_available_yet));
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
