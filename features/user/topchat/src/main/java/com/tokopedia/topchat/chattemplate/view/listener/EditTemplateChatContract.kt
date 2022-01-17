package com.tokopedia.topchat.chattemplate.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateUiModel

/**
 * Created by stevenfredian on 12/22/17.
 */
class EditTemplateChatContract {
    interface View : CustomerView {
        fun onResult(editTemplateViewModel: EditTemplateUiModel, index: Int, s: String)
        fun finish()
        fun dropKeyboard()
        fun showError(error: Throwable)
        fun onResult(editTemplateViewModel: EditTemplateUiModel, index: Int)
    }
}