package com.tokopedia.inbox.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.inbox.R
import com.tokopedia.inbox.di.DaggerInboxComponent
import com.tokopedia.inbox.view.custom.AccountSwitcherMenuItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AccountSwitcherBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var sellerItem: AccountSwitcherMenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
    }

    private fun initInject() {
        DaggerInboxComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initContentView()
        return super.onCreateView(inflater, container, savedInstanceState)?.also {
            initViewBinding(it)
        }
    }

    private fun initViewBinding(view: View) {
        sellerItem = view.findViewById(R.id.switcher_seller)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSellerMenu()
    }

    private fun initSellerMenu() {
        bindSellerName()
        bindSellerProfilePicture()
    }

    private fun bindSellerName() {
        sellerItem?.setName(userSession.shopName)
    }

    private fun bindSellerProfilePicture() {
        sellerItem?.loadProfilePicture(userSession.shopAvatar)
    }

    private fun initContentView() {
        val contentView = View.inflate(context, R.layout.fragment_account_switcher, null)
        setChild(contentView)
    }

    companion object {
        fun create(): AccountSwitcherBottomSheet {
            return AccountSwitcherBottomSheet()
        }
    }
}