package com.tokopedia.payment.setting.authenticate.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard

class AuthenticateCreditCardAdapter(authenticateCCAdapterFactory: AuthenticateCCAdapterFactory)
    : BaseListAdapter<TypeAuthenticateCreditCard, AuthenticateCCAdapterFactory>(authenticateCCAdapterFactory) {

    fun selectAuth(typeAuthenticateCreditCard: TypeAuthenticateCreditCard?){
        for(typeAuth in data){
            if(typeAuth.equals(typeAuthenticateCreditCard)){
                typeAuth.isSelected = true
            }else{
                typeAuth.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    fun getSelectedState() : Int{
        for(typeAuth in data){
            if(typeAuth.isSelected){
                return typeAuth.stateWhenSelected
            }
        }
        return 0
    }
}
