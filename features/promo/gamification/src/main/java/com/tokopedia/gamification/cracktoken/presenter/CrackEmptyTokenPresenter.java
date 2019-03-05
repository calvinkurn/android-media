package com.tokopedia.gamification.cracktoken.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.contract.CrackEmptyTokenContract;
import com.tokopedia.gamification.data.entity.GamificationSumCouponOuter;
import com.tokopedia.gamification.data.entity.TokoPointDetailEntity;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public class CrackEmptyTokenPresenter extends BaseDaggerPresenter<CrackEmptyTokenContract.View>
        implements CrackEmptyTokenContract.Presenter {

    private GraphqlUseCase getRewardsUseCase;

    @Inject
    public CrackEmptyTokenPresenter(GraphqlUseCase getRewardsUseCase) {
        this.getRewardsUseCase = getRewardsUseCase;
    }

    public void getRewardsCount() {
        getRewardsUseCase.clearRequest();
        GraphqlRequest sumTokenRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_sum_coupon),
                GamificationSumCouponOuter.class);
        getRewardsUseCase.addRequest(sumTokenRequest);
        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_current_points),
                TokoPointDetailEntity.class);
        getRewardsUseCase.addRequest(graphqlRequestPoints);
        getRewardsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                GamificationSumCouponOuter gamificationSumCouponOuter = graphqlResponse.getData(GamificationSumCouponOuter.class);
                TokoPointDetailEntity tokoPointDetailEntity = graphqlResponse.getData(TokoPointDetailEntity.class);
                int points = 0;
                int loyalty = 0;
                int coupons = 0;
                if (gamificationSumCouponOuter != null && gamificationSumCouponOuter.getTokopointsSumCoupon() != null)
                    coupons = gamificationSumCouponOuter.getTokopointsSumCoupon().getSumCoupon();
                if (tokoPointDetailEntity != null && tokoPointDetailEntity.getTokoPoints() != null && tokoPointDetailEntity.getTokoPoints().getStatus() != null && tokoPointDetailEntity.getTokoPoints().getStatus().getPoints() != null) {
                    loyalty = tokoPointDetailEntity.getTokoPoints().getStatus().getPoints().getLoyalty();
                    points = tokoPointDetailEntity.getTokoPoints().getStatus().getPoints().getReward();
                }
                getView().updateRewards(points, coupons, loyalty);
            }
        });

    }


    @Override
    public void detachView() {
        getRewardsUseCase.unsubscribe();
        super.detachView();
    }
}
