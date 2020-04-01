package com.tokopedia.sellerhomedrawer.domain.datamanager

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerDeposit
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.data.userdata.Notifications
import com.tokopedia.sellerhomedrawer.data.userdata.notifications.Inbox
import com.tokopedia.sellerhomedrawer.data.userdata.notifications.Purchase
import com.tokopedia.sellerhomedrawer.data.userdata.notifications.Sales
import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.sellerhomedrawer.presentation.view.subscriber.SellerTokoCashSubscriber
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Named

class SellerDrawerDataManagerImpl(val context: Context,
                                  val viewListener: SellerDrawerDataListener,
                                  val sellerTokoCashUseCase: SellerTokoCashUseCase,
                                  val getSellerHomeUserAttrUseCase: GetSellerHomeUserAttributesUseCase,
                                  @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA_QUERY) val query: String): SellerDrawerDataManager {

    companion object {
        @JvmStatic
        val TAG = SellerDrawerDataManagerImpl::class.java.simpleName
    }

    override fun getTokoCash() {
        sellerTokoCashUseCase.execute(RequestParams.EMPTY, SellerTokoCashSubscriber(context, viewListener))
    }

    override fun unsubscribe() {
        sellerTokoCashUseCase.unsubscribe()
    }

    override fun getSellerUserAttributes(userSession: UserSessionInterface) {

        getSellerHomeUserAttrUseCase.execute(GetSellerHomeUserAttributesUseCase.getUserAttrParams(userSession.userId, query), object : Subscriber<SellerUserData>() {
            override fun onNext(sellerUserData: SellerUserData) {
                renderAndUpdateValues(sellerUserData)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()

            }
        })
    }

    private fun renderAndUpdateValues(sellerUserData: SellerUserData) {
        renderSaldo(sellerUserData)
        renderProfile(sellerUserData)
        renderNotification(sellerUserData.notifications)


        //Update values in usersession
        val profilePicture = sellerUserData.profile?.profilePicture
        if(profilePicture != null) {
            val userSession = UserSession(viewListener.getActivity())
            userSession.profilePicture = sellerUserData.profile?.profilePicture
        }
    }

    private fun renderSaldo(sellerUserData: SellerUserData) {
        val balance = sellerUserData.saldo?.deposit
        if (balance != null) {
            val depositFormat = CurrencyFormatUtil.convertPriceValueToIdrFormat(balance, false)
            val errorFail = "ERROR FAIL"
            val isDepositFormatAvailable = { _depositFormat: String -> _depositFormat.isNotEmpty() && _depositFormat.equals(errorFail, ignoreCase = true).not()}
            if (isDepositFormatAvailable(depositFormat)) {
                val drawerDeposit = SellerDrawerDeposit()
                drawerDeposit.deposit = depositFormat
                viewListener.onGetDeposit(drawerDeposit)
            } else viewListener.onErrorGetDeposit(depositFormat)
        }
    }

    private fun renderProfile(sellerUserData: SellerUserData) {
        if (sellerUserData.profile != null) {
            val sellerDrawerProfile = SellerDrawerProfile()
            sellerUserData.shopInfoMoengage?.info?.shopAvatar?.let { sellerDrawerProfile.shopAvatar = it }
            sellerUserData.shopInfoMoengage?.info?.shopCover?.let { sellerDrawerProfile.shopCover = it }
            MethodChecker.fromHtml(sellerUserData.shopInfoMoengage?.info?.shopName).toString().let { sellerDrawerProfile.shopName = it }
            sellerUserData.profile?.profilePicture?.let { sellerDrawerProfile.userAvatar = it }
            MethodChecker.fromHtml(sellerUserData.profile?.fullName).toString().let { sellerDrawerProfile.userName = it }

            viewListener.onGetProfile(sellerDrawerProfile)
        }

    }

    private fun renderNotification(notifications: Notifications?) {
        if (notifications != null) {
            try {
                viewListener.onGetNotificationDrawer(
                        convertToViewModel(notifications)
                )
            } catch (exception: Exception) {
                viewListener.onErrorGetNotificationDrawer(
                        viewListener.getString(R.string.default_request_error_unknown)
                )
            }
        } else
            viewListener.onErrorGetNotificationDrawer(
                    viewListener.getString(R.string.default_request_error_unknown)
            )

    }

    private fun convertToViewModel(notificationData: Notifications): SellerDrawerNotification {
        val unreads = notificationData.chat?.unreads?: 0
        val inboxMessage = notificationData.inbox?.inboxMessage?: 0

        val drawerNotification = SellerDrawerNotification()
        drawerNotification.inboxMessage = unreads
        drawerNotification.inboxResCenter = notificationData.resolution

        notificationData.inbox?.let { drawerNotification.setInbox(it) }
        notificationData.purchase?.let { drawerNotification.setPurchase(it) }
        notificationData.sales?.let { drawerNotification.setSelling(it) }
        drawerNotification.setNotif(notificationData)
        drawerNotification.totalNotif = notificationData.totalNotif - inboxMessage + unreads

        return drawerNotification
    }

    private fun SellerDrawerNotification.setInbox(inbox: Inbox) {
        this.apply {
            inboxReview = inbox.inboxReputation
            inboxTalk = inbox.inboxTalk
            inboxTicket = inbox.inboxTicket
        }
    }

    private fun SellerDrawerNotification.setPurchase(purchase: Purchase) {
        this.apply {
            purchaseDeliveryConfirm = purchase.purchaseDeliveryConfirm
            purchaseOrderStatus = purchase.purchaseOrderStatus
            purchasePaymentConfirm = purchase.purchasePaymentConfirm
            purchaseReorder = purchase.purchaseReorder
        }
    }

    private fun SellerDrawerNotification.setSelling(sales: Sales) {
        this.apply {
            sellingNewOrder = sales.salesNewOrder
            sellingShippingConfirmation = sales.salesShippingConfirm
            sellingShippingStatus = sales.salesShippingStatus
        }
    }

    private fun SellerDrawerNotification.setNotif(notificationData: Notifications) {
        this.apply {
            incrNotif = notificationData.incrNotif
            totalCart = notificationData.totalCart
        }
    }

}