package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.tokomember_seller_dashboard.model.DataModel
import com.tokopedia.tokomember_seller_dashboard.model.TmMemberListResponse
import com.tokopedia.tokomember_seller_dashboard.model.UserCardMemberModel

object MemberListMapper {
    fun getMemberListAdapterModel(data:TmMemberListResponse?) : MutableList<DataModel>{
        val list:MutableList<DataModel> = mutableListOf()
        data?.membershipGetUserCardMemberList?.userCardMemberList?.userCardMember?.let{
           for(member in it){
               list.add(UserCardMemberModel(member))
           }
        }
        return list
    }
}