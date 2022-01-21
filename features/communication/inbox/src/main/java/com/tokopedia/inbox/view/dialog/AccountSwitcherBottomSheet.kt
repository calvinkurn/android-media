package com.tokopedia.inbox.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.config.InboxConfig
import com.tokopedia.inbox.di.DaggerInboxComponent
import com.tokopedia.inbox.view.custom.AccountSwitcherMenuItem
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AccountSwitcherBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var sellerItem: AccountSwitcherMenuItem? = null
    private var buyerItem: AccountSwitcherMenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        initTitle()
    }

    private fun initInject() {
        DaggerInboxComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initTitle() {
        setTitle("Ganti akun")
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        initContentView()
        initViewConfig()
        return super.onCreateView(inflater, container, savedInstanceState)?.also {
            initViewBinding(it)
        }
    }

    private fun initViewConfig() {
        clearContentPadding = true
    }

    private fun initViewBinding(view: View) {
        sellerItem = view.findViewById(R.id.switcher_seller)
        buyerItem = view.findViewById(R.id.switcher_buyer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBuyerMenuItem()
        initSellerMenuItem()
        initCheckMark()
        initClickListener()
        initBadgeCounter()
    }

    private fun initBuyerMenuItem() {
        bindItemName(buyerItem, userSession.name)
        bindSellerProfilePicture(buyerItem, userSession.profilePicture)
    }

    private fun initSellerMenuItem() {
        if (userSession.hasShop()) {
            sellerItem?.show()
            bindItemName(sellerItem, userSession.shopName)
            bindSellerProfilePicture(sellerItem, userSession.shopAvatar)
        } else {
            sellerItem?.hide()
        }
    }

    private fun initCheckMark() {
        when (InboxConfig.role) {
            RoleType.SELLER -> {
                sellerItem?.showCheckMark()
                buyerItem?.hideCheckMark()
            }
            RoleType.BUYER -> {
                buyerItem?.showCheckMark()
                sellerItem?.hideCheckMark()
            }
        }
    }

    private fun initClickListener() {
        sellerItem?.setOnClickListener {
            updateRole(sellerItem?.role)
        }
        buyerItem?.setOnClickListener {
            updateRole(buyerItem?.role)
        }
    }

    private fun updateRole(role: Int?) {
        if (InboxConfig.role != role) {
            InboxConfig.setRole(role)
        }
        dismiss()
    }

    private fun initBadgeCounter() {
        sellerItem?.bindBadgeCounter(InboxConfig.inboxCounter)
        buyerItem?.bindBadgeCounter(InboxConfig.inboxCounter)
    }

    private fun bindItemName(item: AccountSwitcherMenuItem?, value: String) {
        item?.setName(value)
    }

    private fun bindSellerProfilePicture(item: AccountSwitcherMenuItem?, url: String) {
        item?.loadProfilePicture(url)
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
