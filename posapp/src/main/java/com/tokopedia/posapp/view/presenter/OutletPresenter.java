package com.tokopedia.posapp.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.posapp.domain.usecase.GetOutletUseCase;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.subscriber.GetOutletSubscriber;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletPresenter extends BaseDaggerPresenter<Outlet.View>
        implements Outlet.Presenter {

    private GetOutletUseCase getOutletUseCase;
    private Outlet.View viewListener;

    @Inject
    OutletPresenter(GetOutletUseCase outletUseCase) {
        this.getOutletUseCase = outletUseCase;
    }

    @Override
    public void getOutlet(RequestParams params) {
        getOutletUseCase.execute(params, new GetOutletSubscriber(viewListener));
    }

    @Override
    public void attachView(Outlet.View view) {
        super.attachView(view);
        this.viewListener = view;
    }
}
