package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

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

    private var actionActivate : ConstraintLayout?= null
    private var txtActionActive : Typography ?= null
    private var imgActive : ImageUnify?= null
    private var actionEdit : ConstraintLayout ?= null
    private var editImg : ImageUnify ?= null
    private var actionDelete : ConstraintLayout ?= null
    private var imgDelete : ImageUnify ?= null

    var onDeleteClick: (() -> Unit)? = null
    var changeStatus: ((active: Int) -> Unit)? = null
    var onEditAction: (() -> Unit)? = null
    private var name = ""
    private var activeStatus = 0
    private var groupId = ""
    private var hideDisable = false
    private lateinit var userSession: UserSessionInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        userSession = UserSession(context)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView =
            View.inflate(context, R.layout.topads_dash_select_action_on_group_bottomsheet, null)
        actionActivate = contentView.findViewById(R.id.action_activate)
        txtActionActive = contentView.findViewById(R.id.txt_action_active)
        imgActive = contentView.findViewById(R.id.img_active)
        actionEdit = contentView.findViewById(R.id.action_edit)
        editImg = contentView.findViewById(R.id.edit_img)
        actionDelete = contentView.findViewById(R.id.action_delete)
        imgDelete = contentView.findViewById(R.id.img_delete)
        setChild(contentView)
        setTitle(name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            imgActive?.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_green_dot))
            editImg?.setImageDrawable(view?.context?.let { it1 ->
                getIconUnifyDrawable(it1,
                    IconUnify.EDIT)
            })
            imgDelete?.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_delete))
            actionEdit?.show()
            actionEdit?.setOnClickListener {
                onEditAction?.invoke()
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                        click_ubah_iklan,
                        "{${userSession.shopId}} - {${groupId}}",
                        userSession.userId)
                dismissAllowingStateLoss()
            }

            actionDelete?.setOnClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                        click_hapus_iklan,
                        "{${userSession.shopId}} - {${groupId}}",
                        userSession.userId)
                showConfirmationDialog(name)
                dismiss()
            }
            if (activeStatus != ACTIVATED) {
                txtActionActive?.text = it.getString(R.string.topads_dash_aktifan)
                imgActive?.setImageDrawable(it.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_green_dot))

            } else {
                txtActionActive?.text = it.getString(R.string.topads_dash_nonaktifkan)
                imgActive?.setImageDrawable(it.getResDrawable(R.drawable.topads_dash_grey_dot))
            }
            actionActivate?.setOnClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                        click_aktifkan_iklan,
                        "{${userSession.shopId}} - {${groupId}}",
                        userSession.userId)
                changeStatus?.invoke(activeStatus)
                dismiss()
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        activeStatus: Int,
        name: String, groupId: String, hideDisable: Boolean = false,
    ) {
        this.activeStatus = activeStatus
        this.name = name
        this.groupId = groupId
        this.hideDisable = hideDisable
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }


    private fun showConfirmationDialog(name: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(String.format(it.getString(R.string.topads_dash_group_del_confirm),
                name))
            dialog.setPrimaryCTAText(it.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
            dialog.setSecondaryCTAText(it.getString(R.string.topads_dash_ya_hapus))
            dialog.setPrimaryCTAClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                        click_bataiklan_iklan,
                        "{${userSession.shopId}} - {${groupId}}",
                        userSession.userId)
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                if (hideDisable)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                        click_ya_hapus_iklan,
                        "{${userSession.shopId}} - {${groupId}}",
                        userSession.userId)
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