package com.tokopedia.attachproduct.view.presenter;

import com.tokopedia.attachcommon.data.ResultProduct;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 19/02/18.
 */

public interface AttachProductContract {
    interface View {
        void addProductToList(List<AttachProductItemUiModel> products, boolean hasNextPage);
        void hideAllLoadingIndicator();
        void showErrorMessage(Throwable throwable);
        void updateButtonBasedOnChecked(int checkedCount);
        void setShopName(String shopName);
    }
    interface Activity {
        boolean isSeller();
        String getShopId();
        void finishActivityWithResult(ArrayList<ResultProduct> products);
        void goToAddProduct(String shopId);
        void setShopName(String shopName);
    }
    interface Presenter {
        void loadProductData(String query, String shopId, int page);
        void updateCheckedList(List<AttachProductItemUiModel> products);
        void resetCheckedList();
        void completeSelection();
        void attachView(AttachProductContract.View view);
        void attachActivityContract(AttachProductContract.Activity activityContract);
        void detachView();
    }
}
