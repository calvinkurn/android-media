package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gamification.util.HexValidator
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.util.ViewUtils
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.pojo.wallet.SectionContentItem
import com.tokopedia.v2.home.model.pojo.wallet.Tokopoint
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
        private val textViewActionTokocash = itemView.findViewById<TextView>(R.id.tv_btn_action_tokocash)
        private val textViewTitleTokocash = itemView.findViewById<TextView>(R.id.tv_title_tokocash)
        private val tvBalanceTokocash = itemView.findViewById<TextView>(R.id.tv_balance_tokocash)
        private val imageViewLogoWallet = itemView.findViewById<ImageView>(R.id.iv_logo_tokocash)
        private val tokocashProgressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar_tokocash)
        private val containerWallet = itemView.findViewById<ConstraintLayout>(R.id.container_wallet)

        private val tokoPointHolder = itemView.findViewById<View>(R.id.container_tokopoint)
        private val tvBalanceTokoPoint = itemView.findViewById<TextView>(R.id.tv_balance_tokopoint)
        private val tvActionTokopoint = itemView.findViewById<TextView>(R.id.tv_btn_action_tokopoint)
        private val ivLogoTokoPoint = itemView.findViewById<ImageView>(R.id.iv_logo_tokopoint)
        private val tokopointProgressBarLayout = itemView.findViewById<ProgressBar>(R.id.progress_bar_tokopoint_layout)
        private val tokopointActionContainer = itemView.findViewById<View>(R.id.container_action_tokopoint)
        private val mTextCouponCount = itemView.findViewById<TextView>(R.id.text_coupon_count)


        fun bind(item: WalletDataModel){
            containerWallet.background = ViewUtils.generateBackgroundWithShadow(containerWallet, R.color.white, R.dimen.dp_8, R.color.shadow_6, R.dimen.dp_2, Gravity.CENTER)
            scanHolder.setOnClickListener { goToScanner() }
            renderWallet(item.walletBalance)
            renderTokopoint(item.tokopoint)
        }

        private fun goToScanner() {
            HomePageTracking.eventQrCode(itemView.context)
            RouteManager.route(itemView.context, ApplinkConstInternalMarketplace.QR_SCANNEER)
        }

        private fun renderWallet(item: WalletDataModel.WalletAction){
            if (item.status == Resource.Status.ERROR) {
                showErrorWallet()
            } else if (item.status == Resource.Status.LOADING) {
                showLoadingWallet()
            } else {
                if (!TextUtils.isEmpty(item.walletType) && item.walletType == WALLET_TYPE) {
                    showWallet(item)
                } else {
                    showTokoCash(item)
                }
            }
        }

        private fun showErrorWallet(){
            tokoCashHolder.setOnClickListener {
                tokocashProgressBar.visibility = View.VISIBLE
//                listener.onRefreshTokoCashButtonClicked()
            }
            textViewTitleTokocash.setText(R.string.home_header_tokocash_unable_to_load_label)
            textViewActionTokocash.setText(R.string.home_header_tokocash_refresh_label)
            textViewActionTokocash.visibility = View.VISIBLE
            tvBalanceTokocash.visibility = View.GONE
            tokocashProgressBar.visibility = View.GONE
        }

        private fun showLoadingWallet(){
            textViewActionTokocash.visibility = View.GONE
            textViewTitleTokocash.visibility = View.GONE
            tvBalanceTokocash.visibility = View.GONE
            tokoCashHolder.setOnClickListener(null)
            tokocashProgressBar.visibility = View.VISIBLE
        }

        @SuppressLint("SetTextI18n")
        private fun showWallet(wallet: WalletDataModel.WalletAction){
            tokocashProgressBar.visibility = View.GONE
            textViewActionTokocash.text = wallet.labelActionButton
            textViewActionTokocash.setOnClickListener { goToOvoAppLink(wallet.linked, wallet.appLinkActionButton) }
            tokoCashHolder.setOnClickListener { goToOvoAppLink(wallet.linked, wallet.appLinkActionButton) }
            imageViewLogoWallet.setImageResource(R.drawable.wallet_ic_ovo_home)
            if (wallet.linked) {
                textViewTitleTokocash.text = wallet.cashBalance
                textViewActionTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.text = itemView.resources.getString(R.string.home_header_fintech_points, wallet.pointBalance)
                textViewActionTokocash.visibility = if (wallet.visibleActionButton) View.VISIBLE else View.GONE
                textViewTitleTokocash.visibility = if (wallet.visibleActionButton) View.GONE else View.VISIBLE
            } else {
                textViewTitleTokocash.text = TITLE
                textViewActionTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.visibility = View.GONE
                if (wallet.cashBackData.amount > 0) {
                    textViewTitleTokocash.text = "(+ ${wallet.cashBackData.amountText} )"
                }
            }
        }

        private fun showTokoCash(wallet: WalletDataModel.WalletAction){
            tokocashProgressBar.visibility = View.GONE
            textViewTitleTokocash.text = wallet.labelTitle
            textViewActionTokocash.text = wallet.labelActionButton
            textViewActionTokocash.setOnClickListener {
                if (!wallet.appLinkActionButton.contains("webview") && !wallet.linked) {
                    HomePageTracking.eventTokoCashActivateClick(itemView.context)
                }
//                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkActionButton)
            }
            tokoCashHolder.setOnClickListener {
                if (wallet.appLinkBalance != "" &&
                        !wallet.appLinkBalance.contains("webview") &&
                        wallet.linked) {
                    HomePageTracking.eventTokoCashCheckSaldoClick(itemView.context)
                }

//                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkBalance)
            }
            imageViewLogoWallet.setImageResource(R.drawable.ic_tokocash)

            if (wallet.linked) {
                tvBalanceTokocash.visibility = View.VISIBLE
                tvBalanceTokocash.text = wallet.balance

                textViewActionTokocash.visibility = if (wallet.visibleActionButton) View.VISIBLE else View.GONE
                textViewTitleTokocash.visibility = if (wallet.visibleActionButton) View.VISIBLE else View.GONE
            } else {
                tvBalanceTokocash.visibility = View.GONE
                textViewActionTokocash.visibility = View.VISIBLE
                if (wallet.cashBackData.amount > 0) {
                    textViewActionTokocash.visibility = View.GONE
                    tvBalanceTokocash.visibility = View.VISIBLE
                    tvBalanceTokocash.text = wallet.cashBackData.amountText
                    tvBalanceTokocash.setOnClickListener {
//                            listener.actionInfoPendingCashBackTokocash(wallet.cashBackData, wallet.appLinkActionButton)
                    }
                }
            }
        }

        private fun renderTokopoint(item: WalletDataModel.TokopointAction){
            if(item.status == Resource.Status.LOADING){
                showLoadingTokopoint()
            }else if(item.status == Resource.Status.ERROR){
                showErrorTokopoint()
            }else{
                showTokopoint(item.tokopoint)
            }
        }

        private fun showErrorTokopoint() {
            tokoPointHolder.setOnClickListener {
                tokopointProgressBarLayout.visibility = View.VISIBLE
//                listener.onRefreshTokoPointButtonClicked()
            }
            mTextCouponCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            mTextCouponCount.visibility = View.VISIBLE
            mTextCouponCount.setText(R.string.home_header_tokopoint_unable_to_load_label)
            mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
            tvActionTokopoint.setText(R.string.home_header_tokopoint_refresh_label)
            tvActionTokopoint.visibility = View.VISIBLE
            tokopointProgressBarLayout.visibility = View.GONE
            tokopointActionContainer.visibility = View.VISIBLE
            ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_normal_24)
            tvBalanceTokoPoint.visibility = View.GONE
        }

        private fun showLoadingTokopoint(){
            tokoPointHolder.setOnClickListener(null)
            tokopointProgressBarLayout.visibility = View.VISIBLE
            tokopointActionContainer.visibility = View.GONE
            tvBalanceTokoPoint.visibility = View.GONE
        }

        private fun showTokopoint(item: Tokopoint){
            tokopointProgressBarLayout.visibility = View.GONE
            tokopointActionContainer.visibility = View.VISIBLE
            tvActionTokopoint.visibility = View.GONE
            tvBalanceTokoPoint.visibility = View.VISIBLE
            mTextCouponCount.visibility = View.VISIBLE

            ImageHandler.loadImageAndCache(ivLogoTokoPoint, item.iconImageURL)
            mTextCouponCount.setTypeface(mTextCouponCount.typeface, Typeface.BOLD)
            if (item.sectionContent.isNotEmpty()) {
                setTokopointHeaderData(item.sectionContent[0], tvBalanceTokoPoint)
                if (item.sectionContent.size >= 2) {
                    setTokopointHeaderData(item.sectionContent[1], mTextCouponCount)
                }
            } else {
                tvBalanceTokoPoint.setText(R.string.home_header_tokopoint_no_tokopoints)
                mTextCouponCount.setText(R.string.home_header_tokopoint_no_coupons)
                tvBalanceTokoPoint.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_main_green))
            }

            tokoPointHolder.setOnClickListener {
                HomePageTracking.eventUserProfileTokopoints(itemView.context)
//                listener.actionTokoPointClicked(
//                        element.tokopointsDrawerHomeData.redirectAppLink,
//                        element.tokopointsDrawerHomeData.redirectURL,
//                        if (TextUtils.isEmpty(element.tokopointsDrawerHomeData.mainPageTitle))
//                            OvoViewHolder.TITLE_HEADER_WEBSITE
//                        else
//                            element.tokopointsDrawerHomeData.mainPageTitle
//                )

            }
        }

        private fun setTokopointHeaderData(sectionContentItem: SectionContentItem?, tokopointsTextView: TextView) {
            if (sectionContentItem != null) {

                //Initializing to default value to prevent stale data in case of onresume
                tokopointsTextView.background = null
                tokopointsTextView.text = null
                tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_12))

                if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                    if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.backgroundColour) && HexValidator.validate(sectionContentItem.tagAttributes.backgroundColour)) {
                        val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                        if (drawable is GradientDrawable) {
                            val shapeDrawable = drawable as GradientDrawable?
                            shapeDrawable!!.setColorFilter(Color.parseColor(sectionContentItem.tagAttributes.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                            tokopointsTextView.background = shapeDrawable
                            val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                            tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                            tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                            tokopointsTextView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                        }
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    } else {
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                    }
                    if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                        tokopointsTextView.text = sectionContentItem.tagAttributes.text
                    }
                } else if (!TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                    if (!TextUtils.isEmpty(sectionContentItem.textAttributes.color) && HexValidator.validate(sectionContentItem.textAttributes.color)) {
                        tokopointsTextView.setTextColor(Color.parseColor(sectionContentItem.textAttributes.color))
                    } else {
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                    }
                    if (sectionContentItem.textAttributes.isBold) {
                        tokopointsTextView.setTypeface(null, Typeface.BOLD)
                    } else {
                        tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                    }
                    if (!TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                        tokopointsTextView.text = sectionContentItem.textAttributes.text
                    }

                }
            }
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