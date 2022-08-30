package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.ViewModel

class TmMemberListViewModel : ViewModel() {
    var memberList : MutableList<String> = mutableListOf()
    init{
        for(i in 1..10){
            memberList.add("Indra Nugroho")
        }
    }
}