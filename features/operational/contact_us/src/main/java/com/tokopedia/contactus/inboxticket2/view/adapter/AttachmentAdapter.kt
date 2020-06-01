package com.tokopedia.contactus.inboxticket2.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.view.adapter.AttachmentAdapter.AttachmentViewHolder
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter
import java.io.File
import java.util.*

class AttachmentAdapter constructor(data: List<AttachmentItem>,
                                    presenter: InboxDetailPresenter) : RecyclerView.Adapter<AttachmentViewHolder>() {
    private val attachmentList: MutableList<AttachmentItem>
    private val mPresenter: InboxDetailPresenter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_attachment_item, parent, false)
        return AttachmentViewHolder(v)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bindView(position)
    }

    fun addAll(attachmentItems: List<AttachmentItem>) {
        attachmentList.clear()
        attachmentList.addAll(attachmentItems)
    }

    private fun isUrl(src: String?): Boolean {
        return src?.substring(0, 4) == "http"
    }

    override fun getItemCount(): Int {
        return attachmentList.size
    }

    inner class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivAttachment: ImageView = itemView.findViewById(R.id.iv_attachment)
        fun bindView(index: Int) {
            val thumbnail = attachmentList[index].thumbnail
            if (isUrl(thumbnail)) {
                ImageHandler.LoadImage(ivAttachment, thumbnail ?: "")
            } else {
                ivAttachment.setImageURI(Uri.fromFile(File(thumbnail)))
            }
            ivAttachment.setOnClickListener { previewImage() }
        }

        private fun previewImage() {
            mPresenter.showImagePreview(adapterPosition, attachmentList)
        }

    }

    init {
        attachmentList = ArrayList()
        attachmentList.addAll(data)
        mPresenter = presenter
    }
}