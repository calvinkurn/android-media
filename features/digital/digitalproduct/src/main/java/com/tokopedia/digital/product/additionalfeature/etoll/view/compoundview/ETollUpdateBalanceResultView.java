package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 16/05/18.
 */
public class ETollUpdateBalanceResultView extends LinearLayout {

    private ETollCardInfoView eTollCardInfoView;
    private Button buttonTopup;
    private TextView textLabelProgressTitle;
    private TextView textLabelProgressMessage;

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
        textLabelProgressTitle = view.findViewById(R.id.text_label_progress_title);
        textLabelProgressMessage = view.findViewById(R.id.text_label_progress_message);

        buttonTopup.setOnClickListener(v -> listener.onClick());
    }

    public void showCardInfoFromApi(InquiryBalanceModel inquiryBalanceModel) {
        textLabelProgressTitle.setVisibility(GONE);
        textLabelProgressMessage.setVisibility(GONE);
        buttonTopup.setVisibility(VISIBLE);
        buttonTopup.setText(inquiryBalanceModel.getButtonText());
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showCardInfo(inquiryBalanceModel.getCardInfo());
    }

    public void showError(String errorMessage) {
        textLabelProgressTitle.setVisibility(VISIBLE);
        textLabelProgressTitle.setText(getResources().getString(R.string.update_card_balance_failed_title));
        textLabelProgressTitle.setTextColor(getResources().getColor(R.color.red_600));
        textLabelProgressMessage.setVisibility(VISIBLE);
        textLabelProgressMessage.setText(errorMessage);
        textLabelProgressMessage.setTextColor(getResources().getColor(R.color.digital_grey));
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.removeCardInfo();
        buttonTopup.setVisibility(GONE);
    }

    public void showLoading() {
        textLabelProgressTitle.setVisibility(VISIBLE);
        textLabelProgressTitle.setTextColor(getResources().getColor(R.color.black));
        textLabelProgressTitle.setText(getResources().getString(R.string.update_card_balance_progress_label_title));
        textLabelProgressMessage.setVisibility(VISIBLE);
        textLabelProgressMessage.setTextColor(getResources().getColor(R.color.digital_grey));
        textLabelProgressMessage.setText(getResources().getString(R.string.update_card_balance_progress_label_message));
        eTollCardInfoView.setVisibility(VISIBLE);
        eTollCardInfoView.showLoading();
        buttonTopup.setVisibility(GONE);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE) {
            textLabelProgressMessage.setVisibility(GONE);
        }

        super.setVisibility(visibility);
    }

    public String getCardNumber() {
        return eTollCardInfoView.getCardNumber();
    }

    public String getCardLastBalance() {
        return eTollCardInfoView.getCardLastBalance();
    }

    public String getCardLastUpdatedDate() {
        return eTollCardInfoView.getCardLastUpdatedDate();
    }

}
