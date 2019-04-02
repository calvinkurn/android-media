package com.tokopedia.kol.feature.following_list.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.following_list.domain.interactor
        .GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetKolFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.subscriber
        .GetKolFollowingListLoadMoreSubscriber;
import com.tokopedia.kol.feature.following_list.view.subscriber.GetKolFollowingListSubscriber;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View>
        implements KolFollowingList.Presenter {

    private KolFollowingList.View mainView;
    private final GetKolFollowingListUseCase getKolFollowingListUseCase;
    private final GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase;

    @Inject
    public KolFollowingListPresenter(GetKolFollowingListUseCase getKolFollowingListUseCase,
                                     GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase) {
        this.getKolFollowingListUseCase = getKolFollowingListUseCase;
        this.getKolFollowingListLoadMoreUseCase = getKolFollowingListLoadMoreUseCase;
    }

    @Override
    public void getKolFollowingList(int userId) {
        mainView.showLoading();
        getKolFollowingListUseCase.execute(GetKolFollowingListUseCase.getParam(userId),
                new GetKolFollowingListSubscriber(mainView));
    }

    @Override
    public void getKolLoadMore(int userId, String cursor) {
        getKolFollowingListLoadMoreUseCase.execute(GetKolFollowingListLoadMoreUseCase.getParam(userId, cursor),
                new GetKolFollowingListLoadMoreSubscriber(mainView));

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
    }
}
