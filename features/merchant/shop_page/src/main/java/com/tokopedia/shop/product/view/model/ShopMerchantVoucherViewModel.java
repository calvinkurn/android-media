package com.tokopedia.shop.product.view.model;

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopMerchantVoucherViewModel implements BaseShopProductViewModel {

    private ArrayList<MerchantVoucherViewModel> merchantVoucherViewModels;

    public ShopMerchantVoucherViewModel(ArrayList<MerchantVoucherViewModel> merchantVoucherViewModels) {
        setMerchantVoucherViewModels(merchantVoucherViewModels);
    }

    public void setMerchantVoucherViewModels(ArrayList<MerchantVoucherViewModel> merchantVoucherViewModels) {
        if (merchantVoucherViewModels == null) {
            this.merchantVoucherViewModels = new ArrayList<>();
        } else {
            this.merchantVoucherViewModels = merchantVoucherViewModels;
        }
    }

    public ArrayList<MerchantVoucherViewModel> getShopMerchantVoucherViewModelArrayList() {
        return merchantVoucherViewModels;
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
