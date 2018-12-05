package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration;

    private TextView tvError;
    private TextView tvDuration;
    private TextView tvPrice;
    private ImageView imgCheck;
    private View vSeparatorPrice;
    private View vSeparatorError;
    private TextView tvDurationHeaderInfo;
    private RelativeLayout rlContent;
    private TextView tvPromoPotency;

    private int cartPosition;
    private ShippingDurationAdapter adapter;
    // set true if has courier promo, whether own courier or other duration's courier
    private boolean hasCourierPromo;

    public ShippingDurationViewHolder(View itemView, ShippingDurationAdapter adapter,
                                      int cartPosition, boolean hasCourierPromo) {
        super(itemView);
        this.cartPosition = cartPosition;
        this.adapter = adapter;
        this.hasCourierPromo = hasCourierPromo;

        tvError = itemView.findViewById(R.id.tv_error);
        tvDuration = itemView.findViewById(R.id.tv_duration);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        vSeparatorPrice = itemView.findViewById(R.id.v_separator_price);
        vSeparatorError = itemView.findViewById(R.id.v_separator_error);
        tvDurationHeaderInfo = itemView.findViewById(R.id.tv_duration_header_info);
        rlContent = itemView.findViewById(R.id.rl_content);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
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
            tvError.setText(shippingDurationViewModel.getErrorMessage());
            tvError.setVisibility(View.VISIBLE);
            vSeparatorPrice.setVisibility(View.GONE);
            vSeparatorError.setVisibility(View.VISIBLE);
        } else {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.getContext(), R.color.black_70));
            tvError.setVisibility(View.GONE);
            tvPrice.setText(shippingDurationViewModel.getServiceData().getTexts().getTextRangePrice());
            tvPrice.setVisibility(View.VISIBLE);
            vSeparatorError.setVisibility(View.GONE);
            vSeparatorPrice.setVisibility(View.VISIBLE);
        }

        tvDuration.setText(shippingDurationViewModel.getServiceData().getServiceName());
        imgCheck.setVisibility(shippingDurationViewModel.isSelected() ? View.VISIBLE : View.GONE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shippingDurationViewModel.getErrorMessage())) {
                    shippingDurationViewModel.setSelected(!shippingDurationViewModel.isSelected());
                    shippingDurationAdapterListener.onShippingDurationChoosen(
                            shippingDurationViewModel.getShippingCourierViewModelList(), cartPosition,
                            shippingDurationViewModel.getServiceData(), hasCourierPromo);
                }
            }
        });

        if (getAdapterPosition() == adapter.getItemCount() - 1) {
            shippingDurationAdapterListener.onAllShippingDurationItemShown();
        }

        if (getAdapterPosition() == 0) {
            tvDurationHeaderInfo.setVisibility(View.VISIBLE);
            if (shippingDurationViewModel.isShowShowCase()) {
                setShowCase(shippingDurationAdapterListener);
            }
        } else {
            tvDurationHeaderInfo.setVisibility(View.GONE);
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
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.label_shipping_show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

}
