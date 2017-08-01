package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.domain.model.outlet.DataOutletDomain;
import com.tokopedia.posapp.domain.model.outlet.OutletDomain;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletItemViewModel;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;

import java.util.ArrayList;
import java.util.List;

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
        e.printStackTrace();
        viewListener.onErrorGetOutlet(e.getMessage());
        viewListener.finishLoading();
    }

    @Override
    public void onNext(OutletDomain getOutletDomain) {
        OutletViewModel model = convertToOutletViewModel(getOutletDomain);
        if(model != null) {
            viewListener.onSuccessGetOutlet(model);
        } else {
            viewListener.onErrorGetOutlet("Empty data");
        }
        viewListener.finishLoading();
    }

    private OutletViewModel convertToOutletViewModel(OutletDomain getOutletDomain) {
        if(getOutletDomain.getListOutlet() != null) {
            OutletViewModel outletViewModel = new OutletViewModel();
            List<OutletItemViewModel> outletList = new ArrayList<>();
            for(DataOutletDomain outletDomain : getOutletDomain.getListOutlet()) {
                OutletItemViewModel outletItemViewModel = new OutletItemViewModel(
                        outletDomain.getOutletId(),
                        outletDomain.getOutletName(),
                        outletDomain.getOutletAddres(),
                        outletDomain.getOutletPhone()
                );
                outletList.add(outletItemViewModel);
            }
            outletViewModel.setOutletList(outletList);

            outletViewModel.setNextUri(getOutletDomain.getUriNext());
            outletViewModel.setPrevUri(getOutletDomain.getUriPrevious());
            return outletViewModel;
        }

        return null;
    }
}
