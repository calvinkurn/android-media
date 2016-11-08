package com.tokopedia.core.myproduct.presenter;

import android.content.Context;

import com.tokopedia.core.myproduct.model.EditPriceParam;
import com.tokopedia.core.myproduct.model.GetShopNoteModel;
import com.tokopedia.core.myproduct.model.MyShopInfoModel;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by noiz354 on 5/18/16.
 * Modify by sebast on 5/31/16 for manage product API
 */
public interface NetworkInteractor  {
    String TAG = "STUART";
    String messageTAG = "DiscoveryInteractor";
    String CATALOG_MODEL_EDIT = "CATALOG_MODEL_EDIT";
    String PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA";
    String DEPARTMENT_DATA = "DEPARTMENT_DATA";
    String IMAGE_MODEL_DOWNLOADS = "IMAGE_MODEL_DOWNLOADS";
    String EDIT_PRODUCT_FORM = "EDIT_PRODUCT_FORM";
    int PRD_STATE_DELETED = 0;
    int PRD_STATE_ACTIVE = 1;
    int PRD_STATE_BEST = 2;
    int PRD_STATE_WAREHOUSE = 3;
    int PRD_STATE_PENDING  = -1;
    int PRD_STATE_BANNED = -2;

    void fetchDepartment(Context context);
    void fetchEtalase(Context context);
    void checkAvailibilityOfShopNote(Context context);
    void getMyShopInfo(Context context);
    void getReturnPolicyDetail(Context context, MyShopInfoModel.Info info, GetShopNoteModel.ShopNoteModel returnPolicy);
    void fetchDepartmentChild(Context context, final int depId, final int level);
    void fetchCatalog(Context context, String productDepId, String productName);
    void editProductDetail(CompositeSubscription compositeSubscription, Context context, String productId, String productName, String shopDomain);
    // FROM MANAGE PRODUCT
    void getProductList(Context context,
                        String sort, String keyword, String page,
                        String etalase_id, String catalog_id, String department_id,
                        String picture_status, String condition);
    void changeCategories(Context context,
                          String CtgID, final String ID, String shopID);
    void changeReturnable(Context context, String returnableCondition, String ID);
    void changeInsurance(Context context, String insuranceID, String ID);
    void deleteProduct(Context context, String ID);
    void editPrice(Context context, EditPriceParam param);
    void editEtalase(Context context, String productId, String etalaseID, String etalaseName, int addTo);
}
