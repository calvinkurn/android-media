package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_select_action_on_group_bottomsheet.*

/**
 * Created by Pika on 3/6/20.
 */

private const val ACTIVATED = 1
private const val click_aktifkan_iklan = "click - aktifkan iklan"
private const val click_ubah_iklan = "click - ubah iklan"
private const val click_hapus_iklan = "click - hapus iklan"
private const val click_ya_hapus_iklan = "click - ya hapus iklan"
private const val click_bataiklan_iklan = "click - bataiklan"

class TopadsSelectActionSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var dialog: BottomSheetDialog? = null
    var onDeleteClick: (() -> Unit)? = null
    var changeStatus: ((active: Int) -> Unit)? = null
    var onEditAction: (() -> Unit)? = null
    private var name = ""
    private var activeStatus = 0
    private var groupId = 0
    private var hideDisable = false
    private lateinit var userSession: UserSessionInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        userSession = UserSession(context)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_dash_select_action_on_group_bottomsheet, null)
        setChild(contentView)
        setTitle(name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            img_active.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_green_dot))
            edit_img.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_edit_pen_icon))
            img_delete.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_delete))
            action_edit.show()
            action_edit.setOnClickListener {
                onEditAction?.invoke()
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_ubah_iklan, "{${userSession.shopId}} - {${groupId}}", userSession.userId)
                dismissAllowingStateLoss()
            }

            action_delete.setOnClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_hapus_iklan, "{${userSession.shopId}} - {${groupId}}", userSession.userId)
                showConfirmationDialog(name)
                dismiss()
            }
            if (activeStatus != ACTIVATED) {
                txt_action_active.text = it.getString(R.string.topads_dash_aktifan)
                img_active.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_green_dot))

            } else {
                txt_action_active.text = it.getString(R.string.topads_dash_nonaktifkan)
                img_active.setImageDrawable(it.getResDrawable(R.drawable.topads_dash_grey_dot))
            }
            action_activate.setOnClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_aktifkan_iklan, "{${userSession.shopId}} - {${groupId}}", userSession.userId)
                changeStatus?.invoke(activeStatus)
                dismiss()
            }
        }
    }

    fun show(
            fragmentManager: FragmentManager,
            activeStatus: Int,
            name: String, groupId: Int, hideDisable: Boolean = false) {
        this.activeStatus = activeStatus
        this.name = name
        this.groupId = groupId
        this.hideDisable = hideDisable
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }


    private fun showConfirmationDialog(name: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(String.format(it.getString(R.string.topads_dash_group_del_confirm), name))
            dialog.setPrimaryCTAText(it.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
            dialog.setSecondaryCTAText(it.getString(R.string.topads_dash_ya_hapus))
            dialog.setPrimaryCTAClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_bataiklan_iklan, "{${userSession.shopId}} - {${groupId}}", userSession.userId)
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_ya_hapus_iklan, "{${userSession.shopId}} - {${groupId}}", userSession.userId)
                dialog.dismiss()
                onDeleteClick?.invoke()
            }
            dialog.show()
        }
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): TopadsSelectActionSheet = TopadsSelectActionSheet()
    }
}