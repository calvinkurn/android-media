package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.CardInfo;

/**
 * Created by Rizky on 16/05/18.
 */
public class EMoneyUpdateBalanceResultView extends LinearLayout {

    private EMoneyCardInfoView eMoneyCardInfoView;
    private Button buttonTopup;
    private TextView textLabelProgress;

    private OnTopupEMoneyClickListener listener;

    public interface OnTopupEMoneyClickListener {
        void onClick();
    }

    public EMoneyUpdateBalanceResultView(Context context) {
        super(context);
        init();
    }

    public EMoneyUpdateBalanceResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EMoneyUpdateBalanceResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnTopupEMoneyClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_emoney_update_balance_result, this);

        eMoneyCardInfoView = view.findViewById(R.id.view_emoney_card_info);
        buttonTopup = view.findViewById(R.id.button_topup);
        textLabelProgress = view.findViewById(R.id.text_label_progress);

        buttonTopup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }

    public void showCardInfo(CardInfo cardInfo) {
        setVisibility(VISIBLE);
        textLabelProgress.setVisibility(GONE);
        buttonTopup.setVisibility(VISIBLE);
        eMoneyCardInfoView.showCardInfo(cardInfo);
    }

    public void showLoading() {
        eMoneyCardInfoView.showLoading();
        textLabelProgress.setVisibility(VISIBLE);
        textLabelProgress.setTextColor(getResources().getColor(R.color.grey));
        textLabelProgress.setText(getResources().getString(R.string.update_card_balance_progress_label));
        buttonTopup.setVisibility(GONE);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE) {
            textLabelProgress.setVisibility(GONE);
        }

        super.setVisibility(visibility);
    }

    public void showCardIsNotSupported() {
        eMoneyCardInfoView.stopLoading();
        textLabelProgress.setText(getResources().getString(R.string.card_is_not_supported));
        textLabelProgress.setTextColor(getResources().getColor(R.color.red_error));
        buttonTopup.setVisibility(GONE);
    }

    public String getCardNumber() {
        return eMoneyCardInfoView.getCardNumber();
    }

}
