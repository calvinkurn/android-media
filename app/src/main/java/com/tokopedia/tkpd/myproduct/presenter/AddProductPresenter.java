package com.tokopedia.tkpd.myproduct.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.tkpd.myproduct.model.GetShopNoteModel;
import com.tokopedia.tkpd.myproduct.model.MyShopInfoModel;

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
    void checkNoteAvailibility(Context context);
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
}
