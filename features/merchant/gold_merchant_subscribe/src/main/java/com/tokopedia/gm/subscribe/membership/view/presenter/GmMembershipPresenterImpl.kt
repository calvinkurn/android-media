package com.tokopedia.gm.subscribe.membership.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.subscribe.membership.data.model.GetMembershipData
import com.tokopedia.gm.subscribe.membership.domain.GetGmSubscribeMembershipUsecase
import com.tokopedia.gm.subscribe.membership.domain.SetGmSubscribeMembershipUsecase
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GmMembershipPresenterImpl @Inject
constructor(private val getGmSubscribeMembershipUsecase: GetGmSubscribeMembershipUsecase,
            private val setGmSubscribeMembershipUsecase: SetGmSubscribeMembershipUsecase)  : BaseDaggerPresenter<GmMembershipView>(), GmMembershipPresenter {

    override fun detachView() {
        getGmSubscribeMembershipUsecase.unsubscribe()
        super.detachView()
    }

    override fun getMembershipData() {
        getGmSubscribeMembershipUsecase.execute(RequestParams.EMPTY,
                object : Subscriber<GetMembershipData>() {
                    override fun onCompleted() {}
                    override fun onError(throwable: Throwable) {
                        view.onErrorGetGmSubscribeMembershipData(throwable)
                    }
                    override fun onNext(membershipData: GetMembershipData) {
                        view.onSuccessGetGmSubscribeMembershipData(membershipData)
                    }
                })
    }

    override fun setMembershipData(subscriptionType: Int) {
        setGmSubscribeMembershipUsecase.execute(SetGmSubscribeMembershipUsecase.createRequestParams(subscriptionType),
                object : Subscriber<String>() {
                    override fun onCompleted() {}
                    override fun onError(throwable: Throwable) {
                        view.onErrorSetGmSubscribeMembershipData(throwable)
                    }
                    override fun onNext(string: String) {
                        view.onSuccessSetGmSubscribeMembershipData()
                    }
                })
    }
}