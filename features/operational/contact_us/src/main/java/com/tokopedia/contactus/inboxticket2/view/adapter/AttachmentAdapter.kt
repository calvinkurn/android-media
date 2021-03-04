package com.tokopedia.contactus.inboxticket2.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.view.adapter.AttachmentAdapter.AttachmentViewHolder
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import java.io.File

private const val HEADER_USER_ID_KEY = "X-TKPD-UserId"
private const val HEADER_CASE_ID_KEY = "X-TKPD-CaseId"
private const val HEADER_REFERER_KEY = "Referer"
private const val HEADER_REFERER_VALUE = "tokopedia.com/help/inbox"
private const val CONTACT_US = "contact-us"
class AttachmentAdapter constructor(data: List<AttachmentItem>,
                                    presenter: InboxDetailContract.Presenter,
                                    private val userId: String,
                                    private val caseId: String) : RecyclerView.Adapter<AttachmentViewHolder>() {
    private val attachmentList: MutableList<AttachmentItem>
    private val mPresenter: InboxDetailContract.Presenter
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

    private fun isUrl(src: String): Boolean {
        return if (src.length >= 4) {
            src.substring(0, 4) == "http"
        } else false
    }

    override fun getItemCount(): Int {
        return attachmentList.size
    }

    inner class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivAttachment: ImageView = itemView.findViewById(R.id.iv_attachment)
        fun bindView(index: Int) {
            val thumbnail = attachmentList[index].thumbnail
            if (isUrl(thumbnail ?: "")) {
                loadImage(thumbnail ?: "")
            } else {
                thumbnail?.let { ivAttachment.setImageURI(Uri.fromFile(File(it))) }
            }
            ivAttachment.setOnClickListener { previewImage() }
        }

        private fun previewImage() {
            mPresenter.showImagePreview(adapterPosition, attachmentList)
        }

        private fun loadImage(url: String) {
            if (itemView.context != null) {
                Glide.with(itemView.context)
                        .load(getUrl(url))
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                        .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                        .into(ivAttachment)
            }
        }

        private fun getUrl(url: String?): GlideUrl {
            return if (url?.contains(CONTACT_US) == true) {
                GlideUrl(url, LazyHeaders.Builder()
                        .addHeader(HEADER_USER_ID_KEY, userId)
                        .addHeader(HEADER_CASE_ID_KEY, caseId)
                        .addHeader(HEADER_REFERER_KEY, HEADER_REFERER_VALUE)
                        .build())
            } else {
                GlideUrl(url)
            }
        }

    }

    init {
        attachmentList = ArrayList()
        attachmentList.addAll(data)
        mPresenter = presenter
    }
}