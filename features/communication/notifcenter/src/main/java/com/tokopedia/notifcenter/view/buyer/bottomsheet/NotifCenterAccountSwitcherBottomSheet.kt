package com.tokopedia.notifcenter.view.buyer.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.view.buyer.customview.NotifCenterBaseAccountSwitcherMenuItem
import com.tokopedia.notifcenter.util.NotifCenterConfig
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotifCenterAccountSwitcherBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var sellerItem: NotifCenterBaseAccountSwitcherMenuItem? = null
    private var buyerItem: NotifCenterBaseAccountSwitcherMenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        initTitle()
    }

    private fun initInject() {
        NotificationActivityComponentFactory
            .instance
            .createNotificationComponent(context?.applicationContext as BaseMainApplication)
            .inject(this)
    }

    private fun initTitle() {
        setTitle(context?.getString(R.string.notifcenter_title_change_account) ?: "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        when (NotifCenterConfig.role) {
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
        if (NotifCenterConfig.role != role) {
            NotifCenterConfig.setRole(role)
        }
        dismiss()
    }

    private fun initBadgeCounter() {
        sellerItem?.bindBadgeCounter(NotifCenterConfig.inboxCounter)
        buyerItem?.bindBadgeCounter(NotifCenterConfig.inboxCounter)
    }

    private fun bindItemName(item: NotifCenterBaseAccountSwitcherMenuItem?, value: String) {
        item?.setName(value)
    }

    private fun bindSellerProfilePicture(item: NotifCenterBaseAccountSwitcherMenuItem?, url: String) {
        item?.loadProfilePicture(url)
    }

    private fun initContentView() {
        val contentView = View.inflate(context, R.layout.fragment_notifcenter_account_switcher, null)
        setChild(contentView)
    }

    companion object {
        fun create(): NotifCenterAccountSwitcherBottomSheet {
            return NotifCenterAccountSwitcherBottomSheet()
        }
    }
}
