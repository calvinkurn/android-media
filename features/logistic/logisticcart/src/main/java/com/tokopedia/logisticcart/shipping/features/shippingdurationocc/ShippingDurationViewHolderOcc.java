package com.tokopedia.logisticcart.shipping.features.shippingdurationocc;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationViewHolderOcc extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration;

    private TextView tvError;
    private TextView tvDuration;
    private TextView tvPrice;
    private TextView tvTextDesc;
    private ImageView imgCheck;
    private RelativeLayout rlContent;
    private TextView tvPromoPotency;
    private TextView tvOrderPrioritas;
    private Typography tvShippingInformation;
    private Typography tvMvcError;
    private ConstraintLayout layoutMvc;
    private Label labelCodAvailable;

    private int cartPosition;

    public ShippingDurationViewHolderOcc(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvOrderPrioritas = itemView.findViewById(R.id.tv_order_prioritas);
        tvError = itemView.findViewById(R.id.tv_error);
        tvDuration = itemView.findViewById(R.id.tv_duration_or_price);
        tvPrice = itemView.findViewById(R.id.tv_price_or_duration);
        tvTextDesc = itemView.findViewById(R.id.tv_text_desc);
        imgCheck = itemView.findViewById(R.id.img_check);
        rlContent = itemView.findViewById(R.id.rl_content);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
        tvShippingInformation = itemView.findViewById(R.id.tv_shipping_information);
        tvMvcError = itemView.findViewById(R.id.tv_mvc_error);
        layoutMvc = itemView.findViewById(R.id.layout_mvc);
        labelCodAvailable = itemView.findViewById(R.id.lbl_cod_available);
    }

    public void bindData(ShippingDurationUiModel shippingDurationUiModel,
                         ShippingDurationAdapterListener shippingDurationAdapterListener,
                         boolean isDisableOrderPrioritas) {
        if (shippingDurationUiModel.isShowShippingInformation()) {
            tvShippingInformation.setVisibility(View.VISIBLE);
        } else {
            tvShippingInformation.setVisibility(View.GONE);
        }

        // OCC has no MVC
        layoutMvc.setVisibility(View.GONE);
        tvMvcError.setVisibility(View.GONE);

        if (shippingDurationAdapterListener.isToogleYearEndPromotionOn() &&
                shippingDurationUiModel.getServiceData().getIsPromo() == 1) {
            tvPromoPotency.setVisibility(View.VISIBLE);
        } else {
            tvPromoPotency.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shippingDurationUiModel.getErrorMessage())) {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_20));
            tvPrice.setVisibility(View.GONE);
            tvTextDesc.setVisibility(View.GONE);
            tvOrderPrioritas.setVisibility(View.GONE);
            tvError.setText(shippingDurationUiModel.getErrorMessage());
            tvError.setVisibility(View.VISIBLE);
        } else {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvError.setVisibility(View.GONE);
            tvPrice.setText(shippingDurationUiModel.getServiceData().getTexts().getTextRangePrice());
            tvPrice.setVisibility(View.VISIBLE);

            if (!shippingDurationUiModel.getServiceData().getTexts().getTextServiceDesc().isEmpty()) {
                tvTextDesc.setText(shippingDurationUiModel.getServiceData().getTexts().getTextServiceDesc());
                tvTextDesc.setVisibility(View.VISIBLE);
            } else {
                tvTextDesc.setVisibility(View.GONE);
            }

            if (!isDisableOrderPrioritas && shippingDurationUiModel.getServiceData().getOrderPriority().getNow()) {
                String orderPrioritasTxt = itemView.getContext().getString(R.string.order_prioritas);
                SpannableString orderPrioritasLabel = new SpannableString(orderPrioritasTxt);
                orderPrioritasLabel.setSpan(new StyleSpan(Typeface.BOLD), 16, orderPrioritasTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvOrderPrioritas.setText(MethodChecker.fromHtml(shippingDurationUiModel.getServiceData().getOrderPriority().getStaticMessage().getDurationMessage()));
                tvOrderPrioritas.setVisibility(View.VISIBLE);
            } else {
                tvOrderPrioritas.setVisibility(View.GONE);
            }

        }
        TextAndContentDescriptionUtil.setTextAndContentDescription(tvDuration, shippingDurationUiModel.getServiceData().getServiceName(), tvDuration.getContext().getString(R.string.content_desc_tv_duration));
        imgCheck.setVisibility(shippingDurationUiModel.isSelected() ? View.VISIBLE : View.GONE);
        labelCodAvailable.setText(shippingDurationUiModel.getCodText());
        labelCodAvailable.setVisibility(shippingDurationUiModel.isCodAvailable() ? View.VISIBLE : View.GONE);
        if (shippingDurationUiModel.isShowShowCase()) setShowCase(shippingDurationAdapterListener);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shippingDurationUiModel.getErrorMessage())) {
                    shippingDurationUiModel.setSelected(!shippingDurationUiModel.isSelected());
                    shippingDurationAdapterListener.onShippingDurationChoosen(
                            shippingDurationUiModel.getShippingCourierViewModelList(), cartPosition,
                            shippingDurationUiModel.getServiceData());
                }
            }
        });
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

        if (!ShowCasePreference.hasShown(itemView.getContext(), ShippingDurationViewHolderOcc.class.getName()))
            showCaseDialog.show(
                    (Activity) itemView.getContext(),
                    ShippingDurationViewHolderOcc.class.getName(),
                    showCaseObjectList
            );
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .titleTextColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N0)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N150)
                .shadowColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                .backgroundContentColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N700)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.label_shipping_show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

}
