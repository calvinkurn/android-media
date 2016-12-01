package com.tokopedia.core.manage.people.password.interactor;

/**
 * Created by stevenfredian on 9/28/16.
 */
public class ManagePasswordInteractorImpl implements ManagePasswordInteractor{

//    PAS peopleService;
//    CompositeSubscription compositeSubscription;
//
//    public ManagePasswordInteractorImpl() {
//        peopleService = new PeopleService();
//        compositeSubscription = new CompositeSubscription();
//    }
//
//    @Override
//    public void changePassword(Context context, Map<String,String> param
//            , final ChangePasswordListener listener) {
//        Observable<Response<TkpdResponse>> observable = peopleService.getApi()
//                .(AuthUtil.generateParams(context, param));
//
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccess()) {
//                    if (!response.body().isError()) {
//                        listener.onSuccess(response.body().convertDataObj(Profile.class));
//                    } else {
//                        if (response.body().isNullData()) listener.onNullData();
//                        else listener.onError(response.body().getErrorMessages().get(0));
//                    }
//                }
//                else {
//                    new ErrorHandler(new ErrorListener() {
//                        @Override
//                        public void onUnknown() {
//                            listener.onError(null);
//                        }
//
//                        @Override
//                        public void onTimeout() {
//                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getProfile(context, param, listener);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onServerError() {
//                            listener.onError(null);
//                        }
//
//                        @Override
//                        public void onBadRequest() {
//                            listener.onError(null);
//                        }
//
//                        @Override
//                        public void onForbidden() {
//                            listener.onError(null);
//                        }
//                    }, response.code());
//                }
//            }
//        };
//
//        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
}
