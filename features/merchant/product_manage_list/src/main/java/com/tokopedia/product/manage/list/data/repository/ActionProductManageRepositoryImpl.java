package com.tokopedia.product.manage.list.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.product.manage.list.data.source.ActionProductManageDataSource;
import com.tokopedia.product.manage.list.domain.ActionProductManageRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionProductManageRepositoryImpl implements ActionProductManageRepository {

    private final ActionProductManageDataSource actionProductManageDataSource;

    public ActionProductManageRepositoryImpl(ActionProductManageDataSource actionProductManageDataSource) {
        this.actionProductManageDataSource = actionProductManageDataSource;
    }

    @Override
    public Observable<Boolean> editPrice(TKPDMapParam<String, String> params) {
        return actionProductManageDataSource.editPrice(params);
    }

    @Override
    public Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params) {
        return actionProductManageDataSource.deleteProduct(params);
    }
}
