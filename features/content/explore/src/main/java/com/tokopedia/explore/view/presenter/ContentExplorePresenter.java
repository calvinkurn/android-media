package com.tokopedia.explore.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.explore.domain.interactor.GetExploreDataUseCase;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.subscriber.EmptySubscriber;
import com.tokopedia.explore.view.subscriber.GetExploreDataSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/07/18.
 */

public class ContentExplorePresenter
        extends BaseDaggerPresenter<ContentExploreContract.View>
        implements ContentExploreContract.Presenter {

    private final GetExploreDataUseCase getExploreDataUseCase;
    private final TrackAffiliateClickUseCase trackAffiliateClickUseCase;
    private String cursor = "";
    private int categoryId = 0;
    private String search = "";

    @Inject
    public ContentExplorePresenter(GetExploreDataUseCase getExploreDataUseCase, TrackAffiliateClickUseCase trackAffiliateClickUseCase) {
        this.getExploreDataUseCase = getExploreDataUseCase;
        this.trackAffiliateClickUseCase = trackAffiliateClickUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getExploreDataUseCase.unsubscribe();
    }

    @Override
    public void getExploreData(boolean clearData) {
        if (clearData) {
            getView().showRefreshing();
        } else {
            getView().showLoading();
        }
        getExploreDataUseCase.execute(
                GetExploreDataUseCase.getVariables(categoryId, cursor, search),
                new GetExploreDataSubscriber(getView(), clearData)
        );
    }

    @Override
    public void updateCursor(String cursor) {
        this.cursor = cursor;
    }

    @Override
    public void updateCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void updateSearch(String search) {
        this.search = search;
    }

    @Override
    public void trackAffiliate(String url) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                new EmptySubscriber()
        );
    }
}
