package com.tokopedia.core.myproduct.presenter;

import android.content.Context;

import com.tokopedia.core.myproduct.model.GetShopNoteModel;
import com.tokopedia.core.myproduct.model.MyShopInfoModel;

import java.util.Map;

/**
 * Created by noiz354 on 5/18/16.
 */
public interface AddProductPresenter {
    String TAG = "STUART";
    String messageTAG = "AddProductPresenter";

    void setView(AddProductView addProductView);
    void fetchDepartmentParent(Context context);
    void fetchEtalase(Context context);
    void clearEtalaseCache(Context context);

    /**
     * untuk check ketersediaan note di database atau di network
     * @param context
     * @param isShouldFetchData flag untuk menandakan apakah action dibarengin dengan fetch edit
     *                          data dari network maupun dari database
     */
    void checkNoteAvailibility(Context context, boolean isShouldFetchData);

    void clearNoteAvailibility();
    void getMyShopInfo(Context context);
    void getReturnPolicyDetail(Context context, MyShopInfoModel.Info info, GetShopNoteModel.ShopNoteModel returnPolicy);
    void fetchDepartmentChild(Context context, final int depId, final int level);
    void fetchCatalog(Context context, String productDepId, String productName);
    void fetchEditData(Context context, String productId);
    Map<String, Object> getOriginalEditData();
    void subscribe();
    void unsubscribe();
    void getProductDb(Context activity, long productDb);
    void setupNameDebounceListener(Context context);
    void onNameChange(String depId, String productName);
}
