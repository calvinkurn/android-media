package com.tokopedia.fintech.insurance.view.fragment

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import javax.inject.Inject


class MacroInsuranceViewFragment: BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getScreenName(): String {

        return "Macro Insurance"
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}