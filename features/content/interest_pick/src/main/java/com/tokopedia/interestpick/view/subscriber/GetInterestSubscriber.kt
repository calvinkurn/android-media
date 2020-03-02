package com.tokopedia.interestpick.view.subscriber

import android.text.TextUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.data.pojo.GetInterestData
import com.tokopedia.interestpick.data.pojo.Header
import com.tokopedia.interestpick.data.pojo.InterestsItem
import com.tokopedia.interestpick.view.listener.InterestPickContract
import rx.Subscriber

/**
 * @author by milhamj on 07/09/18.
 */
class GetInterestSubscriber(val view: InterestPickContract.View)
    : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.hideLoading()
        view.onErrorGetInterest(ErrorHandler.getErrorMessage(view.getContext(), e))
    }

    override fun onCompleted() {
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        view.hideLoading()
        graphqlResponse?.let {
            val getInterestData: GetInterestData = it.getData(GetInterestData::class.java)
            getInterestData.feedInterestUser
        }?.let {
            if (!TextUtils.isEmpty(it.error)) {
                view.onErrorGetInterest(it.error)
                return
            }

            view.onSuccessGetInterest(
                    convertToInterestList(it.interests),
                    getTitle(it.header)
            )
        }
    }

    private fun convertToInterestList(list: List<InterestsItem>)
            : ArrayList<InterestPickDataViewModel> {
        val interestList: ArrayList<InterestPickDataViewModel> = ArrayList()
        for (item in list) {
            interestList.add(
                    InterestPickDataViewModel(
                            item.id,
                            item.name,
                            item.imageUrl,
                            item.relationships.isSelected
                    )
            )
        }
        return interestList
    }

    private fun getTitle(header: Header): String {
        return if (!TextUtils.isEmpty(header.title)) {
            header.title
        } else {
            view.getContext()!!.getString(R.string.interest_what_do_you_like)
        }
    }
}