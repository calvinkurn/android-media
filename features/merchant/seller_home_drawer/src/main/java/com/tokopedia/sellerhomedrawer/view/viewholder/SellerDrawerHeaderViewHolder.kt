package com.tokopedia.sellerhomedrawer.view.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.view.listener.SellerDrawerItemListener
import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerHeader
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.seller_drawer_header.view.*

class SellerDrawerHeaderViewHolder(itemView: View,
                                   val listener: SellerDrawerItemListener,
                                   val context: Context)
    : AbstractViewHolder<SellerDrawerHeader>(itemView){

    companion object {
        val LAYOUT_RES = R.layout.seller_drawer_header
    }

    private lateinit var userSession: UserSessionInterface
    private var oldUserAvatar = ""

    override fun bind(sellerDrawerHeader: SellerDrawerHeader?) {
        userSession = UserSession(context)
        if (userSession.isLoggedIn && sellerDrawerHeader != null)
            bindDrawerHeader(sellerDrawerHeader)
        else bindDrawerHeaderGuest()
    }

    protected fun bindDrawerHeaderGuest() {
        with(itemView) {
            cover_img.visibility = View.VISIBLE
            drawer_points_layout.visibility = View.GONE
            percent_text.visibility = View.GONE
            layout_progress.visibility = View.GONE
            verified_icon.visibility = View.GONE
            verified.visibility = View.GONE
            drawer_header.visibility = View.GONE
            ImageHandler.loadImageWithId(cover_img, R.drawable.drawer_header_bg)
        }
    }

    protected fun bindDrawerHeader(sellerDrawerHeader: SellerDrawerHeader) {
        with(itemView) {

            drawer_header.visibility = View.VISIBLE
            drawer_points_layout.visibility = View.VISIBLE
            cover_img.visibility = View.GONE
            name_text.visibility = View.VISIBLE
            user_avatar.visibility = View.VISIBLE
            percent_text.visibility = View.VISIBLE

            val sellerUserAvatar = sellerDrawerHeader.drawerProfile.userAvatar

            val isAvatarExistAndNotOld = sellerDrawerHeader.drawerProfile.userAvatar != null &&
                    oldUserAvatar != sellerUserAvatar

            if (isAvatarExistAndNotOld) {
                loadAvatarImage(this, sellerUserAvatar)
            }

            name_text.text = sellerDrawerHeader.drawerProfile.userName
            percent_text.text = String.format("%s%%", sellerDrawerHeader.profileCompletion.toString())
            if (sellerDrawerHeader.profileCompletion == 100) {
                layout_progress.visibility = View.GONE
                verified_icon.visibility = View.VISIBLE
                verified.visibility = View.VISIBLE
            } else {
                ProgressBar.progress = sellerDrawerHeader.profileCompletion
                layout_progress.visibility = View.VISIBLE
                verified_icon.visibility = View.GONE
                verified.visibility = View.GONE
            }
        }
    }

    private fun loadAvatarImage(itemView: View, userAvatar: String) {
        ImageHandler.loadImage(context,
                itemView.user_avatar,
                userAvatar,
                R.drawable.ic_image_avatar_boy,
                R.drawable.ic_image_avatar_boy)
        oldUserAvatar = userAvatar
    }
//
//    private fun setTokoCard(itemView: View) {
//        val remoteConfig = FirebaseRemoteConfigImpl(context)
//        val showTokoCard = remoteConfig.getBoolean(RemoteConfigKey.SHOW_TOKOCARD, true)
//
//        if (!showTokoCard) {
//            holder.tokocardLayout.setVisibility(View.GONE)
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun setTokoPoint(holder: ViewHolder) {
//        val TITLE_HEADER_WEBSITE = "TokoPoints"
//        if (data.getTokoPointDrawerData() != null && data.getTokoPointDrawerData().getOffFlag() == 0) {
//            holder.tokoPointContainer.setVisibility(View.VISIBLE)
//            val title = data.getTokoPointDrawerData().getMainPageTitle()
//            holder.tvTokoPointAction.setText(if (TextUtils.isEmpty(title)) TITLE_HEADER_WEBSITE else "TokoPoints")
//            com.tkpd.library.utils.ImageHandler.loadImageThumbs(context,
//                    holder.ivTokoPointBadge,
//                    data.getTokoPointDrawerData().getUserTier().getTierImageUrl())
//            holder.tvTokoPointCount.setText(data.getTokoPointDrawerData().getUserTier().getRewardPointsStr())
//            holder.tvTokoPointAction.setOnClickListener(View.OnClickListener {
//                eventUserClickedPoints()
//                listener.onTokoPointActionClicked(
//                        data.getTokoPointDrawerData().getMainPageUrl(),
//                        if (TextUtils.isEmpty(title)) TITLE_HEADER_WEBSITE else title
//                )
//            })
//        } else {
//            holder.tokoPointContainer.setVisibility(View.GONE)
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
//
//    private fun setListener(holder: ViewHolder) {
//        holder.saldoLayout.setOnClickListener(View.OnClickListener { listener.onGoToDeposit() })
//        holder.topPointsLayout.setOnClickListener(View.OnClickListener {
//            if (data.getDrawerTopPoints() != null && data.getDrawerTopPoints().getTopPointsUrl() != null)
//                listener.onGoToTopPoints(data.getDrawerTopPoints().getTopPointsUrl())
//        })
//        holder.tokoCashLayout.setOnClickListener(View.OnClickListener { v ->
//            if (data.getDrawerTokoCash() != null && data.getDrawerTokoCash().getDrawerWalletAction() != null) {
//                if (isRegistered(data.getDrawerTokoCash())) {
//                    listener.onWalletBalanceClicked(
//                            data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlBalance(),
//                            data.getDrawerTokoCash().getDrawerWalletAction().getAppLinkBalance()
//                    )
//                    AnalyticsEventTrackingHelper.homepageTokocashClick(v.context, data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlBalance())
//
//                } else {
//                    listener.onWalletActionButtonClicked(
//                            data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlActionButton(),
//                            data.getDrawerTokoCash().getDrawerWalletAction().getAppLinkActionButton()
//                    )
//                    AnalyticsEventTrackingHelper.hamburgerTokocashActivateClick(v.context)
//
//                }
//            } else {
//                tokoCashListener.onRetryTokoCash()
//            }
//        })
//        holder.name.setOnClickListener(View.OnClickListener { listener.onGoToProfile() })
//        holder.avatar.setOnClickListener(View.OnClickListener { listener.onGoToProfile() })
//
//        holder.completeProfile.setOnClickListener(View.OnClickListener { listener.onGoToProfileCompletion() })
//        holder.tokocardLayout.setOnClickListener(View.OnClickListener { listener.onGotoTokoCard() })
//
//
//    }
//
//    private fun setTopCash(holder: ViewHolder) {
//        holder.loadingTokoCash.setVisibility(View.VISIBLE)
//        if (isTokoCashDisabled(data.getDrawerTokoCash())) {
//            holder.loadingTokoCash.setVisibility(View.GONE)
//            holder.retryTopCash.setVisibility(View.VISIBLE)
//        } else {
//            if (data.getDrawerTokoCash().getDrawerWalletAction().getTypeAction() == DrawerWalletAction.TYPE_ACTION_BALANCE) {
//                showTokoCashBalanceView(holder)
//                holder.tokoCash.setText(data.getDrawerTokoCash().getDrawerWalletAction().getBalance())
//                holder.tokoCashLabel.setText(data.getDrawerTokoCash().getDrawerWalletAction().getLabelTitle())
//            } else {
//                showTokoCashActivateView(holder)
//                holder.tokoCashActivationButton.setText(data.getDrawerTokoCash()
//                        .getDrawerWalletAction().getLabelActionButton())
//            }
//            holder.loadingTokoCash.setVisibility(View.GONE)
//            holder.retryTopCash.setVisibility(View.GONE)
//        }
//    }
//
//    private fun showTokoCashBalanceView(holder: ViewHolder) {
//        holder.tokoCashLayout.setVisibility(View.VISIBLE)
//        holder.tokoCashActivationButton.setVisibility(View.GONE)
//        holder.tokoCash.setVisibility(View.VISIBLE)
//    }
//
//    private fun showTokoCashActivateView(holder: ViewHolder) {
//        holder.tokoCashLayout.setVisibility(View.VISIBLE)
//        holder.tokoCashActivationButton.setVisibility(View.VISIBLE)
//        holder.tokoCash.setVisibility(View.GONE)
//    }
//
//    private fun isRegistered(drawerTokoCash: DrawerTokoCash): Boolean {
//        return drawerTokoCash.drawerWalletAction.typeAction == DrawerWalletAction.TYPE_ACTION_BALANCE
//    }
//
//    private fun isTokoCashDisabled(drawerTokoCash: DrawerTokoCash?): Boolean {
//        return (drawerTokoCash == null || drawerTokoCash.drawerWalletAction == null
//                || drawerTokoCash.drawerWalletAction.labelTitle == null
//                || drawerTokoCash.drawerWalletAction.labelTitle == "")
//    }
//
//    private fun setTopPoints(holder: ViewHolder) {
//        if (!data.getDrawerTopPoints().isActive()) {
//            holder.topPointsLayout.setVisibility(View.GONE)
//        } else if (data.getDrawerTopPoints().getTopPoints() == "") {
//            holder.topPointsLayout.setVisibility(View.VISIBLE)
//            holder.loadingLoyalty.setVisibility(View.VISIBLE)
//            holder.topPoint.setVisibility(View.GONE)
//        } else {
//            holder.topPointsLayout.setVisibility(View.VISIBLE)
//            holder.loadingLoyalty.setVisibility(View.GONE)
//            holder.topPoint.setVisibility(View.VISIBLE)
//            holder.topPoint.setText(data.getDrawerTopPoints().getTopPoints())
//        }
//    }
//
//    private fun setDeposit(holder: ViewHolder) {
//        if (data.getDrawerDeposit().getDeposit() == "") {
//            holder.loadingSaldo.setVisibility(View.VISIBLE)
//            holder.deposit.setVisibility(View.GONE)
//        } else {
//            holder.loadingSaldo.setVisibility(View.GONE)
//            holder.deposit.setText(data.getDrawerDeposit().getDeposit())
//            holder.deposit.setVisibility(View.VISIBLE)
//        }
//    }
}