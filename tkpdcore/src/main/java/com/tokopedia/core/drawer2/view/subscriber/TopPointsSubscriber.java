package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsData;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsSubscriber extends Subscriber<TopPointsModel> {

    private final DrawerDataListener viewListener;

    public TopPointsSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetTopPoints(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(TopPointsModel topPointsModel) {
        if (topPointsModel.isSuccess())
            viewListener.onGetTopPoints(
                    convertToViewModel(
                            topPointsModel.getTopPointsData()));
        else
            viewListener.onErrorGetTopPoints(
                    viewListener.getString(R.string.default_request_error_unknown));
    }

    private DrawerTopPoints convertToViewModel(TopPointsData topPointsData) {
        DrawerTopPoints drawerTopPoints = new DrawerTopPoints();
        drawerTopPoints.setActive(topPointsData.isActive());
        drawerTopPoints.setTopPoints(topPointsData.getLoyaltyPoint().getAmount());
        drawerTopPoints.setTopPointsUrl(topPointsData.getUri());
        return drawerTopPoints;
    }
}
