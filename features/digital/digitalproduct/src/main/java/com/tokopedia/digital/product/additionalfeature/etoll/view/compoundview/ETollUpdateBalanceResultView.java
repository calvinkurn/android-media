package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.CardInfo;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 16/05/18.
 */
public class ETollUpdateBalanceResultView extends LinearLayout {

    private ETollCardInfoView eTollCardInfoView;
    private Button buttonTopup;
    private TextView textLabelProgress;

    private OnTopupETollClickListener listener;

    public interface OnTopupETollClickListener {
        void onClick();
    }

    public ETollUpdateBalanceResultView(Context context) {
        super(context);
        init();
    }

    public ETollUpdateBalanceResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ETollUpdateBalanceResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnTopupETollClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_etoll_update_balance_result, this);

        eTollCardInfoView = view.findViewById(R.id.view_etoll_card_info);
        buttonTopup = view.findViewById(R.id.button_topup);
        textLabelProgress = view.findViewById(R.id.text_label_progress);

        buttonTopup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }

    public void showLocalCardInfo(CardInfo cardInfo) {
        textLabelProgress.setVisibility(GONE);
        buttonTopup.setVisibility(VISIBLE);
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showCardInfo(cardInfo);
    }

    public void showCardInfoFromApi(InquiryBalanceModel inquiryBalanceModel) {
        textLabelProgress.setVisibility(GONE);
        buttonTopup.setVisibility(VISIBLE);
        buttonTopup.setText(inquiryBalanceModel.getButtonText());
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showCardInfo(inquiryBalanceModel.getCardInfo());
    }

    public void showCardInfoWithError(InquiryBalanceModel inquiryBalanceModel, String errorMessage) {
        textLabelProgress.setVisibility(VISIBLE);
        textLabelProgress.setText(errorMessage);
        buttonTopup.setVisibility(GONE);
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showCardInfo(inquiryBalanceModel.getCardInfo());
    }

    public void showLoading() {
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showLoading();
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

    public void showError(String erroMessage) {
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.stopLoading();
        eTollCardInfoView.removeCardInfo();
        textLabelProgress.setText(erroMessage);
        textLabelProgress.setTextColor(getResources().getColor(R.color.red_error));
        buttonTopup.setVisibility(GONE);
    }

    public String getCardNumber() {
        return eTollCardInfoView.getCardNumber();
    }

}
