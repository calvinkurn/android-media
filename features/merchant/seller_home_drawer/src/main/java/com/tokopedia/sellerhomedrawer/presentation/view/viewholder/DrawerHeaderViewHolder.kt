package com.tokopedia.sellerhomedrawer.presentation.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.analytics.SellerAnalyticsEventTrackingHelper
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.header.DrawerHeader
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerWalletAction
import com.tokopedia.sellerhomedrawer.presentation.listener.DrawerHeaderListener
import com.tokopedia.sellerhomedrawer.presentation.listener.RetryTokoCashListener
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.sh_drawer_header.view.*
import kotlinx.android.synthetic.main.sh_drawer_header.view.complete_profile
import kotlinx.android.synthetic.main.sh_drawer_saldo.view.*
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.ProgressBar
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.cover_img
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.drawer_header
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.drawer_points_layout
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.layout_progress
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.name_text
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.percent_text
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.user_avatar
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.verified
import kotlinx.android.synthetic.main.sh_seller_drawer_header.view.verified_icon

class DrawerHeaderViewHolder(itemView: View,
                             private val drawerHeaderListener: DrawerHeaderListener,
                             private val retryTokoCashListener: RetryTokoCashListener,
                             val context: Context)
    : AbstractViewHolder<DrawerHeader>(itemView){

    companion object {
        val LAYOUT_RES = R.layout.sh_drawer_header
    }

    private var userSession: UserSessionInterface = drawerHeaderListener.userSession
    private var oldUserAvatar = ""

    override fun bind(drawerHeader: DrawerHeader?) {
        if (userSession.isLoggedIn && drawerHeader != null)
            bindDrawerHeader(drawerHeader)
        else bindDrawerHeaderGuest()
    }

    private fun bindDrawerHeaderGuest() {
        with(itemView) {
            cover_img.visibility = View.VISIBLE
            drawer_points_layout.visibility = View.GONE
            percent_text.visibility = View.GONE
            layout_progress.visibility = View.GONE
            verified_icon.visibility = View.GONE
            verified.visibility = View.GONE
            drawer_header.visibility = View.GONE
            ImageHandler.loadImageWithId(cover_img, R.drawable.sh_drawer_header_bg)
        }
    }

    private fun bindDrawerHeader(drawerHeader: DrawerHeader) {
        with(itemView) {

            drawer_header.visibility = View.VISIBLE
            drawer_points_layout.visibility = View.VISIBLE
            cover_img.visibility = View.GONE
            name_text.visibility = View.VISIBLE
            user_avatar.visibility = View.VISIBLE
            percent_text.visibility = View.VISIBLE

            val sellerUserAvatar = drawerHeader.sellerDrawerProfile.userAvatar

            if (sellerUserAvatar != null && oldUserAvatar != sellerUserAvatar) {
                loadAvatarImage(sellerUserAvatar)
            }

            name_text.text = drawerHeader.sellerDrawerProfile.userName
            percent_text.text = String.format("%s%%", drawerHeader.profileCompletion.toString())
            if (drawerHeader.profileCompletion == 100) {
                layout_progress.visibility = View.GONE
                verified_icon.visibility = View.VISIBLE
                verified.visibility = View.VISIBLE
            } else {
                ProgressBar.progress = drawerHeader.profileCompletion
                layout_progress.visibility = View.VISIBLE
                verified_icon.visibility = View.GONE
                verified.visibility = View.GONE
            }
            setupViewHolder(drawerHeader)
        }
    }

    private fun setupViewHolder(drawerHeader: DrawerHeader) {
        setDeposit(drawerHeader)
        setTopPoints(drawerHeader)
        setTopCash(drawerHeader)
        setTokoPoint(drawerHeader)
        setTokoCard()
        setListener(drawerHeader)
    }

    private fun loadAvatarImage(userAvatar: String) {
        ImageHandler.loadImage(context,
                itemView.user_avatar,
                userAvatar,
                R.drawable.sah_ic_image_avatar_boy,
                R.drawable.sah_ic_image_avatar_boy)
        oldUserAvatar = userAvatar
    }

    private fun loadTokoPointImage(imageUrl: String?) {
        ImageHandler.loadImageThumbs(context,
                itemView.iv_tokopoint_badge,
                imageUrl)
    }

    private fun setTokoCard() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val showTokoCard = remoteConfig.getBoolean(RemoteConfigKey.SHOW_TOKOCARD, true)

        if (!showTokoCard) {
            itemView.drawer_tokocard.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTokoPoint(drawerHeader: DrawerHeader) {
        val titleHeaderWebsite = "TokoPoints"
        with(itemView) {
            if (drawerHeader.tokoPointDrawerData != null && drawerHeader.tokoPointDrawerData?.offFlag == 0) {
                tokopoint_container.visibility = View.VISIBLE
                val title = drawerHeader.tokoPointDrawerData?.mainPageTitle
                tv_tokopoint_action.text = titleHeaderWebsite
                loadTokoPointImage(drawerHeader.tokoPointDrawerData?.userTier?.tierImageUrl)
                tv_tokopoint_count.text = drawerHeader.tokoPointDrawerData?.userTier?.rewardPointsStr
                tv_tokopoint_action.setOnClickListener {
                    eventUserClickedPoints()
                    drawerHeaderListener.onTokoPointActionClicked(
                            drawerHeader.tokoPointDrawerData?.mainPageUrl,
                            title ?: titleHeaderWebsite
                    )
                }

            } else tokopoint_container.visibility = View.GONE
        }
    }

    private fun eventUserClickedPoints() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickHomePage",
                "homepage-tokopoints",
                "click point & tier status",
                "tokopoints")
    }

    private fun setListener(drawerHeader: DrawerHeader) {
        with(itemView) {
            drawer_saldo.setOnClickListener { drawerHeaderListener.onGoToDeposit() }
            drawer_top_points.setOnClickListener {
                if (drawerHeader.sellerDrawerTopPoints?.topPointsUrl != null)
                    drawerHeaderListener.onGoToTopPoints(drawerHeader.sellerDrawerTopPoints?.topPointsUrl)
            }
            drawer_top_cash.setOnClickListener {
                if (drawerHeader.sellerDrawerTokoCash?.drawerWalletAction != null) {
                    if (isRegistered(drawerHeader.sellerDrawerTokoCash)) {
                        drawerHeaderListener.onWalletBalanceClicked(
                                drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.redirectUrlBalance,
                                drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.appLinkBalance
                        )
                        val redirectUrlBalance = drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.redirectUrlBalance
                        if (redirectUrlBalance != null)
                            SellerAnalyticsEventTrackingHelper.homepageTokocashClick(it.context, redirectUrlBalance)
                    } else {
                        drawerHeaderListener.onWalletActionButtonClicked(
                                drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.redirectUrlActionButton,
                                drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.appLinkActionButton
                        )
                        SellerAnalyticsEventTrackingHelper.hamburgerTokocashActivateClick(it.context)
                    }
                }
                else retryTokoCashListener.onRetryTokoCash()
            }
            name_text.setOnClickListener { drawerHeaderListener.onGoToProfile() }
            user_avatar.setOnClickListener { drawerHeaderListener.onGoToProfile() }
            complete_profile.setOnClickListener { drawerHeaderListener.onGoToProfileCompletion() }
            drawer_tokocard.setOnClickListener { drawerHeaderListener.onGotoTokoCard() }
        }
    }

    private fun setTopCash(drawerHeader: DrawerHeader) {
        val isTokoCashDisabled: (SellerDrawerTokoCash?) -> Boolean = { sellerDrawerTokoCash ->
            sellerDrawerTokoCash?.drawerWalletAction?.labelTitle.isNullOrEmpty()
        }
        val isActionTypeBalance by lazy {
            drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.typeAction == SellerDrawerWalletAction.TYPE_ACTION_BALANCE
        }
        with(itemView) {
            loading_top_cash.visibility = View.VISIBLE
            if (isTokoCashDisabled(drawerHeader.sellerDrawerTokoCash)) {
                loading_top_cash.visibility = View.GONE
                retry_top_cash.visibility = View.VISIBLE
            } else {
                if (isActionTypeBalance) {
                    showTokoCashBalanceView()
                    top_cash_value.text = drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.balance
                    toko_cash_label.text = drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.labelTitle
                } else {
                    showTokoCashActivateView()
                    toko_cash_activation_button.text = drawerHeader.sellerDrawerTokoCash?.drawerWalletAction?.labelActionButton
                }
                loading_top_cash.visibility = View.GONE
                retry_top_cash.visibility = View.GONE
            }
        }
    }

    private fun showTokoCashBalanceView() {
        with(itemView) {
            drawer_top_cash.visibility = View.VISIBLE
            toko_cash_activation_button.visibility = View.GONE
            top_cash_value.visibility = View.VISIBLE
        }
    }

    private fun showTokoCashActivateView() {
        with(itemView) {
            drawer_top_cash.visibility = View.VISIBLE
            toko_cash_activation_button.visibility = View.VISIBLE
            top_cash_value.visibility = View.GONE
        }
    }

    private fun isRegistered(sellerDrawerTokoCash: SellerDrawerTokoCash?): Boolean {
        return sellerDrawerTokoCash?.drawerWalletAction?.typeAction == SellerDrawerWalletAction.TYPE_ACTION_BALANCE
    }

    private fun setTopPoints(drawerHeader: DrawerHeader) {
        with(itemView) {
            when {
                !drawerHeader.sellerDrawerTopPoints?.isActive!! ->
                    drawer_top_points.visibility = View.GONE
                drawerHeader.sellerDrawerTopPoints?.topPoints!!.isEmpty() -> {
                    drawer_top_points.visibility = View.VISIBLE
                    loading_loyalty.visibility = View.VISIBLE
                    toppoints_text.visibility = View.GONE }
                else ->  {
                    drawer_top_points.visibility = View.VISIBLE
                    loading_loyalty.visibility = View.GONE
                    toppoints_text.visibility = View.VISIBLE
                    toppoints_text.text = drawerHeader.sellerDrawerTopPoints?.topPoints
                }
            }
        }
    }

    private fun setDeposit(drawerHeader: DrawerHeader) {
        with(itemView) {
            val deposit = drawerHeader.sellerDrawerDeposit?.deposit
            if (deposit != null) {
                if (deposit.isEmpty()) {
                    loading_saldo.visibility = View.VISIBLE
                    deposit_text.visibility = View.GONE
                } else {
                    loading_saldo.visibility = View.GONE
                    deposit_text.apply {
                        text = deposit
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}