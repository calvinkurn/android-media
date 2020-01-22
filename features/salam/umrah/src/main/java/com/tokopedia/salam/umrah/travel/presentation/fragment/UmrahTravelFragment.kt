package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahTravelActivity.Companion.EXTRA_SLUG_NAME

class UmrahTravelFragment: BaseDaggerFragment(){

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    companion object{
        fun getInstance(slugName: String) =
                UmrahTravelFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUG_NAME, slugName)
                    }
                }
    }
}