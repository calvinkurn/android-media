package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.purchase_platform.common.utils.Utils;
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
 * tvDurationOrPrice means it will get duration in existing, and price when ETA is applied
 * tvPriceOrDuration means it will get price in existing, and duration when ETA is applied
 */

public class ShippingDurationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration;

    private TextView tvError;
    private TextView tvDurationOrPrice;
    private TextView tvPriceOrDuration;
    private TextView tvTextDesc;
    private ImageView imgCheck;
    private ImageView imgMvc;
    private Typography tvMvc;
    private RelativeLayout rlContent;
    private TextView tvPromoPotency;
    private TextView tvOrderPrioritas;
    private Typography tvShippingInformation;
    private Typography tvMvcError;
    private Label labelCodAvailable;
    private ConstraintLayout layoutMvc;
    private Label labelCodAvailabelEta;
    private FrameLayout flDisableContainer;
    private Label labelDynamicPricing;

    private int cartPosition;

    public ShippingDurationViewHolder(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvOrderPrioritas = itemView.findViewById(R.id.tv_order_prioritas);
        tvError = itemView.findViewById(R.id.tv_error);
        tvDurationOrPrice = itemView.findViewById(R.id.tv_duration_or_price);
        tvPriceOrDuration = itemView.findViewById(R.id.tv_price_or_duration);
        tvTextDesc = itemView.findViewById(R.id.tv_text_desc);
        imgCheck = itemView.findViewById(R.id.img_check);
        rlContent = itemView.findViewById(R.id.rl_content);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
        tvShippingInformation = itemView.findViewById(R.id.tv_shipping_information);
        labelCodAvailable = itemView.findViewById(R.id.lbl_cod_available);
        labelCodAvailabelEta = itemView.findViewById(R.id.lbl_cod_available_eta);
        imgMvc = itemView.findViewById(R.id.img_mvc);
        tvMvc = itemView.findViewById(R.id.tv_mvc_text);
        tvMvcError = itemView.findViewById(R.id.tv_mvc_error);
        layoutMvc = itemView.findViewById(R.id.layout_mvc);
        flDisableContainer = itemView.findViewById(R.id.fl_container);
        labelDynamicPricing = itemView.findViewById(R.id.lbl_dynamic_pricing);
    }

    public void bindData(ShippingDurationUiModel shippingDurationUiModel,
                         ShippingDurationAdapterListener shippingDurationAdapterListener,
                         boolean isDisableOrderPrioritas) {
        if (shippingDurationUiModel.isShowShippingInformation() && shippingDurationUiModel.getEtaErrorCode() == 1) {
            tvShippingInformation.setVisibility(View.VISIBLE);
        } else {
            tvShippingInformation.setVisibility(View.GONE);
        }

        if (shippingDurationAdapterListener.isToogleYearEndPromotionOn() &&
                shippingDurationUiModel.getServiceData().getIsPromo() == 1) {
            tvPromoPotency.setVisibility(View.VISIBLE);
        } else {
            tvPromoPotency.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shippingDurationUiModel.getErrorMessage())) {
            tvDurationOrPrice.setTextColor(ContextCompat.getColor(tvDurationOrPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
            tvPriceOrDuration.setVisibility(View.GONE);
            tvTextDesc.setVisibility(View.GONE);
            tvOrderPrioritas.setVisibility(View.GONE);
            tvError.setText(shippingDurationUiModel.getErrorMessage());
            tvError.setVisibility(View.VISIBLE);
        } else {
            tvDurationOrPrice.setTextColor(ContextCompat.getColor(tvDurationOrPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
            tvError.setVisibility(View.GONE);
            tvPriceOrDuration.setVisibility(View.VISIBLE);

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

        /*MVC*/
        if (shippingDurationUiModel.getMerchantVoucherModel() != null && shippingDurationUiModel.getMerchantVoucherModel().isMvc() == 1 ) {
            layoutMvc.setVisibility(View.VISIBLE);
            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext() , R.drawable.fg_enabled_item));
            ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.getMerchantVoucherModel().getMvcLogo());
            tvMvc.setText(shippingDurationUiModel.getMerchantVoucherModel().getMvcTitle());
            tvMvcError.setVisibility(View.GONE);
        } else if (shippingDurationUiModel.getMerchantVoucherModel() != null && shippingDurationUiModel.getMerchantVoucherModel().isMvc() == -1 ){
            layoutMvc.setVisibility(View.VISIBLE);
            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext() , R.drawable.fg_disabled_item));
            ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.getMerchantVoucherModel().getMvcLogo());
            tvMvc.setText(shippingDurationUiModel.getMerchantVoucherModel().getMvcTitle());
            tvMvcError.setVisibility(View.VISIBLE);
            tvMvcError.setText(shippingDurationUiModel.getMerchantVoucherModel().getMvcErrorMessage());
        } else {
            layoutMvc.setVisibility(View.GONE);
            tvMvcError.setVisibility(View.GONE);
        }

        /*ETA*/
        if (shippingDurationUiModel.getServiceData().getTexts().getErrorCode() == 0) {
            String shipperNameEta = "";
            if (shippingDurationUiModel.getServiceData().getRangePrice().getMinPrice() == shippingDurationUiModel.getServiceData().getRangePrice().getMaxPrice()) {
                shipperNameEta = shippingDurationUiModel.getServiceData().getServiceName() + " " + "(" +
                        Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.getServiceData().getRangePrice().getMinPrice(), false)) + ")";;
            } else {
                String rangePrice = Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.getServiceData().getRangePrice().getMinPrice(), false)) + " - " +
                        Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.getServiceData().getRangePrice().getMaxPrice(), false));
                shipperNameEta = shippingDurationUiModel.getServiceData().getServiceName() + " " + "(" + rangePrice + ")";
            }
            if (!shippingDurationUiModel.getServiceData().getTexts().getTextEtaSummarize().isEmpty()) {
                tvPriceOrDuration.setText(shippingDurationUiModel.getServiceData().getTexts().getTextEtaSummarize());
            } else {
                tvPriceOrDuration.setText(R.string.estimasi_tidak_tersedia);
            }
            TextAndContentDescriptionUtil.setTextAndContentDescription(tvDurationOrPrice, shipperNameEta, tvDurationOrPrice.getContext().getString(R.string.content_desc_tv_duration));
            labelCodAvailable.setVisibility(View.GONE);
            labelCodAvailabelEta.setText(shippingDurationUiModel.getCodText());
            labelCodAvailabelEta.setVisibility(shippingDurationUiModel.isCodAvailable() ? View.VISIBLE : View.GONE);
        } else {
            TextAndContentDescriptionUtil.setTextAndContentDescription(tvDurationOrPrice, shippingDurationUiModel.getServiceData().getServiceName(), tvDurationOrPrice.getContext().getString(R.string.content_desc_tv_duration));
            tvPriceOrDuration.setText(shippingDurationUiModel.getServiceData().getTexts().getTextRangePrice());
            labelCodAvailabelEta.setVisibility(View.GONE);
            labelCodAvailable.setText(shippingDurationUiModel.getCodText());
            labelCodAvailable.setVisibility(shippingDurationUiModel.isCodAvailable() ? View.VISIBLE : View.GONE);
        }

        /*Dynamic Price*/
        if (shippingDurationUiModel.getDynamicPriceModel().getTextLabel().isEmpty()) {
            labelDynamicPricing.setVisibility(View.GONE);
        } else {
            labelDynamicPricing.setVisibility(View.VISIBLE);
            labelDynamicPricing.setText(shippingDurationUiModel.getDynamicPriceModel().getTextLabel());
        }

        imgCheck.setVisibility(shippingDurationUiModel.isSelected() ? View.VISIBLE : View.GONE);

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
