package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.view.listener.SearchProfileListener
import rx.Subscriber
import java.util.ArrayList

class SearchProfileSubscriber(
    private val searchProfileListener: SearchProfileListener,
    private val startRow : Int
) : Subscriber<SearchProfileModel>() {

    override fun onNext(searchProfileModel: SearchProfileModel?) {
        if(searchProfileModel == null) {
            searchProfileListener.onErrorGetProfileListData()
            return
        }

        val profileListViewModel = convertToProfileListViewModel(searchProfileModel)

        searchProfileListener.onSuccessGetProfileListData(profileListViewModel)
    }

    private fun convertToProfileListViewModel(searchProfileModel: SearchProfileModel): ProfileListViewModel {
        val profileListViewModel = ArrayList<ProfileViewModel>()

        if(searchProfileModel.aceSearchProfile?.profiles == null)
            return ProfileListViewModel(listOf(), false, 0)

        val profileListModel = searchProfileModel.aceSearchProfile
        var position = startRow
        for (item in profileListModel.profiles) {
            val profileViewModel = ProfileViewModel(
                item.id,
                item.name,
                item.avatar,
                item.username,
                item.followed,
                item.iskol,
                item.isaffiliate,
                item.followers,
                item.postCount,
                position++
            )

            profileListViewModel.add(profileViewModel)
        }

        return ProfileListViewModel(profileListViewModel,
                searchProfileModel.aceSearchProfile.hasNext,
                searchProfileModel.aceSearchProfile.count
        )
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        searchProfileListener.onErrorGetProfileListData()
    }
}