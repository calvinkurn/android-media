package com.tokopedia.sellerhomedrawer.view.viewholder

//import android.annotation.SuppressLint
//import android.content.Context
//import android.view.View
//import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
//import com.tokopedia.abstraction.common.utils.image.ImageHandler
//import com.tokopedia.core.analytics.AnalyticsEventTrackingHelper
//import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash
//import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction
//import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
//import com.tokopedia.remoteconfig.RemoteConfigKey
//import com.tokopedia.sellerhomedrawer.R
//import com.tokopedia.sellerhomedrawer.view.listener.RetryTokoCashListener
//import com.tokopedia.sellerhomedrawer.view.listener.SellerDrawerHeaderListener
//import com.tokopedia.sellerhomedrawer.view.viewmodel.header.SellerDrawerHeader
//import com.tokopedia.track.TrackApp
//import com.tokopedia.user.session.UserSession
//import com.tokopedia.user.session.UserSessionInterface
//import kotlinx.android.synthetic.main.drawer_header.view.*
//import kotlinx.android.synthetic.main.drawer_header.view.complete_profile
//import kotlinx.android.synthetic.main.seller_drawer_header.view.ProgressBar
//import kotlinx.android.synthetic.main.seller_drawer_header.view.cover_img
//import kotlinx.android.synthetic.main.seller_drawer_header.view.drawer_header
//import kotlinx.android.synthetic.main.seller_drawer_header.view.drawer_points_layout
//import kotlinx.android.synthetic.main.seller_drawer_header.view.layout_progress
//import kotlinx.android.synthetic.main.seller_drawer_header.view.name_text
//import kotlinx.android.synthetic.main.seller_drawer_header.view.percent_text
//import kotlinx.android.synthetic.main.seller_drawer_header.view.user_avatar
//import kotlinx.android.synthetic.main.seller_drawer_header.view.verified
//import kotlinx.android.synthetic.main.seller_drawer_header.view.verified_icon
//import kotlinx.android.synthetic.main.seller_drawer_saldo.view.*
//
//class SellerDrawerHeaderViewHolder(itemView: View,
//                                   private val sellerDrawerHeaderListener: SellerDrawerHeaderListener,
//                                   private val retryTokoCashListener: RetryTokoCashListener,
//                                   val context: Context)
//    : AbstractViewHolder<SellerDrawerHeader>(itemView){
//
//    companion object {
//        val LAYOUT_RES = R.layout.seller_drawer_header
//    }
//
//    private lateinit var userSession: UserSessionInterface
//    private var oldUserAvatar = ""
//
//    override fun bind(sellerDrawerHeader: SellerDrawerHeader?) {
//        userSession = UserSession(context)
//        if (userSession.isLoggedIn && sellerDrawerHeader != null)
//            bindDrawerHeader(sellerDrawerHeader)
//        else bindDrawerHeaderGuest()
//    }
//
//    protected fun bindDrawerHeaderGuest() {
//        with(itemView) {
//            cover_img.visibility = View.VISIBLE
//            drawer_points_layout.visibility = View.GONE
//            percent_text.visibility = View.GONE
//            layout_progress.visibility = View.GONE
//            verified_icon.visibility = View.GONE
//            verified.visibility = View.GONE
//            drawer_header.visibility = View.GONE
//            ImageHandler.loadImageWithId(cover_img, R.drawable.drawer_header_bg)
//        }
//    }
//
//    protected fun bindDrawerHeader(sellerDrawerHeader: SellerDrawerHeader) {
//        with(itemView) {
//
//            drawer_header.visibility = View.VISIBLE
//            drawer_points_layout.visibility = View.VISIBLE
//            cover_img.visibility = View.GONE
//            name_text.visibility = View.VISIBLE
//            user_avatar.visibility = View.VISIBLE
//            percent_text.visibility = View.VISIBLE
//
//            val sellerUserAvatar = sellerDrawerHeader.drawerProfile.userAvatar
//
//            val isAvatarExistAndNotOld = sellerDrawerHeader.drawerProfile.userAvatar != null &&
//                    oldUserAvatar != sellerUserAvatar
//
//            if (isAvatarExistAndNotOld) {
//                loadAvatarImage(sellerUserAvatar)
//            }
//
//            name_text.text = sellerDrawerHeader.drawerProfile.userName
//            percent_text.text = String.format("%s%%", sellerDrawerHeader.profileCompletion.toString())
//            if (sellerDrawerHeader.profileCompletion == 100) {
//                layout_progress.visibility = View.GONE
//                verified_icon.visibility = View.VISIBLE
//                verified.visibility = View.VISIBLE
//            } else {
//                ProgressBar.progress = sellerDrawerHeader.profileCompletion
//                layout_progress.visibility = View.VISIBLE
//                verified_icon.visibility = View.GONE
//                verified.visibility = View.GONE
//            }
//            setupViewHolder(sellerDrawerHeader)
//        }
//    }
//
//    private fun setupViewHolder(sellerDrawerHeader: SellerDrawerHeader) {
//        setDeposit(sellerDrawerHeader)
//        setTopPoints(sellerDrawerHeader)
//        setTopCash(sellerDrawerHeader)
//        setTokoPoint(sellerDrawerHeader)
//        setTokoCard()
//        setListener(sellerDrawerHeader)
//    }
//
//    private fun loadAvatarImage(userAvatar: String) {
//        ImageHandler.loadImage(context,
//                itemView.user_avatar,
//                userAvatar,
//                R.drawable.ic_image_avatar_boy,
//                R.drawable.ic_image_avatar_boy)
//        oldUserAvatar = userAvatar
//    }
//
//    private fun loadTokoPointImage(imageUrl: String?) {
//        ImageHandler.loadImageThumbs(context,
//                itemView.iv_tokopoint_badge,
//                imageUrl)
//    }
//
//    private fun setTokoCard() {
//        val remoteConfig = FirebaseRemoteConfigImpl(context)
//        val showTokoCard = remoteConfig.getBoolean(RemoteConfigKey.SHOW_TOKOCARD, true)
//
//        if (!showTokoCard) {
//            itemView.drawer_tokocard.visibility = View.GONE
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun setTokoPoint(sellerDrawerHeader: SellerDrawerHeader) {
//        val titleHeaderWebsite = "TokoPoints"
//        with(itemView) {
//            if (sellerDrawerHeader.tokoPointDrawerData != null && sellerDrawerHeader.tokoPointDrawerData?.offFlag == 0) {
//                tokopoint_container.visibility = View.VISIBLE
//                val title = sellerDrawerHeader.tokoPointDrawerData?.mainPageTitle
//                tv_tokopoint_action.text = titleHeaderWebsite
//                loadTokoPointImage(sellerDrawerHeader.tokoPointDrawerData?.userTier?.tierImageUrl)
//                tv_tokopoint_count.text = sellerDrawerHeader.tokoPointDrawerData?.userTier?.rewardPointsStr
//                tv_tokopoint_action.setOnClickListener {
//                    eventUserClickedPoints()
//                    sellerDrawerHeaderListener.onTokoPointActionClicked(
//                            sellerDrawerHeader.tokoPointDrawerData?.mainPageUrl,
//                            title ?: titleHeaderWebsite
//                    )
//                }
//
//            } else tokopoint_container.visibility = View.GONE
//        }
//    }
//
//    private fun eventUserClickedPoints() {
//        TrackApp.getInstance().getGTM().sendGeneralEvent(
//                "clickHomePage",
//                "homepage-tokopoints",
//                "click point & tier status",
//                "tokopoints")
//    }
//
//    private fun setListener(sellerDrawerHeader: SellerDrawerHeader) {
//        with(itemView) {
//            drawer_saldo.setOnClickListener { sellerDrawerHeaderListener.onGoToDeposit() }
//            drawer_top_points.setOnClickListener {
//                if (sellerDrawerHeader.drawerTopPoints?.topPointsUrl != null)
//                    sellerDrawerHeaderListener.onGoToTopPoints(sellerDrawerHeader.drawerTopPoints?.topPointsUrl)
//            }
//            drawer_top_cash.setOnClickListener {
//                if (sellerDrawerHeader.drawerTokoCash?.drawerWalletAction != null) {
//                    if (isRegistered(sellerDrawerHeader.drawerTokoCash!!)) {
//                        sellerDrawerHeaderListener.onWalletBalanceClicked(
//                                sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.redirectUrlBalance,
//                                sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.appLinkBalance
//                        )
//                        AnalyticsEventTrackingHelper.homepageTokocashClick(it.context, sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.redirectUrlBalance)
//                    } else {
//                        sellerDrawerHeaderListener.onWalletActionButtonClicked(
//                                sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.redirectUrlActionButton,
//                                sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.appLinkActionButton
//                        )
//                        AnalyticsEventTrackingHelper.hamburgerTokocashActivateClick(it.context)
//                    }
//                }
//                else retryTokoCashListener.onRetryTokoCash()
//            }
//            name_text.setOnClickListener { sellerDrawerHeaderListener.onGoToProfile() }
//            user_avatar.setOnClickListener { sellerDrawerHeaderListener.onGoToProfile() }
//            complete_profile.setOnClickListener { sellerDrawerHeaderListener.onGoToProfileCompletion() }
//            drawer_tokocard.setOnClickListener { sellerDrawerHeaderListener.onGotoTokoCard() }
//        }
//    }
//
//    private fun setTopCash(sellerDrawerHeader: SellerDrawerHeader) {
//        val isTokoCashDisabled: (DrawerTokoCash?) -> Boolean = { drawerTokoCash ->
//            drawerTokoCash == null || drawerTokoCash.drawerWalletAction == null
//                    || drawerTokoCash.drawerWalletAction.labelTitle == null
//                    || drawerTokoCash.drawerWalletAction.labelTitle.isEmpty()
//        }
//        val isActionTypeBalance by lazy {
//            sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.typeAction == DrawerWalletAction.TYPE_ACTION_BALANCE
//        }
//        with(itemView) {
//            loading_top_cash.visibility = View.VISIBLE
//            if (isTokoCashDisabled(sellerDrawerHeader.drawerTokoCash)) {
//                loading_top_cash.visibility = View.GONE
//                retry_top_cash.visibility = View.VISIBLE
//            } else {
//                if (isActionTypeBalance) {
//                    showTokoCashBalanceView()
//                    top_cash_value.text = sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.balance
//                    toko_cash_label.text = sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.labelTitle
//                } else {
//                    showTokoCashActivateView()
//                    toko_cash_activation_button.text = sellerDrawerHeader.drawerTokoCash?.drawerWalletAction?.labelActionButton
//                }
//                loading_top_cash.visibility = View.GONE
//                retry_top_cash.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun showTokoCashBalanceView() {
//        with(itemView) {
//            drawer_top_cash.visibility = View.VISIBLE
//            toko_cash_activation_button.visibility = View.GONE
//            top_cash_value.visibility = View.VISIBLE
//        }
//    }
//
//    private fun showTokoCashActivateView() {
//        with(itemView) {
//            drawer_top_cash.visibility = View.VISIBLE
//            toko_cash_activation_button.visibility = View.VISIBLE
//            top_cash_value.visibility = View.GONE
//        }
//    }
//
//    private fun isRegistered(drawerTokoCash: DrawerTokoCash): Boolean {
//        return drawerTokoCash.drawerWalletAction.typeAction == DrawerWalletAction.TYPE_ACTION_BALANCE
//    }
//
//    private fun setTopPoints(sellerDrawerHeader: SellerDrawerHeader) {
//        with(itemView) {
//            when {
//                !sellerDrawerHeader.drawerTopPoints?.isActive!! ->
//                    drawer_top_points.visibility = View.GONE
//                sellerDrawerHeader.drawerTopPoints?.topPoints!!.isEmpty() -> {
//                    drawer_top_points.visibility = View.VISIBLE
//                    loading_loyalty.visibility = View.VISIBLE
//                    toppoints_text.visibility = View.GONE }
//                else ->  {
//                    drawer_top_points.visibility = View.VISIBLE
//                    loading_loyalty.visibility = View.GONE
//                    toppoints_text.visibility = View.VISIBLE
//                    toppoints_text.text = sellerDrawerHeader.drawerTopPoints?.topPoints
//                }
//            }
//        }
//    }
//
//    private fun setDeposit(sellerDrawerHeader: SellerDrawerHeader) {
//        with(itemView) {
//            if (sellerDrawerHeader.drawerDeposit?.deposit?.isEmpty()!!) {
//                loading_saldo.visibility = View.VISIBLE
//                deposit_text.visibility = View.GONE
//            } else {
//                loading_saldo.visibility = View.GONE
//                deposit_text.apply {
//                    text = sellerDrawerHeader.drawerDeposit?.deposit
//                    visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//}