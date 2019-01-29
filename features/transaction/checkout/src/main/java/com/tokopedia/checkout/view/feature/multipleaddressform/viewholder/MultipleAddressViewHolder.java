package com.tokopedia.checkout.view.feature.multipleaddressform.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressAdapter;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressItemAdapter;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceFragment;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private TextView senderName;
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView shippingDestinationList;
    private ImageView imgFreeReturn;
    private TextView tvFreeReturnLabel;
    private TextView tvPreOrder;
    private TextView tvCashback;
    private Button btAddNewShipment;
    private LinearLayout rlProductPoliciesLayout;
    private ImageView imgShopBadge;

    public MultipleAddressViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        senderName = itemView.findViewById(R.id.sender_name);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        shippingDestinationList = itemView.findViewById(R.id.shipping_destination_list);
        imgFreeReturn = itemView.findViewById(R.id.iv_free_return_icon);
        tvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        tvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        tvCashback = itemView.findViewById(R.id.tv_cashback);
        btAddNewShipment = itemView.findViewById(R.id.bt_add_new_shipment);
        rlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);

    }

    public void bindAdapterView(
            ArrayList<MultipleAddressAdapterData> dataList,
            MultipleAddressAdapterData data,
            MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
            MultipleAddressAdapter.MultipleAddressAdapterListener addressListener,
            CompositeSubscription compositeSubscription,
            boolean firstItemPosition
    ) {
        if (data.isOfficialStore()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_badge_official));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else if (data.isGoldMerchant()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_shop_gold));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        senderName.setText(data.getSenderName());
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
            setShowCase();
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
            rlProductPoliciesLayout.setVisibility(View.VISIBLE);
        } else {
            rlProductPoliciesLayout.setVisibility(View.GONE);
        }
    }

    private void setShowCase() {
        ShowCaseObject showCase = new ShowCaseObject(
                btAddNewShipment, context.getString(R.string.label_showcase_multiple_address_title),
                context.getString(R.string.label_showcase_multiple_address_message),
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        if (!ShowCasePreference.hasShown(context, CartAddressChoiceFragment.class.getName()))
            showCaseDialog.show(
                    (Activity) context,
                    CartAddressChoiceFragment.class.getName(),
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
