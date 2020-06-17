package com.tokopedia.checkout.subfeature.multiple_address.view.viewholder;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.subfeature.multiple_address.domain.model.MultipleAddressAdapterData;
import com.tokopedia.checkout.subfeature.multiple_address.view.MultipleAddressAdapter;
import com.tokopedia.checkout.subfeature.multiple_address.view.MultipleAddressItemAdapter;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private Typography senderName;
    private Typography senderCity;
    private ImageView productImage;
    private Typography productName;
    private Typography productPrice;
    private RecyclerView shippingDestinationList;
    private ImageView imgFreeReturn;
    private Typography tvFreeReturnLabel;
    private Typography tvPreOrder;
    private Typography tvCashback;
    private Button btAddNewShipment;
    private FlexboxLayout productPoliciesLayout;
    private ImageView imgShopBadge;

    public MultipleAddressViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        senderName = itemView.findViewById(R.id.sender_name);
        senderCity = itemView.findViewById(R.id.sender_city);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        shippingDestinationList = itemView.findViewById(R.id.shipping_destination_list);
        imgFreeReturn = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.iv_free_return_icon);
        tvFreeReturnLabel = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.tv_free_return_label);
        tvPreOrder = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.tv_pre_order);
        tvCashback = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.tv_cashback);
        btAddNewShipment = itemView.findViewById(R.id.bt_add_new_shipment);
        productPoliciesLayout = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.rl_product_policies_layout);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);

    }

    public void bindAdapterView(
            ArrayList<MultipleAddressAdapterData> dataList,
            MultipleAddressAdapterData data,
            MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
            MultipleAddressAdapter.MultipleAddressAdapterListener addressListener,
            CompositeSubscription compositeSubscription,
            boolean firstItemPosition,
            boolean isShowOnboarding
    ) {
        if (data.isOfficialStore()) {
            ImageHandler.LoadImage(imgShopBadge, data.getOfficialStoreLogoUrl());
            imgShopBadge.setVisibility(View.VISIBLE);
        } else if (data.isGoldMerchant()) {
            ImageHandler.LoadImage(imgShopBadge, data.getGoldMerchantLogoUrl());
            imgShopBadge.setVisibility(View.VISIBLE);
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        String labelShop = senderName.getContext().getResources().getString(com.tokopedia.purchase_platform.common.R.string.label_toko) + " ";
        int startLabelShop = labelShop.length();
        String shopName = data.getSenderName();

        SpannableStringBuilder completeLabelShop = new SpannableStringBuilder();
        completeLabelShop.append(labelShop);
        completeLabelShop.append(shopName);
        completeLabelShop.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLabelShop, completeLabelShop.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        senderName.setText(completeLabelShop);
        senderCity.setText(data.getSenderCity());
        productName.setText(data.getProductName());
        productPrice.setText(data.getProductPrice());
        ImageHandler.LoadImage(productImage, data.getProductImageUrl());
        shippingDestinationList.setLayoutManager(new LinearLayoutManager(context));
        shippingDestinationList.setAdapter(
                new MultipleAddressItemAdapter(
                        getAdapterPosition(), data, data.getItemListData(), listener, compositeSubscription)
        );
        ((SimpleItemAnimator) shippingDestinationList.getItemAnimator()).setSupportsChangeAnimations(false);
        btAddNewShipment.setOnClickListener(onAddAddressClickedListener(dataList, addressListener));
        if (firstItemPosition) {
            setShowCase(isShowOnboarding);
        }

        if (data.isFreeReturn()) {
            imgFreeReturn.setVisibility(View.VISIBLE);
        } else {
            imgFreeReturn.setVisibility(View.GONE);
        }

        if (data.isCashBack()) {
            tvCashback.setText(data.getCashBackInfo());
            tvCashback.setVisibility(View.VISIBLE);
        } else {
            tvCashback.setVisibility(View.GONE);
        }

        if (data.isPreOrder()) {
            tvPreOrder.setText(data.getPreOrderInfo());
            tvPreOrder.setVisibility(View.VISIBLE);
        } else {
            tvPreOrder.setVisibility(View.GONE);
        }

        if (data.isCashBack() || data.isFreeReturn() || data.isPreOrder()) {
            productPoliciesLayout.setVisibility(View.VISIBLE);
        } else {
            productPoliciesLayout.setVisibility(View.GONE);
        }
    }

    private void setShowCase(boolean isShowOnboarding) {
        ShowCaseObject showCase = new ShowCaseObject(
                btAddNewShipment, context.getString(R.string.label_showcase_multiple_address_title),
                context.getString(R.string.label_showcase_multiple_address_message),
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        if (isShowOnboarding)
            showCaseDialog.show(
                    (Activity) context,
                    MultipleAddressViewHolder.class.getName(),
                    showCaseObjectList
            );
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(com.tokopedia.logisticcart.R.layout.show_case_checkout)
                .titleTextColorRes(com.tokopedia.abstraction.R.color.white)
                .spacingRes(com.tokopedia.abstraction.R.dimen.dp_12)
                .arrowWidth(com.tokopedia.abstraction.R.dimen.dp_16)
                .textColorRes(com.tokopedia.abstraction.R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(com.tokopedia.abstraction.R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    private View.OnClickListener onAddAddressClickedListener(
            final ArrayList<MultipleAddressAdapterData> dataList,
            final MultipleAddressAdapter.MultipleAddressAdapterListener addressListener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressListener.onAddNewShipmentAddress(dataList, getAdapterPosition());
            }
        };
    }

}
