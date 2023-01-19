package com.tokopedia.contactus.inboxtickets.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxtickets.domain.AttachmentItem
import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.view.adapter.holder.InboxDetailViewHolder
import com.tokopedia.contactus.inboxtickets.view.adapter.holder.InboxHeaderViewHolder
import com.tokopedia.contactus.inboxtickets.view.listeners.AttachmentListener
import com.tokopedia.contactus.inboxtickets.view.listeners.InboxDetailListener
import com.tokopedia.contactus.inboxtickets.view.utils.CLOSED
import com.tokopedia.contactus.inboxtickets.view.utils.Utils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.htmltags.HtmlUtil

private const val ROLE_TYPE_AGENT = "agent"
private const val VIEW_TYPE_HEADER = 0

class InboxDetailAdapter(
    private val mContext: Context,
    private val commentList: MutableList<CommentsItem>,
    needAttachment: Boolean,
    private val inboxDetailListener: InboxDetailListener,
    private val userId: String,
    private val caseId: String,
    private val ticketStatus: String
) : RecyclerView.Adapter<InboxDetailViewHolder>() {

    private var needAttachment: Boolean
    private val indexExpanded: Int = -1
    private var searchMode = false
    private var searchText: String? = null
    private val utils: Utils by lazy { Utils() }
    private val hintAttachmentString: SpannableString

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = inflater.inflate(R.layout.layout_item_message_header, parent, false)
            InboxHeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.layout_item_message, parent, false)
            InboxMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: InboxDetailViewHolder, position: Int) {
        if (holder is InboxMessageViewHolder) {
            holder.bindViewHolder(position)
        } else if (holder is InboxHeaderViewHolder) {
            holder.bind(commentList[position], inboxDetailListener, utils)
        }
    }

    fun enterSearchMode(text: String) {
        if (text.isNotEmpty()) {
            searchMode = true
            searchText = text
            notifyDataSetChanged()
        }
    }

    fun exitSearchMode() {
        searchMode = false
        searchText = ""
        notifyDataSetChanged()
    }

    fun setNeedAttachment(isAttachment: Boolean) {
        needAttachment = isAttachment
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addComment(commentsItem: CommentsItem) {
        commentList.add(commentsItem)
        notifyItemRangeChanged(itemCount - 2, 2)
        inboxDetailListener.scrollTo(itemCount - 1)
    }

    inner class InboxMessageViewHolder(val view: View) :
        InboxDetailViewHolder(view),
        View.OnClickListener {
        private var ivProfile: ImageView? = null
        private var tvName: TextView? = null
        private var tvDateRecent: TextView? = null
        private var tvComment: TextView? = null
        private var tvCollapsedTime: TextView? = null
        private var layoutItemView: View? = null
        private var rvAttachedImage: RecyclerView? = null
        private var tvAttachmentHint: TextView? = null
        private var attachmentAdapter: AttachmentAdapter? = null
        private val layoutManager: LinearLayoutManager
        private val ratingThumbsUp: ImageView
        private val ratingThumbsDown: ImageView

        private fun findindViewsId(view: View) {
            ivProfile = view.findViewById(R.id.iv_profile)
            tvName = view.findViewById(R.id.tv_name)
            tvDateRecent = view.findViewById(R.id.tv_date_recent)
            tvComment = view.findViewById(R.id.tv_comment)
            tvCollapsedTime = view.findViewById(R.id.tv_collapsed_time)
            layoutItemView = view.findViewById(R.id.layout_item_message)
            rvAttachedImage = view.findViewById(R.id.rv_attached_image)
            tvAttachmentHint = view.findViewById(R.id.tv_hint_attachment)
        }

        fun bindViewHolder(position: Int) {
            if (commentList[position].attachment?.size.orZero() > 0) {
                if (attachmentAdapter == null) {
                    attachmentAdapter = AttachmentAdapter(
                        commentList[position].attachment
                            ?: listOf(),
                        object : AttachmentListener {
                            override fun showImagePreview(
                                position: Int,
                                imagesItems: List<AttachmentItem>
                            ) {
                                inboxDetailListener.showImageAttachment(position, imagesItems)
                            }
                        },
                        userId,
                        caseId
                    )
                } else {
                    attachmentAdapter?.addAll(
                        commentList[position].attachment
                            ?: listOf()
                    )
                }
                rvAttachedImage?.adapter = attachmentAdapter
                rvAttachedImage?.show()
                tvCollapsedTime?.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.attach_rotated,
                    0,
                    0,
                    0
                )
            } else {
                rvAttachedImage?.hide()
                tvCollapsedTime?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            val item = commentList[position]
            if (item.createdBy.isNullCreatedBy()) {
                ImageHandler.loadImageCircle2(mContext, ivProfile, item.createdBy.picture)
                if (isRoleAgent(item)) tvName?.text =
                    view.context.getString(R.string.contact_us_tokopedia_care_team)
                else tvName?.text = item.createdBy.name
            }
            if (item.rating.isNotEmpty() && item.rating == KEY_DIS_LIKED) {
                ratingThumbsDown.show()
                ratingThumbsDown.setColorFilter(
                    ContextCompat.getColor(
                        mContext,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
                ratingThumbsUp.hide()
            } else if (item.rating.isNotEmpty() && item.rating == KEY_LIKED) {
                ratingThumbsUp.show()
                ratingThumbsUp.setColorFilter(
                    ContextCompat.getColor(
                        mContext,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                ratingThumbsDown.hide()
            }
            if (position == commentList.size - 1 || !commentList[position].isCollapsed || searchMode) {
                tvDateRecent?.text = item.createTime
                tvCollapsedTime?.text = ""
                tvCollapsedTime?.hide()
                if (!CLOSED.equals(ticketStatus, ignoreCase = true) && isRoleAgent(
                        item
                    ) && (item.rating.isEmpty())
                ) {
                    settingRatingButtonsVisibility(View.VISIBLE)
                    ratingThumbsUp.clearColorFilter()
                    ratingThumbsDown.clearColorFilter()
                }
                val ticket = ticketStatus
                if (CLOSED.equals(
                        ticket,
                        ignoreCase = true
                    ) && item.rating != KEY_LIKED && item.rating != KEY_DIS_LIKED ||
                    !isRoleAgent(item) || item.id.isEmpty()
                ) {
                    settingRatingButtonsVisibility(View.GONE)
                }
                if (searchMode) {
                    tvComment?.text = utils.getHighlightText(
                        view.context,
                        searchText ?: "",
                        HtmlUtil.fromHtml(item.message).toString()
                    )
                    tvComment?.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    tvComment?.text = HtmlUtil.fromHtml(item.message)
                    tvComment?.movementMethod = LinkMovementMethod.getInstance()
                }
                tvComment?.show()
                if (position == commentList.size - 1 && needAttachment) {
                    tvAttachmentHint?.text = hintAttachmentString
                    tvAttachmentHint?.show()
                } else {
                    tvAttachmentHint?.hide()
                }
                if (commentList[position].attachment?.size.orZero() > 0) {
                    rvAttachedImage?.show()
                }
            } else {
                tvAttachmentHint?.hide()
                tvDateRecent?.text = MethodChecker.fromHtml(item.message)
                tvComment?.text = ""
                tvCollapsedTime?.text = item.shortTime
                tvCollapsedTime?.show()
                settingRatingButtonsVisibility(View.GONE)
                tvComment?.hide()
                rvAttachedImage?.hide()
            }
            ratingThumbsUp.setOnClickListener {
                if (item.rating.isEmpty() && !(item.rating == KEY_LIKED || item.rating == KEY_DIS_LIKED)) {
                    ratingThumbsUp.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                    ratingThumbsDown.hide()
                    inboxDetailListener.onCommentClick(true, position, item.id)
                    sendGTMEvent(InboxTicketTracking.Label.EventHelpful)
                }
            }
            ratingThumbsDown.setOnClickListener {
                if (item.rating.isEmpty() && !(item.rating == KEY_LIKED || item.rating == KEY_DIS_LIKED)) {
                    ratingThumbsDown.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        )
                    )
                    ratingThumbsUp.hide()
                    inboxDetailListener.onCommentClick(false, position, item.id)
                    sendGTMEvent(InboxTicketTracking.Label.EventNotHelpful)
                }
            }
        }

        private fun sendGTMEvent(eventLabel: String) {
            ContactUsTracking.sendGTMInboxTicket(
                view.context,
                InboxTicketTracking.Event.EventName,
                InboxTicketTracking.Category.EventHelpMessageInbox,
                InboxTicketTracking.Action.EventClickCsatPerReply,
                eventLabel
            )
        }

        private fun isRoleAgent(item: CommentsItem?): Boolean {
            return item?.createdBy?.role == ROLE_TYPE_AGENT
        }

        private fun settingRatingButtonsVisibility(visibility: Int) {
            ratingThumbsUp.visibility = visibility
            ratingThumbsDown.visibility = visibility
        }

        private fun toggleCollapse() {
            val tapIndex = adapterPosition
            if (tapIndex != commentList.size - 1) {
                val item = commentList[tapIndex]
                val isCollapsed = item.isCollapsed
                item.isCollapsed = !isCollapsed
            }
            notifyItemChanged(tapIndex)
            inboxDetailListener.scrollTo(indexExpanded)
        }

        override fun onClick(view: View) {
            if (view.id != R.id.tv_comment) {
                toggleCollapse()
            }
        }

        init {
            findindViewsId(view)
            ratingThumbsUp = itemView.findViewById(R.id.iv_csast_status_good)
            ratingThumbsDown = itemView.findViewById(R.id.iv_csast_status_bad)
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            rvAttachedImage?.layoutManager = layoutManager
            itemView.setOnClickListener(this)
            tvComment?.setOnClickListener(this)
            tvDateRecent?.setOnClickListener(this)
        }
    }

    companion object {
        private const val KEY_LIKED = "101"
        private const val KEY_DIS_LIKED = "102"
    }

    init {
        this.needAttachment = needAttachment
        val src = mContext.getString(R.string.hint_attachment)
        hintAttachmentString = SpannableString(src)
        hintAttachmentString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            8,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
}
