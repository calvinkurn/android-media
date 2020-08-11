package com.tokopedia.product.manage.item.main.base.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.EditProductFormApi;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.editproductform.EditProductFormServiceModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormCloud {
    public static final String SHOP_ID = "shop_id";
    public static final String PRODUCT_ID = "product_id";
    private final Context context;
    private final EditProductFormApi api;

    @Inject
    public EditProductFormCloud(@ApplicationContext Context context, EditProductFormApi api) {
        this.context = context;
        this.api = api;
    }

    public Observable<EditProductFormServiceModel> fetchEditProductForm(String productId) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        UserSessionInterface userSession = new UserSession(context);
        String shopId = userSession.getShopId();
        params.put(SHOP_ID, shopId);
        params.put(PRODUCT_ID, productId);
        return api.fetchEditProductForm(AuthUtil.generateParamsNetwork(context, params)).map(new Func1<Response<EditProductFormServiceModel>, EditProductFormServiceModel>() {
            @Override
            public EditProductFormServiceModel call(Response<EditProductFormServiceModel> editProductFormServiceModelResponse) {
                return editProductFormServiceModelResponse.body();
            }
        });
    }
}
