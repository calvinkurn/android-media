package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopMerchantVoucherViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopMerchantVoucherViewHolder extends AbstractViewHolder<ShopMerchantVoucherViewModel> {

    private MerchantVoucherListWidget merchantVoucherListWidget;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_merchant_voucher;

    public ShopMerchantVoucherViewHolder(View itemView, MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener onMerchantVoucherListWidgetListener) {
        super(itemView);
        findViews(itemView);
        merchantVoucherListWidget.setOnMerchantVoucherListWidgetListener(onMerchantVoucherListWidgetListener);
    }

    @Override
    public void bind(ShopMerchantVoucherViewModel shopMerchantVoucherViewModel) {
        Parcelable recyclerViewState = merchantVoucherListWidget.getRecyclerView().getLayoutManager().onSaveInstanceState();

        merchantVoucherListWidget.setData(shopMerchantVoucherViewModel.getShopMerchantVoucherViewModelArrayList());

        if (recyclerViewState != null) {
            merchantVoucherListWidget.getRecyclerView().getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void findViews(View view) {
        merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget);

    }

}
