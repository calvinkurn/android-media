package com.tokopedia.tokopoints.notification;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.example.tokopoints.notification.R;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.notification.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.notification.view.TokoPointsPopupNotificationBottomSheet;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class TokoPointsNotificationManager {

    public static void fetchNotification(Context context, String notificationType, FragmentManager fragmentManager) {
        GraphqlUseCase useCase = new GraphqlUseCase();
        Map<String, Object> variable = new HashMap<>();
        variable.put(Constant.KEY_TYPE, notificationType);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(context.getApplicationContext().getResources(), R.raw.tp_gql_popup_notification),
                TokoPointDetailEntity.class, variable);
        useCase.clearRequest();
        useCase.addRequest(request);
        useCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                TokoPointDetailEntity data = response.getData(TokoPointDetailEntity.class);

                if (data == null
                        || data.getTokoPoints() == null
                        || data.getTokoPoints().getPopupNotif() == null
                        || data.getTokoPoints().getPopupNotif().isEmpty()) {
                    return;
                }

                TokoPointsPopupNotificationBottomSheet bottomSheets = new TokoPointsPopupNotificationBottomSheet();
                bottomSheets.setData(data.getTokoPoints().getPopupNotif());
                bottomSheets.show(fragmentManager, notificationType);
            }
        });
    }
}
