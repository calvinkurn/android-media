package com.tokopedia.tokochat.common.view.common

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.TokoChatGuideChatBottomSheet
import com.tokopedia.tokochat.common.view.common.TokoChatBottomSheetActivity.Type.Companion.toType

/**
 * Bottom Sheet page for TokoChat and related feature
 * NOTE: CLOSE KEYBOARD BEFORE OPEN ANY BOTTOM SHEET
 */
class TokoChatBottomSheetActivity: BaseActivity() {

    private val guideChatBottomSheet: TokoChatGuideChatBottomSheet by lazy {
        TokoChatGuideChatBottomSheet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data == null) {
            finish() // finish when data is null
        } else {
            val typeString = intent?.data?.lastPathSegment?: ""
            val type = typeString.toType()
            if (type == null) {
                finish() // finish when type is not found
            } else {
                openBottomSheet(type)
            }
        }
    }

    private fun openBottomSheet(type: Type) {
        when (type) {
            Type.GUIDE_CHAT -> {
                guideChatBottomSheet.setOnDismissListener {
                    finish()
                }
                guideChatBottomSheet.setCloseClickListener {
                    finish()
                }
                if (!guideChatBottomSheet.isAdded) {
                    guideChatBottomSheet.show(supportFragmentManager)
                }
            }
        }
    }

    enum class Type(val value: String) {
        // Reserve for future bottomsheet types
        GUIDE_CHAT(ApplinkConstInternalCommunication.GUIDE_CHAT);

        companion object {
            fun String.toType(): Type? {
                return values().find { it.value == this }
            }
        }
    }
}
