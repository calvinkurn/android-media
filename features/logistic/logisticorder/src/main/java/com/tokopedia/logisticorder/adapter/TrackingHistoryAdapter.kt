package com.tokopedia.logisticorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.AdapterTrackingHistoryViewHolderBinding
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.utils.TrackingPageUtil
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil

/**
 * Created by kris on 5/11/18. Tokopedia
 */
class TrackingHistoryAdapter(private val trackingHistoryData: List<TrackHistoryModel>,
                             private val userSession: UserSessionInterface,
                             private val orderId: Long?,
                             private val listener: OnImageClicked) : RecyclerView.Adapter<TrackingHistoryAdapter.TrackingHistoryViewHolder>() {
    interface OnImageClicked {
        fun onImageItemClicked(imageId: String, orderId: Long, description: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryViewHolder {
        val binding = AdapterTrackingHistoryViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackingHistoryViewHolder(binding, userSession, orderId, listener)
    }

    override fun onBindViewHolder(holder: TrackingHistoryViewHolder, position: Int) {
        val trackingHistory = trackingHistoryData[position]
        holder.bind(trackingHistory, position)
    }

    override fun getItemCount(): Int {
        return trackingHistoryData.size
    }

    inner class TrackingHistoryViewHolder(private val binding: AdapterTrackingHistoryViewHolderBinding,
                                          private val userSession: UserSessionInterface,
                                          private val orderId: Long?,
                                          private val listener: OnImageClicked) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TrackHistoryModel, position: Int) {
            binding.run {
                title.run {
                    text = DateUtil.formatDate("yyyy-MM-dd", "EEEE, dd MMM yyyy",data.date)
                    setTextColor(MethodChecker.getColor(itemView.context, if (position == 0) com.tokopedia.unifyprinciples.R.color.Unify_G400 else com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                }
                date.text = "${DateUtil.formatDate("HH:mm:ss", "HH:mm",data.time)} WIB"
                description.text = if (data.status.isNotEmpty()) MethodChecker.fromHtml(data.status) else ""
                if (data.partnerName.isNotEmpty()) {
                    courierNameHistory.text = root.context.getString(R.string.label_kurir_rekomendasi, data.partnerName)
                } else {
                    courierNameHistory.visibility = View.GONE
                }
                var dotColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
                if (position == 0) {
                    dotColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
                    dotTrail.visibility = View.VISIBLE
                    dotTrail.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
                } else if (position == trackingHistoryData.size - 1) {
                    dotTrail.visibility = View.GONE
                } else {
                    dotTrail.visibility = View.VISIBLE
                    dotTrail.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
                }
                dotImage.setImageDrawable(getIconUnifyDrawable(itemView.context,
                    IconUnify.CHECK_CIRCLE, dotColor))
                if (data.proof.imageId.isEmpty()) {
                    imgProof.visibility = View.GONE
                } else {
                    if (orderId != null) {
                        imgProof.visibility = View.VISIBLE
                        val url: String = TrackingPageUtil.getDeliveryImage(data.proof.imageId, orderId, TrackingPageUtil.IMAGE_SMALL_SIZE,
                                userSession.userId, TrackingPageUtil.DEFAULT_OS_TYPE, userSession.deviceId)
                        val authKey = String.format("%s %s", TrackingPageUtil.HEADER_VALUE_BEARER, userSession.accessToken)
                        val newUrl = GlideUrl(url, LazyHeaders.Builder()
                                .addHeader(TrackingPageUtil.HEADER_KEY_AUTH, authKey)
                                .build())
                        Glide.with(itemView.context)
                                .load(newUrl)
                                .centerCrop()
                                .placeholder(itemView.context.getDrawable(R.drawable.ic_image_error))
                                .error(itemView.context.getDrawable(R.drawable.ic_image_error))
                                .dontAnimate()
                                .into(imgProof)
                        imgProof.setOnClickListener { view: View? -> listener.onImageItemClicked(data.proof.imageId, orderId, data.proof.description) }
                    }
                }
            }
        }
    }
}