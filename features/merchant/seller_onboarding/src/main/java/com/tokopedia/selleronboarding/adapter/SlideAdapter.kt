package com.tokopedia.selleronboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalytic
import com.tokopedia.selleronboarding.model.SlideUiModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.viewholder_sob_onboarding.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SlideAdapter : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    private val items = mutableListOf<SlideUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_sob_onboarding, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addSlideItem(item: SlideUiModel) {
        this.items.add(item)
    }

    fun clearSlideItems() {
        this.items.clear()
    }

    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val irisSession: IrisSession = IrisSession(itemView.context)
        private val userSession: UserSessionInterface = UserSession(itemView.context)

        fun bind(item: SlideUiModel) = with(itemView) {
            tvHeaderText.text = item.headerText
            showIllustrationImage(item)

            addOnImpressionListener(item.impressHolder) {
                sendOpenSlideAnalytics()
            }
        }

        private fun showIllustrationImage(item: SlideUiModel) = with(itemView) {
            try {
                imgIllustrationSob.loadImage(item.drawableUrl)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        private fun sendOpenSlideAnalytics() {
            val position = adapterPosition.plus(1)
            SellerOnboardingAnalytic.sendEventOpenScreen(
                    position,
                    irisSession.getSessionId(),
                    userSession.deviceId
            )
        }
    }
}