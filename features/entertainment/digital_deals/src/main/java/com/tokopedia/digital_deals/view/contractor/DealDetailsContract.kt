package com.tokopedia.digital_deals.view.contractor

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.digital_deals.view.model.ProductItem
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.model.Outlet
import com.tokopedia.digital_deals.view.model.response.EventContentData
import com.tokopedia.usecase.RequestParams
import java.util.ArrayList

class DealDetailsContract {

    interface View : CustomerView {

        val activity: Activity

        val params: RequestParams

        val rootView: android.view.View

        val layoutManager: LinearLayoutManager

        val isEnableBuyFromArguments: Boolean

        val isRecommendationEnableFromArguments: Boolean

        val isEnableLikeFromArguments: Boolean

        val isEnableShareFromArguments: Boolean

        fun navigateToActivityRequest(intent: Intent, requestCode: Int)

        fun renderDealDetails(detailsViewModel: DealsDetailsResponse)

        fun addDealsToCards(productItems: ArrayList<ProductItem>)

        fun showProgressBar()

        fun hideProgressBar()

        fun showShareButton()

        fun hideCollapsingHeader()

        fun showCollapsingHeader()

        fun setLikes(likes: Int, isLiked: Boolean)

        fun addFooter()

        fun removeFooter()

        fun hideCheckoutView()

        fun hideRecomendationDealsView()

        fun hideLikeButtonView()
    }

    interface Presenter : CustomerPresenter<DealDetailsContract.View> {

        val allOutlets: List<Outlet>

        fun initialize()

        fun onDestroy()

        fun onOptionMenuClick(id: Int): Boolean

        fun onBannerSlide(page: Int)

        fun startBannerSlide(viewPager: TouchViewPager)

        fun getEventContent(onSuccess: (EventContentData) -> Unit, onError: (Throwable) -> Unit)
    }
}
