package com.tokopedia.updateinactivephone.features.successpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneSucccessPageBinding
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class BaseInactivePhoneSuccessFragment : BaseDaggerFragment() {

    var viewBinding by autoClearedNullable<FragmentInactivePhoneSucccessPageBinding>()
    var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    protected abstract fun onClickButtonGotoHome()
    protected abstract fun description(): String
    protected abstract fun title(): String

    override fun getScreenName(): String = ""
    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentInactivePhoneSucccessPageBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inactivePhoneUserDataModel = arguments?.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.btnGotoHome?.setOnClickListener {
            onClickButtonGotoHome()
        }

        initTitle()
        initDescription()
    }

    private fun initDescription() {
        viewBinding?.textDescription?.text = MethodChecker.fromHtml(description())
    }

    private fun initTitle() {
        viewBinding?.textTitle?.text = title()
    }

    open fun setImageHeader(url: String) {
        viewBinding?.imgHeader?.setImageUrl(url, isSkipCache = true)
    }

    open fun gotoHome() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
            startActivity(intent)

            it.finish()
        }
    }
}