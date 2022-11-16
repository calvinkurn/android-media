package com.tokopedia.sellerhome.view.helper

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.data.SellerHomeSharedPref
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 14/11/22.
 */

class NewSellerJourneyHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPref: SellerHomeSharedPref,
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val IMG_WELCOMING_DIALOG =
            "https://images.tokopedia.net/img/android/seller_home/img_sah_new_seller_dialog.png"
    }

    private var sectionWidgetView: View? = null
    private var notificationView: View? = null
    private var navigationView: View? = null
    private var otherMenuView: View? = null

    fun showDialog() {
        if (!sharedPref.getNewSellerWelcomingDialogEligibility(userSession.userId)) {
            return
        }

        val dialog = DialogUnify(
            context,
            DialogUnify.SINGLE_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )

        with(dialog) {
            setImageUrl(IMG_WELCOMING_DIALOG)
            setTitle(
                context.getString(
                    R.string.sah_new_seller_welcoming_dialog_title,
                    userSession.shopName
                )
            )
            setDescription(context.getString(R.string.sah_new_seller_welcoming_dialog_description))
            setPrimaryCTAText(context.getString(R.string.sah_learn_a_new_look))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setOnDismissListener {
                showNewSellerCoachMark()
                sharedPref.makeNewSellerWelcomingDialogNotEligible(userSession.userId)
            }
            show()
        }
    }

    fun setSectionWidgetView(view: View) {
        this.sectionWidgetView = view
    }

    fun setNotificationView(view: View) {
        this.notificationView = view
    }

    fun setNavigationView(view: View) {
        this.navigationView = view
    }

    fun setOtherMenuView(view: View) {
        this.otherMenuView = view
    }

    private fun showNewSellerCoachMark() {
        if (!sharedPref.getNewSellerWelcomingCoachMarkEligibility()) {
            return
        }

        val coachMarkItems = getCoachMarkList()
        val coachMark = CoachMark2(context)
        coachMark.isDismissed = false
        coachMark.onDismissListener = {
            val isTheLastIndex = coachMark.currentIndex == coachMarkItems.size.minus(Int.ONE)
            if (isTheLastIndex) {
                sharedPref.makeNewSellerWelcomingCoachMarkNotEligible()
            }
        }
        coachMark.showCoachMark(coachMarkItems)
    }

    private fun getCoachMarkList(): ArrayList<CoachMark2Item> {
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