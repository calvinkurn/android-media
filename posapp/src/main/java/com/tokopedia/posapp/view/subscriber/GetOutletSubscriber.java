package com.tokopedia.posapp.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.posapp.domain.model.outlet.OutletDomain;
import com.tokopedia.posapp.view.Outlet;

import rx.Subscriber;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletSubscriber extends Subscriber<OutletDomain> {
    private final Outlet.View viewListener;

    public GetOutletSubscriber(Outlet.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("error outlet");
        e.printStackTrace();
    }

    @Override
    public void onNext(OutletDomain getOutletDomains) {
        // TODO: 7/31/17 complete this
//        viewListener.onSuccessGetOutlet();
        CommonUtils.dumper("berhasil on next get outletdomains");
    }
}
