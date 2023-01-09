package com.tokopedia.sellerhome.view.helper

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.data.SellerHomeSharedPref
import com.tokopedia.sellerhome.view.dialog.NewSellerDialog
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 14/11/22.
 */

class NewSellerJourneyHelper @Inject constructor(
    private val sharedPref: SellerHomeSharedPref, private val userSession: UserSessionInterface
) {

    companion object {
        const val WIDGET_DISMISSAL_ID = "0"
    }

    private var sectionWidgetView: View? = null
    private var notificationView: View? = null
    private var navigationView: View? = null
    private var otherMenuView: View? = null
    private var coachMark: CoachMark2? = null
    private var isWelcomingDialogShown = false
    private var isFirstOrderDialogShown = false

    fun showNewSellerDialog(
        context: Context,
        sectionWidgetAnchor: View?,
        notificationAnchor: View?,
        navigationAnchor: View?,
        otherMenuAnchor: View?
    ) {
        this.sectionWidgetView = sectionWidgetAnchor
        this.notificationView = notificationAnchor
        this.navigationView = navigationAnchor
        this.otherMenuView = otherMenuAnchor

        showNewSellerDialog(context)
    }

    fun showFirstOrderDialog(
        context: Context, info: ShopStateInfoUiModel, onDismiss: () -> Unit
    ) {
        if (!sharedPref.getFirstOrderDialogEligibility(userSession.userId) || isFirstOrderDialogShown) {
            return
        }
        isFirstOrderDialogShown = true

        NewSellerDialog.showFirstOrderDialog(context, info, onDismiss = {
            sharedPref.makeFirstOrderDialogNotEligible(userSession.userId)
            isFirstOrderDialogShown = false
            onDismiss()
        })
    }

    fun shouldFetchShopInfo(): Boolean {
        val userId = userSession.userId
        return sharedPref.getFirstOrderDialogEligibility(userId)
    }

    private fun showNewSellerDialog(context: Context) {
        if (!sharedPref.getWelcomingDialogEligibility(userSession.userId)) {
            showNewSellerCoachMark(context)
            return
        }

        if (isWelcomingDialogShown) return

        NewSellerDialog.showNewSellerJourneyDialog(context, userSession.shopName) {
            showNewSellerCoachMark(context)
            sharedPref.makeWelcomingDialogNotEligible(userSession.userId)
            isWelcomingDialogShown = false
        }
        isWelcomingDialogShown = true
    }

    private fun showNewSellerCoachMark(context: Context) {
        if (!sharedPref.getWelcomingCoachMarkEligibility(userSession.userId)) {
            return
        }

        if (coachMark == null) {
            val coachMarkItems = getCoachMarkList(context)
            coachMark = CoachMark2(context).apply {
                isDismissed = false
                onDismissListener = {
                    sharedPref.makeWelcomingCoachMarkNotEligible(userSession.userId)
                }
                showCoachMark(coachMarkItems)
            }
        }
    }

    private fun getCoachMarkList(context: Context): ArrayList<CoachMark2Item> {
        val coachMarkItems = arrayListOf<CoachMark2Item>()
        sectionWidgetView?.let {
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = it,
                    title = context.getString(R.string.sah_new_seller_welcoming_coach_mark_title_1),
                    description = context.getString(R.string.sah_new_seller_welcoming_coach_mark_description_1),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        }
        notificationView?.let {
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = it,
                    title = context.getString(R.string.sah_new_seller_welcoming_coach_mark_title_2),
                    description = context.getString(R.string.sah_new_seller_welcoming_coach_mark_description_2),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        }
        navigationView?.let {
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = it,
                    title = context.getString(R.string.sah_new_seller_welcoming_coach_mark_title_3),
                    description = context.getString(R.string.sah_new_seller_welcoming_coach_mark_description_3),
                    position = CoachMark2.POSITION_TOP
                )
            )
        }
        otherMenuView?.let {
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = it,
                    title = context.getString(R.string.sah_new_seller_welcoming_coach_mark_title_4),
                    description = context.getString(R.string.sah_new_seller_welcoming_coach_mark_description_4),
                    position = CoachMark2.POSITION_TOP
                )
            )
        }
        return coachMarkItems
    }
}