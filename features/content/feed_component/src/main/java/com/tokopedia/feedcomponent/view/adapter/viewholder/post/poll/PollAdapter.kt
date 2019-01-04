package com.tokopedia.feedcomponent.view.adapter.viewitemView.post.poll

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_poll_option.view.*

/**
 * @author by milhamj on 12/12/18.
 */
class PollAdapter(private val positionInFeed: Int, private val pollViewModel: PollViewModel,
                  private val listener: PollOptionListener)
    : RecyclerView.Adapter<PollAdapter.OptionViewHolder>() {

    private val optionList: MutableList<PollOptionViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poll_option, parent, false)
        return PollAdapter.OptionViewHolder(view, positionInFeed, pollViewModel, listener)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    fun setList(list: MutableList<PollOptionViewModel>) {
        optionList.clear()
        optionList.addAll(list)
        notifyDataSetChanged()
    }

    class OptionViewHolder(v: View,
                           private val rowNumber: Int,
                           private val pollViewModel: PollViewModel,
                           private val listener: PollOptionListener?)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: PollOptionViewModel) {
            val context = itemView.context
            if (element.selected == PollOptionViewModel.DEFAULT) {
                itemView.shadowLayer.gone()
                itemView.percent.gone()
                itemView.percentLayout.gone()
                itemView.progressBar.progress = 0
                itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_default)
            } else {
                itemView.shadowLayer.visible()
                itemView.percent.visible()
                itemView.percentLayout.visible()
                itemView.progressBar.progress = element.percentageNumber
                if (element.selected == PollOptionViewModel.SELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_selected)
                } else if (element.selected == PollOptionViewModel.UNSELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_unselected)
                }
            }

            itemView.option.text = element.option
            itemView.percent.text = element.percentageText
            ImageHandler.loadImageWithTarget(
                    itemView.imageView.context,
                    element.imageUrl,
                    object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(arg0: Bitmap, arg1: GlideAnimation<in Bitmap>) {
                            itemView.imageView.setImageBitmap(arg0)
                            itemView.imageView.post {
                                itemView.shadowLayer.layoutParams = RelativeLayout.LayoutParams(
                                        itemView.imageView.height,
                                        itemView.imageView.width)

                                if (element.selected == PollOptionViewModel.DEFAULT) {
                                    itemView.shadowLayer.visibility = View.GONE
                                } else {
                                    itemView.shadowLayer.visibility = View.VISIBLE
                                }
                            }

                        }
                    }
            )

            itemView.setOnClickListener {
                listener?.onPollOptionClick(rowNumber, pollViewModel.pollId, element.optionId, pollViewModel.voted, element.redirectLink)
            }
        }
    }

    interface PollOptionListener {
        fun onPollOptionClick(positionInFeed: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String)
    }
}