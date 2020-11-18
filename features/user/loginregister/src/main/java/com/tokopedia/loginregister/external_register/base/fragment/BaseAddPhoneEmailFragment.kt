package com.tokopedia.loginregister.external_register.base.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.listener.BaseAddPhoneListener
import kotlinx.android.synthetic.main.fragment_base_add_phone.view.*

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class BaseAddPhoneEmailFragment: BaseDaggerFragment() {

    var baseAddPhoneListener: BaseAddPhoneListener? = null

    override fun getScreenName(): String =  ExternalRegisterConstants.ADD_PHONE_SCREEN
    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_base_add_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.base_add_phone_button_next?.setOnClickListener { baseAddPhoneListener?.onAddPhoneNextButtonClicked() }
        view?.base_add_phone_button_other_methods?.setOnClickListener { baseAddPhoneListener?.onAddPhoneOtherMethodButtonClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.base_external_register_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val item = menu.findItem(R.id.action_external_register_login)
        val s = SpannableString("Masuk")
        s.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.Unify_G400_96)), 0, s.length, 0)
        item.title = s
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_external_register_login -> {
                startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
                activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}