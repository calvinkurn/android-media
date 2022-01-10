package com.tokopedia.logisticorder.adapter

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.AdapterTrackingHistoryViewHolderBinding
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.utils.DateUtil
import com.tokopedia.logisticorder.utils.TrackingPageUtil
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kris on 5/11/18. Tokopedia
 */
class TrackingHistoryAdapter(private val trackingHistoryData: List<TrackHistoryModel>,
                             private val dateUtil: DateUtil,
                             private val orderId: Long,
                             private val listener: OnImageClicked) : RecyclerView.Adapter<TrackingHistoryAdapter.TrackingHistoryViewHolder>() {
    interface OnImageClicked {
        fun onImageItemClicked(imageId: String?, orderId: Long?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryViewHolder {
        val binding = AdapterTrackingHistoryViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackingHistoryViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: TrackingHistoryViewHolder, position: Int) {
        holder.binding.title.setText(dateUtil.getFormattedDate(trackingHistoryData[position].date))
        holder.binding.date.setText(dateUtil.getFormattedTime(trackingHistoryData[position].time))
        setTitleColor(holder, position)
        holder.binding.description.text = if (trackingHistoryData[position].status.isNotEmpty()) Html.fromHtml(trackingHistoryData[position].status) else ""
        if (!trackingHistoryData[position].partnerName.isEmpty()) {
            holder.binding.courierNameHistory.text = holder.binding.root.context.getString(R.string.label_kurir_rekomendasi, trackingHistoryData[position].partnerName)
        } else {
            holder.binding.courierNameHistory.visibility = View.GONE
        }
        if (position == 0) {
            holder.binding.dotImage.setColorFilter(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400))
            holder.binding.dotTrail.visibility = View.VISIBLE
            holder.binding.dotTrail.setBackgroundColor(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400))
        } else if (position == trackingHistoryData.size - 1) {
            holder.binding.dotImage.setColorFilter(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
            holder.binding.dotTrail.visibility = View.GONE
        } else {
            holder.binding.dotImage.setColorFilter(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
            holder.binding.dotTrail.visibility = View.VISIBLE
            holder.binding.dotTrail.setBackgroundColor(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
        }
        if (trackingHistoryData[position].proof.imageId.isEmpty()) {
            holder.binding.imgProof.setVisibility(View.GONE)
        } else {
            holder.binding.imgProof.setVisibility(View.VISIBLE)
            val userSession: UserSessionInterface = UserSession(holder.binding.root.context)
            val url: String = TrackingPageUtil.getDeliveryImage(trackingHistoryData[position].proof.imageId, orderId, "small",
                    userSession.userId, 1, userSession.getDeviceId())
            val authKey = String.format("%s %s", TrackingPageUtil.HEADER_VALUE_BEARER, userSession.accessToken)
            val newUrl = GlideUrl(url, LazyHeaders.Builder()
                    .addHeader(TrackingPageUtil.HEADER_KEY_AUTH, authKey)
                    .build())
            Glide.with(holder.binding.root.context)
                    .load(newUrl)
                    .centerCrop()
                    .placeholder(holder.binding.root.context.getDrawable(R.drawable.ic_image_error))
                    .error(holder.binding.root.context.getDrawable(R.drawable.ic_image_error))
                    .dontAnimate()
                    .into(holder.binding.imgProof)
            holder.binding.imgProof.setOnClickListener(View.OnClickListener { view: View? -> listener.onImageItemClicked(trackingHistoryData[position].proof.imageId, orderId) })
        }
    }

    private fun setTitleColor(holder: TrackingHistoryViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.title.setTextColor(holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400))
        } else {
            holder.binding.title.setTextColor(
                    holder.binding.root.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    override fun getItemCount(): Int {
        return trackingHistoryData.size
    }

    inner class TrackingHistoryViewHolder(private val context: Context, val binding: AdapterTrackingHistoryViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
//        private val title: Typography
//        private val time: Typography
//        private val description: Typography
//        private val dot: ImageView
//        private val dotTrail: View
//        private val imageProof: ImageUnify
//        private val courierName: Typography

        init {
//            title = itemView.findViewById(R.id.title)
//            time = itemView.findViewById(R.id.date)
//            description = itemView.findViewById(R.id.description)
//            dot = itemView.findViewById(R.id.dot_image)
//            dotTrail = itemView.findViewById(R.id.dot_trail)
//            imageProof = itemView.findViewById(R.id.img_proof)
//            courierName = itemView.findViewById(R.id.courier_name_history)
        }
    }

//    init {
//        this.trackingHistoryData = trackingHistoryData
//        this.dateUtil = dateUtil
//        this.orderId = orderId
//        this.listener = listener
//    }
}