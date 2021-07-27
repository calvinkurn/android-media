package com.tokopedia.smartbills.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import kotlinx.android.synthetic.main.fragment_smart_bills_add_telco.*
import javax.inject.Inject

class SmartBillsAddTelcoFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsAddTelcoViewModel

    private var templateTelco: String? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        templateTelco = (activity as SmartBillsAddTelcoActivity).getTemplateTelco()
        return inflater.inflate(R.layout.fragment_smart_bills_add_telco, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_template.text = templateTelco
    }

    companion object {
        fun newInstance() = SmartBillsAddTelcoFragment()
    }
}