package com.tokopedia.mvcwidget.views.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.views.MvcDetailViewContract
import com.tokopedia.mvcwidget.views.MvcFollowViewContainer

class FollowViewHolder (itemView: View,val mvcDetailViewContract: MvcDetailViewContract): RecyclerView.ViewHolder(itemView) {

    val mvcFollowContainer: MvcFollowViewContainer = itemView.findViewById(R.id.mvc_follow_view_container)

    fun setData(followWidget: FollowWidget){
        mvcFollowContainer.setData(followWidget)

        mvcFollowContainer.oneActionView.ll_btn.setOnClickListener {
            mvcDetailViewContract.handleFollowButtonClick()
        }

        mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
            mvcDetailViewContract.handleJadiMemberButtonClick()
        }
    }

}