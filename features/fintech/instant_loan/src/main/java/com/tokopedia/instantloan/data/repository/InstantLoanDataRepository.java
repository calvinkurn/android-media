package com.tokopedia.instantloan.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.data.soruce.cloud.BannerDataCloud;
import com.tokopedia.instantloan.data.soruce.cloud.LoanProfileStatusDataCloud;
import com.tokopedia.instantloan.data.soruce.cloud.PhoneDetailsDataCloud;
import com.tokopedia.instantloan.domain.model.BannerModelDomain;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;
import com.tokopedia.instantloan.domain.repository.InstantLoanDomainRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public class InstantLoanDataRepository implements InstantLoanDomainRepository {

    private BannerDataCloud mBannerDataCloud;
    private LoanProfileStatusDataCloud mLoanStatusDataCloud;
    private PhoneDetailsDataCloud mPhoneDetailsDataCloud;

    @Inject
    public InstantLoanDataRepository(BannerDataCloud bannerDataCloud, LoanProfileStatusDataCloud loanStatusDataCloud, PhoneDetailsDataCloud phoneDetailsDataCloud) {
        this.mBannerDataCloud = bannerDataCloud;
        this.mLoanStatusDataCloud = loanStatusDataCloud;
        this.mPhoneDetailsDataCloud = phoneDetailsDataCloud;
    }

    @Override
    public Observable<List<BannerModelDomain>> getBanners() {
        return mBannerDataCloud.bannerList();
    }

    @Override
    public Observable<LoanProfileStatusModelDomain> getLoanProfileStatus() {
        return mLoanStatusDataCloud.loanProfileStatus();
    }

    @Override
    public Observable<PhoneDataModelDomain> postPhoneData(JsonObject body) {
        return mPhoneDetailsDataCloud.postPhoneData(body);
    }
}
