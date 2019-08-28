package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logisticcart.R;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel;
import com.tokopedia.unifycomponents.ticker.Ticker;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration;

    private TextView tvError;
    private TextView tvDuration;
    private TextView tvPrice;
    private TextView tvCod;
    private TextView tvTextDesc;
    private ImageView imgCheck;
    private Ticker tickerHeaderInfo;
    private RelativeLayout rlContent;
    private TextView tvPromoPotency;
    private TextView tvOrderPrioritas;

    private int cartPosition;
    private ShippingDurationAdapter adapter;

    public ShippingDurationViewHolder(View itemView, ShippingDurationAdapter adapter,
                                      int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;
        this.adapter = adapter;

        tvOrderPrioritas = itemView.findViewById(R.id.tv_order_prioritas);
        tvError = itemView.findViewById(R.id.tv_error);
        tvDuration = itemView.findViewById(R.id.tv_duration);
        tvPrice = itemView.findViewById(R.id.tv_price);
        tvTextDesc = itemView.findViewById(R.id.tv_text_desc);
        imgCheck = itemView.findViewById(R.id.img_check);
        tickerHeaderInfo = itemView.findViewById(R.id.ticker_header_info);
        rlContent = itemView.findViewById(R.id.rl_content);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
        tvCod = itemView.findViewById(R.id.tv_cod_availability);
    }

    public void bindData(ShippingDurationViewModel shippingDurationViewModel,
                         ShippingDurationAdapterListener shippingDurationAdapterListener) {

        if (shippingDurationAdapterListener.isToogleYearEndPromotionOn() &&
                shippingDurationViewModel.getServiceData().getIsPromo() == 1) {
            tvPromoPotency.setVisibility(View.VISIBLE);
        } else {
            tvPromoPotency.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shippingDurationViewModel.getErrorMessage())) {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.getContext(), R.color.font_disabled));
            tvPrice.setVisibility(View.GONE);
            tvTextDesc.setVisibility(View.GONE);
            tvError.setText(shippingDurationViewModel.getErrorMessage());
            tvError.setVisibility(View.VISIBLE);
        } else {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.getContext(), R.color.black_70));
            tvError.setVisibility(View.GONE);
            tvPrice.setText(shippingDurationViewModel.getServiceData().getTexts().getTextRangePrice());
            tvPrice.setVisibility(View.VISIBLE);

            if (!shippingDurationViewModel.getServiceData().getTexts().getTextServiceDesc().isEmpty()) {
                tvTextDesc.setText(shippingDurationViewModel.getServiceData().getTexts().getTextServiceDesc());
                tvTextDesc.setVisibility(View.VISIBLE);
            } else {
                tvTextDesc.setVisibility(View.GONE);
            }

            if (shippingDurationViewModel.getServiceData().getOrderPriority().getNow()) {
                String orderPrioritasTxt = itemView.getContext().getString(R.string.order_prioritas);
                SpannableString orderPrioritasLabel = new SpannableString(orderPrioritasTxt);
                orderPrioritasLabel.setSpan(new StyleSpan(Typeface.BOLD),16,orderPrioritasTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvOrderPrioritas.setText(MethodChecker.fromHtml(shippingDurationViewModel.getServiceData().getOrderPriority().getStaticMessage().getDurationMessage()));
                tvOrderPrioritas.setVisibility(View.VISIBLE);
            }else {
                tvOrderPrioritas.setVisibility(View.GONE);
            }

        }

        tvDuration.setText(shippingDurationViewModel.getServiceData().getServiceName());
        imgCheck.setVisibility(shippingDurationViewModel.isSelected() ? View.VISIBLE : View.GONE);
        tvCod.setText(shippingDurationViewModel.getCodText());
        tvCod.setVisibility(shippingDurationViewModel.isCodAvailable() ? View.VISIBLE : View.GONE);
        if (shippingDurationViewModel.isShowShowCase()) setShowCase(shippingDurationAdapterListener);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shippingDurationViewModel.getErrorMessage())) {
                    shippingDurationViewModel.setSelected(!shippingDurationViewModel.isSelected());
                    shippingDurationAdapterListener.onShippingDurationChoosen(
                            shippingDurationViewModel.getShippingCourierViewModelList(), cartPosition,
                            shippingDurationViewModel.getServiceData());
                }
            }
        });

        if (getAdapterPosition() == 0) {
            tickerHeaderInfo.setVisibility(View.VISIBLE);
        } else {
            tickerHeaderInfo.setVisibility(View.GONE);
        }
    }

    private void setShowCase(ShippingDurationAdapterListener shippingDurationAdapterListener) {
        String label = itemView.getContext().getString(R.string.label_title_showcase_shipping_duration);
        String text = itemView.getContext().getString(R.string.label_body_showcase_shipping_duration);
        ShowCaseObject showCase = new ShowCaseObject(rlContent, label, text, ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();
        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });

        if (!ShowCasePreference.hasShown(itemView.getContext(), ShippingDurationViewHolder.class.getName()))
            showCaseDialog.show(
                    (Activity) itemView.getContext(),
                    ShippingDurationViewHolder.class.getName(),
                    showCaseObjectList
            );
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.label_shipping_show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

}
