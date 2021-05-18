package com.tokopedia.mvcwidget.views.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.Tracker
import com.tokopedia.mvcwidget.views.MvcDetailViewContract
import com.tokopedia.mvcwidget.views.MvcFollowViewContainer
import com.tokopedia.mvcwidget.views.WidgetImpression
import com.tokopedia.user.session.UserSession

class FollowViewHolder (itemView: View,val mvcDetailViewContract: MvcDetailViewContract): RecyclerView.ViewHolder(itemView) {

    val mvcFollowContainer: MvcFollowViewContainer = itemView.findViewById(R.id.mvc_follow_view_container)

    fun setData(followWidget: FollowWidget, widgetImpression: WidgetImpression, shopId:String, @MvcSource mvcSource:Int){
        mvcFollowContainer.setData(followWidget,widgetImpression, shopId, mvcSource)

        mvcFollowContainer.oneActionView.ll_btn.setOnClickListener {
            mvcDetailViewContract.handleFollowButtonClick()
            Tracker.clickFollowButton(shopId,UserSession(itemView.context).userId,mvcSource)
        }

        mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
            mvcDetailViewContract.handleJadiMemberButtonClick()
            Tracker.clickJadiMemberButton(shopId,UserSession(itemView.context).userId,mvcSource)
        }

        mvcFollowContainer.twoActionView.tvList.setOnClickListener {
            if (followWidget.isCollapsed) {
                mvcFollowContainer.twoActionView.containerContent.visibility = View.VISIBLE
                followWidget.isCollapsed = !followWidget.isCollapsed
            } else {
                mvcFollowContainer.twoActionView.containerContent.visibility = View.GONE
                followWidget.isCollapsed = !followWidget.isCollapsed
            }
            mvcDetailViewContract.handleCollapseExpand(adapterPosition)
        }
    }

}