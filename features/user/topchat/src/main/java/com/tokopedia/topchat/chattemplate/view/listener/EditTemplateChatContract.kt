package com.tokopedia.topchat.chattemplate.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateResultModel

/**
 * Created by stevenfredian on 12/22/17.
 */
class EditTemplateChatContract {
    interface View : CustomerView {
        fun onResult(editTemplateViewModel: EditTemplateResultModel, index: Int, s: String)
        fun finish()
        fun dropKeyboard()
        fun showError(error: Throwable)
        fun onResult(editTemplateViewModel: EditTemplateResultModel, index: Int)
    }
}