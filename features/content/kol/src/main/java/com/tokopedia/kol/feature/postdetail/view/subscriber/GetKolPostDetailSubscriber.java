//TODO DELETE This file
//package com.tokopedia.kol.feature.postdetail.view.subscriber;
//
//import com.tokopedia.abstraction.base.view.adapter.Visitable;
//import com.tokopedia.config.GlobalConfig;
//import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
//import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
//import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import rx.Subscriber;
//
///**
// * @author by milhamj on 27/07/18.
// */
//
//public class GetKolPostDetailSubscriber extends Subscriber<PostDetailViewModel> {
//
//    private final KolPostDetailContract.View view;
//
//    public GetKolPostDetailSubscriber(KolPostDetailContract.View view) {
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
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            e.printStackTrace();
//        }
//        view.dismissLoading();
//        view.onErrorGetKolPostDetail(ErrorHandler.getErrorMessage(view.getContext(), e));
//        view.stopTrace();
//    }
//
//    @Override
//    public void onNext(PostDetailViewModel postDetailViewModel) {
//
//        view.dismissLoading();
//        List<Visitable> list = new ArrayList<>();
//        list.addAll(postDetailViewModel.getDynamicPostViewModel().getPostList());
//        if(!list.isEmpty()) {
//            view.onSuccessGetKolPostDetail(list, postDetailViewModel);
//        }else{
//            view.onEmptyDetailFeed();
//        }
//        view.stopTrace();
//
//    }
//
//}
