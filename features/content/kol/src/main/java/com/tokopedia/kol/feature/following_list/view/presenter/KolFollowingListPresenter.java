package com.tokopedia.kol.feature.following_list.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.following_list.data.pojo.DataItem;
import com.tokopedia.kol.feature.following_list.data.pojo.FollowerListData;
import com.tokopedia.kol.feature.following_list.domain.interactor
        .GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetKolFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.subscriber
        .GetKolFollowingListLoadMoreSubscriber;
import com.tokopedia.kol.feature.following_list.view.subscriber.GetKolFollowingListSubscriber;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View>
        implements KolFollowingList.Presenter {

    private KolFollowingList.View mainView;
    private final GetKolFollowingListUseCase getKolFollowingListUseCase;
    private final GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase;
    private final GraphqlUseCase getFollowerList;

    public static final String PARAM_ID = "userID";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";


    public static final int DEFAULT_LIMIT = 10;

    @Inject
    public KolFollowingListPresenter(GetKolFollowingListUseCase getKolFollowingListUseCase,
                                     GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase,
                                     GraphqlUseCase getFollowerList) {
        this.getKolFollowingListUseCase = getKolFollowingListUseCase;
        this.getKolFollowingListLoadMoreUseCase = getKolFollowingListLoadMoreUseCase;
        this.getFollowerList = getFollowerList;
    }

    @Override
    public void getKolFollowingList(int userId) {
        mainView.showLoading();
        if (!getView().isOpenFollowerPage()) {
            getKolFollowingListUseCase.execute(GetKolFollowingListUseCase.getParam(userId),
                    new GetKolFollowingListSubscriber(mainView));
        } else {
            getFollowersList(userId);
        }
    }

    @Override
    public void getKolLoadMore(int userId, String cursor) {
        if (!getView().isOpenFollowerPage()) {
            getKolFollowingListLoadMoreUseCase.execute(GetKolFollowingListLoadMoreUseCase.getParam(userId, cursor),
                    new GetKolFollowingListLoadMoreSubscriber(mainView));
        } else {
            getFollowersLoadMore(userId, cursor);
        }

    }

    @Override
    public void attachView(KolFollowingList.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolFollowingListUseCase.unsubscribe();
        getKolFollowingListLoadMoreUseCase.unsubscribe();
        getFollowerList.unsubscribe();
    }

    @Override
    public void getFollowersList(int userId) {
        if (isViewAttached()) {
            String query = GraphqlHelper.loadRawString(mainView.getContext().getResources(), R.raw.query_get_kol_followers_list);
            GraphqlRequest request = new GraphqlRequest(query, FollowerListData.class, getFollowerListParam(userId, "").getParameters());
            getFollowerList.clearRequest();
            getFollowerList.addRequest(request);
            getFollowerList.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mainView.hideLoading();
                    getView().onErrorGetKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    mainView.hideLoading();
                    FollowerListData data = graphqlResponse.getData(FollowerListData.class);
                    getView().onSuccessGetKolFollowingList(mapFollowersData(data));
                }
            });
        }
    }

    @Override
    public void getFollowersLoadMore(int userId, String cursor) {
        if (isViewAttached()) {
            String query = GraphqlHelper.loadRawString(mainView.getContext().getResources(), R.raw.query_get_kol_followers_list);
            GraphqlRequest request = new GraphqlRequest(query, FollowerListData.class, getFollowerListParam(userId, cursor).getParameters());
            getFollowerList.clearRequest();
            getFollowerList.addRequest(request);
            getFollowerList.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().onErrorLoadMoreKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    FollowerListData data = graphqlResponse.getData(FollowerListData.class);
                    getView().onSuccessLoadMoreKolFollowingList(mapFollowersData(data));
                }
            });
        }
    }

    private RequestParams getFollowerListParam(int id, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }

    private KolFollowingResultViewModel mapFollowersData(FollowerListData data) {
        return new KolFollowingResultViewModel(
                !data.getFeedGetUserFollowers().getMeta().getNextCursor().isEmpty(),
                data.getFeedGetUserFollowers().getMeta().getNextCursor(),
                mapFollowData(data.getFeedGetUserFollowers().getData()),
                "",
                ""
        );
    }

    private List<KolFollowingViewModel> mapFollowData(List<DataItem> dataList) {
        List<KolFollowingViewModel> resultList = new ArrayList<>();
        for (DataItem data: dataList) {
            resultList.add(new KolFollowingViewModel(
                    data.getId(),
                    data.getPhoto(),
                    data.getApplink(),
                    data.getApplink(),
                    false,
                    data.getName()
            ));
        }
        return resultList;
    }
}
