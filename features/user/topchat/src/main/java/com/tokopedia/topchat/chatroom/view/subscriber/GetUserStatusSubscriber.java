//package com.tokopedia.topchat.chatroom.view.subscriber;
//
//import com.tokopedia.core.util.GlobalConfig;
//import com.tokopedia.topchat.R;
//import com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus.GetUserStatusDataPojo;
//import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
//import com.tokopedia.topchat.chatroom.view.presenter.ChatRoomPresenter;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import rx.Subscriber;
//
///**
// * Created by Hendri on 31/07/18.
// */
//public class GetUserStatusSubscriber extends Subscriber<GetUserStatusDataPojo> {
//    ChatRoomPresenter presenter;
//    ChatRoomContract.View view;
//
//    public GetUserStatusSubscriber(ChatRoomPresenter presenter, ChatRoomContract.View
//            view) {
//        this.presenter = presenter;
//        this.view = view;
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        if(GlobalConfig.isAllowDebuggingTools()) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onNext(GetUserStatusDataPojo getUserStatusDataPojo) {
//        boolean isOnline = getUserStatusDataPojo.isOnline();
//        long timeStamp = (Calendar.getInstance().getTimeInMillis() / 1000)
//                - getUserStatusDataPojo.getTimestamp();
//        String status;
//        if(isOnline) {
//            status = view.getContext().getString(R.string.topchat_online_status_desc);
//        } else {
//            long minuteDivider = 60;
//            long hourDivider = minuteDivider * 60;
//            long dayDivider = hourDivider * 24;
//            long monthDivider = dayDivider * 30;
//            if((timeStamp / monthDivider) > 0){
//                status = view.getContext().getString(R.string
//                                .topchat_online_months_ago, timeStamp / monthDivider);
//            } else if((timeStamp / dayDivider) > 0) {
//                status = view.getContext().getString(R.string
//                        .topchat_online_days_ago, timeStamp / dayDivider);
//            } else if((timeStamp/hourDivider) > 0) {
//                status = view.getContext().getString(R.string.topchat_online_hours_ago,
//                        timeStamp/hourDivider);
//            } else {
//                long minutes = timeStamp / minuteDivider;
//                if(minutes <= 0) minutes = 1;
//                status = view.getContext().getString(R.string
//                        .topchat_online_minutes_ago, minutes);
//            }
//        }
//        view.setUserStatus(status,isOnline);
//    }
//}
