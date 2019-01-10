package com.tokopedia.broadcast.message.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcastMessageComponent
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessageCreateFragment
import com.tokopedia.design.component.Dialog

class BroadcastMessageCreateActivity: BaseSimpleActivity(), HasComponent<BroadcastMessageComponent> {
    private val router: BroadcastMessageRouter? by lazy {
        application as? BroadcastMessageRouter
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, BroadcastMessageCreateActivity::class.java)
    }

    override fun getNewFragment() = BroadcastMessageCreateFragment()

    override fun getComponent() = DaggerBroadcastMessageComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun onBackPressed() {
        if (fragment is BroadcastMessageCreateFragment && (fragment as BroadcastMessageCreateFragment).isShowDialogWhenBack){
            Dialog(this, Dialog.Type.PROMINANCE).apply {
                this.setTitle(getString(R.string.dialog_title_cancel_create_bm))
                setDesc(getString(R.string.dialog_descr_cancel_create_bm))
                setBtnOk(getString(R.string.dialog_ok_cancel_create_bm))
                setBtnCancel(getString(R.string.dialog_no_cancel_create_bm))
                setOnOkClickListener { sendGTM(true); finish() }
                setOnCancelClickListener { sendGTM(false); dismiss() }
                show()
            }

        } else {
            super.onBackPressed()
        }
    }

    fun sendGTM(isOk: Boolean) {
        router?.run {
            sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_CONFIRMATION,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_CANCEL,
                    if (isOk) BroadcastMessageConstant.VALUE_GTM_EVENT_LABEL_CANCEL_YES
                    else BroadcastMessageConstant.VALUE_GTM_EVENT_LABEL_CANCEL_NO)
        }
    }
}