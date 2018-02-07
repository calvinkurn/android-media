package com.tokopedia.tkpd.beranda.presentation.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.listener.HomeFeedListener;
import com.tokopedia.tkpd.beranda.presentation.view.viewmodel.InspirationProductViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.InspirationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by henrypriyono on 1/3/18.
 */

public class GetHomeFeedsSubscriber extends Subscriber<FeedResult> {

    private static final String TYPE_INSPIRATION = "inspirasi";

    private final HomeFeedListener viewListener;
    private final int page;

    public GetHomeFeedsSubscriber(HomeFeedListener viewListener, int page) {
        this.viewListener = viewListener;
        this.page = page;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onShowRetryGetFeed();
    }

    @Override
    public void onNext(FeedResult feedResult) {
        ArrayList<Visitable> list = convertToViewModel(feedResult.getFeedDomain());

        if (feedResult.isHasNext()) {
            viewListener.updateCursor(getCurrentCursor(feedResult));
        }

        viewListener.onSuccessGetFeed(list);

        if (!feedResult.isHasNext()) {
            viewListener.unsetEndlessScroll();
        }
    }

    private ArrayList<Visitable> convertToViewModel(FeedDomain feedDomain) {
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        addFeedData(listFeedView, feedDomain.getListFeed());
        return listFeedView;
    }

    private void addFeedData(ArrayList<Visitable> listFeedView,
                             List<DataFeedDomain> listFeedDomain) {
        if (listFeedDomain != null) {
            for (DataFeedDomain domain : listFeedDomain) {
                switch (domain.getContent().getType() != null ? domain.getContent().getType() : "") {

                    case TYPE_INSPIRATION:
                        InspirationViewModel inspirationViewModel = convertToInspirationViewModel(domain);
                        if (inspirationViewModel != null
                                && inspirationViewModel.getListProduct() != null
                                && !inspirationViewModel.getListProduct().isEmpty())
                            listFeedView.add(inspirationViewModel);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private InspirationViewModel convertToInspirationViewModel(DataFeedDomain domain) {
        if (domain.getContent() != null
                && !domain.getContent().getInspirationDomains().isEmpty()) {
            return new InspirationViewModel(
                    domain.getContent().getInspirationDomains().get(0).getTitle(),
                    convertToRecommendationListViewModel(domain.getContent().getInspirationDomains().get(0).getListInspirationItem()),
                    domain.getContent().getInspirationDomains().get(0).getSource()
            );
        } else {
            return null;
        }
    }

    private ArrayList<InspirationProductViewModel> convertToRecommendationListViewModel(
            List<InspirationItemDomain> domains) {
        ArrayList<InspirationProductViewModel> listRecommendation = new ArrayList<>();
        if (domains != null && domains.size() == 4)
            for (InspirationItemDomain recommendationDomain : domains) {
                listRecommendation.add(convertToRecommendationViewModel(recommendationDomain));
            }
        return listRecommendation;
    }

    private InspirationProductViewModel convertToRecommendationViewModel(
            InspirationItemDomain recommendationDomain) {
        return new InspirationProductViewModel(recommendationDomain.getId(),
                recommendationDomain.getName(),
                recommendationDomain.getPrice(),
                recommendationDomain.getImageUrl(),
                recommendationDomain.getUrl(),
                page,
                recommendationDomain.getPriceInt());
    }

    private String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getFeedDomain().getListFeed().size() - 1;
        return feedResult.getFeedDomain().getListFeed().get(lastIndex).getCursor();
    }
}
