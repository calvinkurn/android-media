package com.tokopedia.tokochat_common.view.customview.attachment

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.tokochat_common.view.adapter.AttachmentMenuAdapter
import com.tokopedia.tokochat_common.view.listener.TokoChatAttachmentMenuListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatAttachmentMenuUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatAttachmentType

/**
 * Custom RecyclerView for TokoChat Menu Attachment
 */
class TokoChatMenuAttachmentRecyclerView : RecyclerView {

    private val manager = GridLayoutManager(context, getTabCount())
    private val adapter = AttachmentMenuAdapter()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    )

    init {
        setHasFixedSize(true)
        layoutManager = manager
        setAdapter(adapter)
    }

    fun updateAttachmentMenu(listener: TokoChatAttachmentMenuListener) {
        adapter.listener = listener
        adapter.menus.add(getImageAttachmentMenu())
        adapter.notifyDataSetChanged()
    }

    private fun getImageAttachmentMenu(): TokoChatAttachmentMenuUiModel {
        return TokoChatAttachmentMenuUiModel(
            title = TokoChatValueUtil.ATTACHMENT_IMAGE,
            icon = R.drawable.tokochat_ic_attachment_menu_image,
            type = TokoChatAttachmentType.IMAGE_ATTACHMENT
        )
    }

    private fun getTabCount(): Int = Int.ONE
}
