package com.tokopedia.interestpick.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.data.pojo.FeedInterestUser
import com.tokopedia.interestpick.data.pojo.Header
import com.tokopedia.interestpick.data.pojo.InterestPickData
import com.tokopedia.interestpick.data.pojo.InterestsItem
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.viewmodel.InterestPickItemViewModel
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
            val interestpickData: InterestPickData = it.getData(InterestPickData::class.java)
            val feedInterestUser: FeedInterestUser = interestpickData.feedInterestUser

            if (!TextUtils.isEmpty(feedInterestUser.error)) {
                view.onErrorGetInterest(feedInterestUser.error)
                return
            }

            view.onSuccessGetInterest(
                    convertToInterestList(feedInterestUser.interests),
                    getTitle(feedInterestUser.header)
            )
        }
    }

    private fun convertToInterestList(list: List<InterestsItem>)
            : ArrayList<InterestPickItemViewModel> {
        val interestList: ArrayList<InterestPickItemViewModel> = ArrayList()
        for (item in list) {
            interestList.add(
                    InterestPickItemViewModel(
                            item.id,
                            item.name,
                            item.imageUrl,
                            item.relationships.isSelected
                    )
            )
        }
        return interestList
    }

    private fun getTitle(header: Header) : String {
        return if (!TextUtils.isEmpty(header.title)) {
            header.title
        } else {
            view.getContext()!!.getString(R.string.interest_what_do_you_like)
        }
    }
}