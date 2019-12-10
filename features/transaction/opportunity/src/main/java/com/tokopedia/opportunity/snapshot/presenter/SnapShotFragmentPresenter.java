package com.tokopedia.opportunity.snapshot.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */
public abstract class SnapShotFragmentPresenter extends BaseDaggerPresenter<SnapShotFragmentView>{
    public abstract void processDataPass(ProductPass productPass);

    public abstract void requestProductDetail(String opportunityId, ProductPass productPass,
                                              int type, boolean forceNetwork);

}
