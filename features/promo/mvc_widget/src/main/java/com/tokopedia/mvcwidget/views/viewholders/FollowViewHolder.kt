package com.tokopedia.mvcwidget.views.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.views.MvcDetailViewContract
import com.tokopedia.mvcwidget.views.MvcFollowViewContainer
import com.tokopedia.mvcwidget.views.WidgetImpression
import com.tokopedia.user.session.UserSession

class FollowViewHolder (itemView: View,val mvcDetailViewContract: MvcDetailViewContract): RecyclerView.ViewHolder(itemView) {

    private val mvcFollowContainer: MvcFollowViewContainer = itemView.findViewById(R.id.mvc_follow_view_container)

    fun setData(followWidget: FollowWidget, widgetImpression: WidgetImpression, shopId:String, @MvcSource mvcSource:Int){
        mvcFollowContainer.setData(followWidget,widgetImpression, shopId, mvcSource)

        mvcFollowContainer.oneActionView.ll_btn.setOnClickListener {
            mvcDetailViewContract.handleFollowButtonClick()
            Tracker.clickFollowButton(shopId,UserSession(itemView.context).userId,mvcSource)
        }

        when (followWidget.type) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
                    mvcDetailViewContract.handleJadiMemberButtonClick()
                    Tracker.clickJadiMemberButton(shopId,UserSession(itemView.context).userId,mvcSource)
                }
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
                    RouteManager.route(itemView.context, ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT,shopId)
                    Tracker.clickMulaiBelanjaButton(shopId,UserSession(itemView.context).userId,mvcSource)
                }
            }
        }

        mvcFollowContainer.twoActionView.tvList.setOnClickListener {
            if (!followWidget.isCollapsed) {
                expand(mvcFollowContainer.twoActionView.containerContent)
                setAdjustableMargin3(mvcFollowContainer.twoActionView.iconBackgroundContainer)
                mvcFollowContainer.twoActionView.tvList.text = itemView.context.resources.getString(R.string.mvc_text_expand)
                followWidget.isCollapsed = !followWidget.isCollapsed
                Tracker.clickLihatExpand(shopId, UserSession(itemView.context).userId, mvcSource)
            } else {
                collapse(mvcFollowContainer.twoActionView.containerContent)
                setAdjustableMargin0(mvcFollowContainer.twoActionView.iconBackgroundContainer)
                mvcFollowContainer.twoActionView.tvList.text = itemView.context.resources.getString(R.string.mvc_text_collapse)
                followWidget.isCollapsed = !followWidget.isCollapsed
            }
        }
    }

}