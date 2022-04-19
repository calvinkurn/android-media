package com.tokopedia.loginregister.common.view.dialog

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.url.TokopediaUrl

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class PopupErrorDialog {

    companion object {
        fun createDialog(context: Context?, header: String, body: String, url: String = ""): DialogUnify? {
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(header)
                dialog.setDescription(body)
                dialog.setPrimaryCTAText(getString(R.string.check_full_information))
                dialog.setSecondaryCTAText(getString(R.string.close_popup))
                dialog.setPrimaryCTAClickListener {
                    RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
                }
                dialog.setSecondaryCTAClickListener {
                    dialog.hide()
                }
                return dialog
            }
            return null
        }

        fun showPopupErrorAkamai(context: Context?) {
            context?.run {
                createDialog(
                        context,
                        getString(R.string.popup_error_title),
                        getString(R.string.popup_error_desc),
                        TokopediaUrl.getInstance().MOBILEWEB + RegisterInitialFragment.TOKOPEDIA_CARE_PATH
                )?.show()
            }
        }
    }
}