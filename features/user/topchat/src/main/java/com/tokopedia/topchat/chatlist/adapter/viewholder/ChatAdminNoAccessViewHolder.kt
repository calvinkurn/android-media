package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.constant.SellerHomePermissionGroup
import com.tokopedia.shop.common.constant.admin_roles.AdminPermissionUrl
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.pojo.ChatAdminNoAccessUiModel

class ChatAdminNoAccessViewHolder(itemView: View,
                                  private val onActionClick: () -> Unit): AbstractViewHolder<ChatAdminNoAccessUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_admin_no_access
    }

    private var adminNoAccessGlobalError: GlobalError? = itemView.findViewById(R.id.chatAdminNoAccessView)

    override fun bind(element: ChatAdminNoAccessUiModel) {
        bindGlobalError()
    }

    private fun bindGlobalError() {
        adminNoAccessGlobalError?.run {
            ImageHandler.loadImageAndCache(errorIllustration, AdminPermissionUrl.ERROR_ILLUSTRATION)
            val permissionGroup =
                    if (GlobalConfig.isSellerApp()) {
                        SellerHomePermissionGroup.CHAT
                    } else {
                        SellerHomePermissionGroup.DEFAULT
                    }
            errorTitle.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_title, permissionGroup)
            errorDescription.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_desc, permissionGroup)

            if (GlobalConfig.isSellerApp()) {
                errorAction.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_action)
                errorAction.show()
                setButtonFull(true)
            } else {
                errorAction.hide()
            }

            setActionClickListener {
                onActionClick.invoke()
            }

            show()
        }
    }
}