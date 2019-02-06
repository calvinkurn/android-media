package com.tokopedia.recentview.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.recentview.domain.model.RecentViewBadgeDomain;
import com.tokopedia.recentview.domain.model.RecentViewLabelDomain;
import com.tokopedia.recentview.domain.model.RecentViewProductDomain;
import com.tokopedia.recentview.view.listener.RecentView;
import com.tokopedia.recentview.view.viewmodel.LabelsViewModel;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewSubscriber extends Subscriber<List<RecentViewProductDomain>> {

    private static final java.lang.String CASHBACK = "Cashback";
    private static final java.lang.String OFFICIAL_STORE = "Official Store";

    private final RecentView.View viewListener;

    public RecentViewSubscriber(RecentView.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetRecentView(ErrorHandler.getErrorMessage(viewListener.getContext(), e));

    }

    @Override
    public void onNext(List<RecentViewProductDomain> recentViewProductDomains) {
        if (!recentViewProductDomains.isEmpty()) {
            ArrayList<RecentViewDetailProductViewModel> recentsViewModel = convertToViewModel(recentViewProductDomains);
            ArrayList<Visitable> visitableList = new ArrayList<>(recentsViewModel);

            viewListener.onSuccessGetRecentView(visitableList);
            viewListener.sendRecentViewImpressionTracking(recentsViewModel);
        } else {
            viewListener.onEmptyGetRecentView();
        }
    }

    private ArrayList<RecentViewDetailProductViewModel> convertToViewModel(List<RecentViewProductDomain> recentViewProductDomains) {
        ArrayList<RecentViewDetailProductViewModel> listProduct = new ArrayList<>();

        int position = 1;
        for (RecentViewProductDomain domain : recentViewProductDomains) {
            listProduct.add(new RecentViewDetailProductViewModel(
                    Integer.parseInt(domain.getId()),
                    domain.getName(),
                    domain.getPrice(),
                    domain.getImgUri(),
                    convertLabels(domain.getLabels()),
                    domain.getFreeReturn() != null && domain.getFreeReturn().equals("1"),
                    domain.getWishlist(),
                    Integer.parseInt(domain.getRating() != null ? domain.getRating() : "0"),
                    domain.getIsGold() != null && domain.getIsGold().equals("1"),
                    convertToIsOfficial(domain.getBadges()),
                    domain.getShop().getName(),
                    domain.getShop().getLocation(),
                    position
            ));
            position++;
        }
        return listProduct;
    }

    private List<LabelsViewModel> convertLabels(List<RecentViewLabelDomain> labels) {
        List<LabelsViewModel> labelsViewModels = new ArrayList<>();
        for (RecentViewLabelDomain labelDomain : labels) {
            labelsViewModels.add(new LabelsViewModel(labelDomain.getTitle(),
                    labelDomain.getColor()));
        }
        return labelsViewModels;
    }

    private boolean convertToIsOfficial(List<RecentViewBadgeDomain> badges) {
        if (!badges.isEmpty()) {
            for (RecentViewBadgeDomain domain : badges) {
                if (domain.getTitle().contains(OFFICIAL_STORE)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String convertToCashback(List<RecentViewLabelDomain> labels) {
        if (!labels.isEmpty() && labels.get(0).getTitle().startsWith(CASHBACK))
            return labels.get(0).getTitle();
        else return "";
    }
}
