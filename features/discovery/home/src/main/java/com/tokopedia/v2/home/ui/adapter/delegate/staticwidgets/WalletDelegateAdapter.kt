package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.app.Activity
import android.content.ContextWrapper
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.pojo.wallet.Wallet
import com.tokopedia.v2.home.model.vo.Resource
import com.tokopedia.v2.home.model.vo.WalletDataModel

class WalletDelegateAdapter : ViewTypeDelegateAdapter {
    companion object{
        private const val WALLET_TYPE = "OVO"
        private const val TITLE = "OVO"
    }
    override fun isForViewType(item: ModelViewType): Boolean {
        return item is WalletDataModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return WalletViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as WalletViewHolder
        holder.bind(item as WalletDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is WalletViewHolder){
            holder.bind(item as WalletDataModel)
        }
    }

    inner class WalletViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.layout_wallet_login)){
        private val scanHolder = itemView.findViewById<View>(R.id.container_action_scan)
        private val tokoCashHolder = itemView.findViewById<View>(R.id.container_tokocash)
        private val tvActionTokocash = itemView.findViewById<TextView>(R.id.tv_btn_action_tokocash)
        private val tvTitleTokocash = itemView.findViewById<TextView>(R.id.tv_title_tokocash)
        private val tvBalanceTokocash = itemView.findViewById<TextView>(R.id.tv_balance_tokocash)
        private val ivLogoTokocash = itemView.findViewById<ImageView>(R.id.iv_logo_tokocash)
        private val tokocashProgressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar_tokocash)

        fun bind(item: WalletDataModel){
            scanHolder.setOnClickListener { goToScanner() }


            if (item.status == Resource.Status.ERROR) {
                initWalletErrorLoading()
            } else if (item.status == Resource.Status.LOADING) {
                hideWallet()
            } else {
                if (!TextUtils.isEmpty(item.walletData?.wallet?.walletType ?: "") && item.walletData?.wallet?.walletType == WALLET_TYPE) {
                    showWallet(item.walletData.wallet)
                } else {
//                    showTokoCash(item.walletData.wallet)
                }
            }
        }

        private fun goToScanner() {
//            HomePageTracking.eventQrCode(itemView.context)
            RouteManager.route(itemView.context, ApplinkConstInternalMarketplace.QR_SCANNEER)
        }

        private fun initWalletErrorLoading(){
            tokoCashHolder.setOnClickListener {
                tokocashProgressBar.visibility = View.VISIBLE
//                listener.onRefreshTokoCashButtonClicked()
            }
            tvTitleTokocash.setText(R.string.home_header_tokocash_unable_to_load_label)
            tvActionTokocash.setText(R.string.home_header_tokocash_refresh_label)
            tvActionTokocash.visibility = View.VISIBLE
            tvBalanceTokocash.visibility = View.GONE
            tokocashProgressBar.visibility = View.GONE
        }

        private fun hideWallet(){
            tvActionTokocash.visibility = View.GONE
            tvTitleTokocash.visibility = View.GONE
            tvBalanceTokocash.visibility = View.GONE
            tokoCashHolder.setOnClickListener(null)
            tokocashProgressBar.visibility = View.VISIBLE
        }

        private fun showWallet(wallet: Wallet){
            tokocashProgressBar.visibility = View.GONE
            tvActionTokocash.text = wallet.action.text
            tvActionTokocash.setOnClickListener { goToOvoAppLink(wallet.linked, wallet.action.applinks) }
            tokoCashHolder.setOnClickListener { goToOvoAppLink(wallet.linked, wallet.action.applinks) }

            if (wallet.linked) {
                tvTitleTokocash.text = wallet.cashBalance
                tvActionTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.text = itemView.resources.getString(R.string.home_header_fintech_points, wallet.pointBalance)
                tvActionTokocash.visibility = if (wallet.action.visibility == "1") View.VISIBLE else View.GONE
                tvTitleTokocash.visibility = if (wallet.action.visibility == "1") View.GONE else View.VISIBLE
            } else {
                tvTitleTokocash.text = TITLE
                tvActionTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.visibility = View.GONE
//                if (wallet.isPendingTokocashChecked && wallet.cashBalance.isNotEmpty()) {
//                    if (element.cashBackData.amount > 0) {
//                        tvTitleTokocash.text = "(+ ${element.cashBackData.amountText} )"
//                    }
//                } else {
//                    listener.onRequestPendingCashBack()
//                }
            }
        }

        private fun showTokoCash(wallet: Wallet){
            tokocashProgressBar.visibility = View.GONE
//            tvTitleTokocash.text = homeHeaderWalletAction.labelTitle
//            tvActionTokocash.text = homeHeaderWalletAction.labelActionButton
//            tvActionTokocash.setOnClickListener {
//                if (!homeHeaderWalletAction.appLinkActionButton.contains("webview") && !homeHeaderWalletAction.isLinked) {
//                    HomePageTracking.eventTokoCashActivateClick(itemView.context)
//                }
//                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkActionButton)
//            }
            tokoCashHolder.setOnClickListener {
//                if (homeHeaderWalletAction.appLinkBalance != "" &&
//                        !homeHeaderWalletAction.appLinkBalance.contains("webview") &&
//                        homeHeaderWalletAction.isLinked) {
//                    HomePageTracking.eventTokoCashCheckSaldoClick(itemView.context)
//                }

//                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkBalance)
            }
            ivLogoTokocash.setImageResource(R.drawable.ic_tokocash)

//            if (wallet.linked) {
//                tvBalanceTokocash.visibility = View.VISIBLE
//                tvBalanceTokocash.text = wallet.balance
//
//                tvActionTokocash.visibility = if (wallet.isVisibleActionButton) View.VISIBLE else View.GONE
//                tvTitleTokocash.visibility = if (wallet.isVisibleActionButton) View.GONE else View.VISIBLE
//            } else {
//                tvBalanceTokocash.visibility = View.GONE
//                tvActionTokocash.visibility = View.VISIBLE
//                if (wallet.isPendingTokocashChecked && wallet.cashBackData != null) {
//                    if (wallet.cashBackData.amount > 0) {
//                        tvActionTokocash.visibility = View.GONE
//                        tvBalanceTokocash.visibility = View.VISIBLE
//                        tvBalanceTokocash.text = wallet.cashBackData.amountText
//                        tvBalanceTokocash.setOnClickListener {
//                            listener.actionInfoPendingCashBackTokocash(wallet.cashBackData, wallet.appLinkActionButton)
//                        }
//                    }
//                } else {
//                    listener.onRequestPendingCashBack()
//                }
//            }
        }

        private fun goToOvoAppLink(linkedOvo: Boolean, applinkString: String) {
            if (RouteManager.isSupportApplink(itemView.context, applinkString)) {
                if (!linkedOvo) {
                    if (itemView.context !is Activity && itemView.context is ContextWrapper) {
                        val context = itemView.context
                        val activity = context as Activity
                        activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay)
                    }
//                    walletAnalytics.eventClickActivationOvoHomepage()
                } else {
                    HomePageTracking.eventOvo(itemView.context)
                }
                val intentBalanceWallet = RouteManager.getIntent(itemView.context, applinkString)
                itemView.context.startActivity(intentBalanceWallet)
            }
        }

    }
}