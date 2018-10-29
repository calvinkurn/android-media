package com.tokopedia.merchantvoucher.voucherList.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.merchantvoucher.R;


public class MerchantVoucherShimmerViewHolder extends AbstractViewHolder<LoadingModel> {
    public static final int LAYOUT = R.layout.item_shimmering_merchant_voucher;

    public MerchantVoucherShimmerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {

    }
}
