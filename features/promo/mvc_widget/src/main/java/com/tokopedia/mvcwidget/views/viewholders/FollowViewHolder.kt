package com.tokopedia.mvcwidget.views.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.collapse
import com.tokopedia.mvcwidget.expand
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcDetailViewContract
import com.tokopedia.mvcwidget.views.WidgetImpression
import com.tokopedia.mvcwidget.views.followViews.MvcFollowViewContainer
import com.tokopedia.user.session.UserSession

class FollowViewHolder (itemView: View,
                        val mvcDetailViewContract: MvcDetailViewContract
): RecyclerView.ViewHolder(itemView) {

    private val mvcFollowContainer: MvcFollowViewContainer = itemView.findViewById(R.id.mvc_follow_view_container)

    fun setData(followWidget: FollowWidget, widgetImpression: WidgetImpression, shopId:String, @MvcSource mvcSource:Int){
        mvcFollowContainer.setData(followWidget,widgetImpression, shopId, mvcSource, mvcDetailViewContract.getMvcTracker())

        mvcFollowContainer.oneActionView.ll_btn.setOnClickListener {
            mvcDetailViewContract.handleFollowButtonClick()
            mvcDetailViewContract.getMvcTracker()?.clickFollowButton(followWidget.type?:FollowWidgetType.DEFAULT,shopId,UserSession(itemView.context).userId,mvcSource,
                mvcFollowContainer.oneActionView.tvBtn.text?.toString())
        }

        when (followWidget.type) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
                    mvcDetailViewContract.handleJadiMemberButtonClick()
                    mvcDetailViewContract.getMvcTracker()?.clickJadiMemberButton(FollowWidgetType.MEMBERSHIP_OPEN,shopId,UserSession(itemView.context).userId,mvcSource,mvcFollowContainer.twoActionView.btnSecond.text?.toString())
                }
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
                    RouteManager.route(itemView.context, ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT,shopId)
                    mvcDetailViewContract.getMvcTracker()?.clickMulaiBelanjaButton(FollowWidgetType.MEMBERSHIP_CLOSE,shopId,UserSession(itemView.context).userId,mvcSource,mvcFollowContainer.twoActionView.btnSecond.text?.toString())
                }
            }
        }

        mvcFollowContainer.twoActionView.tvList.setOnClickListener {
            if (!followWidget.isCollapsed) {
                expand(
                    mvcFollowContainer.twoActionView.containerContent,
                    mvcFollowContainer.twoActionView.iconBackgroundContainer
                )
                mvcFollowContainer.twoActionView.tvList.text = itemView.context.resources.getString(R.string.mvc_text_expand)
                followWidget.isCollapsed = !followWidget.isCollapsed
                mvcDetailViewContract.getMvcTracker()?.clickLihatExpand(shopId, UserSession(itemView.context).userId, mvcSource)
            } else {
                collapse(
                    mvcFollowContainer.twoActionView.containerContent,
                    mvcFollowContainer.twoActionView.iconBackgroundContainer
                )
                mvcFollowContainer.twoActionView.tvList.text = itemView.context.resources.getString(R.string.mvc_text_collapse)
                followWidget.isCollapsed = !followWidget.isCollapsed
            }
        }
    }

}
