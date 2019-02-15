//package com.tokopedia.withdraw.domain.usecase;
//
//import com.tokopedia.usecase.RequestParams;
//import com.tokopedia.usecase.UseCase;
//import com.tokopedia.user.session.UserSession;
//import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
//import com.tokopedia.withdraw.domain.source.DepositSource;
//
//import java.util.Date;
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import rx.Observable;
//
//import static com.tokopedia.network.utils.AuthUtil.md5;
//
///**
// * @author by StevenFredian on 30/07/18.
// */
//
//public class DepositUseCase extends UseCase<InfoDepositDomainModel> {
//
//    private DepositSource depositSource;
//
//    private static final String PARAM_USER_ID = "user_id";
//    private static final String PARAM_DEVICE_ID = "device_id";
//    private static final String PARAM_HASH = "hash";
//    private static final String PARAM_OS_TYPE = "os_type";
//    private static final String PARAM_TIMESTAMP = "device_time";
//
//    @Inject
//    public DepositUseCase(DepositSource depositSource) {
//        this.depositSource = depositSource;
//    }
//
//    @Override
//    public Observable<InfoDepositDomainModel> createObservable(RequestParams requestParams) {
//        return depositSource.getWithdrawForm(getParameters(requestParams));
//    }
//
//    private HashMap<String, Object> getParameters(RequestParams requestParams) {
//        return requestParams.getParameters();
//    }
//
//    public static RequestParams createParams(UserSession userSession) {
//        RequestParams requestParams = RequestParams.create();
//        String deviceId = userSession.getDeviceId();
//        String userId = userSession.getUserId();
//        String hash = md5(userId + "~" + deviceId);
//
//        HashMap<String, Object> params = new HashMap<>();
//        params.put(PARAM_USER_ID, userId);
//        params.put(PARAM_DEVICE_ID, deviceId);
//        params.put(PARAM_HASH, hash);
//        params.put(PARAM_OS_TYPE, "1");
//        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
//
//        requestParams.putAll(params);
//
//        return requestParams;
//    }
//
//}
