package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChooseAddressBottomSheet: BottomSheetUnify(), HasComponent<ChooseAddressComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var chooseAddressLayout: ConstraintLayout? = null
    private var noAddressLayout: ConstraintLayout? = null
    private var loginLayout: ConstraintLayout? = null
    private var buttonLogin: IconUnify? = null
    private var buttonAddAddress: IconUnify? = null
    private var fm: FragmentManager? = null

    /*test no address with this*/
    private var addressList: List<Int> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /*on activity result here*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initLayout() {
        val view = View.inflate(context, R.layout.bottomsheet_choose_address, null)
        setupView(view)
        setChild(view)
    }

    private fun setupView(child: View) {
        chooseAddressLayout = child.findViewById(R.id.choose_address_layout)
        noAddressLayout = child.findViewById(R.id.no_address_layout)
        loginLayout = child.findViewById(R.id.login_layout)
        buttonLogin = child.findViewById(R.id.btn_chevron_login)
        buttonAddAddress = child.findViewById(R.id.btn_chevron_add)
    }

    private fun initView() {
        if (!userSession.isLoggedIn) {
            chooseAddressLayout?.gone()
            noAddressLayout?.gone()
            loginLayout?.visible()
        } else {
            if (addressList.isEmpty()) {
                chooseAddressLayout?.gone()
                noAddressLayout?.visible()
                loginLayout?.gone()
            } else {
                chooseAddressLayout?.visible()
                noAddressLayout?.gone()
                loginLayout?.gone()
            }
        }

        renderButton()
    }

    private fun renderButton() {
        buttonLogin?.setOnClickListener {
            startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }

        buttonAddAddress?.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(EXTRA_IS_FULL_FLOW, true)
                putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
            }, REQUEST_CODE_ADD_ADDRESS)
        }
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    override fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"
        const val REQUEST_CODE_ADD_ADDRESS = 199
    }

}